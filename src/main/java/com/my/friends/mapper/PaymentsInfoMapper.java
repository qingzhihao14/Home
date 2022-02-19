package com.my.friends.mapper;

import com.my.friends.dao.PaymentsInfo;
import com.my.friends.dao.PaymentsInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PaymentsInfoMapper {
    long countByExample(PaymentsInfoExample example);

    int deleteByExample(PaymentsInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(PaymentsInfo record);

    int insertSelective(PaymentsInfo record);

    List<PaymentsInfo> selectByExample(PaymentsInfoExample example);

    PaymentsInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") PaymentsInfo record, @Param("example") PaymentsInfoExample example);

    int updateByExample(@Param("record") PaymentsInfo record, @Param("example") PaymentsInfoExample example);

    int updateByPrimaryKeySelective(PaymentsInfo record);

    int updateByPrimaryKey(PaymentsInfo record);
}