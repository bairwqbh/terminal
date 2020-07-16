package per.cby.terminal.service;

import java.util.function.Consumer;
import java.util.function.Function;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.terminal.constant.FotaIssuedOrder;
import per.cby.terminal.model.Fota;
import per.cby.terminal.model.FotaReport;

import io.netty.buffer.ByteBuf;

/**
 * FOTA升级协议业务服务
 * 
 * @author chenboyang
 * @since 2019年12月5日
 *
 */
public interface FotaProtocolService {

    /**
     * 上报解析
     * 
     * @param message 上报信息
     * @return 解析数据
     */
    FotaReport reportParse(TerminalMessage message);

    /**
     * 生成下发元信息报文数据
     * 
     * @param fota FOTA升级信息
     * @return 报文数据
     */
    byte[] issuedMate(Fota fota);

    /**
     * 生成下发分片报文数据
     * 
     * @param fotaReport 上报信息
     * @param shard      分片数据
     * @return 报文数据
     */
    byte[] issuedShard(FotaReport fotaReport, byte[] shard);

    /**
     * 生成下发指令报文数据
     * 
     * @param order 指令
     * @return 报文数据
     */
    default byte[] issuedOrder(FotaIssuedOrder order) {
        return protocolWrap(order, null);
    }

    /**
     * 报文协议封装
     * 
     * @param order    指令
     * @param function 业务函数
     * @return 协议数据
     */
    byte[] protocolWrap(FotaIssuedOrder order, Function<ByteBuf, ByteBuf> function);

    /**
     * 报文协议封装
     * 
     * @param consumer 业务函数
     * @return 协议数据
     */
    byte[] protocolWrap(Consumer<ByteBuf> consumer);

}
