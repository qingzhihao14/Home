package com.my.friends.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * t_person
 * @author 
 */
@Data
public class Person implements Serializable {
    /**
     * uuid
     */
    private String uuid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * 性别（1男 0女）
     */
    private Integer sex;

    /**
     * 年龄
     */
    private String age;

    /**
     * 类型（普通0、红娘1）
     */
    private Integer type;

    /**
     * 省份
     */
    private String province;

    /**
     * 个人照片id
     */
    private String picturesId;

    /**
     * 爱情宣言
     */
    private String declaration;

    /**
     * 密语
     */
    private String secretKey;

    private static final long serialVersionUID = 1L;
}