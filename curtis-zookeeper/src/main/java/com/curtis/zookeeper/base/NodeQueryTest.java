// package com.curtis.zookeeper.base;
//
//
// import org.apache.zookeeper.*;
// import org.apache.zookeeper.data.Stat;
// import org.junit.Test;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import java.io.IOException;
// import java.nio.charset.StandardCharsets;
// import java.util.List;
// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.TimeUnit;
//
// /**
//  * @author curtis
//  * @desc 查看ZK节点
//  * @date 2021-02-27
//  * @email 397773935@qq.com
//  * @reference
//  */
// public class NodeQueryTest {
//
//     private static final CountDownLatch CONNECTED_SEMAPHORE = new CountDownLatch(1);
//
//     private static final Logger LOGGER = LoggerFactory.getLogger(NodeQueryTest.class);
//
//     /**
//      * 测试：查看节点的子节点列表
//      */
//     @Test
//     public void testLsNode() throws IOException, KeeperException, InterruptedException {
//         ZooKeeper zooKeeper = new ZooKeeper("192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181",
//                 5000, new InitWatcher());
//         try {
//             CONNECTED_SEMAPHORE.await();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//         LOGGER.info("Zookeeper Session Established.");
//
//         // 创建用于测试的父节点以及子节点。
//         zooKeeper.create("/zk-test-1", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//         zooKeeper.create("/zk-test-1/child-1", "mydata1".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//         zooKeeper.create("/zk-test-1/child-2", "mydata2".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//         zooKeeper.create("/zk-test-1/child-3", "mydata3".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//         zooKeeper.create("/zk-test-1/child-1/child-1-1", "mydata11".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//
//         /********************************************** 查看节点的子节点列表 **********************************************/
//         // 指令格式：ls [-s] [-w] [-R] path
//         // 使用ls path可以查看节点的子节点列表，使用ls -s path可以查看节点的子节点列表以及该节点详细信息。使用ls -R path可以递归查看该节点的所有子节点信息。
//         // 使用ls -w path可以在查看节点的子节点的同时注册监听器，刚节点的子节点有变化时可以监听到，注意只能监听一次。
//
//         // 查看节点的子节点列表
//         List<String> children1 = zooKeeper.getChildren("/zk-test-1", false);
//         LOGGER.info("节点/zk-test-1的子节点是：{}", children1);
//
//         // 查看节点的子节点列表以及该节点详细信息
//         Stat stat = new Stat();
//         List<String> children2 = zooKeeper.getChildren("/zk-test-1", false, stat);
//         LOGGER.info("节点/zk-test-1的详细信息是：{}", stat);
//         LOGGER.info("节点/zk-test-1的子节点是：{}", children2);
//
//
//
//         TimeUnit.SECONDS.sleep(30);
//
//         // 测试完毕删除数据
//         zooKeeper.delete("/zk-test-1/child-1/child-1-1", -1);
//         zooKeeper.delete("/zk-test-1/child-1", -1);
//         zooKeeper.delete("/zk-test-1/child-2", -1);
//         zooKeeper.delete("/zk-test-1/child-3", -1);
//         zooKeeper.delete("/zk-test-1", -1);
//
//     }
//
//     static class InitWatcher implements Watcher {
//         @Override
//         public void process(WatchedEvent event) {
//             Event.KeeperState state = event.getState();
//             System.out.println(state);
//             System.out.println(event);
//             // if (Event.KeeperState.SyncConnected == state) {
//             //     connectedSemaphore.countDown();
//             // }
//             CONNECTED_SEMAPHORE.countDown();
//             LOGGER.info("event:" + event);
//         }
//     }
// }
