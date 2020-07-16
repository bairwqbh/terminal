package per.cby.terminal.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 通信模组
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Data
@TableName("ter_commodu")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Commodu对象", description = "通信模组")
public class Commodu extends BaseModel<Commodu> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("通信模组编号")
    private String imei;

    @Size(max = 32)
    @ApiModelProperty("通信模组类型")
    private String commoduType;

    @Size(max = 32)
    @ApiModelProperty("通信卡编号")
    private String imsi;

    @Size(max = 32)
    @ApiModelProperty("物联网卡号")
    private String iotNo;

    @Size(max = 32)
    @ApiModelProperty("唯一通信号")
    private String msisdn;

}
