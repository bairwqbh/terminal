package per.cby.terminal.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import per.cby.collect.common.event.DataReportEvent;
import per.cby.collect.common.event.FotaReportEvent;
import per.cby.collect.common.event.IssuedEvent;
import per.cby.collect.common.event.RegistEvent;
import per.cby.terminal.event.FotaExecuteEvent;
import per.cby.terminal.event.MessageIssuedEvent;
import per.cby.terminal.handler.DataReportHandler;
import per.cby.terminal.handler.FotaExecuteHandler;
import per.cby.terminal.handler.FotaInterHandler;
import per.cby.terminal.handler.IssuedHandler;
import per.cby.terminal.handler.MessageIssuedAdapter;
import per.cby.terminal.handler.RegistHandler;

/**
 * 终端数据上报监听器
 * 
 * @author chenboyang
 * @since 2019年10月29日
 *
 */
@Component("__TERMINAL_HANDLE_LISTENER__")
public class TerminalHandleListener {

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
     * 终端消息数据上报事件监听
     * 
     * @param message 终端消息
     */
    @Async
    @EventListener(DataReportEvent.class)
    public void dataReport(DataReportEvent event) {
        dataReportHandler.accept(event.getSource());
    }

    /**
     * 终端消息下发事件监听
     * 
     * @param message 终端消息
     */
    @Async
    @EventListener(IssuedEvent.class)
    public void issued(IssuedEvent event) {
        issuedHandler.accept(event.getSource());
    }

    /**
     * 终端FOTA执行事件监听
     * 
     * @param fota 升级信息
     */
    @Async
    @EventListener(FotaExecuteEvent.class)
    public void fotaExecute(FotaExecuteEvent event) {
        fotaExecuteHandler.accept(event.getSource());
    }

    /**
     * 终端FOTA上报事件监听
     * 
     * @param message 终端消息
     */
    @Async
    @EventListener(FotaReportEvent.class)
    public void fotaReport(FotaReportEvent event) {
        fotaInterHandler.accept(event.getSource());
    }

    /**
     * 终端注册事件监听
     * 
     * @param message 终端消息
     */
    @Async
    @EventListener(RegistEvent.class)
    public void regist(RegistEvent event) {
        registHandler.accept(event.getSource());
    }

    /**
     * 报文下发事件监听
     * 
     * @param message 终端消息
     */
    @Async
    @EventListener(RegistEvent.class)
    public void messageIssued(MessageIssuedEvent event) {
        messageIssuedAdapter.accept(event.getSource());
    }

}
