package org.jeecg.modules.wallconverions.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules. wallconverions.entity.WallConverions;
import org.jeecg.modules. wallconverions.service.IWallConverionsService;

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
 * @Description: wall_converions
 * @Author: jeecg-boot
 * @Date:   2021-06-03
 * @Version: V1.0
 */
@Api(tags="wall_converions")
@RestController
@RequestMapping("/ wallconverions/wallConverions")
@Slf4j
public class WallConverionsController extends JeecgController<WallConverions, IWallConverionsService> {
	@Autowired
	private IWallConverionsService wallConverionsService;

	/**
	 * 分页列表查询
	 *
	 * @param wallConverions
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "wall_converions-分页列表查询")
	@ApiOperation(value="wall_converions-分页列表查询", notes="wall_converions-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WallConverions wallConverions,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WallConverions> queryWrapper = QueryGenerator.initQueryWrapper(wallConverions, req.getParameterMap());
		Page<WallConverions> page = new Page<WallConverions>(pageNo, pageSize);
		IPage<WallConverions> pageList = wallConverionsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wallConverions
	 * @return
	 */
	@AutoLog(value = "wall_converions-添加")
	@ApiOperation(value="wall_converions-添加", notes="wall_converions-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WallConverions wallConverions) {
		wallConverionsService.save(wallConverions);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param wallConverions
	 * @return
	 */
	@AutoLog(value = "wall_converions-编辑")
	@ApiOperation(value="wall_converions-编辑", notes="wall_converions-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WallConverions wallConverions) {
		wallConverionsService.updateById(wallConverions);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wall_converions-通过id删除")
	@ApiOperation(value="wall_converions-通过id删除", notes="wall_converions-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wallConverionsService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "wall_converions-批量删除")
	@ApiOperation(value="wall_converions-批量删除", notes="wall_converions-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wallConverionsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "wall_converions-通过id查询")
	@ApiOperation(value="wall_converions-通过id查询", notes="wall_converions-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WallConverions wallConverions = wallConverionsService.getById(id);
		if(wallConverions==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wallConverions);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wallConverions
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WallConverions wallConverions) {
        return super.exportXls(request, wallConverions, WallConverions.class, "wall_converions");
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
        return super.importExcel(request, response, WallConverions.class);
    }

}
