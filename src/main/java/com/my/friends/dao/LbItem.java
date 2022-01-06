package com.my.friends.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * t_lb_item
 * @author 
 */
@Data
public class LbItem implements Serializable {
    /**
     * 代码
     */
    private String id;

    /**
     * 类别代码
     */
    private String parent;

    /**
     * 项目代码
     */
    private String code;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 价格
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

    private static final long serialVersionUID = 1L;
}