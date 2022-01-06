package com.my.friends.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * t_picture
 * @author 
 */
@Data
public class Picture implements Serializable {
    /**
     * 代码
     */
    private String id;

    /**
     * 订单编码
     */
    private String orderno;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 相对路径
     */
    private String path;

    /**
     * 文件类型
     */
    private String type;

    private static final long serialVersionUID = 1L;
}