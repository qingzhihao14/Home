package com.my.friends.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * t_user
 * @author 
 */
@Data
public class User implements Serializable {
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
    private Integer wechat;

    /**
     * 备注
     */
    private String note;

    private static final long serialVersionUID = 1L;
}