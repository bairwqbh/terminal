package per.cby.terminal.handler;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.collect.common.constant.CollectConstant;
import per.cby.collect.common.model.TerminalMessage;
import per.cby.terminal.annotation.ReportCommonHandle;
import per.cby.terminal.service.FotaProcessService;

/**
 * 终端FOTA交互处理器
 * 
 * @author chenboyang
 * @since 2019年12月3日
 *
 */
@Component("__FOTA_INTER_HANDLER__")
public class FotaInterHandler implements Consumer<TerminalMessage>, CollectConstant {

    @Autowired
    private FotaProcessService fotaProcessService;

    @Override
    @ReportCommonHandle
    public void accept(TerminalMessage message) {
        fotaProcessService.associate(message);
    }

}
