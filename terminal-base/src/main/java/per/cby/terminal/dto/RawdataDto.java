package per.cby.terminal.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 终端报文数据查询参数
 * </p>
 *
 * @author chenboyang
 * @since 2020年5月8日
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "RawdataDto对象", description = "终端报文数据查询参数")
public class RawdataDto {

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("消息编号")
    private String messageId;

    @ApiModelProperty("终端编号")
    private String terminalId;

    @ApiModelProperty("通信模组编号")
    private String imei;

    @ApiModelProperty("通道编号")
    private String channelId;

    @ApiModelProperty("传输类型")
    private String transportType;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

}
