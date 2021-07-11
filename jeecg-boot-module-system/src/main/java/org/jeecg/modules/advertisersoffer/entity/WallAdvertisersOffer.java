package org.jeecg.modules.advertisersoffer.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

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
 * @Description: wall_advertisers_offer
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
 */
@Data
@TableName("wall_advertisers_offer")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wall_advertisers_offer对象", description="wall_advertisers_offer")
public class WallAdvertisersOffer implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private String name;
	/**pkg*/
    @Excel(name = "pkg", width = 15)
    @ApiModelProperty(value = "pkg")
    private String pkg;
	/**tracklink*/
    @Excel(name = "点击地址", width = 15)
    @ApiModelProperty(value = "tracklink")
    private String tracklink;
	/**icon*/
    @Excel(name = "icon", width = 15)
    @ApiModelProperty(value = "icon")
    private String icon;
	/**previewlink*/
    @Excel(name = "预览地址", width = 15)
    @ApiModelProperty(value = "previewlink")
    private String previewlink;
    /**
     * 平台(0：android  1：ios ）
     */
    @Excel(name = "平台", width = 15, dicCode="offer_platform")
    @Dict(dicCode = "offer_platform")
    private Integer platform;
	/**payout*/
    @Excel(name = "payout", width = 15)
    @ApiModelProperty(value = "payout")
    private BigDecimal payout;
	/**cap*/
    @Excel(name = "cap", width = 15)
    @ApiModelProperty(value = "cap")
    private Integer cap;
	/**kpi*/
    @Excel(name = "kpi", width = 15)
    @ApiModelProperty(value = "kpi")
    private String kpi;
	/**des*/
    @Excel(name = "des", width = 15)
    @ApiModelProperty(value = "des")
    private String des;
	/**weight*/
    @Excel(name = "权重", width = 15)
    @ApiModelProperty(value = "weight")
    private Integer weight;
	/**操作说明解释配图json*/
    @ApiModelProperty(value = "操作说明解释配图json")
    private String creatives;
    /**
     * 状态(1：正常  2：冻结 ）
     */
    @Excel(name = "状态", width = 15,dicCode="offer_status")
    @Dict(dicCode = "offer_status")
    private Integer status;
	/**积分*/
    @Excel(name = "积分", width = 15)
    @ApiModelProperty(value = "积分")
    private Integer goal;
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
    @ApiModelProperty(value = "0表示未删除,1表示删除")
    private Integer delFlag;
    /**
     * 关联广告主
     */
    @Excel(name="广告主",width = 15,dictTable ="wall_advertisers",dicText = "name",dicCode = "id")
    @Dict(dictTable ="wall_advertisers",dicText = "name",dicCode = "id")
    private String advertisersId;

    private transient String advertiserName;

    @Excel(name = "事件名称", width = 15)
    @ApiModelProperty(value = "事件名称")
    private String eventName;

    @Excel(name = "任务类型", width = 15,dicCode="offer_category")
    @ApiModelProperty(value = "任务类型")
    @Dict(dicCode="offer_category")
    private Integer category;

    @Excel(name = "难易程度", width = 15,dicCode="estimate_days")
    @ApiModelProperty(value = "难易程度")
    @Dict(dicCode = "estimate_days")
    private Integer estimateDays;

    @Excel(name = "国家", width = 15,dicCode="country")
    @ApiModelProperty(value = "国家")
    @Dict(dicCode = "country")
    private String country;

    @Excel(name = "设备类型", width = 15,dicCode="device_type")
    @ApiModelProperty(value = "设备类型")
    @Dict(dicCode = "device_type")
    private Integer deviceType;
}
