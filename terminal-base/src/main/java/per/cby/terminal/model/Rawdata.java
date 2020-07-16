package per.cby.terminal.model;

import java.time.LocalDateTime;
import java.util.Map;

import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.mongo.model.BaseMongoModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 终端报文数据
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
@ApiModel(value = "Rawdata对象", description = "终端报文数据")
public class Rawdata extends BaseMongoModel {

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

    @ApiModelProperty("载荷")
    private byte[] payload;

    @ApiModelProperty("数据长度")
    private Integer dataLength;

    @ApiModelProperty("时间戳")
    private LocalDateTime timestamp;

    @ApiModelProperty("消息头部参数")
    private Map<String, Object> header = BaseUtil.newHashMap();

}
