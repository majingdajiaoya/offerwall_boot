package org.jeecg.modules.wallclicklog.service;

import org.jeecg.modules.wallclicklog.entity.WallClickLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.wallofferstasks.entity.WallOffersTasks;

/**
 * @Description: wall_click_log
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
public interface IWallClickLogService extends IService<WallClickLog> {

    WallClickLog getLogByUuid(String clickId);

}
