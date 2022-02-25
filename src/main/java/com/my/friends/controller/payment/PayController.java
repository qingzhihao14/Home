package com.my.friends.controller.payment;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.my.friends.dao.*;
import com.my.friends.mapper.SqlService;
import com.my.friends.pay.paymentdemo.enums.OrderStatus;
import com.my.friends.pay.paymentdemo.util.HttpUtils;
import com.my.friends.pay.paymentdemo.util.WechatPay2ValidatorForRequest;
import com.my.friends.pay.paymentdemo.vo.R;
import com.my.friends.service.PersonService;
import com.my.friends.service.pay.WxPayService;
import com.my.friends.utils.CodeMsg;
import com.my.friends.utils.Result;
import com.mysql.jdbc.StringUtils;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import io.swagger.annotations.*;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@CrossOrigin //跨域
@RestController
@RequestMapping("/api/wx-pay")
@Api(description = "微信支付", hidden=true)
public class PayController {


    private static final
    org.apache.commons.logging.Log log = LogFactory.getLog(PayController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private WxPayService payService;

    @Autowired
    private PersonService personService;

    @Resource
    SqlService sqlService;

    @Resource
    WxPayService wxPayService;

    @Resource
    private Verifier verifier;


    /*
     * 获取微信用户openid
     * */
    public Result getLoginUser(HttpServletRequest request){

        String token = request.getHeader("token");
        log.info("saveLoginInfo获取header_token="+token);
        if(!StringUtils.isNullOrEmpty(token)){
            String cookiez = redisTemplate.opsForValue().get(token);
            log.info("saveLoginInfo_通过redis获取到的session值="+cookiez);
            if(!StringUtils.isNullOrEmpty(cookiez)){
                String[] strings = cookiez.split("，");
                if(strings.length<=0){
                    return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
                }
                User user = sqlService.getUser(strings[1]);
                return Result.success(user);
            }else{
                return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
            }
        }else{
            return Result.error(CodeMsg.SESSION_NOT_EXSIST,"Token无效");
        }
    }

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
    @ApiOperation(value = "下单【个人】")
    @RequestMapping(value = "/payone", method = {RequestMethod.POST, RequestMethod.GET})
    public Result order( @RequestParam Map<String,String> remap,
                         @ApiParam(value = "图片上传",required = false,defaultValue = "")  @RequestParam(value = "file",required = false) MultipartFile[] files,
                         HttpServletRequest request) throws IOException {

        // 处理参数
        String code =  remap.get("code"); //商品code
        if(StringUtils.isNullOrEmpty(code)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"商品code为空");
        }
        String count = remap.get("count");
        if(StringUtils.isNullOrEmpty(count)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"商品count为空");
        }
        String pay = remap.get("pay");
        if(StringUtils.isNullOrEmpty(pay)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"商品pay为空");
        }
        String addressid = remap.get("addressid");
        if(StringUtils.isNullOrEmpty(addressid)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"商品addressid为空");
        }
        String servicetime = remap.get("servicetime");
        if(StringUtils.isNullOrEmpty(servicetime)){
            return Result.error(CodeMsg.PARAMETER_ISNULL,"商品servicetime为空");
        }
        // 优惠券 待开发
        String coupon = remap.get("coupon");
        String note = remap.get("note");

        Result result = getLoginUser(request);
        if(result.getCode() != 0){
            return result;
        }
        User user = (User) result.getData();
        //测试
//        User user = new User();
//        user.setId("f1b90f0a726a44c3a0483c6e975a49b3");
//        user.setCode("2eBH3z8B9XuKCzONuliFSQ==");
//        user.setName("Mercedes⁰");
//        user.setNote("https://thirdwx.qlogo.cn/mmopen/vi_32/EyiavrAbAbbv4MQCONoI5pgiaLiaEuqRFianyYbbAic0Idiaibd9EJjlSzMVtFRj1reaMI6VB0OVT9KZVSicaGtjXdPZow/132");

        // 记录日志
        String param = JSONUtil.toJsonStr(remap);
        personService.insertLog(new Logss( user.getCode(),  user.getName(),  "下单【个人】",  "/order",  param));

        return payService.nativePay(user, code,count,pay,addressid,servicetime,coupon,note,files);
    }



    // 1.2 查询订单
    @ApiOperation(value = "查询订单（根据用户code）【个人】")
    @GetMapping("/getOrder")
    public Result getOrder(
            HttpServletRequest request){
        String way = request.getParameter("way");
        String code = "";
        User user = new User();
        if(!"all".equals(way)){
            Result result = getLoginUser(request);
            if(result.getCode() != 0){
                return result;
            }
            user = (User) result.getData();
            code = user.getCode();
        }
        HashMap<Object, Object> remap = new HashMap<>();
        remap.put("userCode",code);
        String param = JSONUtil.toJsonStr(remap);
        personService.insertLog(new Logss( user.getCode(),  user.getName(),  "查询订单【个人】",  "/getOrder",  param));

//        code = "A9UJ96+nXmI35xuI9N52AA==";
        return payService.getOrder(code);
    }

    /**
     * 支付通知
     * 微信支付通过支付通知接口将用户支付成功消息通知给商户
     */
    @ApiOperation("支付通知")
    @PostMapping("/native/notify")
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response){

        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();//应答对象

        try {

            //处理通知参数
            String body = HttpUtils.readData(request);
            Map<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
            String requestId = (String)bodyMap.get("id");
            log.info("支付通知的id ===> {}"+ requestId);
            //log.info("支付通知的完整数据 ===> {}", body);
            //int a = 9 / 0;

            //签名的验证
            WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest
                    = new WechatPay2ValidatorForRequest(verifier, requestId, body);
            if(!wechatPay2ValidatorForRequest.validate(request)){

                log.error("通知验签失败");
                //失败应答
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "通知验签失败");
                return gson.toJson(map);
            }
            log.info("通知验签成功");

            //处理订单
            wxPayService.processOrder(bodyMap);

            //应答超时
            //模拟接收微信端的重复通知
            TimeUnit.SECONDS.sleep(5);

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "成功");
            return gson.toJson(map);

        } catch (Exception e) {
            e.printStackTrace();
            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return gson.toJson(map);
        }

    }

    /**
     * 查询本地订单状态
     * @param orderNo
     * @return
     */
    @ApiOperation("查询订单状态（根据订单号）【个人】")
    @GetMapping("/query-order-status/{orderNo}")
    public Result queryOrderStatus(@PathVariable String orderNo){

        String orderStatus = sqlService.getOrderStatusByOrderNo(orderNo);
        if(OrderStatus.SUCCESS.getType().equals(orderStatus)){
            return Result.success("支付成功"); //支付成功
        }

        return Result.error(CodeMsg.PAY_FAILED,orderStatus);
    }

    /**
     * 用户取消订单
     * @param orderNo
     * @return
     * @throws Exception
     */
    @ApiOperation("用户取消订单")
    @PostMapping("/cancel/{orderNo}")
    public R cancel(@PathVariable String orderNo) throws Exception {

        log.info("取消订单");

        wxPayService.cancelOrder(orderNo);
        return R.ok().setMessage("订单已取消");
    }


    /**
     * 查询订单
     * @param orderNo
     * @return
     * @throws Exception
     */
    @ApiOperation("查询订单：测试订单状态用")
    @GetMapping("/query/{orderNo}")
    public R queryOrder(@PathVariable String orderNo) throws Exception {

        log.info("查询订单");

        String result = wxPayService.queryOrder(orderNo);
        return R.ok().setMessage("查询成功").data("result", result);

    }

    @ApiOperation("申请退款")
    @PostMapping("/refunds/{orderNo}/{reason}")
    public R refunds(@PathVariable String orderNo, @PathVariable String reason) throws Exception {
        log.info("申请退款");
        wxPayService.refund(orderNo, reason);
        return R.ok();
    }

    /**
     * 查询退款
     * @param refundNo
     * @return
     * @throws Exception
     */
    @ApiOperation("查询退款：测试用")
    @GetMapping("/query-refund/{refundNo}")
    public R queryRefund(@PathVariable String refundNo) throws Exception {

        log.info("查询退款");

        String result = wxPayService.queryRefund(refundNo);
        return R.ok().setMessage("查询成功").data("result", result);
    }


    /**
     * 退款结果通知
     * 退款状态改变后，微信会把相关退款结果发送给商户。
     */
    @ApiOperation("退款结果通知")
    @PostMapping("/refunds/notify")
    public String refundsNotify(HttpServletRequest request, HttpServletResponse response){

        log.info("退款通知执行");
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();//应答对象

        try {
            //处理通知参数
            String body = HttpUtils.readData(request);
            Map<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
            String requestId = (String)bodyMap.get("id");
            log.info("支付通知的id ===> {}"+ requestId);

            //签名的验证
            WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest
                    = new WechatPay2ValidatorForRequest(verifier, requestId, body);
            if(!wechatPay2ValidatorForRequest.validate(request)){

                log.error("通知验签失败");
                //失败应答
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "通知验签失败");
                return gson.toJson(map);
            }
            log.info("通知验签成功");

            //处理退款单
            wxPayService.processRefund(bodyMap);

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "成功");
            return gson.toJson(map);

        } catch (Exception e) {
            e.printStackTrace();
            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return gson.toJson(map);
        }
    }
}