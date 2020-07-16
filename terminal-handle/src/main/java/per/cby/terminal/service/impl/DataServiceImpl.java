package per.cby.terminal.service.impl;

import java.time.LocalDateTime;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.collect.common.constant.CollectConstant;
import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.util.JudgeUtil;
import per.cby.terminal.bo.TerminalConfig;
import per.cby.terminal.bo.TerminalState;
import per.cby.terminal.constant.TransportType;
import per.cby.terminal.model.Commodu;
import per.cby.terminal.model.Rawdata;
import per.cby.terminal.model.Terminal;
import per.cby.terminal.model.TerminalCommodu;
import per.cby.terminal.redis.ImeiSnHash;
import per.cby.terminal.redis.SnImeiHash;
import per.cby.terminal.redis.TerminalConfigHash;
import per.cby.terminal.redis.TerminalStateHash;
import per.cby.terminal.service.CommoduService;
import per.cby.terminal.service.DataService;
import per.cby.terminal.service.RawdataService;
import per.cby.terminal.service.TerminalCommoduService;
import per.cby.terminal.service.TerminalService;

/**
 * 终端报文数据服务接口实现
 * 
 * @author chenboyang
 * @since 2019年11月7日
 *
 */
@Service("__DATA_SERVICE__")
public class DataServiceImpl implements DataService, CollectConstant {

    @Autowired
    private RawdataService rawdataService;

    @Autowired
    private TerminalStateHash terminalStateHash;

    @Autowired
    private TerminalConfigHash terminalConfigHash;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private CommoduService commoduService;

    @Autowired
    private TerminalCommoduService terminalCommoduService;

    @Autowired
    private SnImeiHash snImeiHash;

    @Autowired
    private ImeiSnHash imeiSnHash;

    @Override
    public void saveRawdata(TerminalMessage message, String transportType) {
        if (message == null) {
            return;
        }
        TerminalConfig config = terminalConfigHash.get(message.getTerminalId());
        Rawdata data = new Rawdata();
        data.setTransportType(transportType);
        data.setMessageId(message.getMessageId());
        data.setTerminalId(message.getTerminalId());
        data.setImei(message.getImei());
        data.setChannelId(message.getChannel());
        if (message.getPayload() != null) {
            data.setDataLength(message.getPayload().length);
        } else {
            data.setDataLength(0);
        }
        if (BooleanUtils.isTrue(config.getClearRaw())) {
            data.setPayload(new byte[0]);
        } else {
            data.setPayload(message.getPayload());
        }
        data.setTimestamp(message.getTimestamp());
        data.setHeader(message.getHeader());
        rawdataService.save(data);
    }

    @Override
    public void upateState(TerminalMessage message, TransportType transportType) {
        TerminalState state = terminalStateHash.get(message.getTerminalId());
        if (message.getImei() != null) {
            state.setImei(message.getImei());
        }
        if (message.getChannel() != null) {
            state.setChannel(message.getChannel());
        }
        if (message.getHeader().get(GATEWAY) != null) {
            state.getGateway().add(String.valueOf(message.getHeader().get(GATEWAY)));
        }
        LocalDateTime now = LocalDateTime.now();
        switch (transportType) {
            case REPORT:
                if (state.getFirstReportTime() == null) {
                    state.setFirstReportTime(now);
                }
                state.setLastReportTime(now);
                state.setReportCount(state.getReportCount().longValue() + 1);
                break;
            case ISSUED:
                if (state.getFirstIssuedTime() == null) {
                    state.setFirstIssuedTime(now);
                }
                state.setLastIssuedTime(now);
                state.setIssuedCount(state.getIssuedCount().longValue() + 1);
                break;
            default:
                break;
        }
        terminalStateHash.put(state.getSn(), state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoSaveInfo(TerminalMessage message) {
        String terminalId = message.getTerminalId();
        String imei = message.getImei();
        String channel = message.getChannel();
        if (JudgeUtil.isAllBlank(terminalId, imei)) {
            return;
        }
        if (StringUtils.isNotBlank(terminalId) && !terminalService.checkExist(Terminal::getTerminalId, terminalId)) {
            Terminal terminal = new Terminal();
            terminal.setTerminalId(terminalId);
            terminalService.save(terminal);
        }
        if (StringUtils.isNotBlank(imei) && !commoduService.checkExist(Commodu::getImei, imei)) {
            Commodu commodu = new Commodu();
            commodu.setImei(imei);
            commodu.setCommoduType(channel);
            commoduService.save(commodu);
        }
        if (JudgeUtil.isAllNotBlank(terminalId, imei)
                && !terminalCommoduService.checkExist(new LambdaQueryWrapper<TerminalCommodu>()
                        .eq(TerminalCommodu::getTerminalId, terminalId).eq(TerminalCommodu::getImei, imei))) {
            terminalCommoduService.lambdaUpdate().eq(TerminalCommodu::getTerminalId, terminalId).or()
                    .eq(TerminalCommodu::getImei, imei).remove();
            TerminalCommodu terminalCommodu = new TerminalCommodu();
            terminalCommodu.setTerminalId(terminalId);
            terminalCommodu.setImei(imei);
            terminalCommoduService.save(terminalCommodu);
            snImeiHash.put(terminalId, imei);
            imeiSnHash.put(imei, terminalId);
        }
    }

}
