package com.my.friends.mapper;

import com.my.friends.dao.Lb;
import com.my.friends.dao.LbExample;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;

public interface LbMapper {
    long countByExample(LbExample example);

    int deleteByExample(LbExample example);

    int deleteByPrimaryKey(String id);

    int insert(Lb record);

    int insertSelective(Lb record);

    ArrayList<Lb> selectByExample(LbExample example);

    Lb selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Lb record, @Param("example") LbExample example);

    int updateByExample(@Param("record") Lb record, @Param("example") LbExample example);

    int updateByPrimaryKeySelective(Lb record);

    int updateByPrimaryKey(Lb record);
}