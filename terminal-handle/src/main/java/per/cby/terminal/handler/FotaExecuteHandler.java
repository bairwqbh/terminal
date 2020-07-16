package per.cby.terminal.handler;

import java.util.function.Consumer;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.frame.common.exception.BusinessAssert;
import per.cby.terminal.model.Fota;
import per.cby.terminal.service.FotaProcessService;

import lombok.extern.slf4j.Slf4j;

/**
 * 终端FOTA执行处理器
 * 
 * @author chenboyang
 * @since 2019年12月3日
 *
 */
@Slf4j
@Component("__FOTA_EXECUTE_HANDLER__")
public class FotaExecuteHandler implements Consumer<Fota> {

    @Autowired
    private FotaProcessService fotaProcessService;

    @Override
    public void accept(Fota fota) {
        log.info("终端FOTA执行消费，数据内容为：{}", fota);
        BusinessAssert.isTrue(fota != null && CollectionUtils.isNotEmpty(fota.getTerminalIdList()),
                "FOTA升级信息为空或绑定终端为空");
        fotaProcessService.assoStart(fota);
    }

}
