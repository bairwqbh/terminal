package per.cby.terminal.service;

import java.io.OutputStream;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import per.cby.frame.common.base.BaseService;
import per.cby.terminal.bo.MessageIssued;
import per.cby.terminal.bo.TerminalConfig;
import per.cby.terminal.model.Terminal;
import per.cby.terminal.vo.TerminalBindVo;

/**
 * <p>
 * 终端 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
public interface TerminalService extends BaseService<Terminal> {

    /**
     * 获取终端绑定信息
     * 
     * @return 终端绑定信息
     */
    default TerminalBindVo bindInfo() {
        return bindInfo(null);
    }

    /**
     * 获取终端绑定信息
     * 
     * @param imei 模组编号
     * @return 终端绑定信息
     */
    TerminalBindVo bindInfo(String imei);

    /**
     * 获取终端FOTA绑定信息
     * 
     * @param typeId    终端类型编号
     * @param upgradeId 升级编号
     * @return 终端FOTA绑定信息
     */
    TerminalBindVo fotaBindInfo(String typeId, String upgradeId);

    /**
     * 导出数据模板
     * 
     * @param output 数据输入流
     */
    void expTpl(OutputStream output);

    /**
     * 导入数据
     * 
     * @param input 数据输入流
     * @return 操作结果
     */
    boolean impData(MultipartFile file);

    /**
     * 导出数据
     * 
     * @param param  查询参数
     * @param output 数据输入流
     */
    void expData(Map<String, Object> param, OutputStream output);

    /**
     * 报文下发
     * 
     * @param messageIssued 报文下发信息
     * @return 操作结果
     */
    boolean messageIssued(MessageIssued messageIssued);

    /**
     * 终端配置
     * 
     * @param config 终端配置
     * @return 操作结果
     */
    boolean config(TerminalConfig config);

}
