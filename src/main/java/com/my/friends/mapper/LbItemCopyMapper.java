package com.my.friends.mapper;

import com.my.friends.dao.LbItemCopy;
import com.my.friends.dao.LbItemCopyExample;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LbItemCopyMapper {
    long countByExample(LbItemCopyExample example);

    int deleteByExample(LbItemCopyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LbItemCopy record);

    int insertSelective(LbItemCopy record);

    ArrayList<LbItemCopy> selectByExample(LbItemCopyExample example);

    LbItemCopy selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LbItemCopy record, @Param("example") LbItemCopyExample example);

    int updateByExample(@Param("record") LbItemCopy record, @Param("example") LbItemCopyExample example);

    int updateByPrimaryKeySelective(LbItemCopy record);

    int updateByPrimaryKey(LbItemCopy record);
}