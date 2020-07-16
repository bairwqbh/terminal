package per.cby.terminal.event;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.event.AbstractEvent;

/**
 * 报文下发事件
 * 
 * @author chenboyang
 * @since 2020年5月29日
 *
 */
public class MessageIssuedEvent extends AbstractEvent<TerminalMessage> {

    private static final long serialVersionUID = 1L;

    /**
     * 构建报文下发事件
     * 
     * @param message 报文消息
     */
    public MessageIssuedEvent(TerminalMessage message) {
        super(message);
    }

}
