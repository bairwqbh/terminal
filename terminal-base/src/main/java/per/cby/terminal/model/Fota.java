package per.cby.terminal.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;
import per.cby.frame.common.db.mybatis.annotation.AutoId;
import per.cby.system.model.Attach;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 终端程序升级
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Data
@TableName("ter_fota")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Fota对象", description = "终端程序升级")
public class Fota extends BaseModel<Fota> {

    private static final long serialVersionUID = 1L;

    @AutoId
    @TableField(fill = FieldFill.INSERT)
    @Size(max = 32)
    @ApiModelProperty("升级编号")
    private String upgradeId;

    @Size(max = 32)
    @ApiModelProperty("终端类型编号")
    private String typeId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("程序版本号")
    private String versionNo;

    @NotNull
    @ApiModelProperty("版本序列")
    private Integer versionSerial;

    @Size(max = 4000)
    @ApiModelProperty("内容描述")
    private String content;

    @Size(max = 32)
    @ApiModelProperty("关联模式")
    private String assoMode;

    @ApiModelProperty("数据长度")
    private Integer dataLength;
    
    @ApiModelProperty("是否分片")
    private Boolean shardable;

    @ApiModelProperty("分片大小")
    private Integer shardSize;

    @ApiModelProperty("分片数量")
    private Integer shardNum;

    @Size(max = 32)
    @ApiModelProperty("发布人")
    private String publisher;

    @ApiModelProperty("发布时间")
    private LocalDateTime publishTime;

    @Size(max = 32)
    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("是否还有缓存")
    @TableField(exist = false)
    private Boolean isCache;

    /** 版本附件信息 */
    @TableField(exist = false)
    private Attach attach;

    /** 绑定终端列表 */
    @TableField(exist = false)
    private List<String> terminalIdList;

}
