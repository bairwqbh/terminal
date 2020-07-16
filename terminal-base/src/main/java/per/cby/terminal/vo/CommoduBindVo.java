package per.cby.terminal.vo;

import java.util.List;

import per.cby.frame.common.util.BaseUtil;
import per.cby.terminal.model.Commodu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 模组绑定信息
 * </p>
 *
 * @author chenboyang
 * @since 2019-06-19
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CommoduBindVo对象", description = "模组绑定信息")
public class CommoduBindVo {

    @ApiModelProperty("模组编号列表")
    private List<Commodu> list = BaseUtil.newArrayList();

    @ApiModelProperty("绑定的模组编号列表")
    private List<String> value = BaseUtil.newArrayList();

}
