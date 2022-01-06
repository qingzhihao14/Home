package com.my.friends.mapper;

import com.my.friends.dao.Qy;
import com.my.friends.dao.QyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QyMapper {
    long countByExample(QyExample example);

    int deleteByExample(QyExample example);

    int deleteByPrimaryKey(String id);

    int insert(Qy record);

    int insertSelective(Qy record);

    List<Qy> selectByExample(QyExample example);

    Qy selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Qy record, @Param("example") QyExample example);

    int updateByExample(@Param("record") Qy record, @Param("example") QyExample example);

    int updateByPrimaryKeySelective(Qy record);

    int updateByPrimaryKey(Qy record);
}