package com.my.friends.service.impl;

import com.my.friends.dao.*;
import com.my.friends.dao.extend.LbXm;
import com.my.friends.mapper.*;
import com.my.friends.mapper.extend.LbXmMapper;
import com.my.friends.service.PersonService;
import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.catalina.mbeans.UserMBean;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Resource
    private PersonMapper personMapper;
    @Resource
    private LbMapper lbMapper;
    @Resource
    private LbItemMapper lbItemMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private PictureMapper pictureMapper;
    @Resource
    private QyMapper qyMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private LbXmMapper lbXmMapper;
    @Resource
    private AdminMapper adminMapper;

    @Override
    public Person getMyInfo(String WeChat) {
        PersonExample personExample = new PersonExample();
        if(StringUtils.isNullOrEmpty(WeChat)){
            return new Person();
        }
        personExample.createCriteria().andWechatEqualTo(WeChat);
        ArrayList<Person> people = personMapper.selectByExample(personExample);
        Person person = people.get(0);
        System.out.println(person);
        return person;
    }

    @Override
    public Person getOnePerson(String WeChat) {

        PersonExample personExample = new PersonExample();
        // 随机获取除本人外 其他人的一个微信号
        personExample.createCriteria().andWechatNotEqualTo(WeChat).andSexEqualTo(0).andUuidIsNotNull();
        ArrayList<Person> people = personMapper.selectByExample(personExample);
//        System.out.println("符合条件的人："+people);
        Random random = new Random();
        int n = random.nextInt(people.size());
        Person person = people.get(n);
        System.out.println("随机获取一个人："+person);
        return person;
    }
    //============================================================================
    /*
    * 0.查询类别、项目
    * */
    @Override
    public ArrayList selectLbXm(String parent) {
        return lbXmMapper.selectLbXm(parent);
    }
    /*
     * 1.类别
     * */

    // 1.1查询
    @Override
    public ArrayList<Lb> getLb() {
        LbExample example = new LbExample();
        example.createCriteria().andIdIsNotNull();
        ArrayList<Lb> lbs = lbMapper.selectByExample(example);
        return lbs;
    }
    //1.2新增
    @Override
    public Boolean insertOrUpdateLb(Lb lb) {

        LbExample example = new LbExample();
        int count = 0;
        if(!"".equals(lb.getId()) && lb.getId()!= null){
            // 编辑
            example.createCriteria().andIdEqualTo(lb.getId());
            count = lbMapper.updateByExample(lb, example);
        }else{
            // 新增
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            lb.setId(uuid);
            example.createCriteria().andCodeIsNotNull();
            ArrayList<Lb> lbs = lbMapper.selectByExample(example);
            if(lbs.size()==0){
                lb.setCode("LB1");
            }else{
                Integer max = lbs.stream()
                        .map(Lb::getCode)
                        .map(s -> {
                            return Integer.parseInt(s.substring(2));
                        }).max(Integer::compare).get();
                lb.setCode("LB"+(max+1));
            }
            count = lbMapper.insert(lb);
        }
        return count>0?true:false;
    }




    /*
     * 2.项目
     * */

    //1.1新增
    @Override
    public Boolean insertOrUpdateItem(LbItem lbItem) {

        LbItemExample example = new LbItemExample();
        int count = 0;
        if(!"".equals(lbItem.getId()) && lbItem.getId()!= null){
            // 编辑
            example.createCriteria().andIdEqualTo(lbItem.getId());
            count = lbItemMapper.updateByExample(lbItem, example);
        }else{
            // 新增
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            lbItem.setId(uuid);
            example.createCriteria().andCodeIsNotNull();
            ArrayList<LbItem> lbItems = lbItemMapper.selectByExample(example);
            if(lbItems.size()==0){
                lbItem.setCode("XM1");
            }else{
                Integer max = lbItems.stream()
                        .map(LbItem::getCode)
                        .map(s -> {
                            return Integer.parseInt(s.substring(2));
                        }).max(Integer::compare).get();
                lbItem.setCode("XM"+(max+1));
            }
            count = lbItemMapper.insert(lbItem);
        }
        return count>0?true:false;
    }

    /*
     *
     * 下单
     *
     * usercode 用户代码
     * code 项目代码
     * orderno 订单编码(支付返回)
     * pay 支付金额(支付返回)
     * addressno 服务地址编码()
     * servicetime 服务时间
     * coupon 优惠券
     * note 备注
     * */
    // 更新state状态订单状态(0-未完成，1-已完成，2-已取消)
    @Override
    public Boolean order(String usercode, String code, String orderno, Integer pay, String addressid, String address,String phone,String name, String servicetime, String coupon, String note) {
        Order order = new Order();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        order.setId(uuid);
        order.setState(0);
        order.setUsercode(usercode);
        order.setCode(code);
        order.setOrderno(orderno);
        order.setPay(pay);
        this.insertOrUpdateAddress(addressid,usercode,address,phone,name);
//        order.setAddressno(addressno);
        order.setServicetime(servicetime);
        order.setCoupon(coupon);
        order.setNote(note);
        int insert = orderMapper.insert(order);
        return insert>0 ? true:false;
    }

    /*
     * 4.用户信息
     * */
    // 1.1 新增或更新项目
    @Override
    public String login(User user) {
        String wechat = user.getWechat();
        UserExample example = new UserExample();
        example.createCriteria().andWechatEqualTo(wechat);
        List<User> users = userMapper.selectByExample(example);
        if(users.size()>0){
            return users.get(0).getId();
        }
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        user.setId(uuid);
        user.setCode(user.getWechat());
        int insert = userMapper.insert(user);
        Boolean flag= insert>0 ? true : false;
        return flag? user.getId():"登录失败";
    }

    @Override
    public List<Order> getOrder(String usercode) {
        OrderExample example = new OrderExample();
        example.createCriteria().andUsercodeEqualTo(usercode);
        List<Order> orders = orderMapper.selectByExample(example);
        return orders;
    }

    //查询d订单地址
    @Override
    public Address getAddressById(String id) {
        AddressExample example = new AddressExample();
        example.createCriteria().andIdEqualTo(id);
        List<Address> addresses = addressMapper.selectByExample(example);
        if(addresses.size()>0){
            return addresses.get(0);
        }else{
            return new Address();
        }
    }
    @Override
    public List<Address> getAddress(String usercode) {
        AddressExample example = new AddressExample();
        example.createCriteria().andCodeEqualTo(usercode);
        List<Address> addresses = addressMapper.selectByExample(example);
        return addresses;
    }

    @Override
    public Boolean insertOrUpdateAddress(String addressid,String usercode,String addressz,String phone,String name) {
        Address address = new Address();
        int count = 0;
        if(!StringUtils.isNullOrEmpty(addressid)){
            address.setId(addressid);
            address.setAddress(addressz);
            address.setCode(usercode);
            address.setName(name);
            address.setPhone(phone);
            // 编辑
            count = addressMapper.updateByPrimaryKey(address);
        }else{
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            address.setId(uuid);
            address.setCode(usercode);
            address.setAddress(addressz);
            address.setPhone(phone);
            address.setName(name);
            count = addressMapper.insert(address);
        }
        return count>0 ? true : false;
    }


    /*
     * 6.管理员登录
     * */
    @Override
    public String adminlogin(Admin admin) {
        String code = admin.getCode();
        AdminExample example = new AdminExample();
        example.createCriteria().andCodeEqualTo(code);
        List<Admin> admins = adminMapper.selectByExample(example);
        if(admins.size()>0){
            return admins.get(0).getId();
        }else{
            return "登录失败";
        }
    }
    /*
     * 6.管理员创建和修改密码
     * */
    @Override
    public Boolean insertOrUpdateAdmin(String id,String code,String psd,String name,Integer sex) {
        Admin admin = new Admin();
        int count = 0;
        if(!StringUtils.isNullOrEmpty(id)){
            admin.setId(id);
            admin.setCode(code);
            admin.setPsd(psd);
            admin.setName(name);
            admin.setSex(sex);
            // 编辑
            count = adminMapper.updateByPrimaryKey(admin);
        }else{
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            admin.setId(uuid);
            admin.setCode(code);
            admin.setPsd(psd);
            admin.setName(name);
            admin.setSex(sex);
            count = adminMapper.insert(admin);
        }
        return count>0 ? true : false;
    }
}
