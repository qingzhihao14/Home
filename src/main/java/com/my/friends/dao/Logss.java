package com.my.friends.dao;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_log
 * @author 
 */
@Data
public class Logss implements Serializable {
    /**
     * 订单id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单标题
     */
    private String username;

    private String urlName;

    /**
     * 订单二维码连接
     */
    private String url;

    /**
     * 备注
     */
    private String param;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}