package per.cby.terminal.mongo;

import org.springframework.stereotype.Component;

import per.cby.frame.mongo.annotation.MongoStorage;
import per.cby.frame.mongo.storage.MongoDBStorage;
import per.cby.terminal.model.FotaLog;

/**
 * FOTA升级日志 Mongo存储
 * 
 * @author chenboyang
 * @since 2020年3月23日
 *
 */
@Component("__FOTA_LOG_MONGO__")
@MongoStorage(name = "terminal.fota.log")
public class FotaLogMongo implements MongoDBStorage<FotaLog> {

}
