package com.curtis.zookeeper.base;


import org.apache.zookeeper.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author curtis
 * @desc 创建ZK节点
 * @date 2021-02-27
 * @email 397773935@qq.com
 * @reference
 */
public class NodeCreateTest {

    private static final CountDownLatch CONNECTED_SEMAPHORE = new CountDownLatch(1);

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeCreateTest.class);

    /**
     * 测试：创建持久节点和临时节点
     */
    @Test
    public void testCreateNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181",
                5000, new InitWatcher());
        try {
            CONNECTED_SEMAPHORE.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Zookeeper Session Established.");

        /********************************************** 创建持久节点 **********************************************/
        // 指令格式：create path [data]
        // 使用create path [data]可以创建持久节点，节点数据可选。需要注意的是path可以是多级路径，但是如果父路径不存在，则ZK不会递归创建直接报错。
        String persistentNode1Path = zooKeeper.create("/persistent-node-1", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        LOGGER.info("The Path Of persistentNode1 Is：{}", persistentNode1Path);

        String persistentNode2Path = zooKeeper.create("/persistent-node-2", "mydata".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        LOGGER.info("The Path Of persistentNode2 Is：{}", persistentNode2Path);

        String persistentNode11Path = zooKeeper.create("/persistent-node-1/node-1", "mydata".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        LOGGER.info("The Path Of persistentNode11 Is：{}", persistentNode11Path);

        try {
            // org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /persistent-node-3/node-1
            String persistentNode31Path = zooKeeper.create("/persistent-node-3/node-1", "mydata".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            LOGGER.info("The Path Of persistentNode31 Is：{}", persistentNode31Path);
        } catch (KeeperException ke) {
            ke.printStackTrace();
            LOGGER.info("如果父节点不存在则无法创建节点：{}", ke);
        }

        /********************************************** 创建临时节点 **********************************************/
        // 指令格式：create [-e] path [data]
        // 使用create -e path [data]可以创建临时节点，节点数据可选。**需要注意的是path可以是多级路径，但是要求父路径必须存在并且父路径不能是临时节点，也就是说无法在临时节点下创建节点**。
        String ephemeralNode1Path = zooKeeper.create("/ephemeral_node-1", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        LOGGER.info("The Path Of ephemeralNode1 Is：{}", ephemeralNode1Path);

        String ephemeralNode2Path = zooKeeper.create("/ephemeral_node-2", "mydata".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        LOGGER.info("The Path Of ephemeralNode2 Is：{}", ephemeralNode2Path);

        try {
            // org.apache.zookeeper.KeeperException$NoChildrenForEphemeralsException: KeeperErrorCode = NoChildrenForEphemerals for /ephemeral_node-1/node-1
            String ephemeralNode11Path = zooKeeper.create("/ephemeral_node-1/node-1", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            LOGGER.info("The Path Of ephemeralNode11 Is：{}", ephemeralNode11Path);
        } catch (KeeperException ke) {
            ke.printStackTrace();
            LOGGER.info("在临时节点下创建节点报错，临时节点不能有子节点：{}", ke);
        }

        TimeUnit.SECONDS.sleep(30);
    }

    /**
     * 测试：创建有序节点
     */
    @Test
    public void testCreateSequentialNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181",
                5000, new InitWatcher());
        try {
            CONNECTED_SEMAPHORE.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Zookeeper Session Established.");

        /********************************************** 创建有序持久节点 **********************************************/
        // 指令格式：create [-s] [-e] path  [data]
        // 使用create -s path [data]创建有序持久节点，使用create -s -e path [data]创建有序临时节点。
        String persistentSequentialNode1Path = zooKeeper.create("/persistent_sequential_node", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        LOGGER.info("The Path Of persistentSequentialNode1 Is：{}", persistentSequentialNode1Path);
        String persistentSequentialNode2Path = zooKeeper.create("/persistent_sequential_node", "mydata".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        LOGGER.info("The Path Of persistentSequentialNode2 Is：{}", persistentSequentialNode2Path);
        String persistentSequentialNode3Path = zooKeeper.create("/persistent_sequential_node", "mydata".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        LOGGER.info("The Path Of persistentSequentialNode3 Is：{}", persistentSequentialNode3Path);


        /********************************************** 创建有序临时节点 **********************************************/
        // 指令格式：create [-s] [-e] path  [data]
        // 使用create -s path [data]创建有序持久节点，使用create -s -e path [data]创建有序临时节点。
        String ephemeralSequentialNode1Path = zooKeeper.create("/ephemeral_sequential_node", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        LOGGER.info("The Path Of ephemeralSequentialNode1 Is：{}", ephemeralSequentialNode1Path);
        String ephemeralSequentialNode2Path = zooKeeper.create("/ephemeral_sequential_node", "mydata".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        LOGGER.info("The Path Of ephemeralSequentialNode2 Is：{}", persistentSequentialNode2Path);
        String ephemeralSequentialNode3Path = zooKeeper.create("/ephemeral_sequential_node", "mydata".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        LOGGER.info("The Path Of ephemeralSequentialNode3 Is：{}", ephemeralSequentialNode3Path);

        TimeUnit.SECONDS.sleep(30);

        zooKeeper.close();
    }


    @Test
    public void deleteZkNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181",
                5000, new InitWatcher());
        try {
            CONNECTED_SEMAPHORE.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Zookeeper Session Established.");
        if (zooKeeper.exists("/persistent-node-1/node-1", false) != null) {
            zooKeeper.delete("/persistent-node-1/node-1", -1);
        }
        if (zooKeeper.exists("/persistent-node-1", false) != null) {
            zooKeeper.delete("/persistent-node-1", -1);
        }
        if (zooKeeper.exists("/persistent-node-2", false) != null) {
            zooKeeper.delete("/persistent-node-2", -1);
        }
        if (zooKeeper.exists("/persistent_sequential_node", false) != null) {
            zooKeeper.delete("/persistent_sequential_node", -1);
        }
        if (zooKeeper.exists("/ephemeral_sequential_node", false) != null) {
            zooKeeper.delete("/ephemeral_sequential_node", -1);
        }
    }

    static class InitWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            Event.KeeperState state = event.getState();
            System.out.println(state);
            System.out.println(event);
            // if (Event.KeeperState.SyncConnected == state) {
            //     connectedSemaphore.countDown();
            // }
            CONNECTED_SEMAPHORE.countDown();
            LOGGER.info("event:" + event);
        }
    }
}
