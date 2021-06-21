package org.jeecg.modules.wallplayer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.wallplayer.entity.WallPlayer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.wallplayer.entity.WallPlayer;

import java.util.List;

/**
 * @Description: wall_player
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
public interface IWallPlayerService extends IService<WallPlayer> {

    /**
     * 查询被逻辑删除的wallplayer
     */
    List<WallPlayer> queryLogicDeleted();
    /**
     * 查询被逻辑删除的wallplayer（可拼装查询条件）
     */
    List<WallPlayer> queryLogicDeleted(LambdaQueryWrapper<WallPlayer> wrapper);

    /**
     * 还原被逻辑删除的wallplayer
     */
    boolean revertLogicDeleted(List<String> asList, WallPlayer updateWallPlayer);
    /**
     * 彻底删除被逻辑删除的wallplayer
     */
    boolean removeLogicDeleted(List<String> asList);

    /**
     * 用户名排重查询
     * @param playerName
     * @return
     */
    WallPlayer getPlayerByName(String playerName);

    /**
     * 改密码
     * @param player
     * @return
     */
    Result<?> changePassword(WallPlayer player);
}
