package com.my.friends.service;

import com.my.friends.dao.Lb;
import com.my.friends.dao.LbItem;
import com.my.friends.dao.Person;
import com.my.friends.dao.extend.LbXm;

import java.util.ArrayList;

public interface PersonService {
    // 获取自己的信息
    Person getMyInfo(String WeChat);

    // 获取一个人
    Person getOnePerson(String WeChat);


    /*
    * 1.类别
    * */

    //查询
    ArrayList<Lb> getLb();
    Boolean insertOrUpdateLb(Lb lb);
    //查询
    ArrayList<LbXm> selectLbXm(String parent);


    /*
     * 2.项目
     * */
    Boolean insertOrUpdateItem(LbItem lbItem);

}
