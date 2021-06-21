package org.jeecg.modules.wallplayer.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: wall_player
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
@Data
@TableName("wall_player")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wall_player对象", description="wall_player")
public class WallPlayer implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",hidden=true)
    private Integer id;
	/**playerName*/
	@Excel(name = "playerName", width = 15)
    @ApiModelProperty(value = "playerName",required=true)
    private String playerName;
	/**playerPassword*/
	@Excel(name = "playerPassword", width = 15)
    @ApiModelProperty(value = "playerPassword",required=true)
    private String playerPassword;
	/**ip*/
	@Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip",hidden=true)
    private String ip;
	/**ua*/
	@Excel(name = "ua", width = 15)
    @ApiModelProperty(value = "ua",hidden=true)
    private String ua;
	/**status*/
    @Excel(name = "状态", width = 15,dicCode="offer_status")
    @Dict(dicCode = "offer_status")
    @ApiModelProperty(value = "status",hidden=true)
    private Integer status;
	/**创建人*/
    @ApiModelProperty(value = "创建人",hidden=true)
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间",hidden=true)
    private Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人",hidden=true)
    private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间",hidden=true)
    private Date updateTime;
	/**0表示未删除,1表示删除*/
    @ApiModelProperty(value = "0表示未删除,1表示删除",hidden=true)
    private String delFlag;
    @ApiModelProperty(value = "salt",hidden=true)
    private String salt;
}
