// package com.curtis.zookeeper.base;
//
//
// import org.apache.zookeeper.*;
// import org.junit.Test;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import java.io.IOException;
// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.TimeUnit;
//
// /**
//  * @author curtis
//  * @desc 删除ZK节点
//  * @date 2021-02-27
//  * @email 397773935@qq.com
//  * @reference
//  */
// public class NodeDeleteTest {
//
//     private static final CountDownLatch CONNECTED_SEMAPHORE = new CountDownLatch(1);
//
//     private static final Logger LOGGER = LoggerFactory.getLogger(NodeDeleteTest.class);
//
//     /**
//      * 测试：删除节点
//      */
//     @Test
//     public void testDeleteNode() throws IOException, KeeperException, InterruptedException {
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
//         zooKeeper.create("/zk-test", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//         zooKeeper.create("/zk-test/child-1", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//         zooKeeper.create("/zk-test/child-2", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//
//         /********************************************** 删除指定路径的节点 **********************************************/
//         // 指令格式：delete [-v version] path
//         // 使用delete path删除指定节点。使用delete -v version path来删除指定版本的节点。需要注意的是无法使用delete删除父节点。
//
//         // 无法直接通过delete删除父节点
//         try {
//             zooKeeper.delete("/zk-test", -1);
//         } catch (KeeperException ke) {
//             // ke.printStackTrace();
//             LOGGER.info("异常：无法直接通过delete删除父节点：异常类：{},异常信息{}", ke.getClass(), ke.getMessage());
//         }
//
//         // 使用delete path删除节点
//         zooKeeper.delete("/zk-test/child-1", -1);
//
//         // 使用delete -v version path删除指定版本节点，如果版本不对则抛出异常
//         try {
//             zooKeeper.delete("/zk-test/child-2", 1);
//         } catch (KeeperException ke) {
//             // ke.printStackTrace();
//             LOGGER.info("异常：删除指定版本节点时，如果版本不对则抛出异常：异常类：{},异常信息{}", ke.getClass(), ke.getMessage());
//         }
//         zooKeeper.delete("/zk-test/child-2", 0);
//
//         // 删除所有子节点后可以删除父节点
//         zooKeeper.delete("/zk-test", -1);
//
//         TimeUnit.SECONDS.sleep(30);
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
