package com.my.friends.mapper;

import com.my.friends.dao.Logss;
import com.my.friends.dao.OrdersInfo;
import com.my.friends.dao.OrdersInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrdersInfoMapper {
    long countByExample(OrdersInfoExample example);

    int deleteByExample(OrdersInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(OrdersInfo record);

    int insertSelective(OrdersInfo record);

    List<OrdersInfo> selectByExample(OrdersInfoExample example);

    OrdersInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OrdersInfo record, @Param("example") OrdersInfoExample example);

    int updateByExample(@Param("record") OrdersInfo record, @Param("example") OrdersInfoExample example);

    int updateByPrimaryKeySelective(OrdersInfo record);

    int updateByPrimaryKey(OrdersInfo record);


    /**
     * 分页查询用户
     * @return
     */
    List<OrdersInfo> selectOrdersInfoPage();
}