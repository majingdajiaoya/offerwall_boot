package org.jeecg.modules.wallofferstasks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.wallofferstasks.entity.TaskOfferListVo;
import org.jeecg.modules.wallofferstasks.entity.TaskOfferVo;
import org.jeecg.modules.wallofferstasks.entity.TaskPlayerVo;
import org.jeecg.modules.wallofferstasks.entity.WallOffersTasks;
import org.jeecg.modules.wallofferstasks.mapper.WallOffersTasksMapper;
import org.jeecg.modules.wallofferstasks.service.IWallOffersTasksService;
import org.jeecg.modules.wallplayer.entity.WallPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: wall_offers_tasks
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
@Service
public class WallOffersTasksServiceImpl extends ServiceImpl<WallOffersTasksMapper, WallOffersTasks> implements IWallOffersTasksService {
    @Autowired
    private WallOffersTasksMapper tasksMapper;

    @Override
    public List<WallOffersTasks> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }
    @Override
    public List<WallOffersTasks> queryLogicDeleted(LambdaQueryWrapper<WallOffersTasks> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(WallOffersTasks::getDelFlag, CommonConstant.DEL_FLAG_1);
        return tasksMapper.selectLogicDeleted(wrapper);
    }

    @Override
    public boolean revertLogicDeleted(List<String> tasksIds, WallOffersTasks updateEntity) {
        String ids = String.format("'%s'", String.join("','", tasksIds));
        return tasksMapper.revertLogicDeleted(ids, updateEntity) > 0;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> tasksIds) {
        String ids = String.format("'%s'", String.join("','", tasksIds));
        // 1. 删除tasks
        int line = tasksMapper.deleteLogicDeleted(ids);
        return line != 0;
    }

    @Override
    public Map<Integer, String> getPlayNamesByIds(List<Integer> tasksIds) {
        List<TaskPlayerVo> list = this.tasksMapper.getPlayerNamesByUserIds(tasksIds);

        Map<Integer, String> res = new HashMap<Integer, String>();
        list.forEach(item -> {
                    if (res.get(item.getId()) == null) {
                        res.put(item.getId(), item.getPlayerName());
                    } else {
                        res.put(item.getId(), res.get(item.getId()) + "," + item.getPlayerName());
                    }
                }
        );
        return res;
    }
    @Override
    public Map<Integer, String> getOfferNamesByIds(List<Integer> tasksIds) {
        List<TaskOfferVo> list = this.tasksMapper.getOfferNamesByUserIds(tasksIds);

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
    @Override
    public WallOffersTasks getTaskByIds(String offerId, String playerId) {
        WallOffersTasks task = this.tasksMapper.getTaskByIds(offerId,playerId);
        return task;
    }

    @Override
    public List<TaskOfferListVo> queryTasksByPlayerId(WallOffersTasks task) {
        List<TaskOfferListVo> tasks = this.tasksMapper.queryTasksByPlayerId(task);
        return tasks;
    }
    @Override
    public WallOffersTasks getByClickId(String click_id) {
        return this.tasksMapper.getByClickId(click_id);
    }

    @Override
    public Page<TaskOfferListVo> getTaskPageList(Page<TaskOfferListVo> page, WallOffersTasks task) {
        return page.setRecords(tasksMapper.getTaskPageList(page,  task));
    }

}
