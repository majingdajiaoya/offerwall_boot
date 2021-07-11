package org.jeecg.modules.wallofferstasks.entity;

import lombok.Data;

@Data
public class TaskOfferListVo {
    private Integer id;//taskID
    private String offerName;
    private Integer offerId;
    private Integer playerId;
    private String icon;
    private Integer status;
    private String des;
    private String goal;
}
