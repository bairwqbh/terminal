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
 * 终端程序升级关系
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Data
@TableName("ter_terminal_fota")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TerminalFota对象", description = "终端程序升级关系")
public class TerminalFota extends BaseModel<TerminalFota> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("升级编号")
    private String upgradeId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("终端编号")
    private String terminalId;

}
