package com.my.friends.service;

import com.my.friends.dao.*;
import com.my.friends.dao.extend.LbXm;

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
    ArrayList<Lb> getLb();
    Boolean insertOrUpdateLb(Lb lb);
    //查询
    ArrayList<LbXm> selectLbXm(String parent);


    /*
     * 2.项目
     * */
    Boolean insertOrUpdateItem(LbItem lbItem);

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
    Boolean order(String usercode, String code, String orderno, Integer pay, String address,String phone,String name, String servicetime, String coupon, String note);


    /*
     * 4.用户信息
     * */
    // 新增或更新项目
    String login(User user);

    //查询个人订单信息
    List<Order> getOrder(String usercode);


    /*
     * 5.地址查询
     * */

    //查询d订单地址
    Address getAddressById(String id);
    //查询个人地址集合
    List<Address> getAddress(String usercode);

    Boolean insertOrUpdateAddress(String usercode,String address,String phone,String name);
}
