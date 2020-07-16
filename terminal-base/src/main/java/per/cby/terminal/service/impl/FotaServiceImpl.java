package per.cby.terminal.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.constant.CommMode;
import per.cby.frame.common.event.EventPublisher;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.file.storage.StorageType;
import per.cby.frame.common.file.storage.storage.FileStorage;
import per.cby.frame.common.util.IDUtil;
import per.cby.frame.common.util.JudgeUtil;
import per.cby.frame.common.util.StringUtil;
import per.cby.frame.mongo.storage.GridFsStorage;
import per.cby.frame.rabbitmq.exchange.TopicExchange;
import per.cby.system.model.Attach;
import per.cby.system.service.AttachService;
import per.cby.terminal.constant.FotaStatus;
import per.cby.terminal.constant.TerminalConstant;
import per.cby.terminal.event.FotaExecuteEvent;
import per.cby.terminal.mapper.FotaMapper;
import per.cby.terminal.model.Fota;
import per.cby.terminal.model.TerminalFota;
import per.cby.terminal.redis.FotaDataShardList;
import per.cby.terminal.redis.FotaHash;
import per.cby.terminal.redis.TerminalFotaHash;
import per.cby.terminal.service.FotaService;
import per.cby.terminal.service.TerminalFotaService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 终端程序升级 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Slf4j
@Service("__FOTA_SERVICE__")
public class FotaServiceImpl extends AbstractService<FotaMapper, Fota> implements FotaService, TerminalConstant {

    /** 内部通信模式 */
    @Autowired(required = false)
    private CommMode commMode = CommMode.EVENT;

    @Autowired
    private AttachService attachService;

    @Autowired
    private TerminalFotaService terminalFotaService;

    @Autowired
    private TerminalFotaHash terminalFotaHash;

    @Autowired
    private FotaHash fotaHash;

    @Autowired
    private FotaDataShardList fotaDataShardList;

    @Autowired
    @Qualifier(GridFsStorage.BEAN_NAME)
    private FileStorage fileStorage;

    @Autowired(required = false)
    private TopicExchange topicExchange;

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    protected Wrapper<Fota> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Fota>()
                .like(param.get("keyword") != null, Fota::getVersionNo, param.get("keyword"))
                .eq(StringUtil.isNotBlank(param.get("typeId")), Fota::getTypeId, param.get("typeId"))
                .orderByDesc(Fota::getId);
    }

    @Override
    protected List<Fota> valueQueryResult(List<Fota> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(fota -> {
                if (JudgeUtil.isOneEqual(fota.getStatus(), FotaStatus.INTERRUPT.getCode(), FotaStatus.OVER.getCode())) {
                    String upgradeId = fota.getUpgradeId();
                    boolean isCache = fotaHash.has(upgradeId) || fotaDataShardList.existKey(upgradeId)
                            || (terminalFotaHash.size() > 0 && terminalFotaHash.values().contains(upgradeId));
                    fota.setIsCache(isCache);
                }
            });
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Fota fota, List<String> terminalIdList) {
        boolean result = false;
        fota.setUpgradeId(IDUtil.createUniqueTimeId());
        fota.setVersionSerial(baseMapper.genSerial(fota.getTypeId()));
        fota.setDataLength(fota.getAttach().getSize().intValue());
        fota.setShardable(true);
        fota.setShardSize(512);
        double num = fota.getDataLength().doubleValue() / fota.getShardSize();
        fota.setShardNum((int) Math.ceil(num));
        fota.setPublishTime(LocalDateTime.now());
        if (StringUtils.isBlank(fota.getStatus())) {
            fota.setStatus(FotaStatus.READY.getCode());
        }
        result = super.save(fota);
        if (result && fota.getAttach() != null) {
            fota.getAttach().setStorage(StorageType.MONGO).setBucket(STORAGE_BUCKET).setBucketName(BUCKET_NAME)
                    .setDomainId(DOMAIN).setRowId(fota.getUpgradeId()).setFieldId(BIN_FIELD);
            attachService.save(fota.getAttach());
        }
        if (CollectionUtils.isNotEmpty(terminalIdList)) {
            terminalFotaService.bind(fota.getUpgradeId(), terminalIdList);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        boolean result = false;
        Fota fota = getById(id);
        result = super.removeById(id);
        if (result && fota != null) {
            Attach attach = attachService.getOne(new LambdaQueryWrapper<Attach>().eq(Attach::getBucket, STORAGE_BUCKET)
                    .eq(Attach::getDomainId, DOMAIN).eq(Attach::getRowId, fota.getUpgradeId())
                    .eq(Attach::getFieldId, BIN_FIELD));
            if (attach != null) {
                attachService.removeById(attach.getId());
            }
            terminalFotaService.lambdaUpdate().eq(TerminalFota::getUpgradeId, fota.getUpgradeId()).remove();
            clearCache(fota.getUpgradeId());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAndExecute(Fota fota, List<String> terminalIdList) {
        boolean result = false;
        fota.setStatus(FotaStatus.PROCESS.getCode());
        result = save(fota, terminalIdList);
        if (result) {
            result = execute(fota, terminalIdList);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean execute(Fota fota) {
        boolean result = false;
        fota.setStatus(FotaStatus.PROCESS.getCode());
        result = updateById(fota);
        if (result) {
            List<String> terminalIdList = terminalFotaService.lambdaQuery()
                    .eq(TerminalFota::getUpgradeId, fota.getUpgradeId()).list().stream()
                    .map(TerminalFota::getTerminalId).collect(Collectors.toList());
            result = execute(fota, terminalIdList);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean execute(Fota fota, List<String> terminalIdList) {
        BusinessAssert.notEmpty(terminalIdList, "升级程序未绑定终端，无法执行升级！");
        Attach attach = attachService.lambdaQuery().eq(Attach::getBucket, STORAGE_BUCKET)
                .eq(Attach::getDomainId, DOMAIN).eq(Attach::getRowId, fota.getUpgradeId())
                .eq(Attach::getFieldId, BIN_FIELD).one();
        BusinessAssert.notNull(attach, "升级程序附件信息，无法执行升级！");
        lambdaUpdate().set(Fota::getStatus, FotaStatus.OVER.getCode()).ne(Fota::getUpgradeId, fota.getUpgradeId())
                .eq(StringUtils.isNotBlank(fota.getTypeId()), Fota::getTypeId, fota.getTypeId())
                .eq(Fota::getStatus, FotaStatus.PROCESS.getCode()).update();
        Map<String, String> terminalFotaMap = terminalIdList.stream()
                .collect(Collectors.toMap(Function.identity(), t -> fota.getUpgradeId()));
        terminalFotaHash.putAll(terminalFotaMap);
        fotaHash.put(fota.getUpgradeId(), fota);
        byte[] data = new byte[fota.getShardSize()];
        int length = 0;
        try (InputStream input = fileStorage.find(attach.getBucket(), attach.getName())) {
            while ((length = input.read(data)) > 0) {
                fotaDataShardList.rightPush(fota.getUpgradeId(), ArrayUtils.subarray(data, 0, length));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        fota.setTerminalIdList(terminalIdList);
        switch (commMode) {
            case MQ:
                topicExchange.send(FOTA_EXECUTE, fota);
                break;
            case EVENT:
                eventPublisher.publish(new FotaExecuteEvent(fota));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean interrupt(Fota fota) {
        boolean result = false;
        fota.setStatus(FotaStatus.INTERRUPT.getCode());
        result = updateById(fota);
        if (result) {
            clearCache(fota.getUpgradeId());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean close(Fota fota) {
        boolean result = false;
        fota.setStatus(FotaStatus.OVER.getCode());
        result = updateById(fota);
        if (result) {
            clearCache(fota.getUpgradeId());
        }
        return result;
    }

    @Override
    public boolean check(String typeId) {
        return !checkExist(new LambdaQueryWrapper<Fota>().eq(StringUtils.isNotBlank(typeId), Fota::getTypeId, typeId)
                .eq(Fota::getStatus, FotaStatus.PROCESS.getCode()));
    }

    @Override
    public boolean clearCache(String upgradeId) {
        terminalFotaHash.deleteByValue(upgradeId);
        fotaHash.delete(upgradeId);
        fotaDataShardList.clear(upgradeId);
        return true;
    }

}
