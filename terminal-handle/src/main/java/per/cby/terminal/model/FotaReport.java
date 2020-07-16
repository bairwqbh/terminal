package per.cby.terminal.model;

import java.time.LocalDateTime;

import per.cby.terminal.constant.FotaReportOrder;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * FOTA上报信息
 * </p>
 *
 * @author chenboyang
 * @since 2020年3月23日
 */
@Data
@Accessors(chain = true)
public class FotaReport {

    /** 协议版本号 */
    private Integer version;

    /** 升级编号 */
    private String upgradeId;

    /** 终端编号 */
    private String terminalId;

    /** 对接网关 */
    private String gateway;

    /** 数据载荷 */
    private byte[] payload;

    /** 时间戳 */
    private LocalDateTime timestamp;

    /** 上报指令 */
    private FotaReportOrder order;

    /** 版本序列号 */
    private Integer versionSerial;

    /** 分片序列号 */
    private Integer shardSerial;

}
