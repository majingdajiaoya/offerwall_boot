package org.jeecg.modules.wallclicklog.service.impl;

import org.jeecg.modules.wallclicklog.entity.WallClickLog;
import org.jeecg.modules.wallclicklog.mapper.WallClickLogMapper;
import org.jeecg.modules.wallclicklog.service.IWallClickLogService;
import org.jeecg.modules.wallofferstasks.entity.WallOffersTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: wall_click_log
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
@Service
public class WallClickLogServiceImpl extends ServiceImpl<WallClickLogMapper, WallClickLog> implements IWallClickLogService {
    @Autowired
    private WallClickLogMapper wallClickLogMapper;
    @Override
    public WallClickLog getLogByUuid(String clickId) {
        return wallClickLogMapper.getLogByUuid(clickId);
    }

}
