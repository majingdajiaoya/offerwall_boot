package org.jeecg.modules.advertisersoffer.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;


/**
 * @Description:
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@TableName("wall_advertisers_offer")
@EqualsAndHashCode(callSuper = false)
public class AdvertisersOfferList implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */

    private Integer id;
    private String name;
    private String pkg;
    private String kpi;
    private String des;
    private Integer goal;
    private Integer weight;
    private Integer status;
    private Integer delFlag;
    private String icon;
    private Integer estimateDays;
    private Integer category;
    private Integer deviceType;
}
