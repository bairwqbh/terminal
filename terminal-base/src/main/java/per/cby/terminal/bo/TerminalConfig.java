package per.cby.terminal.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 终端配置信息
 * 
 * @author chenboyang
 * @since 2020年2月15日
 *
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "TerminalConfig对象", description = "终端配置信息")
public class TerminalConfig {

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("终端编号")
    private String terminalId;

    @NotNull
    @ApiModelProperty("清除原始报文")
    private Boolean clearRaw;

}
