package com.my.friends.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * t_admin
 * @author 
 */
@Data
public class Admin implements Serializable {
    /**
     * 代码
     */
    private String id;

    /**
     * 账号
     */
    private String code;

    /**
     * 密码
     */
    private String psd;

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

    private static final long serialVersionUID = 1L;
}