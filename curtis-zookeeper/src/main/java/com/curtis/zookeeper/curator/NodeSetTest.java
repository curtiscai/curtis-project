package com.curtis.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * @author curtis
 * @desc 更新ZK节点
 * @date 2021-03-03
 * @email 397773935@qq.com
 * @reference
 */
public class NodeSetTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(NodeSetTest.class);

    @Test
    public void clearNode() throws Exception {
        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();

        if (client.checkExists().forPath("/zk-test") != null) {
            client.delete().deletingChildrenIfNeeded().forPath("/zk-test");
        }
    }

    /**
     * 测试：更新节点的值
     */
    @Test
    public void testSetNode() throws Exception {
        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();
        LOGGER.info("Zookeeper Session Established.");

        // 创建用于测试的父节点以及子节点。
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test", "mydata".getBytes(StandardCharsets.UTF_8));

        /********************************************** 更新节点 **********************************************/
        // 指令格式：set [-s] [-v version] path data
        // 使用set path data可以修改节点信息，使用set [-v version] path data修改指定版本节点的信息，使用set [-s] path data修改节点的同时返回节点结构体信息。

        // 查看节点的值以及详细信息，我们看到新创建节点的版本是0
        Stat stat1 = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat1).forPath("/zk-test");
        String nodeData = new String(bytes, StandardCharsets.UTF_8);
        LOGGER.info("节点/zk-test的值是：{}", nodeData);
        LOGGER.info("节点/zk-test的详细信息是：{}", stat1);

        // 修改节点的值，节点相关信息会更新，版本也会加1
        Stat stat2 = client.setData().forPath("/zk-test", "mydata2".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("节点/zk-test的Stat是：{}", stat2);


        // 修改指定版本节点的值，如果版本信息不对则无法修改
        try {
            client.setData().withVersion(0).forPath("/zk-test", "mydata3".getBytes(StandardCharsets.UTF_8));
        } catch (KeeperException ke) {
            // ke.printStackTrace();
            LOGGER.info("修改指定版本节点的值，如果版本信息不对则无法修改：异常类：{},异常信息：{}", ke.getClass(), ke.getMessage());
        }
        // 修改节点值的同时返回节点详细信息
        Stat stat4 = client.setData().withVersion(1).forPath("/zk-test", "mydata4".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("节点/zk-test的Stat是：{}", stat4);

        TimeUnit.SECONDS.sleep(30);
    }
}
