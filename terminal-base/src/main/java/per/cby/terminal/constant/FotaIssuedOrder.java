package per.cby.terminal.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * FOTA升级下发指令
 * 
 * @author chenboyang
 * @since 2019年12月4日
 *
 */
@Getter
@RequiredArgsConstructor
public enum FotaIssuedOrder {

    ISSUED_MATE("issued_mate", 0x01, "发送升级包元信息"),
    ISSUED_SHARD("issued_shard", 0x02, "发送升级包分片数据"),
    NO_MATE("no_mate", 0x03, "无升级包元信息"),
    NO_SHARD("no_shard", 0x04, "无分片数据");

    /** 代码 */
    private final String code;

    /** 数据 */
    private final int data;

    /** 描述 */
    private final String desc;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举
     */
    public static FotaIssuedOrder value(String code) {
        for (FotaIssuedOrder order : values()) {
            if (order.getCode().equals(code)) {
                return order;
            }
        }
        return null;
    }

    /**
     * 根据数据获取枚举
     * 
     * @param data 数据
     * @return 枚举
     */
    public static FotaIssuedOrder value(int data) {
        for (FotaIssuedOrder order : values()) {
            if (order.getData() == data) {
                return order;
            }
        }
        return null;
    }

}
