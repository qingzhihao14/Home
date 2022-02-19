package com.my.friends.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class Lbs  implements Serializable {
    /**
     * 代码
     */
    private String id;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * 备注
     */
    private String note;
    /**
     * 代码
     */
    private String addressid;

    /**
     * 用户代码
     */
//    private String code;

    /**
     * 地址编码
     */
    private String realname;

    /**
     * 地址
     */
    private String address;

    private String phone;

    private static final long serialVersionUID = 1L;
}
