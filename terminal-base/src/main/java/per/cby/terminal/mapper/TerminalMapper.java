package per.cby.terminal.mapper;

import org.springframework.stereotype.Repository;

import per.cby.terminal.model.Terminal;
import per.cby.frame.common.base.BaseMapper;

/**
 * <p>
 * 终端 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Repository("__TERMINAL_MAPPER__")
public interface TerminalMapper extends BaseMapper<Terminal> {

}
