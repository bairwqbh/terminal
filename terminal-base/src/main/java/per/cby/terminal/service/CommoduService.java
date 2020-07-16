package per.cby.terminal.service;

import java.io.OutputStream;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import per.cby.frame.common.base.BaseService;
import per.cby.terminal.vo.CommoduBindVo;
import per.cby.terminal.model.Commodu;

/**
 * <p>
 * 通信模组 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
public interface CommoduService extends BaseService<Commodu> {

    /**
     * 获取模组绑定信息
     * 
     * @return 模组绑定信息
     */
    default CommoduBindVo bindInfo() {
        return bindInfo(null);
    }

    /**
     * 获取模组绑定信息
     * 
     * @param terminalId 终端编号
     * @return 模组绑定信息
     */
    CommoduBindVo bindInfo(String terminalId);

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

}
