package org.jeecg.modules.walladvertisers.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.walladvertisers.entity.WallAdvertisers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: wall_advertisers
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
public interface WallAdvertisersMapper extends BaseMapper<WallAdvertisers> {
    /**
     * 查询被逻辑删除的advertiser
     */
    List<WallAdvertisers> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<WallAdvertisers> wrapper);
    /**
     * 还原被逻辑删除的advertiser
     */
    int revertLogicDeleted(@Param("advertiserIds") String advertiserIds, @Param("entity") WallAdvertisers entity);

    /**
     * 彻底删除被逻辑删除的advertiser
     */
    int deleteLogicDeleted(@Param("advertiserIds") String advertiserIds);
}
