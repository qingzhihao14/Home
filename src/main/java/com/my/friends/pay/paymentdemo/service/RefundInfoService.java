package com.my.friends.pay.paymentdemo.service;

import com.my.friends.pay.paymentdemo.entity.RefundInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RefundInfoService extends IService<RefundInfo> {

    RefundInfo createRefundByOrderNo(String orderNo, String reason);

    void updateRefund(String content);

    List<RefundInfo> getNoRefundOrderByDuration(int minutes);
}