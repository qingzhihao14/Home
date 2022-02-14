package com.my.friends.service;

import com.my.friends.dao.*;
import com.my.friends.dao.extend.LbXm;
import com.my.friends.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public interface PersonService {
    // 获取自己的信息
    Person getMyInfo(String WeChat);

    // 获取一个人
    Person getOnePerson(String WeChat);


    /*
    * 1.类别
    * */

    //查询
    Result getLb();
    Result insertOrUpdateLb(Lb lb);
    //查询
    Result selectLbXm(String parent);


    /*
     * 2.项目
     * */
    Result insertOrUpdateItem(LbItem lbItem);

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
    Result order(String usercode, String code, String orderno, Integer pay, String addressid, String address, String phone, String name, String servicetime, String coupon, String note, MultipartFile[] files);


    /*
     * 4.用户信息
     * */
    // 新增或更新项目
    Result login(User user);

    //查询个人订单信息
    Result getOrder(String usercode);
    //查询个人订单信息
    Result getPictures(String orderno);
    //查询
    Result getLoginInfo();

    /*
     * 5.地址查询
     * */

    //查询d订单地址
    Address getAddressById(String id);
    //查询个人地址集合
    Result getAddress(String usercode);

    Result insertOrUpdateAddress(String addressid,String usercode,String addressz,String phone,String name);


    /*
     * 6.管理员登录
     * */
    Result adminlogin(Admin admin);
    Result admininfo(String token);

     // 管理员创建和修改密码
    Result insertOrUpdateAdmin(String id,String code,String psd,String name,Integer sex);
}
