package per.cby.terminal.mapper;

import org.springframework.stereotype.Repository;

import per.cby.terminal.model.TerminalFota;
import per.cby.frame.common.base.BaseMapper;

/**
 * <p>
 * 终端程序升级关系 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Repository("__TERMINAL_FOTA_MAPPER__")
public interface TerminalFotaMapper extends BaseMapper<TerminalFota> {

}
