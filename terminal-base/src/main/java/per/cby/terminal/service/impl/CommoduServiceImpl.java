package per.cby.terminal.service.impl;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.StringUtil;
import per.cby.frame.ext.util.ExcelUtil;
import per.cby.terminal.vo.CommoduBindVo;
import per.cby.terminal.mapper.CommoduMapper;
import per.cby.terminal.model.Commodu;
import per.cby.terminal.model.TerminalCommodu;
import per.cby.terminal.service.CommoduService;
import per.cby.terminal.service.TerminalCommoduService;

/**
 * <p>
 * 通信模组 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Service("__COMMODU_SERVICE__")
public class CommoduServiceImpl extends AbstractService<CommoduMapper, Commodu> implements CommoduService {

    @Autowired
    private TerminalCommoduService terminalCommoduService;

    @Override
    protected Wrapper<Commodu> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Commodu>()
                .like(param.get("keyword") != null, Commodu::getImei, param.get("keyword"))
                .eq(StringUtil.isNotBlank(param.get("typeId")), Commodu::getCommoduType, param.get("typeId"))
                .orderByDesc(Commodu::getId);
    }

    @Override
    public CommoduBindVo bindInfo(String terminalId) {
        CommoduBindVo dto = new CommoduBindVo();
        List<TerminalCommodu> terminalCommoduList = terminalCommoduService.lambdaQuery()
                .isNotNull(TerminalCommodu::getImei).list();
        List<String> bindList = null;
        List<String> value = null;
        if (StringUtils.isNotBlank(terminalId)) {
            bindList = terminalCommoduList.stream().filter(t -> !terminalId.equals(t.getTerminalId()))
                    .map(TerminalCommodu::getImei).collect(Collectors.toList());
            value = terminalCommoduList.stream().filter(t -> terminalId.equals(t.getTerminalId()))
                    .map(TerminalCommodu::getImei).collect(Collectors.toList());
        } else {
            bindList = terminalCommoduList.stream().map(TerminalCommodu::getImei).collect(Collectors.toList());
            value = BaseUtil.newArrayList();
        }
        List<Commodu> list = lambdaQuery().notIn(CollectionUtils.isNotEmpty(bindList), Commodu::getImei, bindList)
                .list();
        dto.setList(list);
        dto.setValue(value);
        return dto;
    }

    @Override
    public void expTpl(OutputStream output) {
        ExcelUtil.export(BaseUtil.newArrayList("模组编号", "模组类型", "物联网卡号", "IMSI", "MSISDN"), null, output);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean impData(MultipartFile file) {
        boolean result = false;
        List<Commodu> list = ExcelUtil.read(file, Commodu.class, "imei", "commoduType", "iotNo", "imsi", "msisdn");
        if (CollectionUtils.isNotEmpty(list)) {
            list.removeIf(t -> StringUtils.isBlank(t.getImei()));
            if (CollectionUtils.isNotEmpty(list)) {
                result = saveBatch(list);
            }
        }
        return result;
    }

    @Override
    public void expData(Map<String, Object> param, OutputStream output) {
        List<Commodu> list = list(param);
        List<String> head = BaseUtil.newArrayList("模组编号", "模组类型", "物联网卡号", "IMSI", "MSISDN");
        List<List<?>> body = list.stream().map(t -> {
            List<Object> filedList = BaseUtil.newArrayList();
            filedList.add(t.getImei());
            filedList.add(t.getCommoduType());
            filedList.add(t.getIotNo());
            filedList.add(t.getImsi());
            filedList.add(t.getMsisdn());
            return filedList;
        }).collect(Collectors.toList());
        ExcelUtil.export(head, body, output);
    }

}
