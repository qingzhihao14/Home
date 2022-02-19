package com.my.friends.service.pay.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.my.friends.dao.LbItem;
import com.my.friends.dao.OrdersInfo;
import com.my.friends.dao.OrdersInfoExample;
import com.my.friends.dao.User;
import com.my.friends.mapper.OrdersInfoMapper;
import com.my.friends.mapper.SqlService;
import com.my.friends.pay.paymentdemo.enums.OrderStatus;
import com.my.friends.pay.paymentdemo.util.OrderNoUtils;
import com.my.friends.service.pay.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {



    @Resource
    SqlService sqlService;

    @Resource
    OrdersInfoMapper ordersInfoMapper;

    @Override
    public OrdersInfo createOrderByProductId(User user,String productId,String count,String pay,String addressid,String servicetime,String coupon,String note) {

        //查找已存在但未支付的订单
        OrdersInfo ordersInfo = this.getNoPayOrderByProductId(user,productId);
        if( ordersInfo != null){
            return ordersInfo;
        }

        //获取商品信息
//        Product lbItem = sqlService.selectProductById(productId);
        LbItem lbItem = sqlService.selectProductById(productId);

        //生成订单
        ordersInfo = new OrdersInfo();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        ordersInfo.setId(uuid);
        ordersInfo.setUserId(user.getCode());
        ordersInfo.setProductId(productId);
        ordersInfo.setOrderNo(OrderNoUtils.getOrderNo()); //订单号 （产生随机数）
        ordersInfo.setOrderStatus(OrderStatus.NOTPAY.getType());
        ordersInfo.setTotalFee(lbItem.getPrice()); //分
        ordersInfo.setTitle(lbItem.getName());
        ordersInfo.setAddressno(addressid);
        ordersInfo.setServicetime(servicetime);
        ordersInfo.setCreateTime(new Date());
//        ordersInfo.setCoupon();
        ordersInfo.setNote(note);
        ordersInfoMapper.insert(ordersInfo);

        return ordersInfo;
    }

    /**
     * 存储订单二维码
     * @param orderNo
     * @param codeUrl
     */
    @Override
    public void saveCodeUrl(String orderNo, String codeUrl) {

        QueryWrapper<OrdersInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        OrdersInfo ordersInfo = new OrdersInfo();
        ordersInfo.setCodeUrl(codeUrl);
        ordersInfo.setOrderNo(orderNo);
        OrdersInfoExample example = new OrdersInfoExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<OrdersInfo> ordersInfos = ordersInfoMapper.selectByExample(example);
        if(ordersInfo!=null && ordersInfos.size()>0){
            ordersInfo.setId(ordersInfos.get(0).getId());
            ordersInfoMapper.updateByPrimaryKeySelective(ordersInfo);
//            baseMapper.update(ordersInfo, queryWrapper);
        }
    }

    /**
     * 查询订单列表，并倒序查询
     * @return
     */
//    @Override
//    public List<OrdersInfo> listOrderByCreateTimeDesc() {
//
//        QueryWrapper<OrdersInfo> queryWrapper = new QueryWrapper<OrdersInfo>().orderByDesc("create_time");
//        return baseMapper.selectList(queryWrapper);
//    }

    /**
     * 根据订单号更新订单状态
     * @param orderNo
     * @param orderStatus
     */
//    @Override
//    public void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {
//
//        log.info("更新订单状态 ===> {}", orderStatus.getType());
//
//        QueryWrapper<OrdersInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("order_no", orderNo);
//
//        OrdersInfo ordersInfo = new OrdersInfo();
//        ordersInfo.setOrderStatus(orderStatus.getType());
//
//        baseMapper.update(ordersInfo, queryWrapper);
//    }

    /**
     * 根据订单号获取订单状态
     * @param orderNo
     * @return
     */
//    @Override
//    public String getOrderStatus(String orderNo) {
//
//        QueryWrapper<OrdersInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("order_no", orderNo);
//        OrdersInfo ordersInfo = baseMapper.selectOne(queryWrapper);
//        if(ordersInfo == null){
//            return null;
//        }
//        return ordersInfo.getOrderStatus();
//    }

    /**
     * 查询创建超过minutes分钟并且未支付的订单
     * @param minutes
     * @return
     */
    @Override
    public List<OrdersInfo> getNoPayOrderByDuration(int minutes) {

        Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));

//        QueryWrapper<OrdersInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("order_status", OrderStatus.NOTPAY.getType());
//        queryWrapper.le("create_time", instant);
//
//        List<OrdersInfo> orderInfoList = baseMapper.selectList(queryWrapper);
        log.info("==========OrdersInfo========"+instant);
        List<OrdersInfo> orderInfoList = sqlService.getOrderbyOrderAndLessThanCreateTimeFiveMins(OrderStatus.NOTPAY.getType(),instant);
        return orderInfoList;
    }

    /**
     * 根据订单号获取订单
     * @param orderNo
     * @return
     */
//    @Override
//    public OrdersInfo getOrderByOrderNo(String orderNo) {
//
//        QueryWrapper<OrdersInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("order_no", orderNo);
//        OrdersInfo ordersInfo = baseMapper.selectOne(queryWrapper);
//
//        return ordersInfo;
//    }


    /**
     * 根据商品id查询未支付订单
     * 防止重复创建订单对象
     * @param productId
     * @return
     */
    private OrdersInfo getNoPayOrderByProductId(User user, String productId) {

//        QueryWrapper<OrdersInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("product_id", productId);
//        queryWrapper.eq("order_status", OrderStatus.NOTPAY.getType());
//        queryWrapper.eq("user_id", userId);
//        OrdersInfo ordersInfo = baseMapper.selectOne(queryWrapper);

        OrdersInfo ordersInfo = sqlService.getNoPayOrderByProductId(user.getCode(), productId, OrderStatus.NOTPAY.getType());

        return ordersInfo;
    }
}
