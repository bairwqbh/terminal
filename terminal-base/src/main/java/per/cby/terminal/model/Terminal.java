package per.cby.terminal.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;
import per.cby.terminal.bo.TerminalConfig;
import per.cby.terminal.bo.TerminalState;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 终端
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Data
@TableName("ter_terminal")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Terminal对象", description = "终端")
public class Terminal extends BaseModel<Terminal> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("终端编号")
    private String terminalId;

    @Size(max = 32)
    @ApiModelProperty("终端名称")
    private String terminalName;

    @Size(max = 32)
    @ApiModelProperty("终端类型编号")
    private String typeId;

    @ApiModelProperty("终端使用开始时间")
    private LocalDateTime useStartTime;

    @ApiModelProperty("终端使用结束时间")
    private LocalDateTime useEndTime;

    @Size(max = 32)
    @ApiModelProperty("程序版本号")
    private String versionNo;

    @ApiModelProperty("版本序列")
    private Integer versionSerial;

    @ApiModelProperty("使用次数")
    private Integer useNum;

    @Size(max = 32)
    @ApiModelProperty("状态")
    private String status;

    @TableField(exist = false)
    @ApiModelProperty("终端状态")
    private TerminalState state = new TerminalState();

    @TableField(exist = false)
    @ApiModelProperty("终端配置")
    private TerminalConfig config = new TerminalConfig();

}
