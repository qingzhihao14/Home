package com.my.friends.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.util.IdUtil;
import com.my.friends.dao.*;
import com.my.friends.dao.extend.LbXm;
import com.my.friends.mapper.*;
import com.my.friends.mapper.extend.LbXmMapper;
import com.my.friends.service.PersonService;
import com.my.friends.utils.CodeMsg;
import com.my.friends.utils.JwtUtils;
import com.my.friends.utils.Result;
import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.regexp.internal.RE;
import io.jsonwebtoken.Claims;
import org.apache.catalina.mbeans.UserMBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.*;
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
    @Value("${file.basepath}")
    private String baseAddress;
    /**
     * 配置jwt
     *
     * @return
     */
    JwtUtils jwtUtils = new JwtUtils();
//    @Bean
//    public JwtUtils jwtUtils() {
//        return new JwtUtils();
//    }

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
    public Result selectLbXm(String parent) {

        ArrayList<LbXm> list = lbXmMapper.selectLbXm(parent);
        if(list.size()>0){
            return Result.success(list);
        }else{
            return Result.error(CodeMsg.USER_NOT_EXSIST,"类别号为空");
        }
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
    public Boolean order(
            String usercode, String code, String orderno, Integer pay, String addressid, String address,String phone,String name,
            String servicetime, String coupon, String note, MultipartFile[] files) {
        Order order = new Order();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        order.setId(uuid);
        order.setState(0);
        order.setUsercode(usercode);
        order.setCode(code);
        order.setOrderno(orderno);
        order.setPay(pay);
        Boolean myOrder = this.insertOrUpdateAddress(addressid, usercode, address, phone, name);
        if(!myOrder){
            return false;
        }
        order.setAddressno(addressid);
        order.setServicetime(servicetime);
        order.setCoupon(coupon);
        order.setNote(note);
        int insert = orderMapper.insert(order);
        ArrayList<Picture> pictureList = new ArrayList<>();
        for(MultipartFile mf: files) {
            String id = IdUtil.simpleUUID().substring(0, 15);
            String original_name = mf.getOriginalFilename();
            //            String fileType = mf.getContentType();
            String file_name = "home_" + id + '.' + FileUtil.getSuffix(original_name);
            String path = (File.separator + file_name).replaceAll("\\\\", "/");
            String newfilePath = (baseAddress + File.separator + file_name).replaceAll("\\\\", "/");
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
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("upload failed. filename: " + original_name + "---->>>error message ----->>>>> " + e.getMessage());
                return false;
            }
        }
        if(pictureList.size()>0){
            pictureList.forEach(picture -> {
                pictureMapper.insert(picture);
            });
        }
        return true;
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

    @Override
    public ArrayList<Picture> getPictures(String orderno) {
        PictureExample example = new PictureExample();
        example.createCriteria().andOrdernoEqualTo(orderno);
        ArrayList<Picture> pictures = pictureMapper.selectByExample(example);
        return pictures;
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
    public Result adminlogin(Admin admin) {
        String code = admin.getCode();
        AdminExample example = new AdminExample();
        example.createCriteria().andCodeEqualTo(code);
        List<Admin> admins = adminMapper.selectByExample(example);
        if(admins.size()>0){
            //登录失败
            if (admins == null || !admin.getPsd().equals(admins.get(0).getPsd())) {
                //既可以使用抛异常，也可使用直接返回错误码(推荐)
                return Result.error(CodeMsg.USER_NOT_EXSIST,"用户名或密码错误");
            } else {
                //其他数据以map集合存放在token中
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("id", admins.get(0).getId());
                dataMap.put("name", admins.get(0).getName());
                //生成token并存入数据返回
                String token = jwtUtils.createJwt(admins.get(0).getId(), admins.get(0).getName(), dataMap);
                HashMap<Object, Object> map = new HashMap<>();
                map.put("token",token);
                return Result.success(map);
            }
        }else{
            return Result.error(CodeMsg.USER_NOT_EXSIST,"用户不存在");
        }
    }
    @Override
    public Result admininfo(String token){
        Claims claims = jwtUtils.parseJwt(token);
        String id = claims.getId();
        AdminExample example = new AdminExample();
        example.createCriteria().andIdEqualTo(id);
        List<Admin> admins = adminMapper.selectByExample(example);
        Admin admin = admins.get(0);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",admin.getName());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("admin");
        map.put("roles",arrayList);
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("introduction","测试I am a super administrator");
        return Result.success(map);
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
