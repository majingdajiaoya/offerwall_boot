package org.jeecg.modules.walladvertisers.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.modules.walladvertisers.entity.WallAdvertisers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: wall_advertisers
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
public interface IWallAdvertisersService extends IService<WallAdvertisers> {
    /**
     * 查询被逻辑删除的advertiser
     */
    List<WallAdvertisers> queryLogicDeleted();
    /**
     * 查询被逻辑删除的advertiser（可拼装查询条件）
     */
    List<WallAdvertisers> queryLogicDeleted(LambdaQueryWrapper<WallAdvertisers> wrapper);

    /**
     * 还原被逻辑删除的advertiser
     */
    boolean revertLogicDeleted(List<String> asList, WallAdvertisers updateAdvertiser);
    /**
     * 彻底删除被逻辑删除的advertiser
     */
    boolean removeLogicDeleted(List<String> asList);
}
