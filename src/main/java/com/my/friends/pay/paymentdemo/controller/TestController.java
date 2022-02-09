package com.my.friends.pay.paymentdemo.controller;

import com.my.friends.pay.paymentdemo.config.WxPayConfig;
import com.my.friends.pay.paymentdemo.vo.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "测试控制器")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Resource
    private WxPayConfig wxPayConfig;

    @GetMapping
    public R getWxPayConfig(){

        String mchId = wxPayConfig.getMchId();
        return R.ok().data("mchId", mchId);
    }
}
