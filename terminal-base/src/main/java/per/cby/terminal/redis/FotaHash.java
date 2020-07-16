package per.cby.terminal.redis;

import org.springframework.stereotype.Repository;

import per.cby.frame.redis.annotation.RedisStorage;
import per.cby.frame.redis.storage.hash.DefaultRedisHashStorage;
import per.cby.terminal.model.Fota;

/**
 * FOTA升级信息缓存，key为升级编号value为升级信息
 * 
 * @author chenboyang
 * @since 2019年11月29日
 *
 */
@Repository("__FOTA_HASH__")
@RedisStorage("terminal:fota:hash")
public class FotaHash extends DefaultRedisHashStorage<String, Fota> {

//    @Autowired
//    private FotaService fotaService;
//
//    @Override
//    public Fota get(String hashKey) {
//        Fota fota = super.get(hashKey);
//        if (fota == null) {
//            fota = fotaService.lambdaQuery().eq(Fota::getUpgradeId, hashKey)
//                    .eq(Fota::getStatus, FotaStatus.PROCESS.getCode()).one();
//            if (fota != null) {
//                put(hashKey, fota);
//            }
//        }
//        return fota;
//    }

}
