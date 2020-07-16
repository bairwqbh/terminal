package per.cby.terminal.redis;

import org.springframework.stereotype.Repository;

import per.cby.frame.redis.annotation.RedisStorage;
import per.cby.frame.redis.storage.hash.DefaultRedisHashStorage;

/**
 * 终端升级关系映射，key为终端编号value为升级编号
 * 
 * @author chenboyang
 * @since 2019年11月29日
 *
 */
@Repository("__TERMINAL_FOTA_HASH__")
@RedisStorage("terminal:fota:relate:hash")
public class TerminalFotaHash extends DefaultRedisHashStorage<String, String> {

//    @Autowired
//    private TerminalFotaService terminalFotaService;
//
//    @Autowired
//    private FotaService fotaService;
//
//    @Override
//    public String get(String hashKey) {
//        String value = super.get(hashKey);
//        if (value == null && !has(hashKey)) {
//            TerminalFota terminalFota = terminalFotaService.getOne(new LambdaQueryWrapper<TerminalFota>()
//                    .eq(TerminalFota::getTerminalId, hashKey).orderByDesc(TerminalFota::getId));
//            if (terminalFota != null) {
//                boolean check = SqlHelper
//                        .retBool(fotaService.lambdaQuery().eq(Fota::getUpgradeId, terminalFota.getUpgradeId())
//                                .eq(Fota::getStatus, FotaStatus.PROCESS.getCode()).count());
//                if (check) {
//                    value = terminalFota.getUpgradeId();
//                }
//            }
//            put(hashKey, value);
//        }
//        return value;
//    }

}
