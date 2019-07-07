package com.jee.rpc.registry;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 服务注册 ，ZK 在该架构中扮演了“服务注册表”的角色，
 *     用于注册所有服务器的地址与端口，并对客户端提供服务发现的功能
 *
 * @author jeeLearner
 * @date 2019/7/6
 */
public class ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    /**
     * new CountDownLatch(1) 实例化一个倒计数器，count指定计数个数
     * countDown() 计数减一
     * await()  等待，当计数减到0时，所有线程并行执行
     */
    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    /**
     * 创建zookeeper链接
     *
     * @param data
     */
    public void register(String data) {
        if (data != null){
            ZooKeeper zk = connectServer();
            if (zk != null){
                createNode(zk, data);
            }
        }
    }

    /**
     * 创建zookeeper链接，监听
     *
     * @return
     */
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(
                    registryAddress,
                    Constant.ZK_SESSION_TIMEOUT,
                    new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                            if (event.getState() == Event.KeeperState.SyncConnected) {
                                latch.countDown();
                            }
                        }
                    });
            latch.await();
        } catch (Exception e){
            LOGGER.error("", e);
        }
        return zk;
    }

    /**
     * 创建节点
     *
     * @param zk
     * @param data
     */
    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            //创建注册节点path
            Stat exists = zk.exists(Constant.ZK_REGISTRY_PATH, null);
            if (exists == null){
                Thread.sleep(2000);
                zk.create(Constant.ZK_REGISTRY_PATH, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            //创建注册节点path下的data节点path
            //path: /registry/data0000000001
            Thread.sleep(2000);
            String path = zk.create(Constant.ZK_DATA_PATH, bytes, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            //String path = zk.create(Constant.ZK_DATA_PATH, bytes, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            LOGGER.info("create zookeeper node ({} => {})", path, data);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}

