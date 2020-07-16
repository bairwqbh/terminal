package per.cby.terminal.redis;

import org.springframework.stereotype.Repository;

import per.cby.frame.redis.annotation.RedisStorage;
import per.cby.frame.redis.storage.hash.DefaultRedisHashStorage;
import per.cby.terminal.bo.TerminalConfig;

/**
 * 终端配置信息哈希存储
 * 
 * @author chenboyang
 * @since 2020年2月15日
 *
 */
@Repository("__TERMINAL_CONFIG_HASH__")
@RedisStorage("terminal:config")
public class TerminalConfigHash extends DefaultRedisHashStorage<String, TerminalConfig> {

    @Override
    public TerminalConfig get(String hashKey) {
        TerminalConfig config = super.get(hashKey);
        if (config == null) {
            config = new TerminalConfig();
            config.setTerminalId(hashKey);
            config.setClearRaw(false);
            put(hashKey, config);
        }
        return config;
    }

}
