package com.curtis.zookeeper.base;

import org.apache.zookeeper.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class NodeOperatorTest {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) {
        System.out.println(String.format("%010d",12));
    }

    @Test
    public void test() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.2.101:2181,192.168.2.102:2181,192.168.2.103:2181",
                5000, new NodeOperatorTest.ZookeeperWatcher());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Zookeeper Session Established.");

        // String createNode = zooKeeper.create("/zk-node-presistent", "data1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

        for (int i = 0; i < 100; i++) {
            try {
                zooKeeper.create("/zk-node-1/zk-node-presistent-", "data1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test2() throws IOException, KeeperException, InterruptedException {


        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                try {
                    test();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        while (Thread.activeCount() > 1){
            Thread.yield();
        }

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
