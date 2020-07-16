package per.cby.terminal.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * FOTA升级异步关联模式
 * 
 * @author chenboyang
 * @since 2020年3月20日
 *
 */
@Getter
@RequiredArgsConstructor
public enum AssoMode {

    NONE("none", "无"),
    MQTT("mqtt", "MQTT"),
    IRIDIUM("iridium", "铱星"),
    ORBCOMM("orbcomm", "海事");

    private final String code;
    private final String desc;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举
     */
    public static AssoMode value(String code) {
        for (AssoMode value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return NONE;
    }

}
