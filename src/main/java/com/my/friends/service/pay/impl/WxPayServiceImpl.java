package com.my.friends.service.pay.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.google.gson.Gson;
import com.my.friends.controller.PersonController;
import com.my.friends.dao.*;
import com.my.friends.mapper.*;
import com.my.friends.pay.paymentdemo.config.WxPayConfig;
import com.my.friends.pay.paymentdemo.enums.OrderStatus;
import com.my.friends.pay.paymentdemo.enums.PayType;
import com.my.friends.pay.paymentdemo.enums.wxpay.WxApiType;
import com.my.friends.pay.paymentdemo.enums.wxpay.WxNotifyType;
import com.my.friends.pay.paymentdemo.enums.wxpay.WxRefundStatus;
import com.my.friends.pay.paymentdemo.enums.wxpay.WxTradeState;
import com.my.friends.pay.paymentdemo.util.OrderNoUtils;
import com.my.friends.pay.paymentdemo.vo.R;
import com.my.friends.service.pay.OrderInfoService;
import com.my.friends.service.pay.WxPayService;
import com.my.friends.utils.CodeMsg;
import com.my.friends.utils.JwtUtils;
import com.my.friends.utils.Result;
import com.my.friends.utils.pay.QRCodeUtil;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class WxPayServiceImpl implements WxPayService {
    /**
     * 配置Jwt
     *
     * @return
     */
    JwtUtils jwtUtils = new JwtUtils();

    @Value("${file.basepath}")
    private String baseAddress;

    private static final Log log= LogFactory.getLog(PersonController.class);


    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private WxPayConfig wxPayConfig;

    @Resource
    private CloseableHttpClient wxPayClient;

    @Resource
    private PictureMapper pictureMapper;

    @Resource
    SqlService sqlService;

    @Resource
    WxPayService wxPayService;

    @Resource
    PaymentsInfoMapper paymentsInfoMapper;

    @Resource
    RefundsInfoMapper refundsInfoMapper;

    @Resource
    AddressMapper addressMapper;

    private final ReentrantLock lock = new ReentrantLock();

    /*
     *
     * 下单 -- 上传图片
     *
     * */
    @Override
    public Result nativePayPicUploads(String orderno, MultipartFile[] files) {

        ArrayList<Picture> pictureList = new ArrayList<>();
        for(MultipartFile mf: files) {
            String id = IdUtil.simpleUUID().substring(0, 15);
            String original_name = mf.getOriginalFilename();
            //            String fileType = mf.getContentType();
            String file_name = "home_" + id + '.' + FileUtil.getSuffix(original_name);
            String path = (File.separator + file_name).replaceAll("\\\\", "/");
            String newfilePath = (baseAddress + File.separator + file_name).replaceAll("\\\\", "/");
            log.info("picture==newfilePath="+newfilePath);
            Picture picture = new Picture();
            picture.setId(id);
            picture.setOrderno(orderno);
            picture.setName(file_name);
            picture.setPath(path);
            picture.setType(FileUtil.getSuffix(original_name));
            try {
                // 创建本地文件存放 文件夹 路径实例
                File dest = new File(baseAddress);
                // 判断本地 文件夹 不存在就创建
                if (!dest.exists()) {
                    dest.mkdirs();
                }
                // 创建文件实例
                File uploadFile = FileUtil.file(newfilePath);
                // 如果文件在本地存在就删除
                if (uploadFile.exists()) {
                    uploadFile.delete();
                }
                mf.transferTo(uploadFile);
                pictureList.add(picture);
                log.info("picture==="+picture);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("upload failed. filename: " + original_name + "---->>>error message ----->>>>> " + e.getMessage());
                return Result.error(CodeMsg.OP_FAILED,"上传图片失败");
            }
        }

        if(pictureList.size()>0){
            pictureList.forEach(picture -> {
                pictureMapper.insert(picture);
            });
        }
        return Result.success();
    }
    /**
     * 创建订单，调用Native支付接口
     * @param productId
     * @return code_url 和 订单号
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result nativePay(User user,
                            String productId,
                            String count,
                            String pay,
//                            String addressid,
                            String detailInfo,String telNumber,String userName,
                            String servicetime,
                            String coupon,
                            String note,
                            MultipartFile[] files) throws Exception {

        log.info("生成订单");
        //生成地址信息
//        OrdersInfo ordersInfo = orderInfoService.createOrderByProductId(user, productId,count,pay,addressid,servicetime,coupon,note);
        String addressByAddressId = orderInfoService.createAddressByAddressId( detailInfo, telNumber, userName);
        //生成订单
        OrdersInfo ordersInfo = orderInfoService.createOrderByProductId(user, productId,count,pay,addressByAddressId,servicetime,coupon,note);
        log.info("开始上传图片===");
        if(files!=null && files.length > 0){
            log.info("有"+files.length+"个图片上传===");
            // 上传图片
            Result result = nativePayPicUploads(ordersInfo.getOrderNo(), files);
            if(result.getCode() != 0){
                return result;
            }
//        return Result.error(CodeMsg.PARAMETER_ISNULL,"无图片");
        }

        String codeUrl = ordersInfo.getCodeUrl();
        if(ordersInfo != null && !StringUtils.isEmpty(codeUrl)){
            log.info("订单已存在，二维码已保存");
            //返回二维码
            Map<String, Object> map = new HashMap<>();
            map.put("codeUrl", codeUrl);
            map.put("orderNo", ordersInfo.getOrderNo());
            return Result.success(map);
        }
        log.info("调用统一下单API");

        //调用统一下单API
        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType()));

//        // 请求body参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap();
        paramsMap.put("appid", wxPayConfig.getAppid());
        paramsMap.put("mchid", wxPayConfig.getMchId());
        paramsMap.put("description", ordersInfo.getTitle());
        paramsMap.put("out_trade_no", ordersInfo.getOrderNo());
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        log.info("notify_url==="+wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
//
        Map amountMap = new HashMap();
        amountMap.put("total", ordersInfo.getTotalFee());
        amountMap.put("currency", "CNY");

        paramsMap.put("amount", amountMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===> {}" + jsonParams);

        StringEntity entity = new StringEntity(jsonParams,"utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            }
            else {
                log.info("Native下单失败,响应码 = " + statusCode+ ",返回结果 = " + bodyAsString);
                return Result.success(bodyAsString);
//                throw new IOException("request failed");

            }
            if(statusCode != 200 && statusCode != 204){
                log.info("Native下单失败,响应码 = " + statusCode+ ",返回结果 = " + bodyAsString);
                return Result.success(bodyAsString);
            }

            //响应结果
            Map<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
            //二维码
            codeUrl = resultMap.get("code_url");
            // 链接生成二维码
            String filename = QRCodeUtil.encode(codeUrl, "", baseAddress);
            //保存二维码
            String orderNo = ordersInfo.getOrderNo();
//            orderInfoService.saveCodeUrl(orderNo, codeUrl);
            orderInfoService.saveCodeUrl(orderNo, filename);



            //返回二维码
            Map<String, Object> map = new HashMap<>();
            map.put("codeUrl", codeUrl);
            map.put("codeurl", filename);
            map.put("orderNo", ordersInfo.getOrderNo());
            return Result.success(map);

        } finally {
            response.close();
        }

    }


    @Override
    public Result getOrder(String usercode) {
        ArrayList<AllOrdersInfo> OrdersInfos = sqlService.getOrdersInfoByUsercode(usercode);
//        ArrayList<OrdersInfo> OrdersInfos = sqlService.getOrdersInfoByUsercode(usercode);
        if(OrdersInfos.size()>0){
            return Result.success(OrdersInfos);
        }else{
            return Result.error(CodeMsg.NOT_FIND_DATA,"无类别数据，请添加");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processOrder(Map<String, Object> bodyMap) throws GeneralSecurityException {
        log.info("处理订单");

        //解密报文
        String plainText = decryptFromResource(bodyMap);

        //将明文转换成map
        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);
        String orderNo = (String)plainTextMap.get("out_trade_no");


        /*在对业务数据进行状态检查和处理之前，
        要采用数据锁进行并发控制，
        以避免函数重入造成的数据混乱*/
        //尝试获取锁：
        // 成功获取则立即返回true，获取失败则立即返回false。不必一直等待锁的释放
        if(lock.tryLock()){
            try {
                //处理重复的通知
                //接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的。
//                String orderStatus = orderInfoService.getOrderStatus(orderNo);
                String orderStatus = sqlService.getOrderStatusByOrderNo(orderNo);
                if(!OrderStatus.NOTPAY.getType().equals(orderStatus)){
                    return;
                }

                //模拟通知并发
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //更新订单状态
//                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
                log.info("更新订单状态 ===> {}" + OrderStatus.SUCCESS.getType());
                sqlService.updateStatusByOrderNo( OrderStatus.SUCCESS.getType(),orderNo);

                //记录支付日志
                wxPayService.createPaymentInfo(plainText);
            } finally {
                //要主动释放锁
                lock.unlock();
            }
        }
    }

    /**
     * 记录支付日志
     * @param plainText
     */
    @Override
    public void createPaymentInfo(String plainText) {

        log.info("记录支付日志");

        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);

        //订单号
        String orderNo = (String)plainTextMap.get("out_trade_no");
        //业务编号
        String transactionId = (String)plainTextMap.get("transaction_id");
        //支付类型
        String tradeType = (String)plainTextMap.get("trade_type");
        //交易状态
        String tradeState = (String)plainTextMap.get("trade_state");
        //用户实际支付金额
        Map<String, Object> amount = (Map)plainTextMap.get("amount");
        Integer payerTotal = ((Double) amount.get("payer_total")).intValue();

        PaymentsInfo paymentInfo = new PaymentsInfo();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        paymentInfo.setId(uuid);
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentType(PayType.WXPAY.getType());
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setTradeType(tradeType);
        paymentInfo.setTradeState(tradeState);
        paymentInfo.setPayerTotal(payerTotal);
        paymentInfo.setContent(plainText);
        log.info(paymentInfo);
        paymentsInfoMapper.insert(paymentInfo);
    }


    /**
     * 对称解密
     * @param bodyMap
     * @return
     */
    private String decryptFromResource(Map<String, Object> bodyMap) throws GeneralSecurityException {

        log.info("密文解密");

        //通知数据
        Map<String, String> resourceMap = (Map) bodyMap.get("resource");
        //数据密文
        String ciphertext = resourceMap.get("ciphertext");
        //随机串
        String nonce = resourceMap.get("nonce");
        //附加数据
        String associatedData = resourceMap.get("associated_data");

        log.info("密文 ===> {}"+ ciphertext);
        AesUtil aesUtil = new AesUtil(wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        String plainText = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);

        log.info("明文 ===> {}"+ plainText);

        return plainText;
    }


    /**
     * 用户取消订单
     * @param orderNo
     */
    @Override
    public void cancelOrder(String orderNo) throws Exception {

        //调用微信支付的关单接口
        this.closeOrder(orderNo);

        //更新商户端的订单状态
        sqlService.updateStatusByOrderNo( OrderStatus.CANCEL.getType(),orderNo);
    }

    /**
     * 关单接口的调用
     * @param orderNo
     */
    private void closeOrder(String orderNo) throws Exception {

        log.info("关单接口的调用，订单号 ===> {}"+ orderNo);

        //创建远程请求对象
        String url = String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(), orderNo);
        url = wxPayConfig.getDomain().concat(url);
        HttpPost httpPost = new HttpPost(url);

        //组装json请求体
        Gson gson = new Gson();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mchid", wxPayConfig.getMchId());
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===> {}"+ jsonParams);

        //将请求参数设置到请求对象中
        StringEntity entity = new StringEntity(jsonParams,"utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功200");
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功204");
            } else {
                log.info("Native下单失败,响应码 = " + statusCode);
                throw new IOException("request failed");
            }

        } finally {
            response.close();
        }
    }

    @Override
    public String queryOrder(String orderNo) throws Exception {

        log.info("查单接口调用 ===> {}"+ orderNo);

        String url = String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderNo);
        url = wxPayConfig.getDomain().concat(url).concat("?mchid=").concat(wxPayConfig.getMchId());

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());//响应体
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            if (statusCode == 200) { //处理成功
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                log.info("查单接口调用,响应码 = " + statusCode+ ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }

            return bodyAsString;

        } finally {
            response.close();
        }

    }

    /**
     * 根据订单号查询微信支付查单接口，核实订单状态
     * 如果订单已支付，则更新商户端订单状态，并记录支付日志
     * 如果订单未支付，则调用关单接口关闭订单，并更新商户端订单状态
     * @param orderNo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkOrderStatus(String orderNo) throws Exception {

        log.warn("根据订单号核实订单状态 ===> {}"+ orderNo);

        //调用微信支付查单接口
        String result = this.queryOrder(orderNo);

        Gson gson = new Gson();
        Map<String, String> resultMap = gson.fromJson(result, HashMap.class);

        //获取微信支付端的订单状态
        String tradeState = resultMap.get("trade_state");

        //判断订单状态
        if(WxTradeState.SUCCESS.getType().equals(tradeState)){

            log.warn("核实订单已支付 ===> {}"+ orderNo);

            //如果确认订单已支付则更新本地订单状态
//            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
            sqlService.updateStatusByOrderNo(OrderStatus.SUCCESS.getType(),orderNo);
            //记录支付日志
//            paymentInfoService.createPaymentInfo(result);
            wxPayService.createPaymentInfo(result);
        }

        if(WxTradeState.NOTPAY.getType().equals(tradeState)){
            log.warn("核实订单未支付 ===> {}"+ orderNo);

            //如果订单未支付，则调用关单接口
//            this.closeOrder(orderNo);

            //更新本地订单状态
//            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
//            sqlService.updateStatusByOrderNo(OrderStatus.CLOSED.getType(),orderNo);
            sqlService.updateStatusByOrderNo(OrderStatus.NOTPAY.getType(),orderNo);
        }

        if(WxTradeState.CLOSED.getType().equals(tradeState)){
            log.warn("核实订单已关闭 ===> {}"+ orderNo);

            //如果订单未支付，则调用关单接口
            this.closeOrder(orderNo);

            //更新本地订单状态
//            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
//            sqlService.updateStatusByOrderNo(OrderStatus.CLOSED.getType(),orderNo);
            sqlService.updateStatusByOrderNo(OrderStatus.CLOSED.getType(),orderNo);
        }


    }

    /**
     * 找出申请退款超过minutes分钟并且未成功的退款单
     * @param minutes
     * @return
     */
    @Override
    public List<RefundsInfo> getNoRefundOrderByDuration(int minutes) {

        //minutes分钟之前的时间
        Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));

//        QueryWrapper<RefundsInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("refund_status", WxRefundStatus.PROCESSING.getType());
//        queryWrapper.le("create_time", instant);
//        List<RefundsInfo> refundInfoList = baseMapper.selectList(queryWrapper);

        Date date = Date.from(instant);
        RefundsInfoExample example = new RefundsInfoExample();
        example.createCriteria()
                .andRefundStatusEqualTo(WxRefundStatus.PROCESSING.getType())
                .andCreateTimeLessThanOrEqualTo(date);
        List<RefundsInfo> refundInfoList = refundsInfoMapper.selectByExample(example);
        log.info("==========RefundsInfo========"+instant);
        log.info("==========RefundsInfo========"+refundInfoList);
//        List<RefundsInfo> refundInfoList = sqlService.getRefundsInfobyOrderAndLessThanCreateTimeFiveMins(WxRefundStatus.PROCESSING.getType(),instant);
        return refundInfoList;
    }


    /**
     * 根据退款单号核实退款单状态
     * @param refundNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkRefundStatus(String refundNo) throws Exception {

        log.warn("根据退款单号核实退款单状态 ===> {}"+ refundNo);

        //调用查询退款单接口
        String result = this.queryRefund(refundNo);

        //组装json请求体字符串
        Gson gson = new Gson();
        Map<String, String> resultMap = gson.fromJson(result, HashMap.class);

        //获取微信支付端退款状态
        String status = resultMap.get("status");

        String orderNo = resultMap.get("out_trade_no");

        if (WxRefundStatus.SUCCESS.getType().equals(status)) {

            log.warn("核实订单已退款成功 ===> {}"+ refundNo);

            //如果确认退款成功，则更新订单状态
//            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);

            sqlService.updateStatusByOrderNo(OrderStatus.REFUND_SUCCESS.getType(),orderNo);
            //更新退款单
//            refundsInfoService.updateRefund(result);
            wxPayService.updateRefund(result);
        }

        if (WxRefundStatus.ABNORMAL.getType().equals(status)) {

            log.warn("核实订单退款异常  ===> {}"+ refundNo);

            //如果确认退款成功，则更新订单状态
//            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_ABNORMAL);
            sqlService.updateStatusByOrderNo(OrderStatus.REFUND_ABNORMAL.getType(),orderNo);

            //更新退款单
//            refundsInfoService.updateRefund(result);
            wxPayService.updateRefund(result);
        }
    }
    /**
     * 查询退款接口调用
     * @param refundNo
     * @return
     */
    @Override
    public String queryRefund(String refundNo) throws Exception {

        log.info("查询退款接口调用 ===> {}"+ refundNo);

        String url =  String.format(WxApiType.DOMESTIC_REFUNDS_QUERY.getType(), refundNo);
        url = wxPayConfig.getDomain().concat(url);

        //创建远程Get 请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 查询退款返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                throw new RuntimeException("查询退款异常, 响应码 = " + statusCode+ ", 查询退款返回结果 = " + bodyAsString);
            }

            return bodyAsString;

        } finally {
            response.close();
        }
    }
    /**
     * 记录退款记录
     * @param content
     */
    @Override
    public void updateRefund(String content) {

        //将json字符串转换成Map
        Gson gson = new Gson();
        Map<String, String> resultMap = gson.fromJson(content, HashMap.class);

        //根据退款单编号修改退款单
//        QueryWrapper<RefundsInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("refund_no", resultMap.get("out_refund_no"));

        //设置要修改的字段
        RefundsInfo refundsInfo = new RefundsInfo();

        refundsInfo.setRefundId(resultMap.get("refund_id"));//微信支付退款单号

        //查询退款和申请退款中的返回参数
        if(resultMap.get("status") != null){
            refundsInfo.setRefundStatus(resultMap.get("status"));//退款状态
            refundsInfo.setContentReturn(content);//将全部响应结果存入数据库的content字段
        }
        //退款回调中的回调参数
        if(resultMap.get("refund_status") != null){
            refundsInfo.setRefundStatus(resultMap.get("refund_status"));//退款状态
            refundsInfo.setContentNotify(content);//将全部响应结果存入数据库的content字段
        }

        //更新退款单
//        baseMapper.update(refundsInfo, queryWrapper);
        RefundsInfoExample example = new RefundsInfoExample();
        example.createCriteria().andRefundNoEqualTo(resultMap.get("out_refund_no"));
        refundsInfoMapper.updateByExampleSelective(refundsInfo,example);
    }

    /**
     * 退款
     * @param orderNo
     * @param reason
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refund(String orderNo, String reason) throws Exception {

        log.info("创建退款单记录");
        //根据订单编号创建退款单
        RefundsInfo refundsInfo = wxPayService.createRefundByOrderNo(orderNo, reason);

        log.info("调用退款API");

        //调用统一下单API
        String url = wxPayConfig.getDomain().concat(WxApiType.DOMESTIC_REFUNDS.getType());
        HttpPost httpPost = new HttpPost(url);

        // 请求body参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap();
        paramsMap.put("out_trade_no", orderNo);//订单编号
        paramsMap.put("out_refund_no", refundsInfo.getRefundNo());//退款单编号
        paramsMap.put("reason",reason);//退款原因
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.REFUND_NOTIFY.getType()));//退款通知地址

        Map amountMap = new HashMap();
        amountMap.put("refund", refundsInfo.getRefund());//退款金额
        amountMap.put("total", refundsInfo.getTotalFee());//原订单金额
        amountMap.put("currency", "CNY");//退款币种
        paramsMap.put("amount", amountMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数 ===> {}" + jsonParams);

        StringEntity entity = new StringEntity(jsonParams,"utf-8");
        entity.setContentType("application/json");//设置请求报文格式
        httpPost.setEntity(entity);//将请求报文放入请求对象
        httpPost.setHeader("Accept", "application/json");//设置响应报文格式

        //完成签名并执行请求，并完成验签
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {

            //解析响应结果
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 退款返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                throw new RuntimeException("退款异常, 响应码 = " + statusCode+ ", 退款返回结果 = " + bodyAsString);
            }

            //更新订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_PROCESSING);

            //更新退款单
            wxPayService.updateRefund(bodyAsString);

        } finally {
            response.close();
        }
    }

    /**
     * 处理退款单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processRefund(Map<String, Object> bodyMap) throws Exception {

        log.info("退款单");

        //解密报文
        String plainText = decryptFromResource(bodyMap);

        //将明文转换成map
        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);
        String orderNo = (String)plainTextMap.get("out_trade_no");

        if(lock.tryLock()){
            try {

                String orderStatus = sqlService.getOrderStatusByOrderNo(orderNo);
                if (!OrderStatus.REFUND_PROCESSING.getType().equals(orderStatus)) {
                    return;
                }

                //更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);

                //更新退款单
                wxPayService.updateRefund(plainText);

            } finally {
                //要主动释放锁
                lock.unlock();
            }
        }
    }


    /**
     * 根据订单号创建退款订单
     * @param orderNo
     * @return
     */
    @Override
    public RefundsInfo createRefundByOrderNo(String orderNo, String reason) {

        //根据订单号获取订单信息
        OrdersInfo orderInfo = orderInfoService.getOrderByOrderNo(orderNo);

        //根据订单号生成退款订单
        RefundsInfo refundInfo = new RefundsInfo();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        refundInfo.setId(uuid);
        refundInfo.setOrderNo(orderNo);//订单编号
        refundInfo.setRefundNo(OrderNoUtils.getRefundNo());//退款单编号
        refundInfo.setTotalFee(orderInfo.getTotalFee());//原订单金额(分)
        refundInfo.setRefund(orderInfo.getTotalFee());//退款金额(分)
        refundInfo.setReason(reason);//退款原因

        //保存退款订单
//        baseMapper.insert(refundInfo);
        refundsInfoMapper.insert(refundInfo);
        return refundInfo;
    }



}
