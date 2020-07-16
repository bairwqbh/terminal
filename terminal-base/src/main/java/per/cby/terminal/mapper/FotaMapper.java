package per.cby.terminal.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import per.cby.terminal.model.Fota;
import per.cby.frame.common.base.BaseMapper;

/**
 * <p>
 * 终端程序升级 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Repository("__FOTA_MAPPER__")
public interface FotaMapper extends BaseMapper<Fota> {

    /**
     * 生成FOTA新版本序列
     * 
     * @param typeId 类型编号
     * @return 版本序列
     */
    @Select("SELECT CASE WHEN MAX(version_serial) IS NOT NULL THEN MAX(version_serial) + 1 ELSE 1 END FROM ter_fota WHERE type_id = #{typeId}")
    Integer genSerial(@Param("typeId") String typeId);

}
