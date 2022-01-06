package com.my.friends.dao;

import java.io.Serializable;
import lombok.Data;

/**
 * t_address
 * @author 
 */
@Data
public class Address implements Serializable {
    /**
     * 代码
     */
    private String id;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 地址编码
     */
    private String addressno;

    /**
     * 地址
     */
    private String address;

    private static final long serialVersionUID = 1L;
}