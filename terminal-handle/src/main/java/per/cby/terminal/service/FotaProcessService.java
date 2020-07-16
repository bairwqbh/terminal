package per.cby.terminal.service;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.terminal.constant.AssoMode;
import per.cby.terminal.model.Fota;

/**
 * FOTA升级业务处理服务
 * 
 * @author chenboyang
 * @since 2020年3月20日
 *
 */
public interface FotaProcessService {

    /**
     * FOTA升级异步关联启动
     * 
     * @param fota 终端程序升级信息
     */
    void assoStart(Fota fota);

    /**
     * FOTA升级消息异步关联
     * 
     * @param message 请求消息
     * @return 响应消息
     */
    void associate(TerminalMessage message);

    /**
     * FOTA升级消息同步交换
     * 
     * @param message 请求消息
     * @return 响应消息
     */
    TerminalMessage exchange(TerminalMessage message);

    /**
     * FOTA升级消息异步关联发送
     * 
     * @param mode    关联模式
     * @param message 消息
     */
    void assoSend(AssoMode mode, TerminalMessage message);

}
