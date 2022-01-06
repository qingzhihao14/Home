package com.my.friends.mapper;

import com.my.friends.dao.Person;
import com.my.friends.dao.PersonExample;
import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;

public interface PersonMapper {
    long countByExample(PersonExample example);

    int deleteByExample(PersonExample example);

    int deleteByPrimaryKey(String uuid);

    int insert(Person record);

    int insertSelective(Person record);

    ArrayList<Person> selectByExample(PersonExample example);

    Person selectByPrimaryKey(String uuid);

    int updateByExampleSelective(@Param("record") Person record, @Param("example") PersonExample example);

    int updateByExample(@Param("record") Person record, @Param("example") PersonExample example);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);
}