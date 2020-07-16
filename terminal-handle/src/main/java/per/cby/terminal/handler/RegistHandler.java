package per.cby.terminal.handler;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.terminal.annotation.ReportCommonHandle;
import per.cby.terminal.service.DataService;

/**
 * 终端注册处理器
 * 
 * @author chenboyang
 * @since 2019年12月3日
 *
 */
@Component("__REGIST_HANDLER__")
public class RegistHandler implements Consumer<TerminalMessage> {

    @Autowired
    private DataService dataService;

    @Override
    @ReportCommonHandle
    public void accept(TerminalMessage message) {
        dataService.autoSaveInfo(message);
    }

}
