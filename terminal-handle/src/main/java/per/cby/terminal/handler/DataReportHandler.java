package per.cby.terminal.handler;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.constant.CommMode;
import per.cby.frame.common.event.EventPublisher;
import per.cby.frame.rabbitmq.exchange.TopicExchange;
import per.cby.terminal.annotation.ReportCommonHandle;
import per.cby.terminal.constant.HandleConstant;
import per.cby.terminal.event.TerminalMessageReportEvent;

/**
 * 终端数据上报处理器
 * 
 * @author chenboyang
 * @since 2019年12月3日
 *
 */
@Component("__DATA_REPORT_HANDLER__")
public class DataReportHandler implements Consumer<TerminalMessage>, HandleConstant {

    @Autowired(required = false)
    private CommMode commMode;

    @Autowired(required = false)
    private TopicExchange topicExchange;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    @ReportCommonHandle
    public void accept(TerminalMessage message) {
        switch (commMode) {
            case MQ:
                topicExchange.send(TERMINAL_MESSAGE_REPORT, message);
                break;
            case EVENT:
                eventPublisher.publish(new TerminalMessageReportEvent(message));
                break;
            default:
                break;
        }
    }

}
