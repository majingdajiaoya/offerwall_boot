package org.jeecg.modules.advertisersoffer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @Description: wall_advertisers_offer
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
 */
public interface IWallAdvertisersOfferService extends IService<WallAdvertisersOffer> {

    /**
     * 查询被逻辑删除的offer
     */
    List<WallAdvertisersOffer> queryLogicDeleted();
    /**
     * 查询被逻辑删除的offer（可拼装查询条件）
     */
    List<WallAdvertisersOffer> queryLogicDeleted(LambdaQueryWrapper<WallAdvertisersOffer> wrapper);

    /**
     * 还原被逻辑删除的offer
     */
    boolean revertLogicDeleted(List<String> asList, WallAdvertisersOffer updateOffer);
    /**
     * 彻底删除被逻辑删除的offer
     */
    boolean removeLogicDeleted(List<String> asList);

    /**
     * 关联查询advertisername
     * @param offerIds
     * @return
     */
    Map<Integer, String> getAdvertiserNamesByIds(List<Integer> offerIds);
}
