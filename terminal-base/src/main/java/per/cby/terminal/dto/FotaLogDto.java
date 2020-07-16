package per.cby.terminal.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * FOTA升级日志查询参数
 * </p>
 *
 * @author chenboyang
 * @since 2020年3月23日
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "FotaLogDto对象", description = "FOTA升级日志查询参数")
public class FotaLogDto {

    @ApiModelProperty("升级编号")
    private String upgradeId;

    @ApiModelProperty("终端编号")
    private String terminalId;

}
