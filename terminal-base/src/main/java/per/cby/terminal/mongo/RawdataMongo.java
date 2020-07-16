package per.cby.terminal.mongo;

import org.springframework.stereotype.Component;

import per.cby.frame.mongo.annotation.MongoStorage;
import per.cby.frame.mongo.storage.MongoDBStorage;
import per.cby.terminal.model.Rawdata;

/**
 * 终端报文数据Mongo存储
 * 
 * @author chenboyang
 * @since 2019年11月14日
 *
 */
@Component("__RAWDATA_MONGO__")
@MongoStorage(name = "terminal.rawdata")
public class RawdataMongo implements MongoDBStorage<Rawdata> {

}
