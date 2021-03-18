package com.curtis.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author curtis
 * @desc 查看ZK节点
 * @date 2021-03-03
 * @email 397773935@qq.com
 * @reference
 */
public class NodeQueryTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(NodeQueryTest.class);

    @Test
    public void clearNode() throws Exception {

        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();

        if (client.checkExists().forPath("/zk-test-1") != null) {
            client.delete().deletingChildrenIfNeeded().forPath("/zk-test-1");
        }

        if (client.checkExists().forPath("/zk-test-2") != null) {
            client.delete().deletingChildrenIfNeeded().forPath("/zk-test-2");
        }
    }

    /**
     * 测试：查看节点的子节点列表
     */
    @Test
    public void testLsNode() throws Exception {
        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();
        LOGGER.info("Zookeeper Session Established.");

        // 创建用于测试的父节点以及子节点。
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-1", "mydata1".getBytes(StandardCharsets.UTF_8));
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-1/child-1", "mydata11".getBytes(StandardCharsets.UTF_8));
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-1/child-2", "mydata12".getBytes(StandardCharsets.UTF_8));
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-1/child-3", "mydata13".getBytes(StandardCharsets.UTF_8));
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-1/child-1/child-1-1", "mydata111".getBytes(StandardCharsets.UTF_8));

        /********************************************** 查看节点的子节点列表 **********************************************/
        // 指令格式：ls [-s] [-w] [-R] path
        // 使用ls path可以查看节点的子节点列表，使用ls -s path可以查看节点的子节点列表以及该节点详细信息。使用ls -R path可以递归查看该节点的所有子节点信息。
        // 使用ls -w path可以在查看节点的子节点的同时注册监听器，父节点的子节点有变化时可以监听到，注意只能监听一次。

        // 查看节点的子节点列表
        List<String> children1 = client.getChildren().forPath("/zk-test-1");
        LOGGER.info("节点/zk-test-1的子节点是：{}", children1);

        // 查看节点的子节点列表以及该节点详细信息
        Stat stat = new Stat();
        List<String> children2 = client.getChildren().storingStatIn(stat).forPath("/zk-test-1");
        LOGGER.info("节点/zk-test-1的详细信息是：{}", stat);
        LOGGER.info("节点/zk-test-1的子节点是：{}", children2);

        TimeUnit.SECONDS.sleep(30);
    }

    /**
     * 测试：查看节点内容
     */
    @Test
    public void testGetNode() throws Exception {
        String zookeeperConnectionString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();
        LOGGER.info("Zookeeper Session Established.");

        // 创建用于测试的父节点以及子节点。
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-2", "mydata2".getBytes(StandardCharsets.UTF_8));
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-2/child-1", "mydata21".getBytes(StandardCharsets.UTF_8));
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-2/child-2", "mydata22".getBytes(StandardCharsets.UTF_8));
        client.create().withMode(CreateMode.PERSISTENT).forPath("/zk-test-2/child-3", "mydata23".getBytes(StandardCharsets.UTF_8));

        /********************************************** 查看节点的值 **********************************************/
        // 指令格式：get [-s] [-w] path
        // 使用get path获取节点的值。使用get -s path获取节点的详细信息。
        byte[] bytes2 = client.getData().forPath("/zk-test-2");
        String nodeData2 = new String(bytes2, StandardCharsets.UTF_8);
        LOGGER.info("节点/zk-test-2的值是：{}", nodeData2);

        byte[] bytes21 = client.getData().forPath("/zk-test-2/child-1");
        String nodeData21 = new String(bytes21, StandardCharsets.UTF_8);
        LOGGER.info("节点/zk-test-2/child-1的值是：{}", nodeData21);

        Stat stat = new Stat();
        byte[] bytes22 = client.getData().storingStatIn(stat).forPath("/zk-test-2/child-2");
        String nodeData22 = new String(bytes22, StandardCharsets.UTF_8);
        LOGGER.info("节点/zk-test-2/child-2的值是：{}", nodeData22);
        LOGGER.info("节点/zk-test-2/child-2的详细信息是：{}", stat);

        TimeUnit.SECONDS.sleep(30);
    }
}
