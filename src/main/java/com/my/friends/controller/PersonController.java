package com.my.friends.controller;

import com.my.friends.dao.Lb;
import com.my.friends.dao.LbItem;
import com.my.friends.dao.Person;
import com.my.friends.dao.extend.LbXm;
import com.my.friends.service.PersonService;
import com.mysql.jdbc.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    public ArrayList<LbXm> getLbXms(@RequestParam String parent){
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
    // 1.2 新增或更新项目
    @ApiOperation(value = "新增或更新项目信息")
    @PostMapping("/insertOrUpdateLbItem")
    public Boolean insertOrUpdateItem(@RequestBody LbItem lbItem){
        String parent = lbItem.getParent();
        if(StringUtils.isNullOrEmpty(parent)){
            return false;
        }
        return personService.insertOrUpdateItem(lbItem);
    }
}
