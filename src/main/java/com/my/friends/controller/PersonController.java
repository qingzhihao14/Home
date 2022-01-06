package com.my.friends.controller;

import com.my.friends.dao.Person;
import com.my.friends.service.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/friends")
@Api(description = "信息管理")
public class PersonController {
    @Autowired
    private PersonService personService;

    @ApiOperation(value = "获取用户信息", notes = "www.qinzh.club/friends/getMyInfo?WeChat='a123'")
    @GetMapping("/getMyInfo")
    public Person getMyInfo(
            @ApiParam(value = "微信号",required = true,defaultValue = "a123") @RequestParam String WeChat
    ){
        return personService.getMyInfo(WeChat);
    }

    @ApiOperation(value = "随机获取除本人之外的用户信息", notes = "www.qinzh.club/friends/getOnePerson?WeChat='a123'")
    @GetMapping("/getOnePerson")
    public Person getOnePerson(
            @ApiParam(value = "微信号",required = true,defaultValue = "a123") @RequestParam String WeChat
    ){
        return personService.getOnePerson(WeChat);
    }

}
