package per.cby.terminal.service.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.util.CodeUtil;
import per.cby.terminal.constant.FotaConstant;
import per.cby.terminal.constant.FotaIssuedOrder;
import per.cby.terminal.constant.FotaReportOrder;
import per.cby.terminal.model.Fota;
import per.cby.terminal.model.FotaReport;
import per.cby.terminal.service.FotaProtocolService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * FOTA升级协议业务服务接口简单实现
 * 
 * @author chenboyang
 * @since 2019年12月5日
 *
 */
@Service("__SIMPLE_FOTA_PROTOCOL_SERVICE__")
public class SimpleFotaProtocolService implements FotaProtocolService, FotaConstant {

    @Override
    public FotaReport reportParse(TerminalMessage message) {
        // 校验消息
        BusinessAssert.isTrue(message != null && StringUtils.isNotBlank(message.getTerminalId())
                && ArrayUtils.isNotEmpty(message.getPayload()), "FOTA升级上报信息为空或报文数据为空！");

        // 校验报文总长
        byte[] payload = message.getPayload();
        BusinessAssert.isTrue(payload.length >= MIN_LIMIT, "报文总长低于下限！");
        ByteBuf wrap = Unpooled.copiedBuffer(payload);

        // 校验报文头尾标识
        int head = wrap.readUnsignedShort();
        ByteBuf buf = Unpooled.buffer(wrap.readableBytes() - FOTA_FLAG_LENGTH);
        wrap.readBytes(buf);
        int tail = wrap.readUnsignedShort();
        BusinessAssert.isTrue(head == FOTA_HEAD && tail == FOTA_TAIL, "报文头尾标识错误！");

        // 验证数据校验和
        byte[] bytes = new byte[buf.readableBytes() - CHECKSUM_SIZE];
        buf.readBytes(bytes);
        int checksum = buf.readUnsignedShort();
        BusinessAssert.isTrue(CodeUtil.verifyChecksum(checksum, bytes), "数据校验有误！");
        buf = Unpooled.copiedBuffer(bytes);

        // 解析协议版本
        int version = buf.readUnsignedByte();

        // 解析指令
        FotaReportOrder order = FotaReportOrder.value(buf.readUnsignedByte());
        BusinessAssert.notNull(order, "FOTA升级上报指令错误！");

        // 封装上报信息
        FotaReport fotaReport = new FotaReport();
        fotaReport.setVersion(version);
        fotaReport.setTerminalId(message.getTerminalId());
        fotaReport.setPayload(payload);
        fotaReport.setTimestamp(LocalDateTime.now());
        fotaReport.setOrder(order);

        // 解析载荷
        int length = buf.readUnsignedShort();
        if (length > 0) {
            BusinessAssert.isTrue(buf.readableBytes() >= length, "载荷数据长度有误！");
            buf = ByteBufUtil.readBytes(ByteBufAllocator.DEFAULT, buf, length);
            // 处理获取分片的字段信息
            if (FotaReportOrder.GET_SHARD.equals(order)) {
                // 解析报文体
                BusinessAssert.isTrue(buf.readableBytes() >= GET_SHARD_LIMIT, "获取分片数据报文低于最小限制");
                int versionSerial = buf.readUnsignedShort();
                int shardSerial = buf.readUnsignedShort();
                fotaReport.setVersionSerial(versionSerial);
                fotaReport.setShardSerial(shardSerial);
            }
        }
        return fotaReport;
    }

    @Override
    public byte[] issuedMate(Fota fota) {
        return protocolWrap(FotaIssuedOrder.ISSUED_MATE, buf -> {
            buf.writeShort(fota.getVersionSerial().shortValue());
            buf.writeInt(fota.getDataLength().intValue());
            buf.writeShort(fota.getShardSize().shortValue());
            buf.writeShort(fota.getShardNum().intValue());
            return buf;
        });
    }

    @Override
    public byte[] issuedShard(FotaReport fotaReport, byte[] shard) {
        return protocolWrap(FotaIssuedOrder.ISSUED_SHARD, buf -> {
            buf.writeShort(fotaReport.getVersionSerial().shortValue());
            buf.writeShort(fotaReport.getShardSerial().shortValue());
            buf.writeShort(shard.length);
            buf.writeBytes(shard);
            return buf;
        });
    }

    @Override
    public byte[] protocolWrap(FotaIssuedOrder order, Function<ByteBuf, ByteBuf> function) {
        return protocolWrap(buf -> {
            buf.writeByte(order.getData());
            int length = 0;
            ByteBuf payload = null;
            if (function != null) {
                payload = function.apply(Unpooled.buffer());
                length = payload.readableBytes();
            }
            buf.writeShort(length);
            if (payload != null) {
                buf.writeBytes(payload);
            }
        });
    }

    @Override
    public byte[] protocolWrap(Consumer<ByteBuf> consumer) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(VERSION);
        if (consumer != null) {
            consumer.accept(buf);
        }
        int checksum = CodeUtil.checksum(ByteBufUtil.getBytes(buf));
        buf.writeShort(checksum);
        ByteBuf wrap = Unpooled.buffer();
        wrap.writeShort(FOTA_HEAD);
        wrap.writeBytes(buf);
        wrap.writeShort(FOTA_TAIL);
        byte[] data = ByteBufUtil.getBytes(wrap);
        return data;
    }

}
