package per.cby.terminal.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * FOTA升级状态
 * 
 * @author chenboyang
 * @since 2019年11月26日
 *
 */
@Getter
@RequiredArgsConstructor
public enum FotaStatus {

    READY("ready", "准备"),
    PROCESS("process", "进行中"),
    OVER("over", "结束"),
    INTERRUPT("interrupt", "中断");

    private final String code;
    private final String desc;

}
