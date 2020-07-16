package per.cby.terminal.vo;

import java.util.List;

import per.cby.frame.common.util.BaseUtil;
import per.cby.terminal.model.Terminal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 终端绑定信息
 * </p>
 *
 * @author chenboyang
 * @since 2019-06-19
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "TerminalBindVo对象", description = "终端绑定信息")
public class TerminalBindVo {

    @ApiModelProperty("终端编号列表")
    private List<Terminal> list = BaseUtil.newArrayList();

    @ApiModelProperty("绑定的终端编号列表")
    private List<String> value = BaseUtil.newArrayList();

}
