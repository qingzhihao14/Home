package com.my.friends.service;

import com.my.friends.dao.*;
import com.my.friends.utils.Result;
import com.my.friends.utils.pageHelper.PageRequest;
import com.my.friends.utils.pageHelper.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


public interface PersonService {


    // 操作日志
    void insertLog(Logss log);

    /*
    * 1.类别
    * */

    //查询
    Result getLb();
    //删除
    Result delLb(String code);
    Result insertOrUpdateLb(Lb lb);
    Result insertOrUpdateLbs(Lb lb,String create,MultipartFile[] files);
    Result insertOrUpdateLbPic(String id, MultipartFile[] files);
    //查询
    Result getXm(String code);
    //删除
    Result delXm(String code);
    Result changeIsChoice(String ischoice,String id);
    //查询
    Result selectLbXm(String parent);


    /*
     * 2.项目
     * */
    Result insertOrUpdateItem(LbItem lbItem,MultipartFile[] files,String create);
    Result insertOrUpdateLbItemPic(String id, MultipartFile[] files);
    /*
    *
    * 3.下单
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
    Result order(String usercode, String code, String orderno, Integer pay, String addressid, String address, String phone, String name, String servicetime, String coupon, String note, MultipartFile[] files);


    /*
     * 4.用户信息
     * */
    // 新增或更新项目
    Result login(User user,String userName,String detailInfo,String telNumber);

    //查询个人订单信息
    Result getPictures(String orderno);
    //查询
    Result getLoginInfo(String code,String flag);

    /*
     * 5.地址查询
     * */

    //查询d订单地址
    Address getAddressById(String id);
    //查询个人地址集合
    Result getAddress(String usercode);

    Result insertOrUpdateAddress(String addressid,String usercode,String addressz,String phone,String name);


    /*
     * 6.管理员登录
     * */
    Result adminlogin(Admin admin);
    Result admininfo(String token);

     // 管理员创建和修改密码
    Result insertOrUpdateAdmin(String id,String code,String psd,String name,Integer sex);

    /**
     * 分页查询接口
     * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
     * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
     * 影响服务层以上的分页接口，起到了解耦的作用
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult findPage(PageRequest pageRequest);
}
