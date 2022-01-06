package com.my.friends.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * t_order
 * @author 
 */
@Data
public class Order implements Serializable {
    /**
     * 代码
     */
    private String id;

    /**
     * 用户代码
     */
    private String usercode;

    /**
     * 项目代码
     */
    private String code;

    /**
     * 订单编码
     */
    private String orderno;

    /**
     * 订单状态(0-未完成，1-已完成，2-已取消)
     */
    private Integer state;

    /**
     * 支付金额
     */
    private Integer pay;

    /**
     * 购买金额
     */
    private Integer sold;

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

    private static final long serialVersionUID = 1L;
}