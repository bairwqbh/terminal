package per.cby.terminal.redis;

import org.springframework.stereotype.Component;

import per.cby.frame.redis.annotation.RedisStorage;
import per.cby.frame.redis.storage.hash.DefaultRedisHashStorage;

/**
 * 终端编号与模组编号关系映射，key为终端编号value为模组编号
 * 
 * @author chenboyang
 * @since 2019年11月29日
 *
 */
@Component("__SN_IMEI_HASH__")
@RedisStorage("terminal:sn:imei:hash")
public class SnImeiHash extends DefaultRedisHashStorage<String, String> {

//    @Autowired
//    private TerminalCommoduService terminalCommoduService;
//
//    @Override
//    public String get(String hashKey) {
//        String imei = super.get(hashKey);
//        if (imei == null) {
//            TerminalCommodu terminalCommodu = terminalCommoduService.lambdaQuery()
//                    .eq(TerminalCommodu::getTerminalId, hashKey).one();
//            if (terminalCommodu != null) {
//                imei = terminalCommodu.getImei();
//                if (imei != null) {
//                    put(hashKey, imei);
//                }
//            }
//        }
//        return imei;
//    }

}
