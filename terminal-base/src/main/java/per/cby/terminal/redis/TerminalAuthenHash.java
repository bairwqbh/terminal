package per.cby.terminal.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.frame.redis.annotation.RedisStorage;
import per.cby.frame.redis.storage.value.DefaultCatalogRedisValueStorage;
import per.cby.terminal.model.Terminal;
import per.cby.terminal.service.TerminalService;

/**
 * 终端权限映射，key为终端编号value为是否授权
 * 
 * @author chenboyang
 * @since 2020年3月19日
 *
 */
@Component("__TERMINAL_AUTHEN_HASH__")
@RedisStorage("terminal:authen:hash")
public class TerminalAuthenHash extends DefaultCatalogRedisValueStorage<Boolean> {

    @Autowired
    private TerminalService terminalService;

    @Override
    public Boolean get(String hashKey) {
        Boolean value = super.get(hashKey);
        if (value == null) {
            value = terminalService.checkExist(Terminal::getTerminalId, hashKey);
            if (value) {
                set(hashKey, value);
            } else {
                set(hashKey, value, 1, TimeUnit.MINUTES);
            }
        }
        return value;
    }

}
