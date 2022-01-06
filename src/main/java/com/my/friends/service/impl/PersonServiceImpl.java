package com.my.friends.service.impl;

import com.my.friends.dao.*;
import com.my.friends.dao.extend.LbXm;
import com.my.friends.mapper.*;
import com.my.friends.mapper.extend.LbXmMapper;
import com.my.friends.service.PersonService;
import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.catalina.mbeans.UserMBean;
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
}
