package per.cby.terminal.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.util.StringUtil;
import per.cby.terminal.mapper.TerminalFotaMapper;
import per.cby.terminal.model.TerminalFota;
import per.cby.terminal.service.TerminalFotaService;

/**
 * <p>
 * 终端程序升级关系 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Service("__TERMINAL_FOTA_SERVICE__")
public class TerminalFotaServiceImpl extends AbstractService<TerminalFotaMapper, TerminalFota> implements TerminalFotaService {

    @Override
    protected Wrapper<TerminalFota> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<TerminalFota>()
                .eq(StringUtil.isNotEmpty(param.get("upgradeId")), TerminalFota::getUpgradeId, param.get("upgradeId"))
                .eq(StringUtil.isNotEmpty(param.get("terminalId")), TerminalFota::getTerminalId,
                        param.get("terminalId"))
                .orderByAsc(TerminalFota::getCreateTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bind(String upgradeId, List<String> terminalIdList) {
        lambdaUpdate().eq(TerminalFota::getUpgradeId, upgradeId).remove();
        if (CollectionUtils.isNotEmpty(terminalIdList)) {
            return saveBatch(terminalIdList.stream()
                    .map(terminalId -> new TerminalFota().setUpgradeId(upgradeId).setTerminalId(terminalId))
                    .collect(Collectors.toList()));
        }
        return true;
    }

}
