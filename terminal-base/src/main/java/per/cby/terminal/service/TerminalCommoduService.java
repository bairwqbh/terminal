package per.cby.terminal.service;

import java.util.List;

import per.cby.frame.common.base.BaseService;
import per.cby.terminal.model.TerminalCommodu;

/**
 * <p>
 * 终端通信模组关系 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
public interface TerminalCommoduService extends BaseService<TerminalCommodu> {

    /**
     * 绑定终端模组关系
     * 
     * @param terminalId    终端编号
     * @param imeiList 模组编号列表
     * @return 绑定结果
     */
    boolean bindCommodu(String terminalId, List<String> imeiList);

    /**
     * 绑定模组终端关系
     * 
     * @param imei  模组编号
     * @param terminalId 终端编号
     * @return 绑定结果
     */
    boolean bindTerminal(String imei, String terminalId);

}
