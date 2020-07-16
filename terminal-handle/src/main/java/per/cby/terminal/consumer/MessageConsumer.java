package per.cby.terminal.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.constant.FlagString;
import per.cby.frame.rabbitmq.RabbitMQExchangeNames;
import per.cby.terminal.constant.HandleConstant;
import per.cby.terminal.handler.DataReportHandler;
import per.cby.terminal.handler.FotaExecuteHandler;
import per.cby.terminal.handler.FotaInterHandler;
import per.cby.terminal.handler.IssuedHandler;
import per.cby.terminal.handler.MessageIssuedAdapter;
import per.cby.terminal.handler.RegistHandler;
import per.cby.terminal.model.Fota;
import com.rabbitmq.client.ConnectionFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 终端消息基础消费者
 * 
 * @author chenboyang
 * @since 2019年10月29日
 *
 */
@Slf4j
@Component("__MESSAGE_CONSUMER__")
@ConditionalOnBean(ConnectionFactory.class)
public class MessageConsumer implements HandleConstant {

    @Autowired
    private DataReportHandler dataReportHandler;

    @Autowired
    private IssuedHandler issuedHandler;

    @Autowired
    private FotaExecuteHandler fotaExecuteHandler;

    @Autowired
    private FotaInterHandler fotaInterHandler;

    @Autowired
    private RegistHandler registHandler;

    @Autowired
    private MessageIssuedAdapter messageIssuedAdapter;

    /**
     * 终端消息数据上报消费
     * 
     * @param message 终端消息
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = REPORT_QUEUE, durable = FlagString.TRUE),
        exchange = @Exchange(value = RabbitMQExchangeNames.TOPIC, type = ExchangeTypes.TOPIC, durable = FlagString.TRUE),
        key = TERMINAL_REPORT
    ))
    public void dataReport(TerminalMessage message) {
        try {
            dataReportHandler.accept(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 终端消息下发消费
     * 
     * @param message 终端消息
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = ISSUED_QUEUE, durable = FlagString.TRUE),
        exchange = @Exchange(value = RabbitMQExchangeNames.TOPIC, type = ExchangeTypes.TOPIC, durable = FlagString.TRUE),
        key = TERMINAL_ISSUED_KEY
    ))
    public void issued(TerminalMessage message) {
        try {
            issuedHandler.accept(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 终端FOTA执行消费
     * 
     * @param fota 升级信息
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = FOTA_EXECUTE, durable = FlagString.TRUE),
        exchange = @Exchange(value = RabbitMQExchangeNames.TOPIC, type = ExchangeTypes.TOPIC, durable = FlagString.TRUE),
        key = FOTA_EXECUTE
    ))
    public void fotaExecute(Fota fota) {
        try {
            fotaExecuteHandler.accept(fota);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 终端FOTA上报消费
     * 
     * @param message 终端消息
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = FOTA_REPORT, durable = FlagString.TRUE),
        exchange = @Exchange(value = RabbitMQExchangeNames.TOPIC, type = ExchangeTypes.TOPIC, durable = FlagString.TRUE),
        key = FOTA_REPORT
    ))
    public void fotaReport(TerminalMessage message) {
        try {
            fotaInterHandler.accept(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 终端注册消费
     * 
     * @param message 终端消息
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = TERMINAL_REGIST, durable = FlagString.TRUE),
        exchange = @Exchange(value = RabbitMQExchangeNames.TOPIC, type = ExchangeTypes.TOPIC, durable = FlagString.TRUE),
        key = TERMINAL_REGIST
    ))
    public void regist(TerminalMessage message) {
        try {
            registHandler.accept(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 报文下发消费
     * 
     * @param message 终端消息
     */
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = MESSAGE_ISSUED, durable = FlagString.TRUE),
        exchange = @Exchange(value = RabbitMQExchangeNames.TOPIC, type = ExchangeTypes.TOPIC, durable = FlagString.TRUE),
        key = MESSAGE_ISSUED
    ))
    public void messageIssued(TerminalMessage message) {
        try {
            messageIssuedAdapter.accept(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
