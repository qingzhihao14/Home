package com.my.friends.service.pay;

import com.my.friends.dao.*;
import com.my.friends.pay.paymentdemo.vo.R;
import com.my.friends.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;


public interface WxPayService {


    /*
    *
    * 1.下单
    *
    * usercode 用户代码
    * code 项目代码
    * orderno 订单编码
    * pay 支付金额
    * addressno 服务地址编码
    * servicetime 服务时间
    * coupon 优惠券
    * note 备注
    * */
    // 更新state状态订单状态(0-未完成，1-已完成，2-已取消)
    Result nativePay(User user, String code,String count,String pay,String detailInfo,String telNumber,String userName,String servicetime,String coupon,String note, MultipartFile[] files,String picids) throws Exception;
    // 下单-图片上传
//    Result nativePayPicUploads(String orderno, MultipartFile[] files);
    Result payFileUpload(HttpServletRequest request) throws Exception;

    //查询个人订单信息
    Result getOrder(String usercode,String orderStatus);

    /**
     * 支付通知
     * 微信支付通过支付通知接口将用户支付成功消息通知给商户
     */
    //处理订单
    void processOrder(Map<String, Object> bodyMap) throws GeneralSecurityException;


    /**
     * 记录支付日志
     * @param plainText
     */
    void createPaymentInfo(String plainText);

    /**
     * 用户取消订单
     * @param orderNo
     * @return
     * @throws Exception
     */
    void cancelOrder(String orderNo) throws Exception;


    String queryOrder(String orderNo) throws Exception;

    void checkOrderStatus(String orderNo) throws Exception;

    List<RefundsInfo> getNoRefundOrderByDuration(int minutes);

    void checkRefundStatus(String refundNo) throws Exception;

    String queryRefund(String orderNo) throws Exception;

    void updateRefund(String content);

    void refund(String orderNo, String reason) throws Exception;

    RefundsInfo createRefundByOrderNo(String orderNo, String reason);

    void processRefund(Map<String, Object> bodyMap) throws Exception;

//    String queryBill(String billDate, String type) throws Exception;

//    String downloadBill(String billDate, String type) throws Exception;
//
//    Map<String, Object> nativePayV2(Long productId, String remoteAddr) throws Exception;


}
