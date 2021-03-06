package com.my.friends.dao;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_lb_item
 * @author 
 */
@Data
public class LbItem implements Serializable {
    /**
     * 商品id
     */
    private String id;

    /**
     * 项目代码
     */
    private String code;

    /**
     * 类别代码
     */
    private String parent;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 价格（分）
     */
    private Integer price;

    /**
     * 单位
     */
    private String unit;

    /**
     * 已售数量
     */
    private Integer sold;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 服务期限
     */
    private String fwqx;

    /**
     * 服务限制
     */
    private String fwxz;

    /**
     * 服务细节
     */
    private String fwxj;

    /**
     * 服务保证
     */
    private String fwbz;

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

    private static final long serialVersionUID = 1L;
}