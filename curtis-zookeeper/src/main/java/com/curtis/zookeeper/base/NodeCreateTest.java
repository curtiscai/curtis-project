package com.curtis.zookeeper.base;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

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

    @Test
    public void testCreateNode() throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181",
                5000, new InitWatcher());
        try {
            CONNECTED_SEMAPHORE.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Zookeeper Session Established.");
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
