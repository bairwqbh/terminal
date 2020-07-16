package per.cby.terminal.event;

import per.cby.frame.common.event.AbstractEvent;
import per.cby.terminal.model.Fota;

/**
 * FOTA执行事件
 * 
 * @author chenboyang
 * @since 2019年12月9日
 *
 */
public class FotaExecuteEvent extends AbstractEvent<Fota> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public FotaExecuteEvent(Fota fota) {
        super(fota);
    }

}
