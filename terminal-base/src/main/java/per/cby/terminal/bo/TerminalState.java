package per.cby.terminal.bo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 终端状态信息
 * 
 * @author chenboyang
 * @since 2020年2月15日
 *
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "TerminalState对象", description = "终端状态信息")
public class TerminalState {

    @ApiModelProperty("终端编号")
    private String sn;

    @ApiModelProperty("模组编号")
    private String imei;

    @ApiModelProperty("传输通道")
    private String channel;

    @ApiModelProperty("网关集合")
    private Set<String> gateway = new HashSet<String>();

    @ApiModelProperty("最早上报时间")
    private LocalDateTime firstReportTime;

    @ApiModelProperty("最后上报时间")
    private LocalDateTime lastReportTime;

    @ApiModelProperty("上报帧计数")
    private Long reportCount;

    @ApiModelProperty("最早下发时间")
    private LocalDateTime firstIssuedTime;

    @ApiModelProperty("最后下发时间")
    private LocalDateTime lastIssuedTime;

    @ApiModelProperty("下发帧计数")
    private Long issuedCount;

}
