package com.curtis.sharding.raw.strategy;

import com.curtis.sharding.raw.CurtisShardingRawApplication;
import com.curtis.sharding.raw.entity.Order;
import com.curtis.sharding.raw.mapper.OrderMapper;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author curtis
 * @desc 测试行表达式分片策略（InlineShardingStrategy）
 *  使用Groovy 的表达式，仅用于对单一分片键进行等值分片（=和In）的情景。不支持范围分片，不使用任何内置分片算法。
 * @date 2020-09-11
 * @email 397773935@qq.com
 * @reference
 */
@ActiveProfiles("dev-inline")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CurtisShardingRawApplication.class)
public class InlineShardingStrategyTest {

    @Autowired
    private OrderMapper orderMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(InlineShardingStrategyTest.class);

    /*
     测试之前需要手动清理所有分库以及分表
     TRUNCATE TABLE db_test_0.tb_order_0;
     TRUNCATE TABLE db_test_0.tb_order_1;
     TRUNCATE TABLE db_test_0.tb_order_2;
     TRUNCATE TABLE db_test_0.tb_order_3;

     TRUNCATE TABLE db_test_1.tb_order_0;
     TRUNCATE TABLE db_test_1.tb_order_1;
     TRUNCATE TABLE db_test_1.tb_order_2;
     TRUNCATE TABLE db_test_1.tb_order_3;
     */

    /**
     * 测试：
     * 在db_test_0中插入用户1000订单号10000~10007的订单。
     * 在db_test_1中插入用户1001订单号11000~11007的订单。
     */
    @Test
    public void testInsertOrder() {
        for (int i = 0; i < 8; i++) {
            orderMapper.insertOrder(new Order(10000L + i, 1000, 1L));
        }

        for (int i = 0; i < 8; i++) {
            orderMapper.insertOrder(new Order(11000L + i, 1001, 1L));
        }
    }

    /**
     * 测试：查询用户1000的订单
     * 根据尾数取模，用户1000的订单仅存在db_test_0中，并且查询条件无订单号不知道在哪个分表，
     * 所以将查询db_test_0的四个分表
     */
    @Test
    public void testGetOrderByUserId() {
        List<Order> orders = orderMapper.getOrderByUserId(1000);
        LOGGER.info("orders = {}", orders);
    }

    /**
     * 测试：查询订单10001L
     * 订单10001L只存在于db_test_0中，查询条件无用户，则无法判定在哪个库中，
     * 所以按照定位尾数取模，将查询db_test_0的tb_order_1和db_test_1的tb_order_1
     */
    @Test
    public void testGetOrderByOrderId() {
        List<Order> orders = orderMapper.getOrderByOrderId(10001L);
        LOGGER.info("orders = {}", orders);
    }

    /**
     * 测试：查询用户1001的11002L订单
     * 用户为1001将查询db_test_1，订单11002L尾数取模是2则查询tb_order_2，
     * 也就是分片至db_test_1.tb_order_2。
     */
    @Test
    public void testGetOrderByUserIdAndOrderId() {
        List<Order> orders = orderMapper.getOrderByUserIdAndOrderId(1001, 11002L);
        LOGGER.info("orders = {}", orders);
    }

    /**
     * 测试：查询订单10000L和10003L的信息
     * 查询条件无用户，不知道在哪个库，则查询所有分库
     * 查询订单10000L和10003L，根据尾数取模，分别分片至tb_order_0和tb_order_3
     */
    @Test
    public void testGetOrdersInOrderIds() {
        List<Order> orders = orderMapper.getOrdersInOrderIds(Lists.newArrayList(10000L, 10003L));
        LOGGER.info("orders = {}", orders);
    }

    /**
     * 内联分片策略仅支持分片键的=和In关键字，不支持范围查询，直接报错而不是路由所有表
     * 异常：Cause: java.lang.IllegalStateException:
     *  Inline strategy cannot support this type sharding:RangeRouteValue(columnName=order_id, tableName=tb_order, valueRange=[10000..10003])
     *
     * Caused by: java.lang.IllegalStateException:
     *  Inline strategy cannot support this type sharding:RangeRouteValue(columnName=order_id, tableName=tb_order, valueRange=[10000..10003])
     */
    @Test
    public void testGetOrdersBetweenOrderId() {
        List<Order> orders = orderMapper.getOrdersBetweenOrderId(10000L, 10003L);
        LOGGER.info("orders = {}", orders);
    }

    /**
     * 内联分片策略仅支持分片键的=和In关键字，不支持范围查询，直接报错而不是路由所有表
     * Cause: java.lang.IllegalStateException:
     *  Inline strategy cannot support this type sharding:RangeRouteValue(columnName=order_id, tableName=tb_order, valueRange=[10001..+∞))
     *
     * Caused by: java.lang.IllegalStateException:
     *  Inline strategy cannot support this type sharding:RangeRouteValue(columnName=order_id, tableName=tb_order, valueRange=[10001..+∞))
     */
    @Test
    public void testGetOrdersGreaterThanOrderId() {
        List<Order> orders = orderMapper.getOrdersGreaterThanOrderId(10001L);
        LOGGER.info("orders = {}", orders);
    }
}
