package per.cby.terminal.service;

import java.io.OutputStream;
import java.util.List;

import per.cby.frame.common.model.Page;
import per.cby.frame.mongo.storage.MongoDBStorage;
import per.cby.terminal.dto.RawdataDto;
import per.cby.terminal.model.Rawdata;

/**
 * <p>
 * 终端报文数据 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-14
 */
public interface RawdataService {

    /**
     * 获取Mongo存储
     * 
     * @return Mongo存储
     */
    MongoDBStorage<Rawdata> mongoStorage();

    /**
     * 保存报文数据
     * 
     * @param rawdata 报文数据
     */
    void save(Rawdata rawdata);

    /**
     * 保存报文数据列表
     * 
     * @param list 列表
     */
    void saveAll(List<Rawdata> list);

    /**
     * 查询报文数据列表
     * 
     * @param dto 查询参数
     * @return 列表
     */
    List<Rawdata> list(RawdataDto dto);

    /**
     * 分页查询报文数据
     * 
     * @param page 分页信息
     * @param dto  查询参数
     * @return 分页信息
     */
    Page<Rawdata> page(Page<Rawdata> page, RawdataDto dto);

    /**
     * 导出数据
     * 
     * @param dto    查询参数
     * @param output 数据输入流
     */
    void expData(RawdataDto dto, OutputStream output);

}
