package com.curtis.sharding.mapper;

import com.curtis.sharding.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author curtis
 * @desc 用户数据访问接口
 * @date 2020-09-03
 * @email 397773935@qq.com
 * @reference
 */
// Spring会扫描@Mapper注解的接口，也可以在启动类或者配置类上使用@MapperScan注解
//@Mapper
@Repository
public interface OrderMapper {

    /**
     * 插入用户订单信息
     *
     * @param order
     * @return
     */
    int insertOrder(Order order);

    /**
     * 获取指定用户订单
     * @param userId
     * @return
     */
    List<Order> getOrderByUserId(@Param("userId") Integer userId);

    /**
     * 获取指定订单
     * @param orderId
     * @return
     */
    List<Order> getOrderByOrderId(@Param("orderId") Long orderId);

    /**
     * 获取指定条件订单
     * @param userId
     * @param orderId
     * @return
     */
    List<Order> getOrderByUserIdAndOrderId(@Param("userId") Integer userId,@Param("orderId") Long orderId);

    /**
     * 查询指定订单集合
     * @param orderIds
     * @return
     */
    List<Order> getOrdersInOrderIds(@Param("orderIds") List<Long> orderIds);


    /**
     * 查询指定订单范围订单
     * @param minOrderId
     * @param maxOrderId
     * @return
     */
    List<Order> getOrdersBetweenOrderId(@Param("minOrderId") Long minOrderId,@Param("maxOrderId") Long maxOrderId);

    /**
     * 查询指定大于指定订单号的订单
     * @param minOrderId
     * @return
     */
    List<Order> getOrdersGreaterThanOrderId(@Param("minOrderId") Long minOrderId);
}
