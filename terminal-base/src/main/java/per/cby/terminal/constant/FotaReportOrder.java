package per.cby.terminal.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * FOTA升级上报指令
 * 
 * @author chenboyang
 * @since 2019年12月4日
 *
 */
@Getter
@RequiredArgsConstructor
public enum FotaReportOrder {

    GET_MATE("get_mate", 0x01, "获取升级包元信息"),
    GET_SHARD("get_shard", 0x02, "获取分片数据"),
    OVER("over", 0x03, "升级包下载完成"),
    NO_FOTA("no_fota", 0x04, "不需要升级");

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
    public static FotaReportOrder value(String code) {
        for (FotaReportOrder order : values()) {
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
    public static FotaReportOrder value(int data) {
        for (FotaReportOrder order : values()) {
            if (order.getData() == data) {
                return order;
            }
        }
        return null;
    }

}
