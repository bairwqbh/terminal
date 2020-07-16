package per.cby.terminal.service;

import per.cby.terminal.model.TerminalFota;

import java.util.List;

import per.cby.frame.common.base.BaseService;

/**
 * <p>
 * 终端程序升级关系 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
public interface TerminalFotaService extends BaseService<TerminalFota> {

    /**
     * 绑定FOTA升级终端关系
     * 
     * @param upgradeId    升级编号
     * @param terminalIdList 终端编号列表
     * @return 绑定结果
     */
    boolean bind(String upgradeId, List<String> terminalIdList);

}
