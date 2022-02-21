package com.my.friends.dao;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_lb
 * @author 
 */
@Data
public class Lb implements Serializable {
    /**
     * 代码
     */
    private String id;

    /**
     * 类别编码
     */
    private String code;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 是否精选
     */
    private Integer ischoice;

    /**
     * 文件名称
     */
    private String picName;

    /**
     * 相对路径
     */
    private String picPath;

    /**
     * 文件类型
     */
    private String picType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}