package org.jeecg.modules.wallplayer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.wallplayer.entity.WallPlayer;
import org.jeecg.modules.wallplayer.mapper.WallPlayerMapper;
import org.jeecg.modules.wallplayer.service.IWallPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: wall_player
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
@Service
public class WallPlayerServiceImpl extends ServiceImpl<WallPlayerMapper, WallPlayer> implements IWallPlayerService {
    @Autowired
    private WallPlayerMapper wallPlayerMapper;

    @Override
    public List<WallPlayer> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }
    @Override
    public List<WallPlayer> queryLogicDeleted(LambdaQueryWrapper<WallPlayer> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(WallPlayer::getDelFlag, CommonConstant.DEL_FLAG_1);
        return wallPlayerMapper.selectLogicDeleted(wrapper);
    }

    @Override
    public boolean revertLogicDeleted(List<String> wallPlayerIds, WallPlayer updateEntity) {
        String ids = String.format("'%s'", String.join("','", wallPlayerIds));
        return wallPlayerMapper.revertLogicDeleted(ids, updateEntity) > 0;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> wallPlayerIds) {
        String ids = String.format("'%s'", String.join("','", wallPlayerIds));
        // 1. 删除offer
        int line = wallPlayerMapper.deleteLogicDeleted(ids);
        return line != 0;
    }

    @Override
    public WallPlayer getPlayerByName(String playerName) {
        return wallPlayerMapper.getPlayerByName(playerName);
    }

    @Override
//    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> changePassword(WallPlayer player) {
        String salt = oConvertUtils.randomGen(8);
        player.setSalt(salt);
        String password = player.getPlayerPassword();
        String passwordEncode = PasswordUtil.encrypt(player.getPlayerName(), password, salt);
        player.setPlayerPassword(passwordEncode);
        this.wallPlayerMapper.updateById(player);
        return Result.ok("密码修改成功!");
    }
}
