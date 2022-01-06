package com.my.friends.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * t_qy
 * @author 
 */
@Data
public class Qy implements Serializable {
    /**
     * 代码
     */
    private String id;

    /**
     * 企业代码
     */
    private String code;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 电话
     */
    private String contact;

    /**
     * 手机
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    private static final long serialVersionUID = 1L;
}