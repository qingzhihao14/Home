package com.my.friends.mapper;

import com.my.friends.dao.*;
import com.my.friends.pay.paymentdemo.enums.OrderStatus;
import com.my.friends.pay.paymentdemo.enums.wxpay.WxRefundStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Mapper
@MapperScan
public interface SqlService {

    /*
     *
     *   获取用户个人信息
     * */

    @Select("SELECT *  " +
            " FROM t_user " +
            " where code=#{code}  limit 1")
    User getUser(@Param("code")  String code);

    /*
     *
     *   获取用户个人地址信息
     * */

    @Select("SELECT *  " +
            " FROM t_address " +
            " where code=#{code}  limit 1")
    Address getAddress(@Param("code")  String code);

    /*
     *
     *   获取用户个人信息
     * */

    @Select("SELECT *  " +
            " FROM  t_orders_info " +
            " where user_id=#{user_id} and product_id=#{product_id}  and order_status=#{order_status}  limit 1")
    OrdersInfo getNoPayOrderByProductId(@Param("user_id")  String user_id, @Param("product_id")  String product_id, @Param("order_status")  String order_status);

    /*
     *
     *   获取用户个人信息
     * */

    @Select("SELECT *  " +
            " FROM  t_lb_item " +
            " where code=#{code}  limit 1")
    LbItem selectProductById(@Param("code")  String code);

    /*
    *
    *   根据订单号查询个人订单
    * */
    @Select("SELECT *  " +
            " FROM  t_orders_info " +
            " where user_id=#{user_id} order by update_time desc ")
    ArrayList<OrdersInfo> getOrdersInfoByUsercode(@Param("user_id") String user_id);

    @Select("SELECT order_status  " +
            " FROM  t_orders_info " +
            " where order_no=#{order_no} limit 1 ")
    String getOrderStatusByOrderNo(@Param("order_no") String order_no);

    @Update(" update t_orders_info " +
            " set order_status = #{order_status}" +
            " where order_no = #{order_no}" )
    int updateStatusByOrderNo(@Param("order_status") String order_status,@Param("order_no") String order_no);

    @Select("SELECT *  " +
            " FROM  t_orders_info " +
            " where order_status=#{order_status} and  unix_timestamp(create_time) < unix_timestamp(#{create_time})  ")
    List<OrdersInfo> getOrderbyOrderAndLessThanCreateTimeFiveMins(String order_status, Instant create_time);

    @Select("SELECT *  " +
            " FROM  t_refunds_info " +
            " where refund_status=#{refund_status} and  unix_timestamp(create_time) < unix_timestamp(#{create_time})  ")
    List<RefundsInfo> getRefundsInfobyOrderAndLessThanCreateTimeFiveMins(String refund_status, Instant create_time);

}