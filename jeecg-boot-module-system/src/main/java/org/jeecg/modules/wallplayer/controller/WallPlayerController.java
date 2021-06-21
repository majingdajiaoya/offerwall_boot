package org.jeecg.modules.wallplayer.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.encryption.AesEncryptUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.wallplayer.entity.WallPlayer;
import org.jeecg.modules.wallplayer.service.IWallPlayerService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: wall_player
 * @Author: jeecg-boot
 * @Date:   2021-05-23
 * @Version: V1.0
 */
@Api(tags="wall_player")
@RestController
@RequestMapping("/wallplayer/wallPlayer")
@Slf4j
public class WallPlayerController extends JeecgController<WallPlayer, IWallPlayerService> {
	@Autowired
	private IWallPlayerService wallPlayerService;
	 @Resource
	 private BaseCommonService baseCommonService;
	/**
	 * 分页列表查询
	 *
	 * @param wallPlayer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wall_player-分页列表查询")
	@ApiOperation(value="wall_player-分页列表查询", notes="wall_player-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WallPlayer wallPlayer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		wallPlayer.setDelFlag("0");
		QueryWrapper<WallPlayer> queryWrapper = QueryGenerator.initQueryWrapper(wallPlayer, req.getParameterMap());
		Page<WallPlayer> page = new Page<WallPlayer>(pageNo, pageSize);
		IPage<WallPlayer> pageList = wallPlayerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wallPlayer
	 * @return
	 */
	@AutoLog(value = "wall_player-添加")
	@ApiOperation(value="wall_player-添加", notes="wall_player-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WallPlayer wallPlayer, HttpServletRequest request) {
		String ip = IPUtils.getIpAddr(request);
		String ua = request.getHeader("user-agent");
		wallPlayer.setIp(ip);
		wallPlayer.setUa(ua);
		wallPlayer.setDelFlag("0");
		String password =wallPlayer.getPlayerPassword();
		String salt = oConvertUtils.randomGen(8);
		if(password==null||"".equals(StrUtil.nullToEmpty(password))){
			password = PasswordUtil.encrypt(wallPlayer.getPlayerName(), "123456", salt);
		}else {
			password = PasswordUtil.encrypt(wallPlayer.getPlayerName(), wallPlayer.getPlayerPassword(), salt);
		}
		wallPlayer.setSalt(salt);
		wallPlayerService.save(wallPlayer);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wallPlayer
	 * @return
	 */
	@AutoLog(value = "wall_player-编辑")
	@ApiOperation(value="wall_player-编辑", notes="wall_player-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WallPlayer wallPlayer) {
		wallPlayerService.updateById(wallPlayer);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wall_player-通过id删除")
	@ApiOperation(value="wall_player-通过id删除", notes="wall_player-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
//		wallPlayerService.removeById(id);
//		return Result.OK("删除成功!");
		Result<WallPlayer> result = new Result<WallPlayer>();
		try {
			WallPlayer player = wallPlayerService.getById(id);
			if(player==null) {
				result.error500("未找到对应实体");
			}else{
				player.setDelFlag("1");
				wallPlayerService.updateById(player);
				result.success("删除成功!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wall_player-批量删除")
	@ApiOperation(value="wall_player-批量删除", notes="wall_player-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.wallPlayerService.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.OK("批量删除成功!");
		Result<WallPlayer> result = new Result<WallPlayer>();
		try {
			String[] arr = ids.split(",");
			for (String id : arr) {
				if(oConvertUtils.isNotEmpty(id)) {
					this.wallPlayerService.update(new WallPlayer().setDelFlag("1"),
							new UpdateWrapper<WallPlayer>().lambda().eq(WallPlayer::getId,id));
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败"+e.getMessage());
		}
		result.success("批量删除成功!");
		return result;
	}
	 /**
	  * 冻结&解冻用户
	  * @param jsonObject
	  * @return
	  */
	 //@RequiresRoles({"admin"})
	 @AutoLog(value = "冻结&解冻用户WallPlayer")
	 @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
	 public Result<WallPlayer> frozenBatch(@RequestBody JSONObject jsonObject) {
		 Result<WallPlayer> result = new Result<WallPlayer>();
		 try {
			 String ids = jsonObject.getString("ids");
			 String status = jsonObject.getString("status");
			 String[] arr = ids.split(",");
			 for (String id : arr) {
				 if(oConvertUtils.isNotEmpty(id)) {
					 this.wallPlayerService.update(new WallPlayer().setStatus(Integer.parseInt(status)),
							 new UpdateWrapper<WallPlayer>().lambda().eq(WallPlayer::getId,id));
				 }
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 result.error500("操作失败"+e.getMessage());
		 }
		 result.success("操作成功!");
		 return result;

	 }
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wall_player-通过id查询")
	@ApiOperation(value="wall_player-通过id查询", notes="wall_player-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WallPlayer wallPlayer = wallPlayerService.getById(id);
//		String password = wallPlayer.getPlayerPassword();
//		String salt = wallPlayer.getSalt();
//		if(password!=null&&!"".equals(StrUtil.nullToEmpty(password))){
//			password = PasswordUtil.decrypt(wallPlayer.getPlayerName(), wallPlayer.getPlayerPassword(), salt);
//		}
//		wallPlayer.setPlayerPassword(password);
		if(wallPlayer==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wallPlayer);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wallPlayer
    */
	@AutoLog(value = "导出wallPlayer数据")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WallPlayer wallPlayer) {
        return super.exportXls(request, wallPlayer, WallPlayer.class, "wall_player");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @AutoLog(value = "通过excel导入wallPlayer数据")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WallPlayer.class);
    }
	 /**
	  * 获取被逻辑删除的wallPlayer列表，无分页
	  *
	  * @return logicDeletedUserList
	  */
	 @GetMapping("/recycleBin")
	 @AutoLog(value = "获取被逻辑删除的wallPlayer列表，无分页")
	 public Result getRecycleBin() {
		 List<WallPlayer> logicDeletedOfferList = wallPlayerService.queryLogicDeleted();
		 return Result.ok(logicDeletedOfferList);
	 }

	 /**
	  * 还原被逻辑删除的wallPlayer
	  *
	  * @param jsonObject
	  * @return
	  */
	 @AutoLog(value = "还原被逻辑删除的wallPlayer")
	 @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
	 public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
		 String wallPlayerIds = jsonObject.getString("wallPlayerIds");
		 if (StringUtils.isNotBlank(wallPlayerIds)) {
			 WallPlayer updatePlayer = new WallPlayer();
			 updatePlayer.setUpdateBy(JwtUtil.getUserNameByToken(request));
			 updatePlayer.setUpdateTime(new Date());
			 wallPlayerService.revertLogicDeleted(Arrays.asList(wallPlayerIds.split(",")), updatePlayer);
		 }
		 return Result.ok("还原成功");
	 }

	 /**
	  * 彻底删除wallPlayer
	  *
	  * @param wallPlayerIds 被删除的offerID，多个id用半角逗号分割
	  * @return
	  */
	 //@RequiresRoles({"admin"})
	 @AutoLog(value = "彻底删除wallPlayer")
	 @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
	 public Result deleteRecycleBin(@RequestParam("wallPlayerIds") String wallPlayerIds) {
		 if (StringUtils.isNotBlank(wallPlayerIds)) {
			 wallPlayerService.removeLogicDeleted(Arrays.asList(wallPlayerIds.split(",")));
		 }
		 return Result.ok("删除成功");
	 }


	 /**
	  * 修改密码
	  */
	 @AutoLog(value = "wall_player-修改密码")
	 @RequestMapping(value = "/changePlayerPassword", method = RequestMethod.PUT)
	 public Result<?> changePlayerPassword(@RequestBody WallPlayer player) {
		 WallPlayer p = this.wallPlayerService.getOne(new LambdaQueryWrapper<WallPlayer>().eq(WallPlayer::getPlayerName, player.getPlayerName()));
		 if (p == null) {
			 return Result.error("用户不存在！");
		 }
		 player.setId(p.getId());
		 return wallPlayerService.changePassword(player);
	 }

}
