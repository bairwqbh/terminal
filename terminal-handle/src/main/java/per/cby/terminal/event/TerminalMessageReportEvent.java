package per.cby.terminal.event;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.event.AbstractEvent;

/**
 * 终端消息上报事件
 * 
 * @author chenboyang
 * @since 2019年12月9日
 *
 */
public class TerminalMessageReportEvent extends AbstractEvent<TerminalMessage> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public TerminalMessageReportEvent(TerminalMessage message) {
        super(message);
    }

}
