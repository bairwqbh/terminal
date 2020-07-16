package per.cby.terminal.service.impl;

import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import per.cby.frame.common.model.Page;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.DateUtil;
import per.cby.frame.common.util.JsonUtil;
import per.cby.frame.common.util.StringUtil;
import per.cby.frame.ext.util.ExcelUtil;
import per.cby.frame.mongo.storage.MongoDBStorage;
import per.cby.frame.mongo.util.MongoUtil;
import per.cby.terminal.dto.RawdataDto;
import per.cby.terminal.model.Rawdata;
import per.cby.terminal.mongo.RawdataMongo;
import per.cby.terminal.service.RawdataService;

/**
 * <p>
 * 终端报文数据 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-14
 */
@Service("__RAWDATA_SERVICE__")
public class RawdataServiceImpl implements RawdataService {

    @Autowired
    private RawdataMongo rawdataMongo;

    @Override
    public MongoDBStorage<Rawdata> mongoStorage() {
        return rawdataMongo;
    }

    @Override
    public void save(Rawdata rawdata) {
        if (rawdata == null) {
            return;
        }
        rawdataMongo.save(rawdata);
    }

    @Override
    public void saveAll(List<Rawdata> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        rawdataMongo.insertAll(list);
    }

    @Override
    public List<Rawdata> list(RawdataDto dto) {
        return rawdataMongo.find(queryWrapper(dto));
    }

    @Override
    public Page<Rawdata> page(Page<Rawdata> page, RawdataDto dto) {
        return rawdataMongo.page(page, queryWrapper(dto));
    }

    @Override
    public void expData(RawdataDto dto, OutputStream output) {
        List<Rawdata> list = list(dto);
        List<String> head = BaseUtil.newArrayList("消息编号", "终端编号", "通信模组编号", "通道编号", "传输类型", "载荷", "数据长度", "时间戳",
                "消息头部参数");
        List<List<?>> body = list.stream().map(t -> {
            List<Object> filedList = BaseUtil.newArrayList();
            filedList.add(t.getMessageId());
            filedList.add(t.getTerminalId());
            filedList.add(t.getImei());
            filedList.add(t.getChannelId());
            filedList.add(t.getTransportType());
            filedList.add(BaseUtil.toStringOrEmpty(t.getPayload(), JsonUtil::toJson));
            filedList.add(t.getDataLength());
            filedList.add(BaseUtil.toStringOrEmpty(t.getTimestamp(), DateUtil::format));
            filedList.add(BaseUtil.toStringOrEmpty(t.getHeader(), JsonUtil::toJson));
            return filedList;
        }).collect(Collectors.toList());
        ExcelUtil.export(head, body, output);
    }

    /**
     * list和page查询方法通用参数封装方法
     * 
     * @param param 参数
     * @return 参数封装
     */
    private Query queryWrapper(RawdataDto dto) {
        Query query = new Query();
        if (dto != null) {
            if (StringUtils.isNotEmpty(dto.getKeyword())) {
                MongoUtil.like(query, dto.getKeyword(), "messageId", "terminalId", "imei");
            }
            if (StringUtil.isNotEmpty(dto.getMessageId())) {
                query.addCriteria(Criteria.where("messageId").is(dto.getMessageId()));
            }
            if (StringUtil.isNotEmpty(dto.getTerminalId())) {
                query.addCriteria(Criteria.where("terminalId").is(dto.getTerminalId()));
            }
            if (StringUtil.isNotEmpty(dto.getImei())) {
                query.addCriteria(Criteria.where("imei").is(dto.getImei()));
            }
            if (StringUtil.isNotEmpty(dto.getChannelId())) {
                query.addCriteria(Criteria.where("channelId").is(dto.getChannelId()));
            }
            if (StringUtil.isNotEmpty(dto.getTransportType())) {
                query.addCriteria(Criteria.where("transportType").is(dto.getTransportType()));
            }
            if (dto.getStartTime() != null || dto.getEndTime() != null) {
                Criteria criteria = Criteria.where("timestamp");
                if (dto.getStartTime() != null) {
                    criteria.gte(dto.getStartTime());
                }
                if (dto.getEndTime() != null) {
                    criteria.lt(dto.getEndTime());
                }
                query.addCriteria(criteria);
            }
        }
        MongoUtil.orderByDesc(query, "timestamp");
        return query;
    }

}
