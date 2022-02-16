package com.my.friends.mapper;

import com.my.friends.dao.Logss;
import com.my.friends.dao.LogssExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LogssMapper {
    long countByExample(LogssExample example);

    int deleteByExample(LogssExample example);

    int deleteByPrimaryKey(String id);

    int insert(Logss record);

    int insertSelective(Logss record);

    List<Logss> selectByExample(LogssExample example);

    Logss selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Logss record, @Param("example") LogssExample example);

    int updateByExample(@Param("record") Logss record, @Param("example") LogssExample example);

    int updateByPrimaryKeySelective(Logss record);

    int updateByPrimaryKey(Logss record);
}