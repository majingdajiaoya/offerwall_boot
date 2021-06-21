package org.jeecg.modules.walladvertisers.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import org.jeecg.modules.advertisersoffer.service.IWallAdvertisersOfferService;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.walladvertisers.entity.WallAdvertisers;
import org.jeecg.modules.walladvertisers.service.IWallAdvertisersService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: wall_advertisers
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
@Api(tags="wall_advertisers")
@RestController
@RequestMapping("/walladvertisers/wallAdvertisers")
@Slf4j
public class WallAdvertisersController extends JeecgController<WallAdvertisers, IWallAdvertisersService> {
	@Autowired
	private IWallAdvertisersService wallAdvertisersService;

	 @Resource
	 private BaseCommonService baseCommonService;

	 @Autowired
	 private IWallAdvertisersOfferService advertisersOfferService;
	/**
	 * 分页列表查询
	 *
	 * @param wallAdvertisers
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wall_advertisers-分页列表查询")
	@ApiOperation(value="wall_advertisers-分页列表查询", notes="wall_advertisers-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WallAdvertisers wallAdvertisers,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		wallAdvertisers.setDelFlag(0);
		QueryWrapper<WallAdvertisers> queryWrapper = QueryGenerator.initQueryWrapper(wallAdvertisers, req.getParameterMap());
		Page<WallAdvertisers> page = new Page<WallAdvertisers>(pageNo, pageSize);
		IPage<WallAdvertisers> pageList = wallAdvertisersService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @AutoLog(value = "wall_advertisers-无分页列表查询")
	 @RequestMapping(value = "/queryallAdvertiser", method = RequestMethod.GET)
	 public Result<List<WallAdvertisers>> queryallAdvertiser(@RequestParam(name="ids",required=false) String ids) {
		 Result<List<WallAdvertisers>> result = new Result<List<WallAdvertisers>>();
		 LambdaQueryWrapper<WallAdvertisers> query = new LambdaQueryWrapper<>();
		 query.eq(WallAdvertisers::getDelFlag, 0);
		 if(oConvertUtils.isNotEmpty(ids)){
			 query.in(WallAdvertisers::getId, ids.split(","));
		 }
		 //此处查询忽略时间条件
		 List<WallAdvertisers> ls = wallAdvertisersService.list(query);
		 result.setSuccess(true);
		 result.setResult(ls);
		 return result;
	 }
	 @AutoLog(value = "wall_advertisers-查询offer的广告主")
	 @RequestMapping(value = "/queryOfferAdvertiser", method = RequestMethod.GET)
	 public Result<List<String>> queryOfferAdvertiser(@RequestParam(name = "advertisersId", required = true) String advertisersId) {
		 Result<List<String>> result = new Result<>();
		 List<String> list = new ArrayList<String>();
		 WallAdvertisers advertiser = wallAdvertisersService.getById(advertisersId);
		 if (advertiser == null ) {
			 result.error500("未找到广告主相关信息");
		 } else {
		 	list.add(advertiser.getId());
			 result.setSuccess(true);
			 result.setResult(list);
		 }
		 return result;
	 }
	 /**
	 *   添加
	 *
	 * @param wallAdvertisers
	 * @return
	 */
	@AutoLog(value = "wall_advertisers-添加")
	@ApiOperation(value="wall_advertisers-添加", notes="wall_advertisers-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WallAdvertisers wallAdvertisers) {
		wallAdvertisers.setDelFlag(0);
		wallAdvertisersService.save(wallAdvertisers);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wallAdvertisers
	 * @return
	 */
	@AutoLog(value = "wall_advertisers-编辑")
	@ApiOperation(value="wall_advertisers-编辑", notes="wall_advertisers-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WallAdvertisers wallAdvertisers) {
		wallAdvertisersService.updateById(wallAdvertisers);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wall_advertisers-通过id删除")
	@ApiOperation(value="wall_advertisers-通过id删除", notes="wall_advertisers-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
//		wallAdvertisersService.removeById(id);
//		return Result.OK("删除成功!");
		Result<WallAdvertisers> result = new Result<WallAdvertisers>();
		try {
			WallAdvertisers advertisers = wallAdvertisersService.getById(id);
			if(advertisers==null) {
				result.error500("未找到对应实体");
			}else{
				advertisers.setDelFlag(1);
				wallAdvertisersService.updateById(advertisers);
				result.success("删除成功!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}
	 /**
	  * 冻结&解冻advertisers
	  * @param jsonObject
	  * @return
	  */
	 //@RequiresRoles({"admin"})
	 @AutoLog(value = "wall_advertisers-冻结&解冻advertisers")
	 @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
	 public Result<WallAdvertisers> frozenBatch(@RequestBody JSONObject jsonObject) {
		 Result<WallAdvertisers> result = new Result<WallAdvertisers>();
		 try {
			 String ids = jsonObject.getString("ids");
			 String status = jsonObject.getString("status");
			 String[] arr = ids.split(",");
			 for (String id : arr) {
				 if(oConvertUtils.isNotEmpty(id)) {
					 this.wallAdvertisersService.update(new WallAdvertisers().setStatus(Integer.parseInt(status)),
							 new UpdateWrapper<WallAdvertisers>().lambda().eq(WallAdvertisers::getId,id));
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
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wall_advertisers-批量删除")
	@ApiOperation(value="wall_advertisers-批量删除", notes="wall_advertisers-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.wallAdvertisersService.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.OK("批量删除成功!");
		Result<WallAdvertisers> result = new Result<WallAdvertisers>();
		try {
			String[] arr = ids.split(",");
			for (String id : arr) {
				if(oConvertUtils.isNotEmpty(id)) {
					this.wallAdvertisersService.update(new WallAdvertisers().setDelFlag(Integer.parseInt("1")),
							new UpdateWrapper<WallAdvertisers>().lambda().eq(WallAdvertisers::getId,id));
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
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wall_advertisers-通过id查询")
	@ApiOperation(value="wall_advertisers-通过id查询", notes="wall_advertisers-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WallAdvertisers wallAdvertisers = wallAdvertisersService.getById(id);
		if(wallAdvertisers==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wallAdvertisers);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wallAdvertisers
    */
    @AutoLog(value = "wallAdvertisers--导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WallAdvertisers wallAdvertisers) {
        return super.exportXls(request, wallAdvertisers, WallAdvertisers.class, "wall_advertisers");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
	@AutoLog(value = "wallAdvertisers--导入数据")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, WallAdvertisers.class);
    }
	 /**
	  * 获取被逻辑删除的advertiser列表，无分页
	  *
	  * @return logicDeletedAdvertiserList
	  */
	 @AutoLog(value = "wallAdvertisers--获取被逻辑删除的advertiser列表")
	 @GetMapping("/recycleBin")
	 public Result getRecycleBin() {
		 List<WallAdvertisers> logicDeletedAdvertiserList = wallAdvertisersService.queryLogicDeleted();
		 return Result.ok(logicDeletedAdvertiserList);
	 }

	 /**
	  * 还原被逻辑删除的advertiser
	  *
	  * @param jsonObject
	  * @return
	  */
	 @AutoLog(value = "wallAdvertisers--还原被逻辑删除的advertiser")
	 @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
	 public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
		 String advertiserIds = jsonObject.getString("advertiserIds");
		 if (StringUtils.isNotBlank(advertiserIds)) {
			 WallAdvertisers updateAdvertiser = new WallAdvertisers();
			 updateAdvertiser.setUpdateBy(JwtUtil.getUserNameByToken(request));
			 updateAdvertiser.setUpdateTime(new Date());
			 wallAdvertisersService.revertLogicDeleted(Arrays.asList(advertiserIds.split(",")), updateAdvertiser);
		 }
		 return Result.ok("还原成功");
	 }

	 /**
	  * 彻底删除advertiser
	  *
	  * @param advertiserIds 被删除的advertiserID，多个id用半角逗号分割
	  * @return
	  */
	 //@RequiresRoles({"admin"})
	 @AutoLog(value = "wallAdvertisers--彻底删除advertiser")
	 @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
	 public Result deleteRecycleBin(@RequestParam("advertiserIds") String advertiserIds) {
		 if (StringUtils.isNotBlank(advertiserIds)) {
			 wallAdvertisersService.removeLogicDeleted(Arrays.asList(advertiserIds.split(",")));
		 }
		 return Result.ok("删除成功");
	 }
}
