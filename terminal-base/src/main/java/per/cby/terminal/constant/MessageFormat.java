package per.cby.terminal.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 报文格式
 * 
 * @author chenboyang
 * @since 2020年5月28日
 *
 */
@Getter
@RequiredArgsConstructor
public enum MessageFormat {

    HEX("hex", "16进制"),
    STRING("string", "字符串"),
    BASE64("base64", "Base64");

    private final String code;
    private final String desc;

    /**
     * 根据代码获取枚举
     * 
     * @param code 代码
     * @return 枚举
     */
    public static MessageFormat value(String code) {
        for (MessageFormat value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
