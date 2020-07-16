package per.cby.terminal.service;

import per.cby.terminal.model.Fota;

import java.util.List;

import per.cby.frame.common.base.BaseService;

/**
 * <p>
 * 终端程序升级 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
public interface FotaService extends BaseService<Fota> {

    /** 存储板块 */
    String STORAGE_BUCKET = "terminal.fota";

    /** 板块名称 */
    String BUCKET_NAME = "FOTA升级";

    /** 业务领域 */
    String DOMAIN = "ter_fota";

    /** 应用包字段 */
    String BIN_FIELD = "bin";

    /**
     * 保存FOTA升级信息
     * 
     * @param fota           FOTA升级信息
     * @param terminalIdList 绑定终端列表
     * @return 操作结果
     */
    boolean save(Fota fota, List<String> terminalIdList);

    /**
     * 保存FOTA升级信息然后执行
     * 
     * @param fota           FOTA升级信息
     * @param terminalIdList 绑定终端列表
     * @return 操作结果
     */
    boolean saveAndExecute(Fota fota, List<String> terminalIdList);

    /**
     * 执行FOTA升级
     * 
     * @param fota FOTA升级信息
     * @return 操作结果
     */
    boolean execute(Fota fota);

    /**
     * 执行FOTA升级
     * 
     * @param fota           FOTA升级信息
     * @param terminalIdList 绑定终端列表
     * @return 操作结果
     */
    boolean execute(Fota fota, List<String> terminalIdList);

    /**
     * 中断FOTA升级
     * 
     * @param fota FOTA升级信息
     * @return 操作结果
     */
    boolean interrupt(Fota fota);

    /**
     * 强制关闭FOTA升级
     * 
     * @param fota FOTA升级信息
     * @return 操作结果
     */
    boolean close(Fota fota);

    /**
     * 检查该类型的终端是否在升级中
     * 
     * @param typeId 终端类型编号
     * @return 检查结果
     */
    boolean check(String typeId);

    /**
     * 清除FOTA升级缓存
     * 
     * @param upgradeId 升级编号
     * @return 操作结果
     */
    boolean clearCache(String upgradeId);

}
