package com.curtis.zookeeper.example.pubsub;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;


/**
 * @author curtis
 * @desc 发布订阅
 * @date 2021-02-27
 * @email 397773935@qq.com
 * @reference
 */
public class Publish {

    private String connectString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";

    private static final CountDownLatch COUNTDOWN_LATCH = new CountDownLatch(1);

    private static final String SWITCH = "/pubsub/switch";

    @Test
    public void publish() throws InterruptedException, IOException, KeeperException {

        ZooKeeper zooKeeper = new ZooKeeper(connectString, 5000, new InitWatcher());
        COUNTDOWN_LATCH.await();

        // 父节点不存在则创建
        // Return the stat of the node of the given path. Return null if no such a node exists.
        // Stat exists = zooKeeper.exists(parentPath, false);
        // if (exists == null) {
        //     String s = zooKeeper.create(parentPath + "/switch", "on".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //     System.out.println(s);
        // }

        // 模拟发布过程，修改指定节点的值
        for (int i = 0; i < 10; i++) {
            String switchData = i % 2 == 0 ? "off" : "on";
            Stat stat = zooKeeper.setData(SWITCH, switchData.getBytes(StandardCharsets.UTF_8), -1);
            if (stat != null) {
                System.out.println("发布成功,SWITCH发布值为：" + switchData);
            }
        }
    }

    static class InitWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                COUNTDOWN_LATCH.countDown();
            }
        }
    }
}
