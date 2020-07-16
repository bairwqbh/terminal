package per.cby.terminal.aspect;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.util.JsonUtil;
import per.cby.frame.common.util.ThreadPoolUtil;
import per.cby.terminal.redis.ImeiSnHash;
import per.cby.terminal.redis.SnImeiHash;
import per.cby.terminal.service.DataService;

import lombok.extern.slf4j.Slf4j;

/**
 * 上报业务统一处理切面
 * 
 * @author chenboyang
 * @since 2020年2月28日
 *
 */
@Slf4j
@Aspect
@Component("__REPORT_HANDLE_ASPECT__")
public class ReportHandleAspect {

    /** 匹配表达式 */
    private static final String EXPRESSION = "@annotation(per.cby.terminal.annotation.ReportCommonHandle)";

    @Autowired
    private SnImeiHash snImeiHash;

    @Autowired
    private ImeiSnHash imeiSnHash;

    @Autowired
    private DataService dataService;

    /**
     * 执行前拦截处理终端消息
     * 
     * @param point    连接点
     * @param logInter 日志信息
     */
    @Before(EXPRESSION)
    public void before(JoinPoint point) {
        Object[] args = point.getArgs();
        if (ArrayUtils.isEmpty(args) || args[0] == null || !TerminalMessage.class.equals(args[0].getClass())) {
            return;
        }
        TerminalMessage message = (TerminalMessage) args[0];
        log.info("收到终端上报消息，数据内容为：{}", JsonUtil.toJson(message));
        if (message.getTerminalId() == null && message.getImei() != null) {
            message.setTerminalId(imeiSnHash.get(message.getImei()));
        }
        if (message.getImei() == null && message.getTerminalId() != null) {
            message.setImei(snImeiHash.get(message.getTerminalId()));
        }
        ThreadPoolUtil.execute(() -> dataService.saveReport(message));
    }

}
