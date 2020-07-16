package per.cby.terminal.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 报文下发信息
 * 
 * @author chenboyang
 * @since 2020年5月28日
 *
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "MessageIssued对象", description = "报文下发信息")
public class MessageIssued {

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("终端编号")
    private String terminalId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("传输网关")
    private String gateway;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("报文格式")
    private String format;

    @NotBlank
    @Size(max = 4000)
    @ApiModelProperty("报文内容")
    private String content;

}
