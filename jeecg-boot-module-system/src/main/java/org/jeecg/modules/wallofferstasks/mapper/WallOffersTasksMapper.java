package org.jeecg.modules.wallofferstasks.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import org.jeecg.modules.wallofferstasks.entity.TaskOfferVo;
import org.jeecg.modules.wallofferstasks.entity.TaskPlayerVo;
import org.jeecg.modules.wallofferstasks.entity.WallOffersTasks;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: wall_offers_tasks
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
public interface WallOffersTasksMapper extends BaseMapper<WallOffersTasks> {

    /**
     * 查询被逻辑删除的task
     */
    List<WallOffersTasks> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<WallOffersTasks> wrapper);
    /**
     * 还原被逻辑删除的task
     */
    int revertLogicDeleted(@Param("tasksIds") String tasksIds, @Param("entity") WallOffersTasks entity);

    /**
     * 彻底删除被逻辑删除的task
     */
    int deleteLogicDeleted(@Param("tasksIds") String tasksIds);
    /**
     *  根据tasksIds,查询player信息
     * @param tasksIds
     * @return
     */
    List<TaskPlayerVo> getPlayerNamesByUserIds(List<Integer> tasksIds);
    /**
     *  根据tasksIds,查询offer信息
     * @param tasksIds
     * @return
     */
    List<TaskOfferVo> getOfferNamesByUserIds(List<Integer> tasksIds);
    /**
     * 查询用户已有的指定任务
     * @param offerId
     * @param playerId
     * @return
     */
    WallOffersTasks getTaskByIds(String offerId, String playerId);
    /**
     * 用户查询名下所有任务
     * @param playerId
     * @return
     */
    List<WallOffersTasks> queryTasksByPlayerId(String playerId);

    /**
     *
     * @param click_id
     * @return
     */
    WallOffersTasks getByClickId(String click_id);

}
