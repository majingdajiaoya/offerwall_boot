package org.jeecg.modules.wallofferstasks.controller;

import java.io.*;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentParser;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import org.jeecg.modules.advertisersoffer.service.IWallAdvertisersOfferService;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.walladvertisers.entity.WallAdvertisers;
import org.jeecg.modules.walladvertisers.service.IWallAdvertisersService;
import org.jeecg.modules.wallofferstasks.entity.WallOffersTasks;
import org.jeecg.modules.wallofferstasks.service.IWallOffersTasksService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.wallplayer.entity.WallPlayer;
import org.jeecg.modules.wallplayer.service.IWallPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: wall_offers_tasks
 * @Author: jeecg-boot
 * @Date: 2021-05-23
 * @Version: V1.0
 */
@Api(tags = "wall_offers_tasks")
@RestController
@RequestMapping("/wallofferstasks/wallOffersTasks")
@Slf4j
public class WallOffersTasksController extends JeecgController<WallOffersTasks, IWallOffersTasksService> {
    @Autowired
    private IWallOffersTasksService wallOffersTasksService;
    @Autowired
    private IWallPlayerService wallPlayerService;
    @Autowired
    private IWallAdvertisersOfferService wallAdvertisersOfferService;
    @Resource
    private BaseCommonService baseCommonService;
    @Autowired
    IWallAdvertisersService wallAdvertisersService;

    /**
     * 分页列表查询
     *
     * @param wallOffersTasks
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wall_offers_tasks-分页列表查询")
    @ApiOperation(value = "wall_offers_tasks-分页列表查询", notes = "wall_offers_tasks-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WallOffersTasks wallOffersTasks,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        wallOffersTasks.setDelFlag(0);
        QueryWrapper<WallOffersTasks> queryWrapper = QueryGenerator.initQueryWrapper(wallOffersTasks, req.getParameterMap());
        Page<WallOffersTasks> page = new Page<WallOffersTasks>(pageNo, pageSize);
        IPage<WallOffersTasks> pageList = wallOffersTasksService.page(page, queryWrapper);

        List<Integer> tasksIds = pageList.getRecords().stream().map(WallOffersTasks::getId).collect(Collectors.toList());
        if (tasksIds != null && tasksIds.size() > 0) {
            //设置playername
            Map<Integer, String> playerNames = wallOffersTasksService.getPlayNamesByIds(tasksIds);
            System.out.println(playerNames);
            pageList.getRecords().forEach(item -> {
//				System.out.println(playerNames.get(item.getId()));
                item.setPlayerName(playerNames.get(item.getId()));
            });
            //设置offername
            Map<Integer, String> offerNames = wallOffersTasksService.getOfferNamesByIds(tasksIds);
            System.out.println(offerNames);
            pageList.getRecords().forEach(item -> {
//				System.out.println(offerNames.get(item.getId()));
                item.setOfferName(offerNames.get(item.getId()));
            });
        }
//		System.out.println(pageList.getRecords());

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wallOffersTasks
     * @return
     */
    @AutoLog(value = "wall_offers_tasks-添加")
    @ApiOperation(value = "wall_offers_tasks-添加", notes = "wall_offers_tasks-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WallOffersTasks wallOffersTasks) {
        wallOffersTasks.setDelFlag(0);
        wallOffersTasksService.save(wallOffersTasks);
        return Result.OK("添加成功！");
    }



    /**
     * 编辑
     *
     * @param wallOffersTasks
     * @return
     */
    @AutoLog(value = "wall_offers_tasks-编辑")
    @ApiOperation(value = "wall_offers_tasks-编辑", notes = "wall_offers_tasks-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WallOffersTasks wallOffersTasks) {
        wallOffersTasksService.updateById(wallOffersTasks);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wall_offers_tasks-通过id删除")
    @ApiOperation(value = "wall_offers_tasks-通过id删除", notes = "wall_offers_tasks-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        Result<WallOffersTasks> result = new Result<WallOffersTasks>();
        try {
            WallOffersTasks task = wallOffersTasksService.getById(id);
            if (task == null) {
                result.error500("未找到对应实体");
            } else {
                task.setDelFlag(1);
                wallOffersTasksService.updateById(task);
                result.success("删除成功!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
//		wallOffersTasksService.removeById(id);
//		return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wall_offers_tasks-批量删除")
    @ApiOperation(value = "wall_offers_tasks-批量删除", notes = "wall_offers_tasks-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
//		this.wallOffersTasksService.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.OK("批量删除成功!");
        Result<WallOffersTasks> result = new Result<WallOffersTasks>();
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (oConvertUtils.isNotEmpty(id)) {
                    this.wallOffersTasksService.update(new WallOffersTasks().setDelFlag(Integer.parseInt("1")),
                            new UpdateWrapper<WallOffersTasks>().lambda().eq(WallOffersTasks::getId, id));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败" + e.getMessage());
        }
        result.success("批量删除成功!");
        return result;
    }

    /**
     * 冻结&解冻用户
     *
     * @param jsonObject
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
    public Result<WallOffersTasks> frozenBatch(@RequestBody JSONObject jsonObject) {
        Result<WallOffersTasks> result = new Result<WallOffersTasks>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (oConvertUtils.isNotEmpty(id)) {
                    this.wallOffersTasksService.update(new WallOffersTasks().setStatus(Integer.parseInt(status)),
                            new UpdateWrapper<WallOffersTasks>().lambda().eq(WallOffersTasks::getId, id));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败" + e.getMessage());
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
    @AutoLog(value = "wall_offers_tasks-通过id查询")
    @ApiOperation(value = "wall_offers_tasks-通过id查询", notes = "wall_offers_tasks-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WallOffersTasks wallOffersTasks = wallOffersTasksService.getById(id);
        if (wallOffersTasks == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wallOffersTasks);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param wallOffersTasks
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WallOffersTasks wallOffersTasks) {
        return super.exportXls(request, wallOffersTasks, WallOffersTasks.class, "wall_offers_tasks");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WallOffersTasks.class);
    }

    /**
     * 获取被逻辑删除的task列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<WallOffersTasks> logicDeletedOfferList = wallOffersTasksService.queryLogicDeleted();
        List<Integer> tasksIds = logicDeletedOfferList.stream().map(WallOffersTasks::getId).collect(Collectors.toList());
        if (tasksIds != null && tasksIds.size() > 0) {
            //设置playername
            Map<Integer, String> playerNames = wallOffersTasksService.getPlayNamesByIds(tasksIds);
            System.out.println(playerNames);
            logicDeletedOfferList.forEach(item -> {
//				System.out.println(playerNames.get(item.getId()));
                item.setPlayerName(playerNames.get(item.getId()));
            });
            //设置offername
            Map<Integer, String> offerNames = wallOffersTasksService.getOfferNamesByIds(tasksIds);
            System.out.println(offerNames);
            logicDeletedOfferList.forEach(item -> {
//				System.out.println(offerNames.get(item.getId()));
                item.setOfferName(offerNames.get(item.getId()));
            });
        }
        return Result.ok(logicDeletedOfferList);
    }

    /**
     * 还原被逻辑删除的tasks
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String tasksIds = jsonObject.getString("tasksIds");
        if (StringUtils.isNotBlank(tasksIds)) {
            WallOffersTasks updateTask = new WallOffersTasks();
            updateTask.setUpdateBy(JwtUtil.getUserNameByToken(request));
            updateTask.setUpdateTime(new Date());
            wallOffersTasksService.revertLogicDeleted(Arrays.asList(tasksIds.split(",")), updateTask);
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除offer
     *
     * @param tasksIds 被删除的offerID，多个id用半角逗号分割
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("tasksIds") String tasksIds) {
        if (StringUtils.isNotBlank(tasksIds)) {
            wallOffersTasksService.removeLogicDeleted(Arrays.asList(tasksIds.split(",")));
        }
        return Result.ok("删除成功");
    }





}
