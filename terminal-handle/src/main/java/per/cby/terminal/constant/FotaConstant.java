package per.cby.terminal.constant;

import per.cby.collect.common.constant.CollectConstant;

/**
 * FOTA升级常量
 * 
 * @author chenboyang
 * @since 2020年3月25日
 *
 */
public interface FotaConstant extends CollectConstant {

    /** 协议版本号 */
    int VERSION = 1;

    /** 数据长度下限 */
    int MIN_LIMIT = 10;

    /** 校验和字节数 */
    int CHECKSUM_SIZE = 2;

    /** 获取分片数据最小长度 */
    int GET_SHARD_LIMIT = 4;

}
