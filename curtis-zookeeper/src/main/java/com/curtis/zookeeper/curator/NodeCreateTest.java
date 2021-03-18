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

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author curtis
 * @desc 创建ZK节点
 * @date 2021-03-03
 * @email 397773935@qq.com
 * @reference
 */
public class NodeCreateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeCreateTest.class);

    @Test
    public void clearNode() throws Exception {

        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();

        if (client.checkExists().forPath("/persistent-node-1") != null) {
            client.delete().forPath("/persistent-node-1");
        }
        if (client.checkExists().forPath("/persistent-node-2") != null) {
            client.delete().forPath("/persistent-node-2");
        }
        if (client.checkExists().forPath("/persistent-node-3/node-2") != null) {
            client.delete().deletingChildrenIfNeeded().forPath("/persistent-node-3");
        }

        if (client.checkExists().forPath("/node-test") != null) {
            client.delete().deletingChildrenIfNeeded().forPath("/node-test");
        }
    }

    /**
     * 测试：创建持久节点和临时节点
     */
    @Test
    public void testCreateNode() throws Exception {
        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();
        /********************************************** 创建持久节点 **********************************************/
        // 指令格式：create path [data]
        // 使用create path [data]可以创建持久节点，节点数据可选。需要注意的是path可以是多级路径，但是如果父路径不存在，则ZK不会递归创建直接报错。
        String persistentNode1Path = client.create().forPath("/persistent-node-1");
        LOGGER.info("The Path Of persistentNode1 Is：{}", persistentNode1Path);

        String persistentNode2Path = client.create().withMode(CreateMode.PERSISTENT).forPath("/persistent-node-2", "mydata".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("The Path Of persistentNode2 Is：{}", persistentNode2Path);

        try {
            // org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /persistent-node-3/node-1
            String persistentNode31Path = client.create().withMode(CreateMode.PERSISTENT).forPath("/persistent-node-3/node-1", "mydata".getBytes(StandardCharsets.UTF_8));
            LOGGER.info("The Path Of persistentNode31 Is：{}", persistentNode31Path);
        } catch (KeeperException ke) {
            ke.printStackTrace();
            LOGGER.info("如果父节点不存在则无法创建节点：异常类：{},异常信息{}", ke.getClass(), ke.getMessage());
        }

        String persistentNode32Path = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/persistent-node-3/node-2", "mydata".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("The Path Of persistentNode32 Is：{}", persistentNode32Path);


        /********************************************** 创建临时节点 **********************************************/
        // 指令格式：create [-e] path [data]
        // 使用create -e path [data]可以创建临时节点，节点数据可选。**需要注意的是path可以是多级路径，但是要求父路径必须存在并且父路径不能是临时节点，也就是说无法在临时节点下创建节点**。
        String ephemeralNode1Path = client.create().withMode(CreateMode.EPHEMERAL).forPath("/ephemeral_node-1");
        LOGGER.info("The Path Of ephemeralNode1 Is：{}", ephemeralNode1Path);

        String ephemeralNode2Path = client.create().withMode(CreateMode.EPHEMERAL).forPath("/ephemeral_node-2", "mydata".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("The Path Of ephemeralNode2 Is：{}", ephemeralNode2Path);

        try {
            // org.apache.zookeeper.KeeperException$NoChildrenForEphemeralsException: KeeperErrorCode = NoChildrenForEphemerals for /ephemeral_node-1/node-1
            String ephemeralNode11Path = client.create().withMode(CreateMode.EPHEMERAL).forPath("/ephemeral_node-1/node-1");
            LOGGER.info("The Path Of ephemeralNode11 Is：{}", ephemeralNode11Path);
        } catch (KeeperException ke) {
            // ke.printStackTrace();
            LOGGER.info("在临时节点下创建节点报错，临时节点不能有子节点：异常类：{},异常信息：{}", ke.getClass(), ke.getMessage());
        }

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 测试：创建有序节点
     */
    @Test
    public void testCreateSequentialNode() throws Exception {
        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();
        LOGGER.info("Zookeeper Session Established.");

        client.create().withMode(CreateMode.PERSISTENT).forPath("/node-test");

        /********************************************** 创建有序持久节点 **********************************************/
        // 指令格式：create [-s] [-e] path  [data]
        // 使用create -s path [data]创建有序持久节点，使用create -s -e path [data]创建有序临时节点。
        String persistentSequentialNode1Path = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/node-test/persistent_sequential_node");
        LOGGER.info("The Path Of persistentSequentialNode1 Is：{}", persistentSequentialNode1Path);
        String persistentSequentialNode2Path = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/node-test/persistent_sequential_node", "mydata".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("The Path Of persistentSequentialNode2 Is：{}", persistentSequentialNode2Path);
        String persistentSequentialNode3Path = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/node-test/persistent_sequential_node", "mydata".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("The Path Of persistentSequentialNode3 Is：{}", persistentSequentialNode3Path);


        /********************************************** 创建有序临时节点 **********************************************/
        // 指令格式：create [-s] [-e] path  [data]
        // 使用create -s path [data]创建有序持久节点，使用create -s -e path [data]创建有序临时节点。
        String ephemeralSequentialNode1Path = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/node-test/ephemeral_sequential_node");
        LOGGER.info("The Path Of ephemeralSequentialNode1 Is：{}", ephemeralSequentialNode1Path);
        String ephemeralSequentialNode2Path = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/node-test/ephemeral_sequential_node", "mydata".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("The Path Of ephemeralSequentialNode2 Is：{}", ephemeralSequentialNode2Path);
        String ephemeralSequentialNode3Path = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/node-test/ephemeral_sequential_node", "mydata".getBytes(StandardCharsets.UTF_8));
        LOGGER.info("The Path Of ephemeralSequentialNode3 Is：{}", ephemeralSequentialNode3Path);

        TimeUnit.SECONDS.sleep(30);
    }
}
