package org.jeecg.modules.wallofferstasks.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.wallofferstasks.entity.TaskOfferListVo;
import org.jeecg.modules.wallofferstasks.entity.WallOffersTasks;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.wallplayer.entity.WallPlayer;

import java.util.List;
import java.util.Map;

/**
 * @Description: wall_offers_tasks
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
public interface IWallOffersTasksService extends IService<WallOffersTasks> {
    /**
     * 查询被逻辑删除的task
     */
    List<WallOffersTasks> queryLogicDeleted();
    /**
     * 查询被逻辑删除的task（可拼装查询条件）
     */
    List<WallOffersTasks> queryLogicDeleted(LambdaQueryWrapper<WallOffersTasks> wrapper);

    /**
     * 还原被逻辑删除的task
     */
    boolean revertLogicDeleted(List<String> asList, WallOffersTasks updateTask);
    /**
     * 彻底删除被逻辑删除的task
     */
    boolean removeLogicDeleted(List<String> asList);

    /*
    关联查询playername
     */
    Map<Integer, String> getPlayNamesByIds(List<Integer> tasksIds);
    /*
        关联查询offername
         */
    Map<Integer, String> getOfferNamesByIds(List<Integer> tasksIds);

    /**
     * 查询用户已有的指定任务
     * @param offerId
     * @param playerId
     * @return
     */
    WallOffersTasks getTaskByIds(String offerId, String playerId);
    /**
     * 用户查询名下所有任务
     * @param task
     * @return
     */
    List<TaskOfferListVo> queryTasksByPlayerId(WallOffersTasks task);
    /**
     *
     * @param click_id
     * @return
     */
    WallOffersTasks getByClickId(String click_id);

    Page<TaskOfferListVo> getTaskPageList(Page<TaskOfferListVo> objectPage, WallOffersTasks task);
}
