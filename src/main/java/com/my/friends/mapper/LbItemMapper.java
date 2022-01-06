package com.my.friends.mapper;

import com.my.friends.dao.LbItem;
import com.my.friends.dao.LbItemExample;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LbItemMapper {
    long countByExample(LbItemExample example);

    int deleteByExample(LbItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(LbItem record);

    int insertSelective(LbItem record);

    ArrayList<LbItem> selectByExample(LbItemExample example);

    LbItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") LbItem record, @Param("example") LbItemExample example);

    int updateByExample(@Param("record") LbItem record, @Param("example") LbItemExample example);

    int updateByPrimaryKeySelective(LbItem record);

    int updateByPrimaryKey(LbItem record);

}