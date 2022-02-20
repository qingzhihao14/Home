package com.my.friends.controller;

import cn.hutool.json.JSONUtil;
import com.my.friends.dao.*;
import com.my.friends.mapper.SqlService;
import com.my.friends.service.CommandService;
import com.my.friends.service.PersonService;
import com.my.friends.utils.CodeMsg;
import com.my.friends.utils.Result;
import com.my.friends.utils.pay.JsCodeSession;
import com.my.friends.utils.pay.WeiXinUtil;
import com.mysql.jdbc.StringUtils;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/home")
@Api(description = "信息管理", hidden=true)
public class PersonController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CommandService commandService;

    @Autowired
    private PersonService personService;

    @Resource
    SqlService sqlService;

    private static final
    org.apache.commons.logging.Log log= LogFactory.getLog(PersonController.class);

//    @GetMapping("/init")
//    public String init(HttpServletRequest req, HttpServletResponse resp) throws NoSuchAlgorithmException, IOException
//    {
//        String signature = req.getParameter("signature");
//        String timestamp = req.getParameter("timestamp");
//        String nonce = req.getParameter("nonce");
//        String echostr = req.getParameter("echostr");
//        String[] arr = {"zhbcm", timestamp, nonce};
//        Arrays.sort(arr);//排序
//        StringBuilder builder = new StringBuilder();
//        for (String s : arr)
//        {
//            builder.append(s);//合并
//        }
//        String sha1 = SHA1.sha1(builder.toString());//加密
//        if (sha1.equals(signature))
//        {
////            PrintWriter pw = resp.getWriter();
////            pw.println(echostr);
////            pw.flush();
////            pw.close();
//            log.info("接入成功！");
//            return echostr;
//        } else
//        {
//            log.error("接入失败！");
//            return echostr;
//        }
//    }

    /*
    * 获取微信用户openid
    * */
    public Result getOpenId(HttpServletRequest request){

        String token = request.getHeader("token");
        log.info("saveLoginInfo获取header_token="+token);
        if(!StringUtils.isNullOrEmpty(token)){
            String cookiez = redisTemplate.opsForValue().get(token);
            log.info("saveLoginInfo_通过redis获取到的session值="+cookiez);
            if(!StringUtils.isNullOrEmpty(cookiez)){
                String[] strings = cookiez.split("，");
                if(strings.length<=0){
                    return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
                }
                User user = sqlService.getUser(strings[1]);
                return Result.success(user);
            }else{
                return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
            }
        }else{
            return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
        }
    }
    /*
     * 0.获取类别、项目信息
     * */
    @ApiOperation(value = "获取类别、项目信息")
    @GetMapping("/getLbXms")
    public Result getLbXms(HttpServletRequest request,
                           @ApiParam(value = "类别号(传空查询所有)",required = false,defaultValue = "LB1")
                           @RequestParam(required = false) String parent){
//        if(StringUtils.isNullOrEmpty(parent)){
//            return Result.error(CodeMsg.PARAMETER_ISNULL,"类别号为空");
//        }

        String value = redisTemplate.opsForValue().get("AAA");
        log.info("Session设置成功{key=AAA,value="+value);
        return personService.selectLbXm(parent);
    }

    /*
    * 1.类别
    * */
    // 1.1 查询类别
    @ApiOperation(value = "获取类别信息")
    @GetMapping("/getLb")
    public Result getLb(HttpServletRequest request){
        HttpSession session = request.getSession();
        //以秒为单位，即在没有活动30分钟后，session将失效
//        session.setMaxInactiveInterval(30*60);
        long l = System.currentTimeMillis();
        session.setAttribute("personalKey", l);
        log.info("Session设置成功{key=personalKey,value="+session.getAttribute("personalKey"));
        redisTemplate.opsForValue().set("AAA", "ttttttttttttttest", 7200, TimeUnit.SECONDS);
        return personService.getLb();
    }
    @ApiOperation(value = "删除类别信息")
    @GetMapping("/delLb")
    public Result delLb(HttpServletRequest request){
        String code = request.getParameter("code");
        if(StringUtils.isNullOrEmpty(code)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"类别code为空");
        }
        return personService.delLb(code);
    }
    // 1.1 查询项目
    @ApiOperation(value = "获取项目信息（热销、精选）")
    @GetMapping("/getXm")
    public Result getXm(HttpServletRequest request){
        String code = request.getParameter("code");
        return personService.getXm(code);
    }
    @ApiOperation(value = "删除项目信息")
    @GetMapping("/delXm")
    public Result delXm(HttpServletRequest request){
        String code = request.getParameter("code");
        if(StringUtils.isNullOrEmpty(code)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"项目code为空");
        }
        return personService.delXm(code);
    }
    @ApiOperation(value = "设置项目精选")
    @GetMapping("/changeIsChoice")
    public Result changeIsChoice(HttpServletRequest request){
        String ischoice = request.getParameter("ischoice");
        String id = request.getParameter("id");
        if(StringUtils.isNullOrEmpty(ischoice)||StringUtils.isNullOrEmpty(id)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"ischoice或id为空");
        }
        return personService.changeIsChoice(ischoice, id);
    }
    // 1.2 新增或更新类别
    @ApiOperation(value = "新增或更新类别信息")
    @RequestMapping(value = "/insertOrUpdateLb", method = {RequestMethod.POST})
    public Result insertOrUpdateLb(@RequestBody Map<String,String> remap,
                                   HttpServletRequest request){
        String id = remap.get("id");
        Lb lb = new Lb();
        if(!StringUtils.isNullOrEmpty(id)){
            lb.setId(id);
        }
        lb.setCode(remap.get("code"));
        lb.setName(remap.get("name"));
        return personService.insertOrUpdateLb(lb);
    }


    /*
    * 2.项目
    * */
    // 1.1 新增或更新项目
    @ApiOperation(value = "新增或更新项目信息")
    @RequestMapping(value = "/insertOrUpdateLbItem", method = {RequestMethod.POST, RequestMethod.GET})
    public Result insertOrUpdateItem( @RequestParam Map<String,String> remap,
                                      @ApiParam(value = "图片上传",required = false,defaultValue = "")  @RequestParam(value = "file",required = false) MultipartFile[] files){
        String create = remap.get("create");
        String parent = remap.get("parent");
        if(StringUtils.isNullOrEmpty(parent)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"类别号为空");
        }
        LbItem lbItem = new LbItem();
        lbItem.setId(remap.get("id"));
        lbItem.setCode(remap.get("code"));
        lbItem.setName(remap.get("name"));
        lbItem.setFwbz(remap.get("fwbz"));
        lbItem.setFwqx(remap.get("fwqx"));
        lbItem.setFwxj(remap.get("fwxj"));
        lbItem.setFwxz(remap.get("fwxz"));
        lbItem.setParent(remap.get("parent"));
        String price = remap.get("price");
        if(!StringUtils.isNullOrEmpty(price)){
            lbItem.setPrice(Integer.parseInt(price));
        }
        lbItem.setUnit(remap.get("unit"));
        return personService.insertOrUpdateItem(lbItem, files,create);
    }


    // 1.1 新增或更新项目
    @ApiOperation(value = "上传项目图片")
    @RequestMapping(value = "/insertOrUpdateLbItemPic", method = {RequestMethod.POST, RequestMethod.GET})
    public Result insertOrUpdateLbItemPic(
//            @RequestBody Map<String,String> remap, //不行
            @RequestParam Map<String,String> remap,
            @ApiParam(value = "图片上传",required = false,defaultValue = "")  @RequestParam(value = "file",required = false) MultipartFile[] files,
            HttpServletRequest request){
        String id = remap.get("id");
        if(StringUtils.isNullOrEmpty(id)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"项目号为空");
        }
        if(files.length == 0){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"无图片");
        }
        return personService.insertOrUpdateLbItemPic(id,files);
    }

    /*
     *
     * 3.下单
     *
     * usercode 用户代码
     * code 项目代码
     * orderno 订单编码
     * pay 支付金额
     * addressno 服务地址编码
     * servicetime 服务时间
     * coupon 优惠券
     * note 备注
     * */
    // 更新state状态订单状态(0-未完成，1-已完成，2-已取消)
    @ApiOperation(value = "下单【个人】")
    @RequestMapping(value = "/order", method = {RequestMethod.GET})
    public Result order(
            @ApiParam(value = "项目代码",required = false,defaultValue = "")  @RequestParam(required = false) String code,
            @ApiParam(value = "订单编码",required = false,defaultValue = "")  @RequestParam(required = false) String orderno,
            @ApiParam(value = "支付金额",required = false,defaultValue = "0")  @RequestParam(required = false) Integer pay,
            @ApiParam(value = "地址id(新增不传、更新传)",required = false,defaultValue = "0")  @RequestParam(required = false) String addressid,
            @ApiParam(value = "地址",required = false,defaultValue = "")  @RequestParam(required = false) String address,
            @ApiParam(value = "手机号",required = false,defaultValue = "")  @RequestParam(required = false) String phone,
            @ApiParam(value = "姓名",required = false,defaultValue = "")  @RequestParam(required = false) String name,
            @ApiParam(value = "服务时间",required = false,defaultValue = "")  @RequestParam(required = false) String servicetime,
            @ApiParam(value = "优惠券",required = false,defaultValue = "")  @RequestParam(required = false) String coupon,
            @ApiParam(value = "备注",required = false,defaultValue = "")  @RequestParam(required = false) String note,
    @ApiParam(value = "图片上传",required = false,defaultValue = "")  @RequestParam(value = "file",required = false) MultipartFile[] files,
            HttpServletRequest request){
        String way = request.getParameter("way");
        String usercode = "";
        if(!"all".equals(way)){
            Result result = getOpenId(request);
            if(result.getCode() != 0){
                return result;
            }
            usercode = (String) result.getData();
        }
        Logss logss = new Logss();
        logss.setUserId(usercode);
        logss.setUsername("**下单");
        logss.setUrlName("下单【个人】");
        logss.setUrl("/order");
        HashMap<Object, Object> reqMap = new HashMap<>();
        reqMap.put("usercode",usercode);
        reqMap.put("code",code);
        reqMap.put("orderno",orderno);
        reqMap.put("pay",pay);
        reqMap.put("addressid",addressid);
        reqMap.put("address",address);
        reqMap.put("phone",phone);
        reqMap.put("name",name);
        reqMap.put("servicetime",servicetime);
        reqMap.put("coupon",coupon);
        reqMap.put("note",note);
        reqMap.put("files",files);
        logss.setParam(JSONUtil.toJsonStr(reqMap));
        personService.insertLog(logss);
        return personService.order( usercode,  code,  orderno,  pay, addressid,  address, phone, name,  servicetime,  coupon,  note, files);
    }



    /*
     * 4.用户登录
     * 根据【微信号】新增或获取账号信息
     * */
    // 1.1 登录
    @ApiOperation(value = "根据【微信code】登录【个人】")
    @GetMapping(path = "/login")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "code",value ="微信code",dataType ="String")})
    public Result login(@ApiIgnore @RequestParam(required = false) Map<String,String> remap,
                        HttpServletRequest request){
//        String wechat = remap.get("wechat");
//        String password = remap.get("password");
//        if(StringUtils.isNullOrEmpty(wechat)){
//            return Result.error(CodeMsg.PARAMETER_ISNULL,"微信号为空");
//        }
//        User user = new User();
//        user.setWechat(wechat);
//        return personService.login(user);


        String code = remap.get("code");
        if(StringUtils.isNullOrEmpty(code)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"微信code");
        }
        JSONObject jsonObject = WeiXinUtil.getSessionkeyAndOpenid(code);

        JsCodeSession jsCodeSession =new JsCodeSession();

        if(!jsonObject.containsKey("errcode")){
            jsCodeSession.setSession_key(jsonObject.getString("session_key"));
            jsCodeSession.setOpenId(jsonObject.getString("openid"));

            log.info("通过appid+appSecret+code获取jsonObject =>"+jsonObject.toString());
            log.info("通过appid+appSecret+code获取session_key+openid =>"+jsCodeSession.toString());

            log.info("开始执行！");
            log.info("head -n 80 /dev/urandom | tr -dc A-Za-z0-9 | head -c 168");
            String personalKey = commandService.executeCmd("head -n 80 /dev/urandom | tr -dc A-Za-z0-9 | head -c 168");

            HashMap<String, String> map = new HashMap<>();
            map.put("cookie",personalKey);
            map.put("openid",jsCodeSession.getOpenId());
            map.put("sessionkey",jsCodeSession.getSession_key());
            log.info("设置Session{key="+personalKey+",value="+jsCodeSession.getOpenId()+"，"+jsCodeSession.getSession_key());
            HttpSession session = request.getSession(true);
            //以秒为单位，即在没有活动30分钟后，session将失效
            session.setMaxInactiveInterval(30*60);
            session.setAttribute(personalKey, jsCodeSession.getOpenId()+"，"+jsCodeSession.getSession_key());
            log.info("Session设置成功{key="+personalKey+",value="+session.getAttribute(personalKey));
            redisTemplate.opsForValue().set(personalKey, jsCodeSession.getOpenId()+"，"+jsCodeSession.getSession_key(), 7200, TimeUnit.SECONDS);

            Logss logss = new Logss();
            logss.setUserId(jsCodeSession.getOpenId());
            logss.setUsername("");
            logss.setUrlName("根据【微信code】登录【个人】");
            logss.setUrl("/login");
            remap.put("code",code);
            logss.setParam(JSONUtil.toJsonStr(remap));
            personService.insertLog(logss);

            return Result.success(map);
        }else{
            return Result.success(jsonObject);
        }
    }

    // 1.2 保存微信登录信息
    @ApiOperation(value = "登录时保存微信登录信息【个人】")
    @RequestMapping(value = "/saveLoginInfo", method = {RequestMethod.POST})
    public Result saveLoginInfo(@RequestBody Map<String,String> remap,HttpServletRequest request){
        String openid = "";
        User user = new User();
        String token = request.getHeader("token");
        log.info("saveLoginInfo获取header_token="+token);
        if(!StringUtils.isNullOrEmpty(token)){
            String cookiez = redisTemplate.opsForValue().get(token);
            log.info("saveLoginInfo_通过redis获取到的session值="+cookiez);
            if(!StringUtils.isNullOrEmpty(cookiez)){
                String[] strings = cookiez.split("，");
                if(strings.length<=0){
                    return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
                }
                openid = strings[1];
            }else{
                return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
            }
        }else{
            return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
        }
        user.setCode(openid);
        if(!StringUtils.isNullOrEmpty(remap.get("gender"))){
            Integer gender = Integer.parseInt(remap.get("gender"));
            user.setSex(gender);
        }
        if(!StringUtils.isNullOrEmpty(remap.get("avatarUrl"))){
            String avatarUrl = remap.get("avatarUrl");
            user.setNote(avatarUrl);
        }
        String nickName = remap.get("nickName");
        if(!StringUtils.isNullOrEmpty(remap.get("nickName"))){
            user.setName(nickName);
        }
        String userName = remap.get("userName");
        String detailInfo = remap.get("detailInfo");
        String telNumber = remap.get("telNumber");

        Logss logss = new Logss();
        logss.setUserId(openid);
        logss.setUsername(nickName);
        logss.setUrlName("保存微信登录信息【个人】");
        logss.setUrl("/saveLoginInfo");
        remap.put("token",token);
        logss.setParam(JSONUtil.toJsonStr(remap));
        personService.insertLog(logss);

        return personService.login(user, userName, detailInfo, telNumber);
    }

    // 1.1 查询个人信息
    @ApiOperation(value = "获取微信登录信息【个人】")
    @GetMapping("/getLoginInfo")
    public Result getLoginInfo(HttpServletRequest request){
        String way = request.getParameter("way");
        String code = "";
        if(!"all".equals(way)){
            Result result = getOpenId(request);
            if(result.getCode() != 0){
                return result;
            }
            code = (String) result.getData();
        }

        Logss logss = new Logss();
        logss.setUserId(code);
        logss.setUsername("");
        logss.setUrlName("获取微信登录信息【个人】");
        logss.setUrl("/getLoginInfo");
        HashMap<Object, Object> remap = new HashMap<>();
        remap.put("way",way);
        remap.put("code",code);
        logss.setParam(JSONUtil.toJsonStr(remap));
        personService.insertLog(logss);
        return personService.getLoginInfo(code,way);
    }
    /*
     * 1.1.1.用户登录之后 --=》废弃
     * 根据【微信code和session】新增或获取账号信息
     * */
//    @ApiOperation(value = "根据【session】获取微信号相关信息")
//    @GetMapping(path = "/getDetailes")
//    public Result getDetailes(HttpServletRequest request){
//        String cookie = request.getHeader("cookie");
//        String token = request.getHeader("token");
//        log.info("getDetailes_cookie="+cookie);
//        log.info("getDetailes_token="+token);
//        Enumeration headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String key = (String) headerNames.nextElement();
//            String value = request.getHeader(key);
//            log.info("key="+key+" -- value="+value);
//            if("token".equals(key)){
//                token = value;
//                log.info("key="+key+" -- value="+value);
//            }
//        }
//        log.info("getDetailes_cookie="+cookie);
//        log.info("getDetailes_token="+token);
//        if(!StringUtils.isNullOrEmpty(token)){
//            HttpSession session = request.getSession();
//            log.info("getDetailes_session="+session);
//            String cookiez = (String)session.getAttribute(token);
//            log.info("getDetailes_cookiez="+cookiez);
//            if(StringUtils.isNullOrEmpty(cookiez)){
//                return Result.error(CodeMsg.SESSION_NOT_EXSIST,"请先登录");
//            }
//            String[] strings = cookiez.split("，");
//            if(strings.length<=0){
//                return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Cookie无效");
//            }
//            SNSUserInfo snsUserInfo = WeiXinUtil.getSNSUserInfo(strings[0], strings[1]);
//            log.info("getDetailes_snsUserInfo="+snsUserInfo.toString());
//            return Result.success(snsUserInfo);
//        }else{
//            return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Cookie无效");
//        }
//    }

    // 1.3 查询订单-图片
    @ApiOperation(value = "查询订单-图片")
    @GetMapping("/getPictureByOrderno")
    public Result getPictureByOrderno(
            @ApiParam(value = "订单号",required = false,defaultValue = "dd202201011212") @RequestParam(required = false) String Orderno){
        if(StringUtils.isNullOrEmpty(Orderno)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"订单号为空");
        }
        return personService.getPictures(Orderno);
    }

    /*
     * 5.地址
     * */
    // 1.1 查询地址
    @ApiOperation(value = "获取地址信息【个人】")
    @GetMapping("/getAddress")
    public Result getAddress(
            HttpServletRequest request){
        String way = request.getParameter("way");
        String code = "";
        if(!"all".equals(way)){
            Result result = getOpenId(request);
            if(result.getCode() != 0){
                return result;
            }
            code = (String) result.getData();
        }

        Logss logss = new Logss();
        logss.setUserId(code);
        logss.setUsername("");
        logss.setUrlName("查询订单【个人】");
        logss.setUrl("/getLoginInfo");
        HashMap<Object, Object> remap = new HashMap<>();
        remap.put("way",way);
        remap.put("code",code);
        logss.setParam(JSONUtil.toJsonStr(remap));
        personService.insertLog(logss);
        return personService.getAddress(code);
    }
    // 1.2 新增或更新地址信息【个人】
    @ApiOperation(value = "新增或更新地址信息")
    @PostMapping("/insertOrUpdateAddress")
    public Result insertOrUpdateAddress(
            @ApiParam(value = "地址id(新增不传、更新传)",required = false,defaultValue = "") @RequestParam(required = false) String addressid,
//            @ApiParam(value = "地址编码",required = false,defaultValue = "") @RequestParam(required = false) String addressno,
            @ApiParam(value = "地址（必传）",required = false,defaultValue = "") @RequestParam(required = false) String address,
            @ApiParam(value = "手机号（必传）",required = false,defaultValue = "") @RequestParam(required = false) String phone,
            @ApiParam(value = "姓名（必传）",required = false,defaultValue = "") @RequestParam(required = false) String name,
            HttpServletRequest request){
        String way = request.getParameter("way");
        String code = "";
        if(!"all".equals(way)){
            Result result = getOpenId(request);
            if(result.getCode() != 0){
                return result;
            }
            code = (String) result.getData();
        }
        if(StringUtils.isNullOrEmpty(address)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"地址为空");
        }
        if(StringUtils.isNullOrEmpty(phone)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"手机号为空");
        }
        if(StringUtils.isNullOrEmpty(name)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"姓名为空");
        }

        Logss logss = new Logss();
        logss.setUserId(code);
        logss.setUsername("");
        logss.setUrlName("查询订单【个人】");
        logss.setUrl("/getLoginInfo");
        HashMap<Object, Object> remap = new HashMap<>();
        remap.put("way",way);
        remap.put("code",code);
        remap.put("addressid",addressid);
        remap.put("address",address);
        remap.put("phone",phone);
        remap.put("name",name);
        logss.setParam(JSONUtil.toJsonStr(remap));
        personService.insertLog(logss);
        return personService.insertOrUpdateAddress(addressid,code,address,phone,name);
    }
    /*
     * 6.管理员登录
     * 根据【微信号】新增或获取账号信息
     * */
    // 1.1 登录
    @ApiOperation(value = "管理员->登录")
//    @RequestMapping(value = "/adminlogin", method = {RequestMethod.POST, RequestMethod.GET})
    @RequestMapping(value = "/adminlogin", method = {RequestMethod.POST})
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query",name = "username",value ="账号",dataType ="String"),
//            @ApiImplicitParam(paramType = "query",name = "password",value ="密码",dataType ="String")})
    public Result adminlogin(
//            @ApiIgnore
            @RequestBody(required = false) Map<String, String> params){
        String code = params.get("username");
        String psd = params.get("password");
        if(StringUtils.isNullOrEmpty(code)&&StringUtils.isNullOrEmpty(psd)){
            return Result.error(CodeMsg.USER_NOT_EXSIST,"用户名或密码不能为空");
        }
        Admin admin = new Admin();
        admin.setCode(code);
        admin.setPsd(psd);
        return personService.adminlogin(admin);
    }
    // 1.1 登录
    @ApiOperation(value = "管理员->获取管理员信息")
    @PostMapping("/admininfo")
    public Result admininfo(HttpServletRequest request){
        String token = request.getParameter("token");
        if(StringUtils.isNullOrEmpty(token)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"登陆失效，请重新登陆");
        }
        return personService.admininfo(token);
    }
    // 1.2 管理员创建和修改密码
    @ApiOperation(value = "管理员->创建和修改密码")
    @PostMapping("/insertOrUpdateAdmin")
    public Result insertOrUpdateAdmin(
            @ApiParam(value = "id(新增不传、更新传)",required = false,defaultValue = "") @RequestParam(required = false) String id,
            @ApiParam(value = "用户代码（必传）",required = false,defaultValue = "") @RequestParam(required = false) String code,
            @ApiParam(value = "密码（必传）",required = false,defaultValue = "") @RequestParam(required = false) String psd,
            @ApiParam(value = "姓名（必传）",required = false,defaultValue = "") @RequestParam(required = false) String name,
            @ApiParam(value = "性别（男1，女0）",required = false,defaultValue = "1") @RequestParam(required = false) Integer sex){
        if(StringUtils.isNullOrEmpty(code)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"用户代码为空");
        }
        if(StringUtils.isNullOrEmpty(psd)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"密码为空");
        }
        return personService.insertOrUpdateAdmin(id,code,psd,name,sex);
    }

}
