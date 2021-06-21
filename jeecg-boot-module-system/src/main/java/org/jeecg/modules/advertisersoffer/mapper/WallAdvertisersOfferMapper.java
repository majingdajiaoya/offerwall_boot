package org.jeecg.modules.advertisersoffer.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.advertisersoffer.entity.OfferAdvertiserVo;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.entity.SysUser;

/**
 * @Description: wall_advertisers_offer
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
 */
public interface WallAdvertisersOfferMapper extends BaseMapper<WallAdvertisersOffer> {

    /**
     * 查询被逻辑删除的offer
     */
    List<WallAdvertisersOffer> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<WallAdvertisersOffer> wrapper);
    /**
     * 还原被逻辑删除的用户
     */
    int revertLogicDeleted(@Param("offerIds") String offerIds, @Param("entity") WallAdvertisersOffer entity);

    /**
     * 彻底删除被逻辑删除的offer
     */
    int deleteLogicDeleted(@Param("offerIds") String offerIds);

    /**
     * 关联查询广告主name
     * @param offerIds
     * @return
     */
    List<OfferAdvertiserVo> getAdvertiserNamesByIds(List<Integer> offerIds);
}
