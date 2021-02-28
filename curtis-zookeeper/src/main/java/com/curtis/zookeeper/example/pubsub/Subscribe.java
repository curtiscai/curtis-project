package com.curtis.zookeeper.example.pubsub;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * @author curtis
 * @desc 发布订阅
 * @date 2021-02-27
 * @email 397773935@qq.com
 * @reference
 */
public class Subscribe {

    private String connectString = "192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181";

    private static final CountDownLatch COUNTDOWN_LATCH = new CountDownLatch(1);

    private String parentPath = "/pubsub";
    private static final String SWITCH = "/pubsub/switch";

    private static Stat STAT = new Stat();

    private static ZooKeeper zooKeeper = null;

    @Test
    public void publish() throws InterruptedException, IOException, KeeperException {

        zooKeeper = new ZooKeeper(connectString, 5000, new InitWatcher());
        COUNTDOWN_LATCH.await();

        // 模拟订阅过程
        zooKeeper.register(new SubscribeWatcher());

        byte[] data = zooKeeper.getData(SWITCH, new SubscribeWatcher(), STAT);
        String dataStr = new String(data, StandardCharsets.UTF_8);
        System.out.println("初次获取SWITCH的值：" + dataStr);

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    static class InitWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                COUNTDOWN_LATCH.countDown();
            }
        }
    }

    static class SubscribeWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            if (Event.EventType.NodeDataChanged == event.getType()) {
                // 监听到值改变就要去更新值
                String path = event.getPath();
                try {
                    byte[] data = zooKeeper.getData(path, true, STAT);
                    String dataStr = new String(data, StandardCharsets.UTF_8);
                    System.out.println("订阅到SWITCH的最新值：" + dataStr);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
