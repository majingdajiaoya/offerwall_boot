package org.jeecg.modules.walladvertisers.entity;

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
 * @Description: wall_advertisers
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
@Data
@TableName("wall_advertisers")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wall_advertisers对象", description="wall_advertisers")
public class WallAdvertisers implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private String id;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private String name;
	/**0:net-30、1:net-60、2:net-90*/
	@Excel(name = "net", width = 15,dicCode = "advertiser_net")
    @Dict(dicCode = "advertiser_net")
    private Integer net;
    /**
     * 状态(1：正常  2：冻结 ）
     */
    @Excel(name = "状态", width = 15,dicCode="offer_status")
    @Dict(dicCode = "offer_status")
    private Integer status;
	/**点击参数*/
	@Excel(name = "点击参数", width = 15)
    @ApiModelProperty(value = "点击参数")
    private String advertisersUrlParams;
	/**回掉地址*/
	@Excel(name = "回掉地址", width = 15)
    @ApiModelProperty(value = "回掉地址")
    private String callbackUrl;
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
    private Integer delFlag;
}
