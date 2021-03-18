package com.curtis.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * @author curtis
 * @desc 删除ZK节点
 * @date 2021-02-27
 * @email 397773935@qq.com
 * @reference
 */
public class NodeDeleteTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeDeleteTest.class);

    /**
     * 测试：删除节点
     */
    @Test
    public void testDeleteNode() throws Exception {

        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();
        LOGGER.info("Zookeeper Session Established.");

        // 创建用于测试的父节点以及子节点。
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test");
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test/child-1");
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test/child-2");
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test/child-3");

        /********************************************** 删除指定路径的节点 **********************************************/
        // 指令格式：delete [-v version] path
        // 使用delete path删除指定节点。使用delete -v version path来删除指定版本的节点。需要注意的是无法使用delete删除父节点。

        // 无法直接通过delete删除父节点
        try {
            client.delete().forPath("/zk-test");
        } catch (KeeperException ke) {
            // ke.printStackTrace();
            LOGGER.info("异常：无法直接通过delete删除父节点：异常类：{},异常信息：{}", ke.getClass(), ke.getMessage());
        }

        // 使用delete path删除节点
        client.delete().forPath("/zk-test/child-1");

        // 使用delete -v version path删除指定版本节点，如果版本不对则抛出异常
        try {
            client.delete().withVersion(1).forPath("/zk-test/child-2");
        } catch (KeeperException ke) {
            // ke.printStackTrace();
            LOGGER.info("异常：删除指定版本节点时，如果版本不对则抛出异常：异常类：{},异常信息：{}", ke.getClass(), ke.getMessage());
        }
        client.delete().withVersion(0).forPath("/zk-test/child-2");

        // 删除节点以及子节点
        client.delete().deletingChildrenIfNeeded().forPath("/zk-test");

        // 删除不存在的节点将抛出异常
        try {
            client.delete().forPath("/zk-test");
        } catch (KeeperException ke) {
            // ke.printStackTrace();
            LOGGER.info("异常：删除不存在的节点将抛出异常：异常类：{},异常信息：{}", ke.getClass(), ke.getMessage());
        }
        TimeUnit.SECONDS.sleep(10);
    }
}
