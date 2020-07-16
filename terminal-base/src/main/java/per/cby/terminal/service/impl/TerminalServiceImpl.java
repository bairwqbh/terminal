package per.cby.terminal.service.impl;

import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.collect.common.constant.CollectConstant;
import per.cby.collect.common.constant.GatewayType;
import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.constant.CommMode;
import per.cby.frame.common.event.EventPublisher;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.CodeUtil;
import per.cby.frame.common.util.DateUtil;
import per.cby.frame.common.util.StringUtil;
import per.cby.frame.ext.util.ExcelUtil;
import per.cby.frame.rabbitmq.exchange.TopicExchange;
import per.cby.terminal.bo.MessageIssued;
import per.cby.terminal.bo.TerminalConfig;
import per.cby.terminal.constant.MessageFormat;
import per.cby.terminal.constant.TerminalConstant;
import per.cby.terminal.event.MessageIssuedEvent;
import per.cby.terminal.mapper.TerminalMapper;
import per.cby.terminal.model.Terminal;
import per.cby.terminal.model.TerminalCommodu;
import per.cby.terminal.model.TerminalFota;
import per.cby.terminal.redis.TerminalAuthenHash;
import per.cby.terminal.redis.TerminalConfigHash;
import per.cby.terminal.redis.TerminalStateHash;
import per.cby.terminal.service.TerminalCommoduService;
import per.cby.terminal.service.TerminalFotaService;
import per.cby.terminal.service.TerminalService;
import per.cby.terminal.vo.TerminalBindVo;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 终端 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Slf4j
@Service("__TERMINAL_SERVICE__")
public class TerminalServiceImpl extends AbstractService<TerminalMapper, Terminal>
        implements TerminalService, TerminalConstant {

    @Autowired
    private TerminalCommoduService terminalCommoduService;

    @Autowired
    private TerminalFotaService terminalFotaService;

    @Autowired
    private TerminalStateHash terminalStateHash;

    @Autowired
    private TerminalConfigHash terminalConfigHash;

    @Autowired
    private TerminalAuthenHash terminalAuthenHash;

    @Autowired(required = false)
    private CommMode commMode = CommMode.EVENT;

    @Autowired(required = false)
    private TopicExchange topicExchange;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    protected Wrapper<Terminal> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Terminal>()
                .and(param.get("keyword") != null,
                        w -> w.like(Terminal::getTerminalId, param.get("keyword")).or().like(Terminal::getTerminalName,
                                param.get("keyword")))
                .eq(StringUtil.isNotBlank(param.get("typeId")), Terminal::getTypeId, param.get("typeId"))
                .orderByDesc(Terminal::getId);
    }

    @Override
    protected List<Terminal> valueQueryResult(List<Terminal> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(terminal -> {
                terminal.setState(terminalStateHash.get(terminal.getTerminalId()));
                terminal.setConfig(terminalConfigHash.get(terminal.getTerminalId()));
            });
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Terminal entity) {
        boolean result = super.save(entity);
        if (result) {
            terminalAuthenHash.set(entity.getTerminalId(), true);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        Terminal terminal = getById(id);
        boolean result = super.removeById(id);
        if (result && terminal != null) {
            terminalAuthenHash.delete(terminal.getTerminalId());
            terminalStateHash.delete(terminal.getTerminalId());
            terminalConfigHash.delete(terminal.getTerminalId());
        }
        return result;
    }

    @Override
    public TerminalBindVo bindInfo(String imei) {
        TerminalBindVo dto = new TerminalBindVo();
        List<String> value = null;
        if (StringUtils.isNotBlank(imei)) {
            value = terminalCommoduService.lambdaQuery().select(TerminalCommodu::getTerminalId)
                    .eq(TerminalCommodu::getImei, imei).list().stream().map(TerminalCommodu::getTerminalId)
                    .collect(Collectors.toList());
        } else {
            value = BaseUtil.newArrayList();
        }
        dto.setList(list());
        dto.setValue(value);
        return dto;
    }

    @Override
    public TerminalBindVo fotaBindInfo(String typeId, String upgradeId) {
        TerminalBindVo dto = new TerminalBindVo();
        List<Terminal> list = lambdaQuery().eq(StringUtils.isNotBlank(typeId), Terminal::getTypeId, typeId).list();
        List<String> value = null;
        if (StringUtils.isNotBlank(upgradeId)) {
            value = terminalFotaService.lambdaQuery().select(TerminalFota::getTerminalId)
                    .eq(TerminalFota::getUpgradeId, upgradeId).list().stream().map(TerminalFota::getTerminalId)
                    .collect(Collectors.toList());
        } else {
            value = BaseUtil.newArrayList();
        }
        dto.setList(list);
        dto.setValue(value);
        return dto;
    }

    @Override
    public void expTpl(OutputStream output) {
        ExcelUtil.export(BaseUtil.newArrayList("终端编号", "终端名称", "终端类型"), null, output);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean impData(MultipartFile file) {
        boolean result = false;
        List<Terminal> list = ExcelUtil.read(file, Terminal.class, "terminalId", "terminalName", "typeId");
        if (CollectionUtils.isNotEmpty(list)) {
            list.removeIf(t -> StringUtils.isBlank(t.getTerminalId()));
            if (CollectionUtils.isNotEmpty(list)) {
                result = saveBatch(list);
                if (result) {
                    terminalAuthenHash
                            .multiSet(list.stream().collect(Collectors.toMap(Terminal::getTerminalId, t -> true)));
                }
            }
        }
        return result;
    }

    @Override
    public void expData(Map<String, Object> param, OutputStream output) {
        List<Terminal> list = list(param);
        List<String> head = BaseUtil.newArrayList("终端编号", "终端名称", "终端类型编号", "终端使用开始时间", "终端使用结束时间", "程序版本号", "版本序列",
                "使用次数", "状态");
        List<List<?>> body = list.stream().map(t -> {
            List<Object> filedList = BaseUtil.newArrayList();
            filedList.add(t.getTerminalId());
            filedList.add(t.getTerminalName());
            filedList.add(t.getTypeId());
            filedList.add(BaseUtil.toStringOrEmpty(t.getUseStartTime(), DateUtil::format));
            filedList.add(BaseUtil.toStringOrEmpty(t.getUseEndTime(), DateUtil::format));
            filedList.add(t.getVersionNo());
            filedList.add(t.getVersionSerial());
            filedList.add(t.getUseNum());
            filedList.add(t.getStatus());
            return filedList;
        }).collect(Collectors.toList());
        ExcelUtil.export(head, body, output);
    }

    @Override
    public boolean messageIssued(MessageIssued messageIssued) {
        GatewayType gateway = GatewayType.value(messageIssued.getGateway());
        BusinessAssert.notNull(gateway, "传输网关错误，无法下发！");
        MessageFormat messageFormat = MessageFormat.value(messageIssued.getFormat());
        BusinessAssert.notNull(gateway, "报文格式错误，无法下发！");
        byte[] payload = null;
        String content = messageIssued.getContent();
        try {
            switch (messageFormat) {
                case HEX:
                    payload = Hex.decodeHex(CodeUtil.trimHex(content));
                    break;
                case STRING:
                    payload = content.getBytes(StandardCharsets.ISO_8859_1);
                    break;
                case BASE64:
                    if (BaseUtil.isBase64(content)) {
                        payload = Base64.decodeBase64(content);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        BusinessAssert.isTrue(ArrayUtils.isNotEmpty(payload), "报文内容解析有误，无法下发！");
        TerminalMessage message = TerminalMessage.create();
        message.setTerminalId(messageIssued.getTerminalId());
        message.setPayload(payload);
        message.getHeader().put(CollectConstant.GATEWAY, messageIssued.getGateway());
        switch (commMode) {
            case MQ:
                topicExchange.send(MESSAGE_ISSUED, message);
                break;
            case EVENT:
                eventPublisher.publish(new MessageIssuedEvent(message));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean config(TerminalConfig config) {
        terminalConfigHash.put(config.getTerminalId(), config);
        return true;
    }

}
