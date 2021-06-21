package org.jeecg.modules.walladvertisers.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.walladvertisers.entity.WallAdvertisers;
import org.jeecg.modules.walladvertisers.mapper.WallAdvertisersMapper;
import org.jeecg.modules.walladvertisers.service.IWallAdvertisersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: wall_advertisers
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
@Service
public class WallAdvertisersServiceImpl extends ServiceImpl<WallAdvertisersMapper, WallAdvertisers> implements IWallAdvertisersService {
    @Autowired
    private WallAdvertisersMapper advertisersMapper;

    @Override
    public List<WallAdvertisers> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }
    @Override
    public List<WallAdvertisers> queryLogicDeleted(LambdaQueryWrapper<WallAdvertisers> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(WallAdvertisers::getDelFlag, CommonConstant.DEL_FLAG_1);
        return advertisersMapper.selectLogicDeleted(wrapper);
    }

    @Override
    public boolean revertLogicDeleted(List<String> advertiserIds, WallAdvertisers updateEntity) {
        String ids = String.format("'%s'", String.join("','", advertiserIds));
        return advertisersMapper.revertLogicDeleted(ids, updateEntity) > 0;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> advertiserIds) {
        String ids = String.format("'%s'", String.join("','", advertiserIds));
        // 1. 删除offer
        int line = advertisersMapper.deleteLogicDeleted(ids);
        return line != 0;
    }
}
