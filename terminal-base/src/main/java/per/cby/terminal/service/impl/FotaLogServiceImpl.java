package per.cby.terminal.service.impl;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import per.cby.collect.common.constant.GatewayType;
import per.cby.frame.common.model.Page;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.DateUtil;
import per.cby.frame.ext.util.ExcelUtil;
import per.cby.frame.mongo.storage.MongoDBStorage;
import per.cby.frame.mongo.util.MongoUtil;
import per.cby.terminal.constant.FotaIssuedOrder;
import per.cby.terminal.constant.FotaReportOrder;
import per.cby.terminal.constant.TransportType;
import per.cby.terminal.dto.FotaLogDto;
import per.cby.terminal.model.FotaLog;
import per.cby.terminal.mongo.FotaLogMongo;
import per.cby.terminal.service.FotaLogService;

/**
 * <p>
 * FOTA升级日志 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2020年3月23日
 */
@Service("__FOTA_LOG_SERVICE__")
public class FotaLogServiceImpl implements FotaLogService {

    @Autowired
    private FotaLogMongo fotaLogMongo;

    @Override
    public MongoDBStorage<FotaLog> mongoStorage() {
        return fotaLogMongo;
    }

    @Override
    public void save(FotaLog fotaLog) {
        fotaLogMongo.save(fotaLog);
    }

    @Override
    public void saveAll(List<FotaLog> list) {
        fotaLogMongo.insertAll(list);
    }

    @Override
    public List<FotaLog> list(FotaLogDto dto) {
        return fotaLogMongo.find(queryWrapper(dto));
    }

    @Override
    public Page<FotaLog> page(Page<FotaLog> page, FotaLogDto dto) {
        return fotaLogMongo.page(page, queryWrapper(dto));
    }

    @Override
    public void exp(FotaLogDto dto, OutputStream output) {
        List<FotaLog> list = list(dto);
        List<String> head = BaseUtil.newArrayList("升级编号", "终端编号", "对接网关", "传输类型", "动作指令", "日志信息", "时间戳");
        List<List<?>> body = list.stream().map(t -> {
            List<Object> filedList = BaseUtil.newArrayList();
            filedList.add(t.getUpgradeId());
            filedList.add(t.getTerminalId());
            filedList.add(Optional.ofNullable(GatewayType.value(t.getGateway())).map(GatewayType::getDesc)
                    .orElse(t.getGateway()));
            filedList.add(Optional.ofNullable(TransportType.value(t.getTransportType())).map(TransportType::getDesc)
                    .orElse(t.getTransportType()));
            String order = t.getOrder();
            FotaReportOrder fro = FotaReportOrder.value(order);
            if (fro != null) {
                order = fro.getDesc();
            } else {
                FotaIssuedOrder fio = FotaIssuedOrder.value(order);
                if (fio != null) {
                    order = fio.getDesc();
                }
            }
            filedList.add(order);
            filedList.add(t.getInfo());
            filedList.add(BaseUtil.toStringOrEmpty(t.getTimestamp(), DateUtil::format));
            return filedList;
        }).collect(Collectors.toList());
        ExcelUtil.export(head, body, output);
    }

    /**
     * 查询方法通用参数封装方法
     * 
     * @param dto 参数
     * @return 参数封装
     */
    private Query queryWrapper(FotaLogDto dto) {
        Query query = new Query();
        if (dto != null) {
            if (StringUtils.isNotEmpty(dto.getUpgradeId())) {
                query.addCriteria(Criteria.where("upgradeId").is(dto.getUpgradeId()));
            }
            if (StringUtils.isNotEmpty(dto.getTerminalId())) {
                query.addCriteria(Criteria.where("terminalId").is(dto.getTerminalId()));
            }
        }
        MongoUtil.orderByDesc(query, "timestamp");
        return query;
    }

}
