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
    @Select("SELECT   "
            + "a.id, " +
            "a.user_id, " +
            "a.product_id, " +
            "a.order_no, " +
            "a.order_status, " +
            "a.total_fee, " +
            "a.title, " +
            "a.code_url, " +
            "a.create_time, " +
            "a.update_time, " +
            "a.addressno, " +
            "a.servicetime, " +
            "a.coupon, " +
            "a.note, " +
            "b.id addId, " +
            "b.code addCode, " +
            "b.name addName, " +
            "b.address addAddress, " +
            "b.phone addPhone, " +
            "c.id fileId, " +
            "c.orderno, " +
            "c.name fileName, " +
            "c.path, " +
            "c.type " +
            "FROM  t_orders_info a  " +
            "left join t_address b on a.addressno=b.id " +
            "left join t_picture c on a.order_no=c.orderno " +
            "where user_id = #{user_id} order by update_time desc ")
//    ArrayList<OrdersInfo> getOrdersInfoByUsercode(@Param("user_id") String user_id);
    ArrayList<AllOrdersInfo> getOrdersInfoByUsercode(@Param("user_id") String user_id);
    /*
     *
     *   根据订单号查询个人订单
     * */
    @Select("<script> SELECT   "
            + "a.id, " +
            "a.user_id, " +
            "a.product_id, " +
            "a.order_no, " +
            "a.order_status, " +
            "a.total_fee, " +
            "a.title, " +
            "a.code_url, " +
            "a.create_time, " +
            "a.update_time, " +
            "a.addressno, " +
            "a.servicetime, " +
            "a.coupon, " +
            "a.note, " +
            "b.id addId, " +
            "b.code addCode, " +
            "b.name addName, " +
            "b.address addAddress, " +
            "b.phone addPhone, " +
            "c.id fileId, " +
            "c.orderno, " +
            "c.name fileName, " +
            "c.path, " +
            "c.type " +
            "FROM  t_orders_info a  " +
            "left join t_address b on a.addressno=b.id " +
            "left join t_picture c on a.order_no=c.orderno " +
//            "where a.user_id = #{user_id} and  a.order_status = #{order_status}  " +
//            "where a.user_id = #{user_id} and  a.order_status in (:order_status)  " +
            "where a.user_id = #{user_id} and  " +
//            "a.order_status in ('#{order_status}')  " +
            "a.order_status in <foreach collection='order_status' item='order_statu' index='index' open='('  separator=',' close=')'>#{order_statu}</foreach>  " +
            "order by update_time desc </script> ")
//    ArrayList<OrdersInfo> getOrdersInfoByUsercode(@Param("user_id") String user_id);
//    ArrayList<AllOrdersInfo> getOrdersInfoByUsercodeAndStatus(@Param("user_id") String user_id,@Param("order_status") List<String> order_status);
        ArrayList<AllOrdersInfo> getOrdersInfoByUsercodeAndStatus(@Param("user_id") String user_id,@Param("order_status") List<String> order_status);
//    ArrayList<AllOrdersInfo> getOrdersInfoByUsercodeAndStatus(@Param("user_id") String user_id,@Param("order_status") String[] order_status);
    /*
     *
     *   根据订单号查询个人订单
     * */
    @Select("SELECT   "
            + "a.id, " +
            "a.user_id, " +
            "a.product_id, " +
            "a.order_no, " +
            "a.order_status, " +
            "a.total_fee, " +
            "a.title, " +
            "a.code_url, " +
            "a.create_time, " +
            "a.update_time, " +
            "a.addressno, " +
            "a.servicetime, " +
            "a.coupon, " +
            "a.note, " +
            "u.id, " +
            "u.code, " +
            "u.name username, " +
            "u.sex, " +
            "u.wechat, " +
            "u.note avatar," +
            "b.id addId, " +
            "b.code addCode, " +
            "b.name name, " +
            "b.address addAddress, " +
            "b.phone addPhone, " +
            "c.id fileId, " +
            "c.orderno, " +
            "c.name fileName, " +
            "c.path, " +
            "c.type " +
            "FROM  t_orders_info a  " +
            "left join t_user u on a.user_id = u.code " +
            "left join t_address b on a.addressno=b.id " +
            "left join t_picture c on a.order_no=c.orderno " +
            "order by update_time desc ")
    ArrayList<AllOrdersInfo> getAllOrdersInfoByUsercode();
//
    @Select("SELECT order_status  " +
            " FROM  t_orders_info " +
            " where order_no=#{order_no} limit 1 ")
    String getOrderStatusByOrderNo(@Param("order_no") String order_no);
    @Select("SELECT *  " +
            " FROM  t_orders_info " +
            " where order_no=#{order_no}  limit 1 ")
    OrdersInfo getOrdersByOrderNo(@Param("order_no") String order_no);

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

    @Select("SELECT *  " +
            " FROM  t_refunds_info " +
            " where order_no=#{order_no}   limit 1")
    RefundsInfo getRefundsInfoByRefundsNo(String order_no);


    /*
    * 定时查询新订单
    * */
    @Select("SELECT count(1)  " +
            " FROM  t_orders_info " +
            " order by create_time desc")
    int getNewOrdersCount();
    @Select("SELECT *  " +
            " FROM  t_orders_info " +
            " order by create_time desc limit #{limit} ")
    ArrayList<OrdersInfo> getNewOrdersList(@Param("limit") int limit);
}
