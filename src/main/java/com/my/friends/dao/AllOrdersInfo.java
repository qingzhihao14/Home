package com.my.friends.dao;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_orders_info
 * @author 
 */
@Data
public class AllOrdersInfo implements Serializable {
    /**
     * 订单id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 支付产品id
     */
    private String productId;

    /**
     * 商户订单编号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单金额(分)
     */
    private Integer totalFee;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 订单二维码连接
     */
    private String codeUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 服务地址编码
     */
    private String addressno;

    /**
     * 服务时间
     */
    private String servicetime;

    /**
     * 优惠券
     */
    private String coupon;

    /**
     * 备注
     */
    private String note;


    private  String name;

    private  String address;

    private  String phone;

    private  String username;

    private  String avatar;

    /**
     * 性别
     */
    private Integer sex;


    private static final long serialVersionUID = 1L;
}