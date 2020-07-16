package per.cby.terminal.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import per.cby.frame.common.base.AbstractService;
import per.cby.terminal.mapper.TerminalCommoduMapper;
import per.cby.terminal.model.TerminalCommodu;
import per.cby.terminal.redis.ImeiSnHash;
import per.cby.terminal.redis.SnImeiHash;
import per.cby.terminal.service.TerminalCommoduService;

/**
 * <p>
 * 终端通信模组关系 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Service("__TERMINAL_COMMODU_SERVICE__")
public class TerminalCommoduServiceImpl extends AbstractService<TerminalCommoduMapper, TerminalCommodu>
        implements TerminalCommoduService {

    @Autowired
    private SnImeiHash snImeiHash;

    @Autowired
    private ImeiSnHash imeiSnHash;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindCommodu(String terminalId, List<String> imeiList) {
        lambdaUpdate().eq(TerminalCommodu::getTerminalId, terminalId).remove();
        if (CollectionUtils.isNotEmpty(imeiList)) {
            saveBatch(imeiList.stream().map(imei -> new TerminalCommodu().setTerminalId(terminalId).setImei(imei))
                    .collect(Collectors.toList()));
            String imei = imeiList.get(imeiList.size() - 1);
            snImeiHash.put(terminalId, imei);
            imeiSnHash.put(imei, terminalId);
        } else {
            String imei = snImeiHash.get(terminalId);
            if (imei != null) {
                snImeiHash.delete(terminalId);
                imeiSnHash.delete(imei);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindTerminal(String imei, String terminalId) {
        lambdaUpdate().eq(TerminalCommodu::getImei, imei).remove();
        if (StringUtils.isNotBlank(terminalId)) {
            save(new TerminalCommodu().setTerminalId(terminalId).setImei(imei));
            snImeiHash.put(terminalId, imei);
            imeiSnHash.put(imei, terminalId);
        } else {
            String sn = imeiSnHash.get(imei);
            if (sn != null) {
                snImeiHash.delete(sn);
                imeiSnHash.delete(imei);
            }
        }
        return true;
    }

}
