package com.mmall.dao;

import com.mmall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> getByUserIdAndOrderNumber(@Param("userId")Integer userId,@Param("orderNumber")Long orderNumber);

    List<OrderItem> getByOrderNumber(@Param("orderNumber")Long orderNumber);

    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);
}