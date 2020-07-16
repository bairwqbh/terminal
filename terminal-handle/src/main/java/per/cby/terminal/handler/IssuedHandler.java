package per.cby.terminal.handler;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.util.JsonUtil;
import per.cby.terminal.service.DataService;

import lombok.extern.slf4j.Slf4j;

/**
 * 终端数据下发处理器
 * 
 * @author chenboyang
 * @since 2019年12月3日
 *
 */
@Slf4j
@Component("__ISSUED_HANDLER__")
public class IssuedHandler implements Consumer<TerminalMessage> {

    @Autowired
    private DataService dataService;

    @Override
    public void accept(TerminalMessage message) {
        log.info("收到终端下发消息，数据内容为：{}", JsonUtil.toJson(message));
        dataService.saveIssued(message);
    }

}
