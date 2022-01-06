package com.my.friends.dao;

import java.io.Serializable;
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

    private static final long serialVersionUID = 1L;
}