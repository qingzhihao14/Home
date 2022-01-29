package com.my.friends.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.log.Log;
import com.my.friends.dao.*;
import com.my.friends.dao.extend.LbXm;
import com.my.friends.service.PersonService;
import com.my.friends.utils.CodeMsg;
import com.my.friends.utils.Result;
import com.mysql.jdbc.StringUtils;
import io.swagger.annotations.*;
import org.apache.poi.util.PackageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
@Api(description = "信息管理", hidden=true)
public class PersonController {
    @Autowired
    private PersonService personService;
//
//    @ApiOperation(value = "获取用户信息")
//    @GetMapping("/getMyInfo")
//    public Person getMyInfo(
//            @ApiParam(value = "微信号",required = true,defaultValue = "a123") @RequestParam String WeChat
//    ){
//        return personService.getMyInfo(WeChat);
//    }
//
//    @ApiOperation(value = "随机获取除本人之外的用户信息")
//    @GetMapping("/getOnePerson")
//    public Person getOnePerson(
//            @ApiParam(value = "微信号",required = true,defaultValue = "a123") @RequestParam String WeChat
//    ){
//        return personService.getOnePerson(WeChat);
//    }


    /*
     * 0.获取类别、项目信息
     * */
    @ApiOperation(value = "获取类别、项目信息")
    @GetMapping("/getLbXms")
    public Result getLbXms(@ApiParam(value = "类别号(传空查询所有)",required = false,defaultValue = "LB1")
                             @RequestParam(required = false) String parent){
//        if(StringUtils.isNullOrEmpty(parent)){
//            return Result.error(CodeMsg.PARAMETER_ISNULL,"类别号为空");
//        }
        return personService.selectLbXm(parent);
    }

    /*
    * 1.类别
    * */
    // 1.1 查询类别
    @ApiOperation(value = "获取类别、项目信息")
    @GetMapping("/getLb")
    public Result getLb(){
        return personService.getLb();
    }
    // 1.2 新增或更新类别
    @ApiOperation(value = "新增或更新类别信息")
//    @PostMapping("/insertOrUpdateLb")
    @RequestMapping(value = "/insertOrUpdateLb", method = {RequestMethod.POST})
    public Result insertOrUpdateLb(@RequestBody Map<String,String> remap){
        String id = remap.get("id");
        if(StringUtils.isNullOrEmpty(id)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"类别号为空");
        }
        Lb lb = new Lb();
        lb.setId(id);
        lb.setCode(remap.get("code"));
        lb.setName(remap.get("name"));
        return personService.insertOrUpdateLb(lb);
    }


    /*
    * 2.项目
    * */
    // 1.1 新增或更新项目
    @ApiOperation(value = "新增或更新项目信息")
//    @PostMapping("/insertOrUpdateLbItem")
    @RequestMapping(value = "/insertOrUpdateLbItem", method = {RequestMethod.POST})
    public Result insertOrUpdateItem(@RequestBody Map<String,String> remap){
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
        if(StringUtils.isNullOrEmpty(price)){
            lbItem.setPrice(Integer.parseInt(price));
        }
        String sold = remap.get("sold");
        if(StringUtils.isNullOrEmpty(sold)){
            lbItem.setSold(Integer.parseInt(sold));
        }
        lbItem.setUnit(remap.get("unit"));
        return personService.insertOrUpdateItem(lbItem);
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
    @ApiOperation(value = "下单")
    @RequestMapping(value = "/order", method = {RequestMethod.GET})
    public Result order(
            @ApiParam(value = "用户代码",required = false,defaultValue = "") @RequestParam(required = false) String usercode,
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
    @ApiParam(value = "图片上传",required = false,defaultValue = "")  @RequestParam(value = "file",required = false) MultipartFile[] files){
        return personService.order( usercode,  code,  orderno,  pay, addressid,  address, phone, name,  servicetime,  coupon,  note, files);
    }



    /*
     * 4.用户登录
     * 根据【微信号】新增或获取账号信息
     * */
    // 1.1 登录
    @ApiOperation(value = "根据【微信号】新增或获取账号信息"
//                ,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE
                )
    @GetMapping(path = "/login"
//            ,consumes = "application/json", produces = "application/json"
            )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "username",value ="微信号",dataType ="String")})
    public Result login(
            @ApiIgnore @RequestParam(required = false) Map<String,String> remap){
        String wechat = remap.get("wechat");
        String password = remap.get("password");
        if(StringUtils.isNullOrEmpty(wechat)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"微信号为空");
        }
        User user = new User();
        user.setWechat(wechat);
        return personService.login(user);
    }
    // 1.2 查询订单
    @ApiOperation(value = "查询订单")
    @GetMapping("/getOrder")
    public Result getOrder(
            @ApiParam(value = "用户代码",required = false,defaultValue = "LB1") @RequestParam(required = false) String usercode){
        if(StringUtils.isNullOrEmpty(usercode)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"用户代码为空");
        }
        return personService.getOrder(usercode);
    }
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
    @ApiOperation(value = "获取地址信息")
    @GetMapping("/getAddress")
    public Result getAddress(
            @ApiParam(value = "用户代码",required = false,defaultValue = "") @RequestParam(required = false) String usercode){
        if(StringUtils.isNullOrEmpty(usercode)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"用户代码为空");
        }
        return personService.getAddress(usercode);
    }
    // 1.2 新增或更新地址信息
    @ApiOperation(value = "新增或更新地址信息")
    @PostMapping("/insertOrUpdateAddress")
    public Result insertOrUpdateAddress(
            @ApiParam(value = "地址id(新增不传、更新传)",required = false,defaultValue = "") @RequestParam(required = false) String addressid,
            @ApiParam(value = "用户代码（必传）",required = false,defaultValue = "") @RequestParam(required = false) String usercode,
//            @ApiParam(value = "地址编码",required = false,defaultValue = "") @RequestParam(required = false) String addressno,
            @ApiParam(value = "地址（必传）",required = false,defaultValue = "") @RequestParam(required = false) String address,
            @ApiParam(value = "手机号（必传）",required = false,defaultValue = "") @RequestParam(required = false) String phone,
            @ApiParam(value = "姓名（必传）",required = false,defaultValue = "") @RequestParam(required = false) String name){
        if(StringUtils.isNullOrEmpty(usercode)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"用户代码为空");
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

        return personService.insertOrUpdateAddress(addressid,usercode,address,phone,name);
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
