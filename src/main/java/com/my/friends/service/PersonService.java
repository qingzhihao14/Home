package com.my.friends.service;

import com.my.friends.dao.Person;

public interface PersonService {
    // 获取自己的信息
    Person getMyInfo(String WeChat);

    // 获取一个人
    Person getOnePerson(String WeChat);
}
