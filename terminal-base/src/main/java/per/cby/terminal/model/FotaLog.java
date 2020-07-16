package per.cby.terminal.model;

import java.time.LocalDateTime;

import per.cby.frame.mongo.model.BaseMongoModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * FOTA升级日志
 * </p>
 *
 * @author chenboyang
 * @since 2020年3月23日
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
@ApiModel(value = "FotaLog对象", description = "FOTA升级日志")
public class FotaLog extends BaseMongoModel {

    @ApiModelProperty("升级编号")
    private String upgradeId;

    @ApiModelProperty("终端编号")
    private String terminalId;

    @ApiModelProperty("对接网关")
    private String gateway;

    @ApiModelProperty("传输类型")
    private String transportType;

    @ApiModelProperty("动作指令")
    private String order;

    @ApiModelProperty("日志信息")
    private String info;

    @ApiModelProperty("时间戳")
    private LocalDateTime timestamp;

}
