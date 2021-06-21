package org.jeecg.modules.wallclicklog.entity;

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
 * @Description: wall_click_log
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
@Data
@TableName("wall_click_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wall_click_log对象", description="wall_click_log")
public class WallClickLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id",hidden=true)
    private Integer id;
	/**clickId*/
	@Excel(name = "clickId", width = 15)
    @ApiModelProperty(value = "clickId")
    private String clickId;
	/**uuidCode*/
	@Excel(name = "uuidCode", width = 15)
    @ApiModelProperty(value = "uuidCode")
    private String uuidCode;
	/**clickUa*/
	@Excel(name = "clickUa", width = 15)
    @ApiModelProperty(value = "clickUa")
    private String clickUa;
	/**clickIp*/
	@Excel(name = "clickIp", width = 15)
    @ApiModelProperty(value = "clickIp")
    private String clickIp;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**0表示未删除,1表示删除*/
	@Excel(name = "0表示未删除,1表示删除", width = 15)
    @ApiModelProperty(value = "0表示未删除,1表示删除")
    private String delFlag;
}
