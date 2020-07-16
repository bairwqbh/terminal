package per.cby.terminal.model;

import java.time.LocalDateTime;

import per.cby.terminal.constant.FotaIssuedOrder;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * FOTA下发信息
 * </p>
 *
 * @author chenboyang
 * @since 2020年3月24日
 */
@Data
@Accessors(chain = true)
public class FotaIssued {

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

    /** 下发指令 */
    private FotaIssuedOrder order;

    /** 版本序列号 */
    private Integer versionSerial;

    /** 数据长度 */
    private Integer dataLength;

    /** 分片大小 */
    private Integer shardSize;

    /** 分片数量 */
    private Integer shardNum;

    /** 分片序列号 */
    private Integer shardSerial;

}
