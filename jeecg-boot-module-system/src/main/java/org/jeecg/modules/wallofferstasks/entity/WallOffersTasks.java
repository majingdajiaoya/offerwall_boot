package org.jeecg.modules.wallofferstasks.entity;

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
 * @Description: wall_offers_tasks
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
@Data
@TableName("wall_offers_tasks")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wall_offers_tasks对象", description="wall_offers_tasks")
public class WallOffersTasks implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**clickId
     * playerId+offerId+md5(palyerId=x&offerId=x)
     * */
	@Excel(name = "clickId", width = 15)
    @ApiModelProperty(value = "clickId")
    private String clickId;
	/**playerId*/
	@Excel(name = "playerId", width = 15,dictTable ="wall_player",dicText = "player_name",dicCode = "id")
//    @ApiModelProperty(value = "playerId")
    @Dict(dictTable ="wall_player",dicText = "player_name",dicCode = "id")
    private String playerId;

    private transient String playerName;
	/**offerId*/
	@Excel(name = "offerId", width = 15)
    @Dict(dictTable ="wall_advertisers_offer",dicText = "name",dicCode = "id")
//    @ApiModelProperty(value = "offerId")
    private String offerId;

    private transient String offerName;
	/**clickIp*/
	@Excel(name = "clickIp", width = 15)
    @ApiModelProperty(value = "clickIp")
    private String clickIp;
	/**clickUa*/
	@Excel(name = "clickUa", width = 15)
    @ApiModelProperty(value = "clickUa")
    private String clickUa;
    /**
     * 平台(0：android  1：ios ）
     */
    @Excel(name = "平台", width = 15, dicCode="offer_platform")
    @Dict(dicCode = "offer_platform")
    private Integer clickPlatform;
	/**click device*/
	@Excel(name = "clickDevice", width = 15)
    @ApiModelProperty(value = "clickDevice")
    private String clickDevice;
	/**clickOsVersion*/
	@Excel(name = "clickOsVersion", width = 15)
    @ApiModelProperty(value = "clickOsVersion")
    private String clickOsVersion;
	/**1未完成、2完成*/
    @Excel(name = "状态", width = 15,dicCode="task_status")
    @Dict(dicCode = "task_status")
    private Integer status;
	/**clicktime*/
	@Excel(name = "clicktime", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "clicktime")
    private Date clicktime;
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
    @Excel(name = "clickDeviceName", width = 15)
    @ApiModelProperty(value = "clickDeviceName")
	private String clickDeviceName;
}
