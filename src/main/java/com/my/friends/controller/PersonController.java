package com.my.friends.controller;

import com.my.friends.dao.*;
import com.my.friends.dao.extend.LbXm;
import com.my.friends.service.PersonService;
import com.mysql.jdbc.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
@Api(description = "信息管理")
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
    public List<LbXm> getLbXms(@ApiParam(value = "类别号",required = false,defaultValue = "LB1")
                             @RequestParam(required = false) String parent){
        return personService.selectLbXm(parent);
    }

    /*
    * 1.类别
    * */
    // 1.1 查询类别
    @ApiOperation(value = "获取类别、项目信息")
    @GetMapping("/getLb")
    public ArrayList<Lb> getLb(){
        return personService.getLb();
    }
    // 1.2 新增或更新类别
    @ApiOperation(value = "新增或更新类别信息")
    @PostMapping("/insertOrUpdateLb")
    public Boolean getLb(@RequestBody Lb lb){
        return personService.insertOrUpdateLb(lb);
    }


    /*
    * 2.项目
    * */
    // 1.1 新增或更新项目
    @ApiOperation(value = "新增或更新项目信息")
    @PostMapping("/insertOrUpdateLbItem")
    public Boolean insertOrUpdateItem(@RequestBody LbItem lbItem){
        String parent = lbItem.getParent();
        if(StringUtils.isNullOrEmpty(parent)){
            return false;
        }
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
    @ApiOperation(value = "新增或更新类别信息")
    @PostMapping("/order")
    public Boolean getLb(
            @ApiParam(value = "用户代码",required = false,defaultValue = "LB1") @RequestParam(required = false) String usercode,
         @ApiParam(value = "项目代码",required = false,defaultValue = "LB1")  @RequestParam(required = false) String code,
         @ApiParam(value = "订单编码",required = false,defaultValue = "LB1")  @RequestParam(required = false) String orderno,
         @ApiParam(value = "支付金额",required = false,defaultValue = "0")  @RequestParam(required = false) Integer pay,
            @ApiParam(value = "地址",required = false,defaultValue = "LB1")  @RequestParam(required = false) String address,
            @ApiParam(value = "手机号",required = false,defaultValue = "LB1")  @RequestParam(required = false) String phone,
            @ApiParam(value = "姓名",required = false,defaultValue = "LB1")  @RequestParam(required = false) String name,
         @ApiParam(value = "服务时间",required = false,defaultValue = "LB1")  @RequestParam(required = false) String servicetime,
         @ApiParam(value = "优惠券",required = false,defaultValue = "LB1")  @RequestParam(required = false) String coupon,
         @ApiParam(value = "备注",required = false,defaultValue = "LB1")  @RequestParam(required = false) String note){
        return personService.order( usercode,  code,  orderno,  pay,  address, phone, name,  servicetime,  coupon,  note);
    }



    /*
     * 4.登录
     * 根据【微信号】新增或获取账号信息
     * */
    // 1.1 登录
    @ApiOperation(value = "根据【微信号】新增或获取账号信息")
    @PostMapping("/login")
    public String login(@RequestBody User user){
        String wechat = user.getWechat();
        if(StringUtils.isNullOrEmpty(wechat)){
            return "false";
        }
        return personService.login(user);
    }
    // 1.2 查询订单
    @ApiOperation(value = "查询订单")
    @GetMapping("/getOrder")
    public List<Order> getOrder(
            @ApiParam(value = "用户代码",required = false,defaultValue = "LB1") @RequestParam(required = false) String usercode){
        if(StringUtils.isNullOrEmpty(usercode)){
            return null;
        }
        return personService.getOrder(usercode);
    }


    /*
     * 5.地址
     * */
    // 1.1 查询地址
    @ApiOperation(value = "获取地址信息")
    @GetMapping("/getAddress")
    public List<Address> getAddress(
            @ApiParam(value = "用户代码",required = false,defaultValue = "LB1") @RequestParam(required = false) String usercode){
        if(StringUtils.isNullOrEmpty(usercode)){
            return null;
        }
        return personService.getAddress(usercode);
    }
    // 1.2 新增或更新地址信息
    @ApiOperation(value = "新增或更新地址信息")
    @PostMapping("/insertOrUpdateAddress")
    public Boolean getLb(
            @ApiParam(value = "用户代码",required = false,defaultValue = "") @RequestParam(required = false) String usercode,
//            @ApiParam(value = "地址编码",required = false,defaultValue = "") @RequestParam(required = false) String addressno,
            @ApiParam(value = "地址",required = false,defaultValue = "") @RequestParam(required = false) String address,
            @ApiParam(value = "手机号",required = false,defaultValue = "") @RequestParam(required = false) String phone,
            @ApiParam(value = "姓名",required = false,defaultValue = "") @RequestParam(required = false) String name){
        if(StringUtils.isNullOrEmpty(usercode)
            || StringUtils.isNullOrEmpty(address)
            || StringUtils.isNullOrEmpty(phone)
            || StringUtils.isNullOrEmpty(name)
        ){
            return false;
        }
        return personService.insertOrUpdateAddress(usercode,address,phone,name);
    }

}
