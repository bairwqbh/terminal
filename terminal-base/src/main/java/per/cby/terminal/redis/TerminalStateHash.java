package per.cby.terminal.redis;

import org.springframework.stereotype.Component;

import per.cby.frame.redis.annotation.RedisStorage;
import per.cby.frame.redis.storage.hash.DefaultRedisHashStorage;
import per.cby.terminal.bo.TerminalState;

/**
 * 终端状态信息哈希存储
 * 
 * @author chenboyang
 * @since 2020年2月15日
 *
 */
@Component("__TERMINAL_STATE_HASH__")
@RedisStorage("terminal:state")
public class TerminalStateHash extends DefaultRedisHashStorage<String, TerminalState> {

    @Override
    public TerminalState get(String hashKey) {
        TerminalState state = super.get(hashKey);
        if (state == null) {
            state = new TerminalState();
            state.setSn(hashKey);
            state.setReportCount(0L);
            state.setIssuedCount(0L);
            put(hashKey, state);
        }
        return state;
    }

}
