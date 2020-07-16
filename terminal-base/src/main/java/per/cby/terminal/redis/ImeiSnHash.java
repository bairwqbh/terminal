package per.cby.terminal.redis;

import org.springframework.stereotype.Repository;

import per.cby.frame.redis.annotation.RedisStorage;
import per.cby.frame.redis.storage.hash.DefaultRedisHashStorage;

/**
 * 模组编号与终端编号关系映射，key为模组编号value为终端编号
 * 
 * @author chenboyang
 * @since 2019年11月29日
 *
 */
@Repository("__IMEI_SN_HASH__")
@RedisStorage("terminal:imei:sn:hash")
public class ImeiSnHash extends DefaultRedisHashStorage<String, String> {

//    @Autowired
//    private TerminalCommoduService terminalCommoduService;
//
//    @Override
//    public String get(String hashKey) {
//        String sn = super.get(hashKey);
//        if (sn == null) {
//            TerminalCommodu terminalCommodu = terminalCommoduService.lambdaQuery()
//                    .eq(TerminalCommodu::getImei, hashKey).one();
//            if (terminalCommodu != null) {
//                sn = terminalCommodu.getTerminalId();
//                if (sn != null) {
//                    put(hashKey, sn);
//                }
//            }
//        }
//        return sn;
//    }

}
