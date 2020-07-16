package per.cby.terminal.service;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.terminal.constant.TransportType;

/**
 * 终端报文数据服务
 * 
 * @author chenboyang
 * @since 2019年11月7日
 *
 */
public interface DataService {

    /**
     * 保存终端上报消息
     * 
     * @param message 终端消息
     */
    default void saveReport(TerminalMessage message) {
        saveRawdata(message, TransportType.REPORT);
    }

    /**
     * 保存终端下发消息
     * 
     * @param message 终端消息
     */
    default void saveIssued(TerminalMessage message) {
        saveRawdata(message, TransportType.ISSUED);
    }

    /**
     * 保存报文数据
     * 
     * @param message 终端消息
     * @param transportType 传输类型
     */
    default void saveRawdata(TerminalMessage message, TransportType transportType) {
        saveRawdata(message, transportType.getCode());
        upateState(message, transportType);
    }

    /**
     * 保存报文数据
     * 
     * @param message 终端消息
     * @param transportType 传输类型
     */
    void saveRawdata(TerminalMessage message, String transportType);

    /**
     * 更新终端状态
     * 
     * @param message       终端消息
     * @param transportType 传输类型
     */
    void upateState(TerminalMessage message, TransportType transportType);

    /**
     * 自动保存终端信息
     * 
     * @param message 终端消息
     */
    void autoSaveInfo(TerminalMessage message);

}
