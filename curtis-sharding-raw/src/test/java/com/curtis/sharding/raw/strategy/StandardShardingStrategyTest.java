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
 * @desc 测试标准分片策略（StandardShardingStrategy）
 * 对应StandardShardingStrategy。提供对SQL语句中的 =, >, <, >=, <=, IN和BETWEEN AND 的分片操作支持。
 * StandardShardingStrategy只支持单分片键，提供 PreciseShardingAlgorithm和RangeShardingAlgorithm两个分片算法。
 * PreciseShardingAlgorithm是必选的，用于处理 = 和 IN 的分片。RangeShardingAlgorithm是可选的，用于处理BETWEEN AND, >, <, >=, <=分片，
 * 如果不配置 RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理。
 * @date 2020-09-11
 * @email 397773935@qq.com
 * @reference
 */
@ActiveProfiles("dev-standard")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CurtisShardingRawApplication.class)
public class StandardShardingStrategyTest {

    @Autowired
    private OrderMapper orderMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(StandardShardingStrategyTest.class);

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
     * 标准策略如果初始化时指定精确分片算法和范围分片算法则支持分片键的范围查询
     * 查询条件无用户，不知道在哪个库，则查询所有分库
     * 查询订单号大于等于10000L并且小于等于10002L，未跨周期所以不需要查询所有分片表，
     * 尾数取模确定tb_order_0、tb_order_1、tb_order_2三个分表
     */
    @Test
    public void testGetOrdersBetweenOrderId() {
        List<Order> orders = orderMapper.getOrdersBetweenOrderId(10000L, 10002L);
        LOGGER.info("orders = {}", orders);
    }

    /**
     * 标准策略如果初始化时指定精确分片算法和范围分片算法则支持分片键的范围查询
     * 查询条件无用户，不知道在哪个库，则查询所有分库
     * 查询订单号大于等于10003L，需要查询所有分表
     */
    @Test
    public void testGetOrdersGreaterThanOrderId() {
        List<Order> orders = orderMapper.getOrdersGreaterThanOrderId(10003L);
        LOGGER.info("orders = {}", orders);
    }
}
