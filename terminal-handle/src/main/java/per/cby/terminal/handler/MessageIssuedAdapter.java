package per.cby.terminal.handler;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.collect.common.constant.GatewayType;
import per.cby.collect.common.model.TerminalMessage;
import per.cby.collect.ground.common.service.GroundIssuedService;
import per.cby.collect.satellite.common.constant.SatelliteChannel;
import per.cby.collect.satellite.common.service.SatelliteIssuedService;
import per.cby.frame.common.constant.CommMode;
import per.cby.frame.common.event.EventPublisher;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.util.JsonUtil;
import per.cby.frame.rabbitmq.exchange.TopicExchange;
import per.cby.terminal.constant.HandleConstant;
import per.cby.terminal.event.MessageIssuedCallbackEvent;
import per.cby.terminal.redis.ImeiSnHash;
import per.cby.terminal.redis.SnImeiHash;

import lombok.extern.slf4j.Slf4j;

/**
 * 终端报文下发适配器
 * 
 * @author chenboyang
 * @since 2019年12月3日
 *
 */
@Slf4j
@Component("__MESSAGE_ISSUED_ACTUATOR__")
public class MessageIssuedAdapter implements Consumer<TerminalMessage>, HandleConstant {

    @Autowired
    private SnImeiHash snImeiHash;

    @Autowired
    private ImeiSnHash imeiSnHash;

    @Autowired(required = false)
    private GroundIssuedService groundIssuedService;

    @Autowired(required = false)
    private SatelliteIssuedService satelliteIssuedService;

    @Autowired(required = false)
    private CommMode commMode = CommMode.EVENT;

    @Autowired(required = false)
    private TopicExchange topicExchange;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    public void accept(TerminalMessage message) {
        log.info("收到终端报文下发信息，数据内容为：{}", JsonUtil.toJson(message));
        validate(message, this::issued);
        triggerCallback(message);
    }

    /**
     * 校验消息
     * 
     * @param message  终端消息
     * @param consumer 校验成功消息者
     */
    private void validate(TerminalMessage message, BiConsumer<TerminalMessage, GatewayType> consumer) {
        BusinessAssert.notNull(message, "终端消息为空！");
        String gateway = String.valueOf(message.getHeader().get(GATEWAY));
        BusinessAssert.notNull(gateway, "传输网关为空！");
        GatewayType gatewayType = GatewayType.value(gateway);
        BusinessAssert.notNull(gatewayType, "传输网关错误！");
        if (StringUtils.isBlank(message.getTerminalId()) && StringUtils.isNotBlank(message.getImei())) {
            message.setTerminalId(imeiSnHash.get(message.getImei()));
        }
        BusinessAssert.hasText(message.getTerminalId(), "终端编号为空！");
        if (SatelliteChannel.value(gateway) != null) {
            message.setImei(snImeiHash.get(message.getTerminalId()));
            BusinessAssert.hasText(message.getImei(), "模组编号为空，无法进行卫星通道下发！");
        }
        if (consumer != null) {
            consumer.accept(message, gatewayType);
        }
    }

    /**
     * 进行消息的下发适配
     * 
     * @param message 终端消息
     * @param gateway 传输网关
     */
    private void issued(TerminalMessage message, GatewayType gateway) {
        switch (gateway) {
            case MQTT:
                groundIssuedService.mqttIssued(message);
                break;
            case COAP:
                groundIssuedService.coapIssued(message);
                break;
            case SOCKET:
                groundIssuedService.socketIssued(message);
                break;
            case ECSAT:
                satelliteIssuedService.ecsatIssued(message);
                break;
            case IRIDIUM:
                satelliteIssuedService.iridiumIssued(message);
                break;
            case ORBCOMM:
                satelliteIssuedService.orbcommIssued(message);
                break;
            case BEIDOU:
                satelliteIssuedService.beidouIssued(message);
                break;
            case TIANTONG:
                satelliteIssuedService.tiantongIssued(message);
                break;
            default:
                break;
        }
    }

    /**
     * 触发报文下发完成回调
     * 
     * @param message 终端消息
     */
    private void triggerCallback(TerminalMessage message) {
        switch (commMode) {
            case MQ:
                topicExchange.send(MESSAGE_ISSUED_CALLBACK, message);
                break;
            case EVENT:
                eventPublisher.publish(new MessageIssuedCallbackEvent(message));
                break;
            default:
                break;
        }
    }

}
