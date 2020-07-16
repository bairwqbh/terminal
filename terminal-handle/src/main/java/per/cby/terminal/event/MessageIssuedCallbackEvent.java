package per.cby.terminal.event;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.event.AbstractEvent;

/**
 * 报文下发回调事件
 * 
 * @author chenboyang
 * @since 2019年12月9日
 *
 */
public class MessageIssuedCallbackEvent extends AbstractEvent<TerminalMessage> {

    private static final long serialVersionUID = 1L;

    public MessageIssuedCallbackEvent(TerminalMessage message) {
        super(message);
    }

}
