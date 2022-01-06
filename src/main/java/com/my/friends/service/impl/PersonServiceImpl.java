package com.my.friends.service.impl;

import com.my.friends.dao.Person;
import com.my.friends.dao.PersonExample;
import com.my.friends.mapper.PersonMapper;
import com.my.friends.service.PersonService;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;
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
}
