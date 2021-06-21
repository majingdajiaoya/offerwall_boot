package org.jeecg.modules.wallplayer.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.wallplayer.entity.WallPlayer;

/**
 * @Description: wall_player
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
public interface WallPlayerMapper extends BaseMapper<WallPlayer> {

    /**
     * 查询被逻辑删除的wallPlayer
     */
    List<WallPlayer> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<WallPlayer> wrapper);
    /**
     * 还原被逻辑删除的用户
     */
    int revertLogicDeleted(@Param("wallPlayerIds") String wallPlayerIds, @Param("entity") WallPlayer entity);

    /**
     * 彻底删除被逻辑删除的wallPlayer
     */
    int deleteLogicDeleted(@Param("wallPlayerIds") String wallPlayerIds);
    /**
     * 用户名排重查询
     * @param playerName
     * @return
     */
    WallPlayer getPlayerByName(String playerName);
}
