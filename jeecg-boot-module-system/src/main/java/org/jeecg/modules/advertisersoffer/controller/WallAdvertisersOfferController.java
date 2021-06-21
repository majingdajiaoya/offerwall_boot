package org.jeecg.modules.advertisersoffer.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.advertisersoffer.entity.AdvertisersOfferList;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import org.jeecg.modules.advertisersoffer.service.IAdvertisersOfferListService;
import org.jeecg.modules.advertisersoffer.service.IWallAdvertisersOfferService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysUser;
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
 * @Description: wall_advertisers_offer
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
  * http://localhost:8080/offerwall/advertisersoffer/wallAdvertisersOffer/list
 */
@Api(tags="wall_advertisers_offer")
@RestController
@RequestMapping("/advertisersoffer/wallAdvertisersOffer")
@Slf4j
public class WallAdvertisersOfferController extends JeecgController<WallAdvertisersOffer, IWallAdvertisersOfferService> {
	@Autowired
	private IWallAdvertisersOfferService wallAdvertisersOfferService;
	 @Autowired
	 private IAdvertisersOfferListService advertisersOfferListService;

	 @Resource
	 private BaseCommonService baseCommonService;


	 /**
	 * 分页列表查询
	 *
	 * @param wallAdvertisersOffer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wall_advertisers_offer-分页列表查询")
	@ApiOperation(value="wall_advertisers_offer-分页列表查询", notes="wall_advertisers_offer-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WallAdvertisersOffer wallAdvertisersOffer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		wallAdvertisersOffer.setDelFlag(0);
		QueryWrapper<WallAdvertisersOffer> queryWrapper = QueryGenerator.initQueryWrapper(wallAdvertisersOffer, req.getParameterMap());
		queryWrapper.orderByAsc("weight");
		Page<WallAdvertisersOffer> page = new Page<WallAdvertisersOffer>(pageNo, pageSize);
		IPage<WallAdvertisersOffer> pageList = wallAdvertisersOfferService.page(page, queryWrapper);


		List<Integer> offerIds = pageList.getRecords().stream().map(WallAdvertisersOffer::getId).collect(Collectors.toList());
		if(offerIds!=null && offerIds.size()>0){
			//设置advertiserNames
			Map<Integer,String>  advertiserNames = wallAdvertisersOfferService.getAdvertiserNamesByIds(offerIds);
			pageList.getRecords().forEach(item->{
				item.setAdvertiserName(advertiserNames.get(item.getId()));
			});
		}
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wallAdvertisersOffer
	 * @return
	 */
	@AutoLog(value = "wall_advertisers_offer-添加")
	@ApiOperation(value="wall_advertisers_offer-添加", notes="wall_advertisers_offer-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WallAdvertisersOffer wallAdvertisersOffer) {
		wallAdvertisersOffer.setDelFlag(0);
		wallAdvertisersOfferService.save(wallAdvertisersOffer);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wallAdvertisersOffer
	 * @return
	 */
	@AutoLog(value = "wall_advertisers_offer-编辑")
	@ApiOperation(value="wall_advertisers_offer-编辑", notes="wall_advertisers_offer-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WallAdvertisersOffer wallAdvertisersOffer) {
		wallAdvertisersOfferService.updateById(wallAdvertisersOffer);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wall_advertisers_offer-通过id删除")
	@ApiOperation(value="wall_advertisers_offer-通过id删除", notes="wall_advertisers_offer-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<WallAdvertisersOffer> delete(@RequestParam(name="id",required=true) String id) {
		Result<WallAdvertisersOffer> result = new Result<WallAdvertisersOffer>();
		try {
			WallAdvertisersOffer offer = wallAdvertisersOfferService.getById(id);
			if(offer==null) {
				result.error500("未找到对应实体");
			}else{
				offer.setDelFlag(1);
				wallAdvertisersOfferService.updateById(offer);
				result.success("删除成功!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;

//		wallAdvertisersOfferService.removeById(id);
//		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "wall_advertisers_offer-批量删除")
	@ApiOperation(value="wall_advertisers_offer-批量删除", notes="wall_advertisers_offer-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.wallAdvertisersOfferService.removeByIds(Arrays.asList(ids.split(",")));
		Result<WallAdvertisersOffer> result = new Result<WallAdvertisersOffer>();
		try {
			String[] arr = ids.split(",");
			for (String id : arr) {
				if(oConvertUtils.isNotEmpty(id)) {
					this.wallAdvertisersOfferService.update(new WallAdvertisersOffer().setDelFlag(Integer.parseInt("1")),
							new UpdateWrapper<WallAdvertisersOffer>().lambda().eq(WallAdvertisersOffer::getId,id));
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
	 @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
	 public Result<WallAdvertisersOffer> frozenBatch(@RequestBody JSONObject jsonObject) {
		 Result<WallAdvertisersOffer> result = new Result<WallAdvertisersOffer>();
		 try {
			 String ids = jsonObject.getString("ids");
			 String status = jsonObject.getString("status");
			 String[] arr = ids.split(",");
			 for (String id : arr) {
				 if(oConvertUtils.isNotEmpty(id)) {
					 this.wallAdvertisersOfferService.update(new WallAdvertisersOffer().setStatus(Integer.parseInt(status)),
							 new UpdateWrapper<WallAdvertisersOffer>().lambda().eq(WallAdvertisersOffer::getId,id));
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
	@AutoLog(value = "wall_advertisers_offer-通过id查询")
	@ApiOperation(value="wall_advertisers_offer-通过id查询", notes="wall_advertisers_offer-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WallAdvertisersOffer wallAdvertisersOffer = wallAdvertisersOfferService.getById(id);
		if(wallAdvertisersOffer==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wallAdvertisersOffer);
	}

	 @Override
	 protected Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<WallAdvertisersOffer> clazz) {
		 return super.importExcel(request, response, clazz);
	 }

	 /**
    * 导出excel
    *
    * @param request
    * @param wallAdvertisersOffer
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WallAdvertisersOffer wallAdvertisersOffer) {
    	//导出列表（未删除）数据
		wallAdvertisersOffer.setDelFlag(0);
		System.out.println("导出=====");
		System.out.println("导出=====request"+request);
		System.out.println("导出=====wallAdvertisersOffer"+wallAdvertisersOffer);
        return super.exportXls(request, wallAdvertisersOffer, WallAdvertisersOffer.class, "wall_advertisers_offer");
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
        return super.importExcel(request, response, WallAdvertisersOffer.class);
    }
	 /**
	  * 获取被逻辑删除的用户列表，无分页
	  *
	  * @return logicDeletedUserList
	  */
	 @GetMapping("/recycleBin")
	 public Result getRecycleBin() {
		 List<WallAdvertisersOffer> logicDeletedOfferList = wallAdvertisersOfferService.queryLogicDeleted();
		 List<Integer> offerIds = logicDeletedOfferList.stream().map(WallAdvertisersOffer::getId).collect(Collectors.toList());
		 if(offerIds!=null && offerIds.size()>0){
			 //设置advertiserNames
			 Map<Integer,String>  advertiserNames = wallAdvertisersOfferService.getAdvertiserNamesByIds(offerIds);
			 logicDeletedOfferList.forEach(item->{
				 item.setAdvertiserName(advertiserNames.get(item.getId()));
			 });
		 }
		 return Result.ok(logicDeletedOfferList);
	 }

	 /**
	  * 还原被逻辑删除的offer
	  *
	  * @param jsonObject
	  * @return
	  */
	 @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
	 public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
		 String offerIds = jsonObject.getString("offerIds");
		 if (StringUtils.isNotBlank(offerIds)) {
			 WallAdvertisersOffer updateOffer = new WallAdvertisersOffer();
			 updateOffer.setUpdateBy(JwtUtil.getUserNameByToken(request));
			 updateOffer.setUpdateTime(new Date());
			 wallAdvertisersOfferService.revertLogicDeleted(Arrays.asList(offerIds.split(",")), updateOffer);
		 }
		 return Result.ok("还原成功");
	 }

	 /**
	  * 彻底删除offer
	  *
	  * @param offerIds 被删除的offerID，多个id用半角逗号分割
	  * @return
	  */
	 //@RequiresRoles({"admin"})
	 @AutoLog(value = "wall_advertisers_offer-彻底删除offer")
	 @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
	 public Result deleteRecycleBin(@RequestParam("offerIds") String offerIds) {
		 if (StringUtils.isNotBlank(offerIds)) {
			 wallAdvertisersOfferService.removeLogicDeleted(Arrays.asList(offerIds.split(",")));
		 }
		 return Result.ok("删除成功");
	 }


	 @AutoLog(value = "wall_advertisers_offer-分页列表查询")
	 @ApiOperation(value="积分墙-分页列表查询", notes="积分墙offer展示")
	 @GetMapping(value = "/offerList")
	 public Result<?> offerList(AdvertisersOfferList advertisersOfferList,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 advertisersOfferList.setDelFlag(0);
		 advertisersOfferList.setStatus(1);
		 QueryWrapper<AdvertisersOfferList> queryWrapper = QueryGenerator.initQueryWrapper(advertisersOfferList, req.getParameterMap());
		 queryWrapper.orderByAsc("weight");
		 Page<AdvertisersOfferList> page = new Page<AdvertisersOfferList>(pageNo, pageSize);
		 IPage<AdvertisersOfferList> pageList = advertisersOfferListService.page(page, queryWrapper);

		 return Result.OK(pageList);
	 }


}
