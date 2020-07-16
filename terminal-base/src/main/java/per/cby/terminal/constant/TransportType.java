package per.cby.terminal.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 传输类型
 * 
 * @author chenboyang
 * @since 2019年11月12日
 *
 */
@Getter
@RequiredArgsConstructor
public enum TransportType {

    REPORT("report", "上报"),
    ISSUED("issued", "下发");

    private final String code;
    private final String desc;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举
     */
    public static TransportType value(String code) {
        for (TransportType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
