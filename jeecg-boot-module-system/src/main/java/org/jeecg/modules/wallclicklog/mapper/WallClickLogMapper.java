package org.jeecg.modules.wallclicklog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.wallclicklog.entity.WallClickLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.wallofferstasks.entity.WallOffersTasks;

/**
 * @Description: wall_click_log
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
public interface WallClickLogMapper extends BaseMapper<WallClickLog> {

    WallClickLog getLogByUuid(String clickId);

}
