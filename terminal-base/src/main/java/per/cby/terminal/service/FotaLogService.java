package per.cby.terminal.service;

import java.io.OutputStream;
import java.util.List;

import per.cby.frame.common.model.Page;
import per.cby.frame.mongo.storage.MongoDBStorage;
import per.cby.terminal.dto.FotaLogDto;
import per.cby.terminal.model.FotaLog;

/**
 * <p>
 * FOTA升级日志 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-14
 */
public interface FotaLogService {

    /**
     * 获取Mongo存储
     * 
     * @return Mongo存储
     */
    MongoDBStorage<FotaLog> mongoStorage();

    /**
     * 保存FOTA日志
     * 
     * @param fotaLog FOTA日志
     */
    void save(FotaLog fotaLog);

    /**
     * 保存FOTA日志列表
     * 
     * @param list FOTA日志列表
     */
    void saveAll(List<FotaLog> list);

    /**
     * 查询FOTA日志列表
     * 
     * @param dto 查询参数
     * @return 列表
     */
    List<FotaLog> list(FotaLogDto dto);

    /**
     * 分页查询FOTA日志记录
     * 
     * @param page 分页信息
     * @param dto  查询参数
     * @return FOTA日志记录
     */
    Page<FotaLog> page(Page<FotaLog> page, FotaLogDto dto);

    /**
     * 导出FOTA日志
     * 
     * @param dto    查询参数
     * @param output 数据输入流
     */
    void exp(FotaLogDto dto, OutputStream output);

}
