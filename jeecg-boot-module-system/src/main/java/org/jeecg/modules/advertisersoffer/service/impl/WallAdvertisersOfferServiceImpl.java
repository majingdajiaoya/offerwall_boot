package org.jeecg.modules.advertisersoffer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.advertisersoffer.entity.OfferAdvertiserVo;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import org.jeecg.modules.advertisersoffer.mapper.WallAdvertisersOfferMapper;
import org.jeecg.modules.advertisersoffer.service.IWallAdvertisersOfferService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.wallofferstasks.entity.TaskOfferVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: wall_advertisers_offer
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
 */
@Service
public class WallAdvertisersOfferServiceImpl extends ServiceImpl<WallAdvertisersOfferMapper, WallAdvertisersOffer> implements IWallAdvertisersOfferService {
    @Autowired
    private WallAdvertisersOfferMapper offerMapper;

    @Override
    public List<WallAdvertisersOffer> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }
    @Override
    public List<WallAdvertisersOffer> queryLogicDeleted(LambdaQueryWrapper<WallAdvertisersOffer> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(WallAdvertisersOffer::getDelFlag, CommonConstant.DEL_FLAG_1);
        return offerMapper.selectLogicDeleted(wrapper);
    }

    @Override
    public boolean revertLogicDeleted(List<String> offerIds, WallAdvertisersOffer updateEntity) {
        String ids = String.format("'%s'", String.join("','", offerIds));
        return offerMapper.revertLogicDeleted(ids, updateEntity) > 0;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> offerIds) {
        String ids = String.format("'%s'", String.join("','", offerIds));
        // 1. 删除offer
        int line = offerMapper.deleteLogicDeleted(ids);
        return line != 0;
    }
    @Override
    public Map<Integer, String> getAdvertiserNamesByIds(List<Integer> offerIds) {
        List<OfferAdvertiserVo> list = this.offerMapper.getAdvertiserNamesByIds(offerIds);

        Map<Integer, String> res = new HashMap<Integer, String>();
        list.forEach(item -> {
                    if (res.get(item.getId()) == null) {
                        res.put(item.getId(), item.getName());
                    } else {
                        res.put(item.getId(), res.get(item.getId()) + "," + item.getName());
                    }
                }
        );
        return res;
    }
}
