package org.jeecg.modules.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.advertisersoffer.entity.AdvertisersOfferList;
import org.jeecg.modules.advertisersoffer.entity.WallAdvertisersOffer;
import org.jeecg.modules.advertisersoffer.service.IAdvertisersOfferListService;
import org.jeecg.modules.advertisersoffer.service.IWallAdvertisersOfferService;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.walladvertisers.entity.WallAdvertisers;
import org.jeecg.modules.walladvertisers.service.IWallAdvertisersService;
import org.jeecg.modules.wallclicklog.entity.WallClickLog;
import org.jeecg.modules.wallclicklog.service.IWallClickLogService;
import org.jeecg.modules.wallconverions.entity.WallConverions;
import org.jeecg.modules.wallconverions.service.IWallConverionsService;
import org.jeecg.modules.wallofferstasks.entity.TaskOfferListVo;
import org.jeecg.modules.wallofferstasks.entity.WallOffersTasks;
import org.jeecg.modules.wallofferstasks.service.IWallOffersTasksService;
import org.jeecg.modules.wallplayer.entity.WallPlayer;
import org.jeecg.modules.wallplayer.entity.WallPlayerVo;
import org.jeecg.modules.wallplayer.service.IWallPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Description: 对外接口处理方法集合
 * @Author: jeecg-boot
 * @Date: 2021-05-23
 * @Version: V1.0
 */
@Api(tags = "前端接口--前端朋友看这里✌️")
@RestController
@RequestMapping("/api/ads")
@Slf4j
public class Ads {
    @Autowired
    private IWallOffersTasksService wallOffersTasksService;
    @Autowired
    private IWallPlayerService wallPlayerService;
    @Autowired
    private IWallAdvertisersOfferService wallAdvertisersOfferService;
    @Resource
    private BaseCommonService baseCommonService;
    @Autowired
    private IWallAdvertisersService wallAdvertisersService;
    @Autowired
    private IWallClickLogService wallClickLogService;
    @Autowired
    private IWallConverionsService wallConverionsService;
    @Autowired
    private IAdvertisersOfferListService advertisersOfferListService;

    /**
     * 任务重定向
     * @param click_id
     * @param request
     * @param response
     * @return
     */
    @AutoLog(value = "ads-任务重定向")
    @ApiOperation(value = "ads-任务重定向", notes = "ads-任务重定向")
    @GetMapping(value = "/redirectTask")
    public Result<String> redirectTask(String click_id, HttpServletRequest request, HttpServletResponse response) {
        String redictUrl = "";
        //拼接跳转url
        WallOffersTasks task = wallOffersTasksService.getByClickId(click_id);
        if (task!=null) {
            WallAdvertisersOffer offer=wallAdvertisersOfferService.getById(Integer.parseInt(task.getOfferId()));
            log.info("trackUrl==="+offer.getTracklink());
            WallAdvertisers advertiser=wallAdvertisersService.getById(offer.getAdvertisersId());
            String params = advertiser.getAdvertisersUrlParams();
            if (params==null){
                params = StrUtil.nullToEmpty(params);
                log.info("params==="+advertiser.getAdvertisersUrlParams());
            }
            redictUrl = offer.getTracklink()+params;
            String ip = IPUtils.getIpAddr(request);
            String ua = request.getHeader("user-agent");
            //wallclicklog插入,每次点击产生一条日志，传给跳转地址，用的时候再反向查找
            String uuid=UUID.randomUUID().toString().replace("-","")+task.getOfferId()+task.getPlayerId();
            WallClickLog logInfo= new WallClickLog();
            logInfo.setClickId(click_id);
            logInfo.setUuidCode(uuid);
            logInfo.setClickUa(ua);
            logInfo.setClickIp(ip);
            logInfo.setCreateBy(task.getPlayerId());
            logInfo.setCreateTime(new Date());
            logInfo.setDelFlag("0");
            log.info("click_id==="+click_id);
            wallClickLogService.save(logInfo);

            if (redictUrl!=null){
                redictUrl =  redictUrl.replace("[CLICK_ID]",uuid);
                redictUrl =  redictUrl.replace("{CLICK_ID}",uuid);
                redictUrl =  redictUrl.replace("{clk_id}",uuid);
                redictUrl =  redictUrl.replace("{click_id}",uuid);
                redictUrl = redictUrl.replace("[IP]",ip);
                redictUrl=redictUrl.replace("{ip}",ip);
                redictUrl = redictUrl.replace("[UA]",ua);
                redictUrl = redictUrl.replace("{ua}}",ua);

            }
        }
        log.info("redictUrl==="+redictUrl);
        try {
            response.sendRedirect(redictUrl);
        } catch (IOException e) {
            Result.error(500,"任务重定向错误"+e.toString());
            e.printStackTrace();

        }
        return Result.OK("跳转成功！");
    }
    /**
     * event_name校验
     *https://macan-native.com/conv/?clk_id=3772640dfffb5c26fffa42bd1e8b2021771&event_name=7du7jx
     * clickId相当于log日志中的uuidCode
     */
//    @AutoLog(value = "ads-eventName校验")
//    @ApiOperation(value = "ads-eventName校验", notes = "ads-eventName校验")
//    @GetMapping(value = "/checkEventName")
    public boolean  checkEventName(String clickId,String eventName){
        WallClickLog logInfo = wallClickLogService.getLogByUuid(clickId);
        boolean flg = false;
        if (logInfo!=null){
            WallOffersTasks task = wallOffersTasksService.getByClickId(logInfo.getClickId());
            WallAdvertisersOffer offer = wallAdvertisersOfferService.getById(task.getOfferId());
            String offerEventName =StrUtil.nullToEmpty(offer.getEventName());
            if (offerEventName==null||"".equals(offerEventName)){
                //offer无要求，传进任何eventname都可完成
                if (task.getStatus()!=2){
                    task.setStatus(2);//匹配即完成
                    task.setUpdateTime(new Date());
                    task.setUpdateBy(task.getPlayerId());
                    wallOffersTasksService.updateById(task);
                }
                flg=true;
            }else {//offer有要求，则必须匹配才可完成
                if (offerEventName.equals(eventName)&&task.getStatus()!=2){
                    task.setStatus(2);//匹配即完成
                    task.setUpdateTime(new Date());
                    task.setUpdateBy(task.getPlayerId());
                    wallOffersTasksService.updateById(task);
                    flg=true;
                }
            }
        }else{
            log.info("============无点击日志：WallClickLog==============uuidCode==="+clickId);
            flg=false;
        }
        return flg;
    }

    /**
     * 回调信息记录
     * @param token
     * @param clickId
     * @param eventName
     * @param request
     * @return
     */
    @AutoLog(value = "ads=回调")
    @ApiOperation(value = "ads-回调", notes = "ads-回调")
    @PostMapping(value = "/callBackInfo")
    public Result<?>  callBackInfo(String token, String clickId, String eventName, HttpServletRequest request ){
        Result<WallConverions> result = new Result<WallConverions>();
        String ip = IPUtils.getIpAddr(request);
        WallConverions wallConverions = new WallConverions();
        wallConverions.setToken(token);
        wallConverions.setClickId(clickId);
        wallConverions.setEventName(eventName);
        wallConverions.setCreateTime(new Date());
        wallConverions.setDelFlag("0");
        wallConverions.setClickIp(ip);
        try {
            //insert
            wallConverionsService.save(wallConverions);
            //eventName
            boolean flg = this.checkEventName(clickId,eventName);
            result.setMessage("回调信息记录完成！任务完成情况==》"+flg);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("回调失败！");
            result.setSuccess(false);
            return result;
        }
        return result;
    }
    /**
     * 前端用户新增任务
     *
     * @param task
     * @return
     */
    @AutoLog(value = "ads-新增任务")
    @ApiOperation(value = "ads-新增任务", notes = "ads-新增任务")
    @PostMapping(value = "/addTask")
    public Result<?> addTask(WallOffersTasks task, HttpServletRequest request) {
        Result<String> result = new Result<String>();
        //登录验证
        WallPlayer player = (WallPlayer)request.getSession().getAttribute("playerUser");
        if (player==null){
            result.setMessage("用户未登录，请重新登录后再操作！");
            result.setSuccess(false);
            return result;
        }
        if ("".equals(StrUtil.nullToEmpty(task.getOfferId()))){
            result.setMessage("请求参数有误，请重新请求！");
            result.setSuccess(false);
            return result;
        }
        WallOffersTasks taskTmp = wallOffersTasksService.getTaskByIds(task.getOfferId(), player.getId()+"");

        if (taskTmp != null) {
            result.setMessage("任务已存在，请重新选择！");
            result.setSuccess(false);
            return result;
        }
        WallAdvertisersOffer offer = wallAdvertisersOfferService.getById(task.getOfferId());
        String ip = IPUtils.getIpAddr(request);
        String ua = request.getHeader("user-agent");
//        //临时测试使用，上线前去掉
//        ua="Mozilla/5.0 (Linux; Android 9; SM-N950N Build/PPR1.180610.011; wv) " +
//                "AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/81.0.4044.138 Mobile Safari/537.36\n";
        Map<String, String> uaInfo = this.getUaInfo(ua);
        log.info("uaInfo========" + uaInfo);
        if (uaInfo != null) {
            task.setClickPlatform(Integer.parseInt(uaInfo.get("plateform")));
            task.setClickDevice(uaInfo.get("device"));
            task.setClickOsVersion(uaInfo.get("version"));
            task.setClickDeviceName(uaInfo.get("deviceName"));
            result.setMessage("添加任务成功！");
            result.success("添加任务成功");
        } else {
            task.setClickPlatform(-1);
            task.setClickDevice("error");
            task.setClickOsVersion("error");
            task.setClickDeviceName("error");
            result.setMessage("ua数据错误，添加任务成功!");
            result.success("ua数据错误，添加任务成功");
        }
        //click_id 处理
        String plaintext= task.getOfferId()+task.getPlayerId();
        //playerId+offerId+md5(palyerId=x&offerId=x)
        String encode =plaintext + MD5Util.MD5Encode("palyerId="+player.getId()+"&offerId="+task.getOfferId(),"utf-8");
        task.setClickId(encode);

        task.setOfferName(offer.getName());
        task.setPlayerName(player.getPlayerName());
        task.setDelFlag(0);
        task.setStatus(1);
        task.setClickIp(ip);
        task.setClickUa(ua);
        task.setCreateBy(player.getPlayerName());
        task.setCreateTime(new Date());
        wallOffersTasksService.save(task);
        //返回前台302跳转方法地址
        result.setResult("/ads/redirectTask?click_id="+encode);
        System.out.println("result============"+result);
        return result;
    }
    /**
     * 用户查询任务
     *
     * @param playerId
     * @return
     */
    @AutoLog(value = "ads-查询任务")
    @ApiOperation(value = "ads-查询任务", notes = "ads-查询任务")
    @GetMapping(value = "/queryTasks")
    public Result<?> queryTasks(String playerId, HttpServletRequest request) {
//        List<WallOffersTasks> tasks = new ArrayList<WallOffersTasks>();
        Page<TaskOfferListVo> tasks =null;
        Integer pageNo =1;
        Integer pageSize =10;
        //登录验证
        WallPlayer player = (WallPlayer)request.getSession().getAttribute("playerUser");
        if (player==null){
            return Result.error("用户未登录，请重新登录后再操作！");
        }
        WallOffersTasks task = new WallOffersTasks();
        task.setPlayerId(player.getId()+"");
        task.setDelFlag(0);
        try {
            tasks = wallOffersTasksService.getTaskPageList(new Page<TaskOfferListVo>(pageNo, pageSize),task);
        } catch (Exception e) {
            Result.error("查询任务失败" + e.getMessage());
        }
        System.out.println("task===="+task);
        System.out.println("tasks===="+tasks);
        return Result.OK("查询任务成功", tasks);
    }
    /**
     *   前端注册
     *
     * @param wallPlayer
     * @return
     */
    @AutoLog(value = "wallPlayer-注册")
    @ApiOperation(value="wallPlayer-注册", notes="wallPlayer-注册")
    @PostMapping(value = "/register")
    public Result<?> register(@RequestBody WallPlayer wallPlayer, HttpServletRequest request) {
        Result<WallPlayer> result = new Result<WallPlayer>();
        if (wallPlayer.getPlayerName()==null||"".equals(wallPlayer.getPlayerName().trim())
        ||wallPlayer.getPlayerPassword()==null||"".equals(wallPlayer.getPlayerPassword().trim())){
            result.setMessage("用户名或密码有误，请重新填写！");
            result.setSuccess(false);
            return result;
        }
        WallPlayer player = wallPlayerService.getPlayerByName(wallPlayer.getPlayerName());
        System.out.println("player======"+player);
        if (player != null) {
            result.setMessage("用户名已注册");
            result.setSuccess(false);
            System.out.println("result======"+result);
            return result;
        }

        String ip = IPUtils.getIpAddr(request);
        String userAgent = request.getHeader("user-agent");
        String salt = oConvertUtils.randomGen(8);
        String password =wallPlayer.getPlayerPassword();
        if(password==null||"".equals(StrUtil.nullToEmpty(password))){
            password = PasswordUtil.encrypt(wallPlayer.getPlayerName(), "123456", salt);
        }else {
            password = PasswordUtil.encrypt(wallPlayer.getPlayerName(), wallPlayer.getPlayerPassword(), salt);
        }
        try {
            wallPlayer.setSalt(salt);
            wallPlayer.setPlayerPassword(password);
            wallPlayer.setUa(userAgent);
            wallPlayer.setIp(ip);
            wallPlayer.setStatus(1);
            wallPlayer.setDelFlag("0");
            wallPlayer.setCreateTime(new Date());// 设置创建时间
            wallPlayerService.save(wallPlayer);
            result.success("注册成功");
        } catch (Exception e) {
            result.setMessage("注册失败"+e.getMessage());
            result.setSuccess(false);
            result.error500("注册失败"+e.getMessage());
        }
        log.info(wallPlayer.getPlayerName()+"注册成功========");
        return result;
    }
    /**
     *   前端登录
     *
     * @param
     * @return
     */
    @AutoLog(value = "wallPlayer-登录")
    @ApiOperation(value="wallPlayer-登录", notes="wallPlayer-登录")
    @PostMapping (value = "/wallPlayerLogin")
    public Result<?> wallPlayerLogin( WallPlayer player, HttpServletRequest request) {
        Result<WallPlayerVo> result = new Result<WallPlayerVo>();
        String name=player.getPlayerName();
        String password=player.getPlayerPassword();
        WallPlayer checkPlayer = wallPlayerService.getPlayerByName(name);
        if (checkPlayer == null) {
            result.setSuccess(false);
            result.setMessage("用户名或密码错误!");
            return result;
        }
        String checkPassword = PasswordUtil.encrypt(name, password, checkPlayer.getSalt());
        password = checkPlayer.getPlayerPassword();
        if (!password.equals(checkPassword)) {
            result.setSuccess(false);
            result.setMessage("用户名或密码错误!");
            return result;
        }
        WallPlayerVo vo = new WallPlayerVo();
        vo.setId(checkPlayer.getId());
        vo.setPlayerName(checkPlayer.getPlayerName());
        //将user保存到session中，再次访问页面的时候，登录拦截器就可以找到这个user对象，就不需要再次拦截到登录界面了.
        request.getSession().setAttribute("playerUser", checkPlayer);
        result.setResult(vo);
        result.success("登录成功");
        log.info(name+"登录成功========");
        return result;
    }

    public static void main(String[] args) {
        String checkPassword = PasswordUtil.encrypt("test1", "123456", "RCGTeGiH");
        System.out.println("checkPassword=="+checkPassword);

    }
    /**
     * 获取用户访问ua信息
     *
     * @param ua
     * @return
     */
    private Map<String, String> getUaInfo(String ua) throws StringIndexOutOfBoundsException {
        Map<String, String> map = new HashMap<>();
        log.info("ua=========" + ua);
        String plateform = "";
        String version = "";
        String device = "";//0.android 1.iPhone 2.iPad
        String deviceName = "";
        try {
            if (ua.indexOf("Android") > 0) {
                //Mozilla/5.0 (Linux; Android 8.0; SM-G9550 Build/samsung; wv)
                // AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/52.0.2743.100 Mobile Safari/537.36
                plateform = "0";
                device = "0";
                if (ua.indexOf("U; Android") > 0) {
                    //Dalvik/2.1.0 (Linux; U; Android 9; SM-G950N Build/PPR1.180610.011)
                    String uas = ua.substring(ua.indexOf("(") + 1, ua.indexOf(")")).trim();
                    //				System.out.println(uas);
                    String[] infos = uas.split(";");
                    //Android 9
                    version = infos[2].trim().split("\\s+")[1].length() >= 10 ? "" : infos[2].trim().split("\\s+")[1];
                    //				SM-G950N Build/PPR1.180610.011
                    deviceName = infos[3].trim().split("\\s+")[0].length() > 10 ? "" : infos[3].trim().split("\\s+")[0];
                } else {
                    //			(Linux; Android 8.0; SM-G9550 Build/samsung; wv)
                    String uas = ua.substring(ua.indexOf("(") + 1, ua.indexOf(")")).trim();
                    //				System.out.println(uas);
                    String[] infos = uas.split(";");
                    //Android 8.0
                    version = infos[1].trim().split("\\s+")[1].length() >= 10 ? "" : infos[1].trim().split("\\s+")[1];
                    //			//SM-G9550 Build/samsung
                    deviceName = infos[2].trim().split("\\s+")[0].length() >= 10 ? "" : infos[2].trim().split("\\s+")[0];

                }
            } else {
                if (ua.indexOf("iPhone") > 0) {
                    //Mozilla/5.0 (iPhone; CPU iPhone OS 14_4_2 like Mac OS X)
                    // AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/18D70 Safari/604.1
                    plateform = "1";
                    device = "1";
                } else if (ua.indexOf("iPad") > 0) {
                    //Mozilla/5.0 (iPad; CPU OS 14_4_2 like Mac OS X)
                    // AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148
                    plateform = "1";
                    device = "2";
                }
                //(iPhone; CPU iPhone OS 14_4_2 like Mac OS X)
                String uas = ua.substring(ua.indexOf("(") + 1, ua.indexOf(")"));
                //				System.out.println(uas);
                String[] infos = uas.split(";");
                //				//CPU iPhone OS 14_4_2 like Mac OS X
                version = infos[1].substring(infos[1].indexOf("OS") + 2, infos[1].indexOf("like") - 1);
                deviceName = infos[1].substring(infos[1].indexOf("like") + 4);
            }
            map.put("plateform", plateform.trim());
            map.put("version", version.trim());
            map.put("device", device.trim());
            map.put("deviceName", deviceName.trim());
        } catch (StringIndexOutOfBoundsException e) {
            log.info("解析ua数据内容出错");
//				e.printStackTrace();
            return null;
        }

        return map;
    }

    public static List<String> readTxtFile(String filePath) {
        List<String> list = new ArrayList<>();
        try {

            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int i = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
//					 System.out.println("第"+i+"行："+lineTxt);
                    list.add(lineTxt);
                    i++;
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return list;
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
