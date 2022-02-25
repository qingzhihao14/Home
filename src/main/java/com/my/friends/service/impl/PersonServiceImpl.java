package com.my.friends.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.my.friends.controller.PersonController;
import com.my.friends.dao.*;
import com.my.friends.dao.extend.LbXm;
import com.my.friends.mapper.*;
import com.my.friends.mapper.extend.LbXmMapper;
import com.my.friends.service.PersonService;
import com.my.friends.utils.CodeMsg;
import com.my.friends.utils.JwtUtils;
import com.my.friends.utils.Result;
import com.my.friends.utils.pageHelper.PageRequest;
import com.my.friends.utils.pageHelper.PageResult;
import com.my.friends.utils.pageHelper.PageUtils;
import com.mysql.jdbc.StringUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PersonServiceImpl implements PersonService {

    @Resource
    private LbMapper lbMapper;
    @Resource
    private LbItemMapper lbItemMapper;
    @Resource
    private AddressMapper addressMapper;
//    @Resource
//    private OrderMapper orderMapper;
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
    @Resource
    private LogssMapper LogssMapper;
    @Value("${file.basepath}")
    private String baseAddress;
    @Resource
    private OrdersInfoMapper ordersInfoMapper;
    @Resource
    private SqlService sqlService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Log log= LogFactory.getLog(PersonController.class);
    @Resource
    LbsExample lbExamples;
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
    public void insertLog(Logss logss) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        logss.setId(uuid);
        logss.setCreateTime(new Date());
        LogssMapper.insert(logss);
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
            return Result.error(CodeMsg.NOT_FIND_DATA,"无效的类别号");
        }
    }
    /*
     * 1.类别
     * */

    // 1.1查询
    @Override
    public Result getLb() {
        LbExample example = new LbExample();
        example.createCriteria().andIdIsNotNull();
        ArrayList<Lb> list = lbMapper.selectByExample(example);
//        ArrayList<Lbs> objects = lbExamples.selectTeacher("");
//        List<Lbs> collect = objects.stream().map(param -> {
//            param.setId("1");
//            return param;
//        }).collect(Collectors.toList());
        if(list.size()>0){
            return Result.success(list);
        }else{
            return Result.error(CodeMsg.NOT_FIND_DATA,"无类别数据，请添加");
        }
    }

    // 1.1删除
    @Override
    public Result delLb(String code) {
        LbItemExample lbItemExample = new LbItemExample();
        lbItemExample.createCriteria().andParentEqualTo(code);
        ArrayList<LbItem> lbItems = lbItemMapper.selectByExample(lbItemExample);
        if(lbItems.size()>0){
            return Result.error(CodeMsg.OP_FAILED,"请先删除项目");
        }
        LbExample example = new LbExample();
        example.createCriteria().andCodeEqualTo(code);
        int count = lbMapper.deleteByExample(example);
        if(count>0){
            return Result.success("删除成功");
        }else{
            return Result.error(CodeMsg.NOT_FIND_DATA,"删除失败");
        }
    }
    // 1.1查询
    @Override
    public Result getXm(String code) {
        LbItemExample example = new LbItemExample();
        if(StringUtils.isNullOrEmpty(code)){
            example.setOrderByClause(" sold desc ");
            example.createCriteria().andIdIsNotNull();
            ArrayList<LbItem> list = lbItemMapper.selectByExample(example);
            if(list.size()>0){
                return Result.success(list);
            }else{
                return Result.error(CodeMsg.NOT_FIND_DATA,"无项目数据，请添加");
            }
        }else{
            example.createCriteria().andCodeEqualTo(code);
            ArrayList<LbItem> list = lbItemMapper.selectByExample(example);
            if(list.size()>0){
                return Result.success(list.get(0));
            }else{
                return Result.error(CodeMsg.NOT_FIND_DATA,"无项目数据，请添加");
            }
        }
    }
    //1.2新增
    @Override
    public Result insertOrUpdateLb(Lb lb) {
        LbExample example = new LbExample();
        int count = 0;
        if(!"".equals(lb.getId()) && lb.getId()!= null){
            // 编辑
            example.createCriteria().andIdEqualTo(lb.getId());
            count = lbMapper.updateByExample(lb, example);
            if(count>0){
                return Result.success();
            }else{
                return Result.error(CodeMsg.OP_FAILED,"更新失败");
            }
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
            if(count>0){
                return Result.success();
            }else{
                return Result.error(CodeMsg.OP_FAILED,"新增失败");
            }
        }
    }

    //1.2新增
    @Override
    public Result insertOrUpdateLbs(Lb lb,String create,MultipartFile[] files) {
        LbExample example = new LbExample();
        int count = 0;
        if(!"1".equals(create) && !"".equals(lb.getId()) && lb.getId()!= null){
            // 编辑
//            example.createCriteria().andIdEqualTo(lb.getId());
            count = lbMapper.updateByPrimaryKeySelective(lb);
            if(count>0){
                return Result.success();
            }else{
                return Result.error(CodeMsg.OP_FAILED,"更新失败");
            }
        }else{
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
            // 无图片新增
            if("1".equals(create) && StringUtils.isNullOrEmpty(lb.getId())){
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                lb.setId(uuid);
                count = lbMapper.insert(lb);
                if(count>0){
                    return Result.success();
                }else{
                    return Result.error(CodeMsg.OP_FAILED,"新增失败");
                }
            }else {
                // // 有图片新增
                //            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                lb.setId(lb.getId());
                count = lbMapper.updateByPrimaryKeySelective(lb);
                if (count > 0) {
                    //                Result result = insertOrUpdateLbItemPic(uuid, files);
                    //                return result;
                    return Result.success("新增成功");
                } else {
                    // 若信息上传失败 则把上传的图片信息删掉
                    lbItemMapper.deleteByPrimaryKey(lb.getId());
                    return Result.error(CodeMsg.OP_FAILED, "新增失败");
                }
            }
        }
    }

    @Override
    public Result insertOrUpdateLbPic (String id, MultipartFile[] files){
        String original_name = files[0].getOriginalFilename();
        String file_name = "home_" + IdUtil.simpleUUID().substring(0, 15) + '.' + FileUtil.getSuffix(original_name);
        String path = (File.separator + "fl"+File.separator + file_name).replaceAll("\\\\", "/");
        String newfilePath = (baseAddress + path).replaceAll("\\\\", "/");
        Lb lb = new Lb();
        lb.setPicName(file_name);
        lb.setPicPath(path);
        lb.setPicType(FileUtil.getSuffix(original_name));
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
            files[0].transferTo(uploadFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("upload failed. filename: " + original_name + "---->>>error message ----->>>>> " + e.getMessage());
            return Result.error(CodeMsg.OP_FAILED,"上传图片失败");
        }
        int count = 0;
        if("create".equals(id)){
            id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            lb.setId(id);
            count = lbMapper.insert(lb);
        }else{
            lb.setId(id);
            count = lbMapper.updateByPrimaryKeySelective(lb);
        }
        if(count>0){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",id);
            map.put("url",path);
            return Result.success(map);
        }else{
            return Result.error(CodeMsg.OP_FAILED,"上传图片失败");
        }
    }

    // 1.1删除
    @Override
    public Result delXm(String code) {
        LbItemExample lbItemExample = new LbItemExample();
        lbItemExample.createCriteria().andCodeEqualTo(code);
        int count = lbItemMapper.deleteByExample(lbItemExample);
        if(count>0){
            return Result.success("删除成功");
        }else{
            return Result.error(CodeMsg.NOT_FIND_DATA,"删除失败");
        }
    }
    @Override
    public Result changeIsChoice(String ischoice,String id){
        LbItem lbItem = new LbItem();
        lbItem.setId(id);
        lbItem.setIschoice(Integer.parseInt(ischoice));
        int count = lbItemMapper.updateByPrimaryKeySelective(lbItem);
        if(count>0){
            return Result.success("变更成功");
        }else{
            return Result.error(CodeMsg.NOT_FIND_DATA,"变更失败");
        }
    }

    /*
     * 2.项目
     * */

    //1.1新增
    @Override
    public Result insertOrUpdateItem(LbItem lbItem,MultipartFile[] files,String create) {

        LbItemExample example = new LbItemExample();
        int count = 0;

        if(!"1".equals(create) && !"".equals(lbItem.getId()) && lbItem.getId()!= null){
            // 编辑
//            example.createCriteria().andIdEqualTo(lbItem.getId());
//            count = lbItemMapper.updateByExample(lbItem, example);
            count = lbItemMapper.updateByPrimaryKeySelective(lbItem);
            if(count>0){
                return Result.success();
            }else{
                return Result.error(CodeMsg.OP_FAILED,"更新失败");
            }
        }else{
            example.createCriteria().andCodeIsNotNull();
            ArrayList<LbItem> lbItems = lbItemMapper.selectByExample(example);
            if (lbItems.size() == 0) {
                lbItem.setCode("XM1");
            } else {
                Integer max = lbItems.stream()
                        .map(LbItem::getCode)
                        .map(s -> {
                            return Integer.parseInt(s.substring(2));
                        }).max(Integer::compare).get();
                lbItem.setCode("XM" + (max + 1));
            }
            // 无图片新增
            if("1".equals(create) && StringUtils.isNullOrEmpty(lbItem.getId())){
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                lbItem.setId(uuid);
                count = lbItemMapper.insert(lbItem);
                if(count>0){
                    return Result.success();
                }else{
                    return Result.error(CodeMsg.OP_FAILED,"更新失败");
                }
            }else {

                // // 有图片新增
                //            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                lbItem.setId(lbItem.getId());
                count = lbItemMapper.updateByPrimaryKeySelective(lbItem);
                if (count > 0) {
                    //                Result result = insertOrUpdateLbItemPic(uuid, files);
                    //                return result;
                    return Result.success("新增成功");
                } else {
                    // 若信息上传失败 则把上传的图片信息删掉
                    lbItemMapper.deleteByPrimaryKey(lbItem.getId());
                    return Result.error(CodeMsg.OP_FAILED, "新增失败");
                }
            }
        }
    }
    @Override
    public Result insertOrUpdateLbItemPic (String id, MultipartFile[] files){
        String original_name = files[0].getOriginalFilename();
        String file_name = "home_" + IdUtil.simpleUUID().substring(0, 15) + '.' + FileUtil.getSuffix(original_name);
        String path = (File.separator + file_name).replaceAll("\\\\", "/");
        String newfilePath = (baseAddress + File.separator + file_name).replaceAll("\\\\", "/");
        LbItem lbItem = new LbItem();
        lbItem.setPicName(file_name);
        lbItem.setPicPath(path);
        lbItem.setPicType(FileUtil.getSuffix(original_name));
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
            files[0].transferTo(uploadFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("upload failed. filename: " + original_name + "---->>>error message ----->>>>> " + e.getMessage());
            return Result.error(CodeMsg.OP_FAILED,"上传图片失败");
        }
        int count = 0;
        if("create".equals(id)){
            id = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            lbItem.setId(id);
            count = lbItemMapper.insert(lbItem);
        }else{
            lbItem.setId(id);
            count = lbItemMapper.updateByPrimaryKeySelective(lbItem);
        }
        if(count>0){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",id);
            map.put("url",file_name);
            return Result.success(map);
        }else{
            return Result.error(CodeMsg.OP_FAILED,"上传图片失败");
        }
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
    public Result order(
            String usercode, String code, String orderno, Integer pay, String addressid, String address,String phone,String name,
            String servicetime, String coupon, String note, MultipartFile[] files) {
        return Result.success();
//        Order order = new Order();
//        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
//        order.setId(uuid);
//        order.setState(0);
//        order.setUsercode(usercode);
//        order.setCode(code);
//        order.setOrderno(orderno);
//        order.setPay(pay);
//        Result myOrderResult = this.insertOrUpdateAddress(addressid, usercode, address, phone, name);
//        if(myOrderResult.getCode()!=0){
//            return myOrderResult;
//        }
//        order.setAddressno(addressid);
//        order.setServicetime(servicetime);
//        order.setCoupon(coupon);
//        order.setNote(note);
//        int count = orderMapper.insert(order);
//        if(count>0){
//            ArrayList<Picture> pictureList = new ArrayList<>();
//            for(MultipartFile mf: files) {
//                String id = IdUtil.simpleUUID().substring(0, 15);
//                String original_name = mf.getOriginalFilename();
//                //            String fileType = mf.getContentType();
//                String file_name = "home_" + id + '.' + FileUtil.getSuffix(original_name);
//                String path = (File.separator + file_name).replaceAll("\\\\", "/");
//                String newfilePath = (baseAddress + File.separator + file_name).replaceAll("\\\\", "/");
//                Picture picture = new Picture();
//                picture.setId(id);
//                picture.setOrderno(orderno);
//                picture.setName(file_name);
//                picture.setPath(path);
//                picture.setType(FileUtil.getSuffix(original_name));
//                try {
//                    // 创建本地文件存放 文件夹 路径实例
//                    File dest = new File(baseAddress);
//                    // 判断本地 文件夹 不存在就创建
//                    if (!dest.exists()) {
//                        dest.mkdirs();
//                    }
//                    // 创建文件实例
//                    File uploadFile = FileUtil.file(newfilePath);
//                    // 如果文件在本地存在就删除
//                    if (uploadFile.exists()) {
//                        uploadFile.delete();
//                    }
//                    mf.transferTo(uploadFile);
//                    pictureList.add(picture);
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    System.out.println("upload failed. filename: " + original_name + "---->>>error message ----->>>>> " + e.getMessage());
//                    return Result.error(CodeMsg.OP_FAILED,"上传图片失败");
//                }
//            }
//            if(pictureList.size()>0){
//                pictureList.forEach(picture -> {
//                    pictureMapper.insert(picture);
//                });
//            }
//            return Result.success();
//        }else{
//            return Result.error(CodeMsg.OP_FAILED,"新增失败");
//        }
    }

    /*
     * 4.保存用户信息
     * */
    // 1.1 新增或更新项目
    @Override
    public Result login(User user,String userName,String detailInfo,String telNumber) {
        log.info("微信登录"+user);
        // 修改地址
        if(StringUtils.isNullOrEmpty(user.getName())){
            // 根据usercode查询是否存在 不存在新增 存在按usercode更新地址信息
            AddressExample exampleAdd = new AddressExample();
            exampleAdd.createCriteria().andCodeEqualTo(user.getCode());
            List<Address> addresses = addressMapper.selectByExample(exampleAdd);
            if(addresses.size()>0){
                Address addressz = addresses.get(0);
                String id = addressz.getId();
                // 编辑
                Result result = insertOrUpdateAddress(id, user.getCode(), detailInfo, telNumber, userName);
                return result;
            }else{
                Result result = insertOrUpdateAddress("", user.getCode(), detailInfo, telNumber, userName);
                return result;
            }
        }else{
            // 修改基本信息
            String code = user.getCode();
            UserExample example = new UserExample();
            example.createCriteria().andCodeEqualTo(code);
            ArrayList<User> users = userMapper.selectByExample(example);
            log.info("再次登录，更新微信用户"+users);
            int count = 0;
            if (users.size() > 0) {
                // 再次登录的用户
                User userz = users.get(0);
                userz.setCode(user.getCode());
                userz.setSex(user.getSex());
                userz.setNote(user.getNote());
                userz.setName(user.getName());
                log.info("再次登录，更新微信用户"+userz);
                // 编辑
                count = userMapper.updateByPrimaryKeySelective(userz);
                if(count>0){
                    return Result.success();
                }else{
                    return Result.error(CodeMsg.OP_FAILED,"更新失败");
                }
            }else{
                // 第一次登录，添加用户
                log.info("第一次登录，添加微信用户");
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                user.setId(uuid);
                count = userMapper.insert(user);
                if (count > 0) {
                    return Result.success();
                } else {
                    return Result.error(CodeMsg.OP_FAILED, "登录失败");
                }
            }
        }
    }
    // 1.1查询
    @Override
    public Result getLoginInfo(String code,String flag) {
        UserExample example = new UserExample();
        if (!StringUtils.isNullOrEmpty(code)) {
            example.createCriteria().andCodeEqualTo(code);
            ArrayList<User> list = userMapper.selectByExample(example);
            if(list.size()>0){
                AddressExample exampleAdd = new AddressExample();
                exampleAdd.createCriteria().andCodeEqualTo(code);
                List<Address> addresses = addressMapper.selectByExample(exampleAdd);
                String userName = "";
                String detailInfo = "";
                String telNumber = "";
                if(addresses.size()>0){
                    userName = addresses.get(0).getName();
                    detailInfo = addresses.get(0).getAddress();
                    telNumber = addresses.get(0).getPhone();
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("name",list.get(0).getName());
                map.put("note",list.get(0).getNote());
                map.put("sex",list.get(0).getSex());
                map.put("userName",userName);
                map.put("detailInfo",detailInfo);
                map.put("telNumber",telNumber);
                return Result.success(map);
            }else{
                return Result.error(CodeMsg.NOT_FIND_DATA,"无微信用户信息");
            }
        }else{
            if("all".equals(flag)){
                example.createCriteria().andIdIsNotNull();
                ArrayList<User> list = userMapper.selectByExample(example);
                if(list.size()>0){
                    return Result.success(list);
                }else{
                    return Result.error(CodeMsg.NOT_FIND_DATA,"无微信用户信息");
                }
            }else{
                return Result.error(CodeMsg.PARAMETER_ISNULL,"登陆失效，请重新登陆");
            }
        }
    }


    @Override
    public Result getPictures(String orderno) {
        PictureExample example = new PictureExample();
        example.createCriteria().andOrdernoEqualTo(orderno);
        ArrayList<Picture> list = pictureMapper.selectByExample(example);
        if(list.size()>0){
            return Result.success(list);
        }else{
            return Result.error(CodeMsg.NOT_FIND_DATA,"无图片，请上传");
        }
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
    public Result getAddress(String usercode) {
        AddressExample example = new AddressExample();
        example.createCriteria().andCodeEqualTo(usercode);
        List<Address> list = addressMapper.selectByExample(example);
        if(list.size()>0){
            return Result.success(list);
        }else{
            return Result.error(CodeMsg.NOT_FIND_DATA,"无地址信息，请添加");
        }
    }

    @Override
    public Result insertOrUpdateAddress(String addressid,String usercode,String addressz,String phone,String name) {
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
            if(count>0){
                return Result.success();
            }else{
                return Result.error(CodeMsg.OP_FAILED,"更新失败");
            }
        }else{
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            address.setId(uuid);
            address.setCode(usercode);
            address.setAddress(addressz);
            address.setPhone(phone);
            address.setName(name);
            count = addressMapper.insert(address);
            if(count>0){
                return Result.success();
            }else{
                return Result.error(CodeMsg.OP_FAILED,"新增失败");
            }
        }
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
        map.put("username",admin.getCode());
        map.put("userid",admin.getId());
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
    public Result insertOrUpdateAdmin(String id,String code,String psd,String name,Integer sex) {
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
            if(count>0){
                return Result.success();
            }else{
                return Result.error(CodeMsg.OP_FAILED,"更新失败");
            }
        }else{
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            admin.setId(uuid);
            admin.setCode(code);
            admin.setPsd(psd);
            admin.setName(name);
            admin.setSex(sex);
            count = adminMapper.insert(admin);
            if(count>0){
                return Result.success();
            }else{
                return Result.error(CodeMsg.OP_FAILED,"新增失败");
            }
        }
    }
    @Override
    public PageResult findPage(PageRequest pageRequest) {
//            String pageNum = remap.get("pageNum");
//            String pageSize = remap.get("pageSize");
//        PageRequest pageRequest = new PageRequest();
        String searchParam = pageRequest.getSearchParam();
        PageResult result = PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
        Stream<AllOrdersInfo> mapStream = result.getContent().stream().map(ordersInfo -> {
                    AllOrdersInfo allOrdersInfo = new AllOrdersInfo();
                    BeanUtil.copyProperties(ordersInfo, allOrdersInfo);
                    String userId = allOrdersInfo.getUserId();
                    String addressno = allOrdersInfo.getAddressno();
                    User user = sqlService.getUser(userId);
                    Address address = sqlService.getAddress(addressno);
                    if (!ObjectUtils.isEmpty(address)) {
                        allOrdersInfo.setName(address.getName());
                        allOrdersInfo.setAddress(address.getAddress());
                        allOrdersInfo.setPhone(address.getPhone());
                    } else {
                        allOrdersInfo.setName("");
                        allOrdersInfo.setAddress("");
                        allOrdersInfo.setPhone("");
                    };
                    allOrdersInfo.setUsername(user.getName());
                    allOrdersInfo.setAvatar(user.getNote());
                    allOrdersInfo.setSex(user.getSex());
                    return allOrdersInfo;
                });
        List<Object> lis = new ArrayList<>();
        Stream<AllOrdersInfo> allOrdersInfoStream = null;
        if(!StringUtils.isNullOrEmpty(searchParam)){
            allOrdersInfoStream = mapStream
//                    .filter(allOrdersInfo -> allOrdersInfo.getName().contains(searchParam));
//                    .filter(allOrdersInfo -> allOrdersInfo.getOrderNo().contains(searchParam))
//                    .filter(allOrdersInfo -> allOrdersInfo.getTitle().contains(searchParam))
//                    .filter(allOrdersInfo -> allOrdersInfo.getAddress().contains(searchParam))
                    .filter(allOrdersInfo -> allOrdersInfo.getPhone().contains(searchParam));
//                    .filter(allOrdersInfo -> allOrdersInfo.getName().contains(searchParam))

            lis = allOrdersInfoStream.collect(Collectors.toList());
        }else{
            lis = mapStream.collect(Collectors.toList());
        }
        result.setContent(lis);
        return result;
    }


    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<OrdersInfo> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<OrdersInfo> sysMenus = ordersInfoMapper.selectOrdersInfoPage();
        return new PageInfo<OrdersInfo>(sysMenus);
    }

    @Override
    public PageResult findLogPage(PageRequest pageRequest) {
//            String pageNum = remap.get("pageNum");
//            String pageSize = remap.get("pageSize");
//        PageRequest pageRequest = new PageRequest();
        String searchParam = pageRequest.getSearchParam();
        PageResult result = PageUtils.getPageResult(pageRequest, getLogPageInfo(pageRequest));
        Stream<HashMap<Object, Object>> mapStream = result.getContent().stream().map(ordersInfo -> {

            HashMap<Object, Object> map = new HashMap<>();
            BeanUtil.copyProperties(ordersInfo, map);
            String userId = ((Logss)ordersInfo).getUserId();
            User user = sqlService.getUser(userId);
            if (!ObjectUtils.isEmpty(user)) {
                map.put("name",user.getName());
                map.put("sex",user.getSex());
                map.put("avatar",user.getNote());
            }
            return map;
        });
        List<Object> lis = new ArrayList<>();
        Stream<HashMap<Object, Object>> allOrdersInfoStream = null;
        if(!StringUtils.isNullOrEmpty(searchParam)){
            allOrdersInfoStream = mapStream
//                    .filter(allOrdersInfo -> allOrdersInfo.getName().contains(searchParam));
//                    .filter(allOrdersInfo -> allOrdersInfo.getOrderNo().contains(searchParam))
//                    .filter(allOrdersInfo -> allOrdersInfo.getTitle().contains(searchParam))
//                    .filter(allOrdersInfo -> allOrdersInfo.getAddress().contains(searchParam))
                    .filter(allOrdersInfo -> allOrdersInfo.get("name").toString().contains(searchParam));
//                    .filter(allOrdersInfo -> allOrdersInfo.getName().contains(searchParam))

            lis = allOrdersInfoStream.collect(Collectors.toList());
        }else{
            lis = mapStream.collect(Collectors.toList());
        }
        result.setContent(lis);
        return result;
    }


    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<Logss> getLogPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Logss> logss = LogssMapper.selectLogInfoPage();
        return new PageInfo<Logss>(logss);
    }



    /*
     * 定时查询新订单
     * */
    @Override
    public PageResult taskFindOrders(PageRequest pageRequest) {
//            String pageNum = remap.get("pageNum");
//            String pageSize = remap.get("pageSize");
//        PageRequest pageRequest = new PageRequest();
//        String searchParam = pageRequest.getSearchParam();
        PageResult result = PageUtils.getPageResult(pageRequest, taskGetPageInfo(pageRequest));
        if(result.getContent().size()==0){
            return result;
        }
        Stream<AllOrdersInfo> mapStream = result.getContent().stream().map(ordersInfo -> {
            AllOrdersInfo allOrdersInfo = new AllOrdersInfo();
            BeanUtil.copyProperties(ordersInfo, allOrdersInfo);
            String userId = allOrdersInfo.getUserId();
            String addressno = allOrdersInfo.getAddressno();
            User user = sqlService.getUser(userId);
            Address address = sqlService.getAddress(addressno);
            if (!ObjectUtils.isEmpty(address)) {
                allOrdersInfo.setName(address.getName());
                allOrdersInfo.setAddress(address.getAddress());
                allOrdersInfo.setPhone(address.getPhone());
            } else {
                allOrdersInfo.setName("");
                allOrdersInfo.setAddress("");
                allOrdersInfo.setPhone("");
            };
            allOrdersInfo.setUsername(user.getName());
            allOrdersInfo.setAvatar(user.getNote());
            allOrdersInfo.setSex(user.getSex());
            return allOrdersInfo;
        });
        List<Object> lis = new ArrayList<>();
//        Stream<AllOrdersInfo> allOrdersInfoStream = null;
//        if(!StringUtils.isNullOrEmpty(searchParam)){
//            allOrdersInfoStream = mapStream
////                    .filter(allOrdersInfo -> allOrdersInfo.getName().contains(searchParam));
////                    .filter(allOrdersInfo -> allOrdersInfo.getOrderNo().contains(searchParam))
////                    .filter(allOrdersInfo -> allOrdersInfo.getTitle().contains(searchParam))
////                    .filter(allOrdersInfo -> allOrdersInfo.getAddress().contains(searchParam))
//                    .filter(allOrdersInfo -> allOrdersInfo.getPhone().contains(searchParam));
////                    .filter(allOrdersInfo -> allOrdersInfo.getName().contains(searchParam))
//
//            lis = allOrdersInfoStream.collect(Collectors.toList());
//        }else{
//            lis = mapStream.collect(Collectors.toList());
//        }
        lis = mapStream.collect(Collectors.toList());
        result.setContent(lis);
        return result;
    }


    /**
     * 调用分页插件完成分页
     * @param pageRequest
     * @return
     */
    private PageInfo<OrdersInfo> taskGetPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);

        String newOrdersCountA = redisTemplate.opsForValue().get("newOrdersCount");
        int newOrdersCountB = sqlService.getNewOrdersCount();
        if(Integer.parseInt(newOrdersCountA) < newOrdersCountB){
            int count = newOrdersCountB - Integer.parseInt(newOrdersCountA);
            ArrayList<OrdersInfo> newOrdersList = sqlService.getNewOrdersList(count);
            return new PageInfo<OrdersInfo>(newOrdersList);
        }else{
            return new PageInfo<OrdersInfo>(new ArrayList<OrdersInfo>());
        }
    }

}
