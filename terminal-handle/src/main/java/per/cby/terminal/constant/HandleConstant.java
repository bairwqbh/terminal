package per.cby.terminal.constant;

import per.cby.collect.common.constant.CollectConstant;

/**
 * 终端业务处理常量
 * 
 * @author chenboyang
 * @since 2019年11月6日
 *
 */
public interface HandleConstant extends CollectConstant, TerminalConstant {

    /** 终端上报列队 */
    String REPORT_QUEUE = "terminal.report.base.handle";

    /** 终端下发列队 */
    String ISSUED_QUEUE = "terminal.issued.base.handle";

    /** 终端消息上报 */
    String TERMINAL_MESSAGE_REPORT = "terminal.message.report";

    /** 终端下发路由键 */
    String TERMINAL_ISSUED_KEY = "terminal.issued.#";

    /** 终端报文下发回调 */
    String MESSAGE_ISSUED_CALLBACK = "message.issued.callback";

}
