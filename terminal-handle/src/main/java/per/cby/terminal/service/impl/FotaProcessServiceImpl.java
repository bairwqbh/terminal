package per.cby.terminal.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import per.cby.collect.common.model.TerminalMessage;
import per.cby.collect.ground.common.service.GroundIssuedService;
import per.cby.collect.satellite.common.service.SatelliteIssuedService;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.exception.BusinessException;
import per.cby.frame.common.util.ThreadPoolUtil;
import per.cby.frame.mqtt.constant.MqttConstant;
import per.cby.terminal.constant.AssoMode;
import per.cby.terminal.constant.FotaConstant;
import per.cby.terminal.constant.FotaIssuedOrder;
import per.cby.terminal.constant.FotaReportOrder;
import per.cby.terminal.constant.FotaStatus;
import per.cby.terminal.constant.TransportType;
import per.cby.terminal.model.Fota;
import per.cby.terminal.model.FotaIssued;
import per.cby.terminal.model.FotaLog;
import per.cby.terminal.model.FotaReport;
import per.cby.terminal.redis.FotaDataShardList;
import per.cby.terminal.redis.FotaHash;
import per.cby.terminal.redis.SnImeiHash;
import per.cby.terminal.redis.TerminalFotaHash;
import per.cby.terminal.service.DataService;
import per.cby.terminal.service.FotaLogService;
import per.cby.terminal.service.FotaProcessService;
import per.cby.terminal.service.FotaProtocolService;
import per.cby.terminal.service.FotaService;

import lombok.extern.slf4j.Slf4j;

/**
 * FOTA升级业务处理服务接口实现
 * 
 * @author chenboyang
 * @since 2020年3月20日
 *
 */
@Slf4j
@Service("__FOTA_PROCESS_SERVICE__")
public class FotaProcessServiceImpl implements FotaProcessService, FotaConstant {

    @Autowired
    private GroundIssuedService groundIssuedService;

    @Autowired
    private SatelliteIssuedService satelliteIssuedService;

    @Autowired
    private TerminalFotaHash terminalFotaHash;

    @Autowired
    private FotaHash fotaHash;

    @Autowired
    private FotaDataShardList fotaDataShardList;

    @Autowired
    private FotaProtocolService fotaProtocolService;

    @Autowired
    private FotaService fotaService;

    @Autowired
    private DataService dataService;

    @Autowired
    private SnImeiHash snImeiHash;

    @Autowired
    private FotaLogService fotaLogService;

    @Override
    public void assoStart(Fota fota) {
        AssoMode mode = AssoMode.value(fota.getAssoMode());
        if (AssoMode.NONE.equals(mode)) {
            return;
        }
        byte[] payload = fotaProtocolService.issuedMate(fota);
        fota.getTerminalIdList().forEach(terminalId -> {
            TerminalMessage message = TerminalMessage.create();
            message.setTerminalId(terminalId);
            message.setImei(snImeiHash.get(terminalId));
            message.setPayload(payload);
            assoSend(mode, message);
        });
        executeLog(fota, mode);
    }

    @Override
    public void associate(TerminalMessage message) {
        String gateway = String.valueOf(message.getHeader().get(GATEWAY));
        AssoMode mode = AssoMode.value(gateway);
        BusinessAssert.isTrue(!AssoMode.NONE.equals(mode), "FOTA升级异步关联模式有误！");
        TerminalMessage issuedMessage = process(message);
        if (issuedMessage == null) {
            return;
        }
        assoSend(mode, issuedMessage);
    }

    @Override
    public TerminalMessage exchange(TerminalMessage message) {
        ThreadPoolUtil.execute(() -> dataService.saveReport(message));
        TerminalMessage issuedMessage = process(message);
        if (issuedMessage != null) {
            ThreadPoolUtil.execute(() -> dataService.saveIssued(issuedMessage));
        }
        return issuedMessage;
    }

    @Override
    public void assoSend(AssoMode mode, TerminalMessage message) {
        switch (mode) {
            case MQTT:
                groundIssuedService.fotaMqttIssued(message, MqttConstant.QOS_1);
                break;
            case IRIDIUM:
                satelliteIssuedService.iridiumIssued(message);
                break;
            case ORBCOMM:
                satelliteIssuedService.orbcommIssued(message);
                break;
            default:
                break;
        }
    }

    /**
     * FOTA升级业务处理
     * 
     * @param message 请求消息
     * @return 响应消息
     */
    private TerminalMessage process(TerminalMessage message) {
        FotaReport fotaReport = fotaProtocolService.reportParse(message);
        fotaReport.setUpgradeId(terminalFotaHash.get(fotaReport.getTerminalId()));
        String gateway = String.valueOf(message.getHeader().get(GATEWAY));
        fotaReport.setGateway(gateway);
        reportLog(fotaReport);// 记录FOTA上报日志
        FotaIssued fotaIssued = null;
        switch (fotaReport.getOrder()) {
            case GET_MATE:// 获取升级元信息
                fotaIssued = getMate(fotaReport);
                break;
            case GET_SHARD:// 获取升级分片数据
                fotaIssued = getShard(fotaReport);
                break;
            case OVER:// 升级完成
            case NO_FOTA:// 不需要升级
                clear(fotaReport);
                break;
            default:
                throw new BusinessException("FOTA升级上报信息指令错误");
        }
        if (fotaIssued != null) {
            issuedLog(fotaIssued);// 记录FOTA下发日志
            if (ArrayUtils.isNotEmpty(fotaIssued.getPayload())) {
                TerminalMessage issuedMessage = TerminalMessage.create();
                issuedMessage.setTerminalId(message.getTerminalId());
                issuedMessage.setImei(message.getImei());
                issuedMessage.setPayload(fotaIssued.getPayload());
                return issuedMessage;
            }
        }
        return null;
    }

    /**
     * 获取升级包元信息
     * 
     * @param fotaReport 上报信息
     */
    private FotaIssued getMate(FotaReport fotaReport) {
        FotaIssued fotaIssued = fotaExchange(fotaReport);
        FotaIssuedOrder order = null;
        byte[] payload = null;
        if (StringUtils.isNotBlank(fotaReport.getUpgradeId())) {
            Fota fota = fotaHash.get(fotaReport.getUpgradeId());
            if (fota != null) {
                fotaIssued.setVersionSerial(fota.getVersionSerial());
                fotaIssued.setDataLength(fota.getDataLength());
                fotaIssued.setShardSize(fota.getShardSize());
                fotaIssued.setShardNum(fota.getShardNum());
                order = FotaIssuedOrder.ISSUED_MATE;
                payload = fotaProtocolService.issuedMate(fota);
            }
        }
        if (order == null) {
            order = FotaIssuedOrder.NO_MATE;
            payload = fotaProtocolService.issuedOrder(order);
        }
        fotaIssued.setOrder(order);
        fotaIssued.setPayload(payload);
        return fotaIssued;
    }

    /**
     * 获取分片数据
     * 
     * @param fotaReport 上报信息
     */
    private FotaIssued getShard(FotaReport fotaReport) {
        FotaIssued fotaIssued = fotaExchange(fotaReport);
        FotaIssuedOrder order = null;
        byte[] payload = null;
        if (StringUtils.isNotBlank(fotaReport.getUpgradeId())) {
            Fota fota = fotaHash.get(fotaReport.getUpgradeId());
            if (fota != null && fota.getVersionSerial().intValue() == fotaReport.getVersionSerial().intValue()) {
                byte[] shard = fotaDataShardList.index(fota.getUpgradeId(), fotaReport.getShardSerial().intValue() - 1);
                if (ArrayUtils.isNotEmpty(shard)) {
                    fotaIssued.setShardSize(shard.length);
                    order = FotaIssuedOrder.ISSUED_SHARD;
                    payload = fotaProtocolService.issuedShard(fotaReport, shard);
                }
            }
        }
        if (order == null) {
            order = FotaIssuedOrder.NO_SHARD;
            payload = fotaProtocolService.issuedOrder(order);
        }
        fotaIssued.setOrder(order);
        fotaIssued.setPayload(payload);
        return fotaIssued;
    }

    /**
     * FOTA升级信息清理
     * 
     * @param fotaReport 上报信息
     */
    private void clear(FotaReport fotaReport) {
        String upgradeId = fotaReport.getUpgradeId();
        String terminalId = fotaReport.getTerminalId();
        if (StringUtils.isNotBlank(terminalId)) {
            terminalFotaHash.delete(terminalId);
        }
        if (StringUtils.isNotBlank(upgradeId)
                && (terminalFotaHash.size() == 0 || !terminalFotaHash.values().contains(upgradeId))) {
            fotaHash.delete(upgradeId);
            fotaDataShardList.clear(upgradeId);
            fotaService.lambdaUpdate().set(Fota::getStatus, FotaStatus.OVER.getCode()).eq(Fota::getUpgradeId, upgradeId)
                    .update();
        }
    }

    /**
     * FOTA信息交换
     * 
     * @param fotaReport 上报信息
     * @return 下发信息
     */
    private FotaIssued fotaExchange(FotaReport fotaReport) {
        FotaIssued fotaIssued = new FotaIssued();
        fotaIssued.setVersion(fotaReport.getVersion());
        fotaIssued.setUpgradeId(fotaReport.getUpgradeId());
        fotaIssued.setTerminalId(fotaReport.getTerminalId());
        fotaIssued.setGateway(fotaReport.getGateway());
        fotaIssued.setTimestamp(LocalDateTime.now());
        fotaIssued.setVersionSerial(fotaReport.getVersionSerial());
        fotaIssued.setShardSerial(fotaReport.getShardSerial());
        return fotaIssued;
    }

    /**
     * FOTA升级上报信息日志记录
     * 
     * @param fotaReport 上报信息
     */
    private void reportLog(FotaReport fotaReport) {
        try {
            FotaLog fotaLog = new FotaLog();
            fotaLog.setUpgradeId(fotaReport.getUpgradeId());
            fotaLog.setTerminalId(fotaReport.getTerminalId());
            fotaLog.setGateway(fotaReport.getGateway());
            fotaLog.setTransportType(TransportType.REPORT.getCode());
            fotaLog.setTimestamp(fotaReport.getTimestamp());
            FotaReportOrder order = fotaReport.getOrder();
            fotaLog.setOrder(order.getCode());
            StringBuilder sb = new StringBuilder(order.getDesc());
            switch (order) {
                case GET_MATE:
                    break;
                case GET_SHARD:
                    sb.append(",升级包序号").append(fotaReport.getVersionSerial());
                    sb.append(",分片序号").append(fotaReport.getShardSerial());
                    break;
                case OVER:
                    break;
                case NO_FOTA:
                    break;
                default:
                    break;
            }
            fotaLog.setInfo(sb.toString());
            fotaLogService.save(fotaLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * FOTA升级上报信息日志记录
     * 
     * @param fotaIssued 下发信息
     */
    private void issuedLog(FotaIssued fotaIssued) {
        try {
            FotaLog fotaLog = new FotaLog();
            fotaLog.setUpgradeId(fotaIssued.getUpgradeId());
            fotaLog.setTerminalId(fotaIssued.getTerminalId());
            fotaLog.setGateway(fotaIssued.getGateway());
            fotaLog.setTransportType(TransportType.ISSUED.getCode());
            fotaLog.setTimestamp(fotaIssued.getTimestamp());
            FotaIssuedOrder order = fotaIssued.getOrder();
            fotaLog.setOrder(order.getCode());
            StringBuilder sb = new StringBuilder(order.getDesc());
            switch (order) {
                case ISSUED_MATE:
                    sb.append(",升级包序号").append(fotaIssued.getVersionSerial());
                    sb.append(",升级包总长").append(fotaIssued.getDataLength()).append("字节");
                    sb.append(",分片长度").append(fotaIssued.getShardSize()).append("字节");
                    sb.append(",分片数量").append(fotaIssued.getShardNum()).append("片");
                    break;
                case ISSUED_SHARD:
                    sb.append(",升级包序号").append(fotaIssued.getVersionSerial());
                    sb.append(",分片序号").append(fotaIssued.getShardSerial());
                    sb.append(",分片长度").append(fotaIssued.getShardSize()).append("字节");
                    break;
                case NO_MATE:
                    break;
                case NO_SHARD:
                    sb.append(",升级包序号").append(fotaIssued.getVersionSerial());
                    sb.append(",分片序号").append(fotaIssued.getShardSerial());
                    break;
                default:
                    break;
            }
            fotaLog.setInfo(sb.toString());
            fotaLogService.save(fotaLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * FOTA升级关联执行日志记录
     * 
     * @param fota 升级信息
     */
    private void executeLog(Fota fota, AssoMode mode) {
        try {
            String upgradeId = fota.getUpgradeId();
            String transportType = TransportType.ISSUED.getCode();
            FotaIssuedOrder order = FotaIssuedOrder.ISSUED_MATE;
            LocalDateTime now = LocalDateTime.now();
            StringBuilder sb = new StringBuilder();
            List<FotaLog> list = fota.getTerminalIdList().stream().map(terminalId -> {
                FotaLog fotaLog = new FotaLog();
                fotaLog.setUpgradeId(upgradeId);
                fotaLog.setTerminalId(terminalId);
                fotaLog.setGateway(mode.getCode());
                fotaLog.setTransportType(transportType);
                fotaLog.setOrder(order.getCode());
                fotaLog.setTimestamp(now);
                sb.delete(0, sb.length());
                sb.append(order.getDesc());
                sb.append(",升级包序号").append(fota.getVersionSerial());
                sb.append(",升级包总长").append(fota.getDataLength()).append("字节");
                sb.append(",分片长度").append(fota.getShardSize()).append("字节");
                sb.append(",分片数量").append(fota.getShardNum()).append("片");
                fotaLog.setInfo(sb.toString());
                return fotaLog;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(list)) {
                fotaLogService.saveAll(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
