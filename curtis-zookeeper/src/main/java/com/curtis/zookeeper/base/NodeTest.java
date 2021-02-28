package com.curtis.zookeeper.base;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class NodeTest {

    private static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    @Test
    public void testCreateNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181",
                5000, new NodeTest.ZookeeperWatcher());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Zookeeper Session Established.");

        // 判断节点是否存在
        Stat exists = zooKeeper.exists("/zk-node-1", false);
        zooKeeper.exists("/zk-node-1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("回调方法：指定节点：/zk-node-1 不存在。");
            }
        });
        System.out.println(exists);


        // 用于测试的一级节点：zk-node，客户端断开节点删除。
        zooKeeper.create("/zk-node", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        /********************************************** 创建节点 **********************************************/

        // 创建节点-持久节点
        zooKeeper.create("/zk-node/zk-node-persistent", "zk-node-data".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        // 创建节点-持久顺序节点
        zooKeeper.create("/zk-node/zk-node-persistent", "zk-node-data".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        zooKeeper.create("/zk-node/zk-node-persistent", "zk-node-data".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

        // 创建节点-临时节点
        zooKeeper.create("/zk-node/zk-node-ephemeral", "zk-node-data".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        // 创建节点-临时顺序节点
        zooKeeper.create("/zk-node/zk-node-ephemeral", "zk-node-data".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);


        // 父节点不存在，无法直接创建子节点
        try {
            zooKeeper.create("/zk-node-test/zk-node-test", "zk-node-data".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
            // org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /zk-node-test/zk-node-test
            System.out.println(e);
            // KeeperErrorCode = NoNode for /zk-node-test/zk-node-test
            System.out.println(e.getMessage());
        }

        /********************************************** 获取节点数据 **********************************************/
        Stat stat = new Stat();
        byte[] dataByteArray = zooKeeper.getData("/zk-node/zk-node-persistent", true, stat);
        byte[] dataByteArray2 = zooKeeper.getData("/zk-node/zk-node-persistent", false, null);
        java.lang.String dataStr = new java.lang.String(dataByteArray, StandardCharsets.UTF_8);
        java.lang.String dataStr2 = new java.lang.String(dataByteArray2, StandardCharsets.UTF_8);
        System.out.println(dataStr);
        System.out.println(dataStr2);
        System.out.println(stat);

        try {
            byte[] data = zooKeeper.getData("/zk-node/zk-node-notexists", false, null);
        } catch (Exception e) {
            // org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /zk-node/zk-node-notexists
            System.out.println(e);
            // KeeperErrorCode = NoNode for /zk-node/zk-node-notexists
            System.out.println(e.getMessage());
        }


        // 获取指定节点的所有子节点
        List<String> childrenNodeList = zooKeeper.getChildren("/zk-node", false);
        System.out.println("节点/zk-node的所有子节点：" + childrenNodeList.stream().collect(Collectors.joining(", ")));

        // 删除有子节点的节点，抛出异常
        try {
            zooKeeper.delete("/zk-node",-1);
        } catch (Exception e) {
            e.printStackTrace();
            // org.apache.zookeeper.KeeperException$NotEmptyException: KeeperErrorCode = Directory not empty for /zk-node
            System.out.println(e);
            // KeeperErrorCode = Directory not empty for /zk-node
            System.out.println(e.getMessage());
        }

        // 删除错误版本的节点，抛出异常
        try {
            zooKeeper.delete("/zk-node",1);
        } catch (Exception e) {
            e.printStackTrace();
            // org.apache.zookeeper.KeeperException$BadVersionException: KeeperErrorCode = BadVersion for /zk-node
            System.out.println(e);
            // KeeperErrorCode = BadVersion for /zk-node
            System.out.println(e.getMessage());
        }


        // 测试后置操作
        List<String> childrenList = zooKeeper.getChildren("/zk-node", false);
        for (String childNode : childrenList) {
            System.out.println(childNode);
            String nodePath = "/zk-node/" + childNode;
            zooKeeper.delete(nodePath, -1);
        }
        zooKeeper.delete("/zk-node", -1);
        // 临时节点下不能创建子节点

    }

    static class ZookeeperWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            Event.KeeperState state = event.getState();
            if (Event.KeeperState.SyncConnected == state) {
                connectedSemaphore.countDown();
            }
        }
    }
}
