package com.qjq.data.process.zk;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qjq.data.process.util.UtilObj;
import com.qjq.data.process.util.UtilSys;

/**
 * 
 * @since 0.1.0
 */
public class DiamondZkConnection extends ZkConnection {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final int sessionTimeOut;
    private Lock lock;
    private Map<String, String> userPwdMap = new LinkedHashMap<>();

    public void setUserPwdMap(Map<String, String> userPwdMap) {
        this.userPwdMap = userPwdMap;
    }

    public DiamondZkConnection(String zkServers, int sessionTimeOut) {
        super(zkServers, sessionTimeOut);
        this.sessionTimeOut = sessionTimeOut;
        lock = UtilObj.getFieldValue(this, "_zookeeperLock");
    }

    @Override
    public void connect(Watcher watcher) {
        lock.lock();
        try {
            ZooKeeper zk = getZookeeper();
            if (zk != null)
                throw new IllegalStateException("zk client has already been started");

            try {
                logger.debug("Creating new ZookKeeper instance to connect to " + getServers() + ".");
                zk = new ZooKeeper(getServers(), sessionTimeOut, watcher);
                if (!userPwdMap.isEmpty()) {
                    logger.info("设置zk认证信息：{}", userPwdMap.keySet());
                    for (Entry<String, String> entry : userPwdMap.entrySet()) {
                        String pwd = entry.getValue();
                        if (pwd == null)
                            pwd = "";
                        zk.addAuthInfo("digest", (entry.getKey() + ":" + pwd).getBytes(UtilSys.UTF_8));
                    }
                }
            } catch (IOException e) {
                throw new ZkException("Unable to connect to " + getServers(), e);
            }
            UtilObj.setFieldValue(this, "_zk", zk);
        } finally {
            lock.unlock();
        }
    }

}
