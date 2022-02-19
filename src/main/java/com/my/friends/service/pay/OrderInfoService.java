package com.my.friends.service.pay;

import com.my.friends.dao.OrdersInfo;
import com.my.friends.dao.User;

import java.util.List;

public interface OrderInfoService {

    OrdersInfo createOrderByProductId(User user,String productId,String count,String pay,String addressid,String servicetime,String coupon,String note);

    void saveCodeUrl(String orderNo, String codeUrl);
//
//    List<OrderInfo> listOrderByCreateTimeDesc();
//
//    void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus);
//
//    String getOrderStatus(String orderNo);
//
    List<OrdersInfo> getNoPayOrderByDuration(int minutes);
//
//    OrderInfo getOrderByOrderNo(String orderNo);
}
