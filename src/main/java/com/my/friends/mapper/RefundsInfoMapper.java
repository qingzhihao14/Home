package com.my.friends.mapper;

import com.my.friends.dao.RefundsInfo;
import com.my.friends.dao.RefundsInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RefundsInfoMapper {
    long countByExample(RefundsInfoExample example);

    int deleteByExample(RefundsInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(RefundsInfo record);

    int insertSelective(RefundsInfo record);

    List<RefundsInfo> selectByExample(RefundsInfoExample example);

    RefundsInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") RefundsInfo record, @Param("example") RefundsInfoExample example);

    int updateByExample(@Param("record") RefundsInfo record, @Param("example") RefundsInfoExample example);

    int updateByPrimaryKeySelective(RefundsInfo record);

    int updateByPrimaryKey(RefundsInfo record);
}