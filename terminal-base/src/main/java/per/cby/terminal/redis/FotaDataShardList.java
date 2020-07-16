package per.cby.terminal.redis;

import org.springframework.stereotype.Component;

import per.cby.frame.redis.annotation.RedisStorage;
import per.cby.frame.redis.storage.list.FlexRedisListStorageImpl;

/**
 * FOTA升级数据分片列表存储，key为升级编号value为分片列表
 * 
 * @author chenboyang
 * @since 2019年11月29日
 *
 */
//@Slf4j
@Component("__FOTA_DATA_SHARD_LIST__")
@RedisStorage("terminal:fota:data:shard:list")
public class FotaDataShardList extends FlexRedisListStorageImpl<byte[]> {

//    @Autowired
//    private FotaHash fotaHash;
//
//    @Autowired
//    public AttachService attachService;
//
//    @Autowired
//    @Qualifier(GridFsStorage.BEAN_NAME)
//    private FileStorage fileStorage;
//
//    @Override
//    public byte[] index(String key, long index) {
//        byte[] shard = super.index(key, index);
//        if (shard == null && !hasKey(key(key))) {
//            try {
//                Fota fota = fotaHash.get(key);
//                if (fota != null) {
//                    Attach attach = attachService.lambdaQuery().eq(Attach::getBucket, FotaService.STORAGE_BUCKET)
//                            .eq(Attach::getDomainId, FotaService.DOMAIN).eq(Attach::getRowId, fota.getUpgradeId())
//                            .eq(Attach::getFieldId, FotaService.BIN_FIELD).one();
//                    if (attach != null) {
//                        byte[] data = new byte[fota.getShardSize()];
//                        int length = 0;
//                        try (InputStream input = fileStorage.find(attach.getBucket(), attach.getName())) {
//                            while ((length = input.read(data)) > 0) {
//                                rightPush(fota.getUpgradeId(), ArrayUtils.subarray(data, 0, length));
//                            }
//                        } catch (IOException e) {
//                            log.error(e.getMessage(), e);
//                        }
//                        shard = super.index(key, index);
//                    }
//                }
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            }
//        }
//        return shard;
//    }

}
