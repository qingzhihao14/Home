package com.my.friends.mapper;

import com.my.friends.dao.Lbs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

import java.util.ArrayList;

@Mapper
@MapperScan
public interface LbsExample {
    @Select("SELECT a.`id`, a.`code`, a.`name`, a.`sex`, a.`wechat`, a.`note`, b.`id` `addressid`,b.`name` `realname`, b.`address`, b.`phone` " +
            " FROM t_user a left join `t_address` b on a.`code` = b.`code`" +
            " where a.`code`=#{code} ")
    ArrayList<Lbs> selectTeacher(@Param("code")  String code);
}