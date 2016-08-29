package com.qjq.data.process.zk;

import java.util.Collections;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * zk监听器
 * 
 * @since 0.1.0
 */
public class AppMasterZkListener implements IZkChildListener, IZkStateListener {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final DiamondClient diamondClient;
    private boolean master = false;
    private String myMasterNode;
    private AppMasterChangeListener listener;
    private KeeperState curState;

    public synchronized boolean isMaster() {
        return master;
    }

    public ZkClient getZkClient() {
        return diamondClient.getZkClient();
    }

    public synchronized void setLitener(AppMasterChangeListener listener) {
        logger.info("设置master监听器：{}", listener);
        this.listener = listener;
        if (listener != null) {
            listener.onMasterChange(master);
        }
    }

    public AppMasterZkListener(DiamondClient diamondClient) {
        this.diamondClient = diamondClient;
    }

    private synchronized void setMaster(boolean master) {
        if (this.master != master) {
            this.master = master;
            logger.info("master发生改变[{}]={}", myMasterNode, master);
            if (listener != null) {
                listener.onMasterChange(master);
            }
        }
    }

    private void procChildren(List<String> children) {
        String myNodeName = myMasterNode == null ? null : myMasterNode.substring(myMasterNode.lastIndexOf('/') + 1);
        logger.info("master children节点[{}] my={}, childs={}", diamondClient.getAppMasterNode(), myNodeName, children);
        String curMaster = null;
        boolean existMy = false;
        Collections.sort(children);
        for (String child : children) {
            if (child.length() == 10) {
                if (curMaster == null) {
                    curMaster = child;
                }
                if (child.equals(myNodeName)) {
                    existMy = true;
                }
            }
        }
        if (!existMy) {
            logger.info("当前master节点={}, 我的节点[{}]不存在，需要创建", curMaster, myNodeName);
            createMyMasterNode();
            return;
        }
        setMaster(curMaster.equals(myNodeName));
    }

    public synchronized void createMyMasterNode() {
        ZkClient zkClient = getZkClient();
        if (myMasterNode != null) {
            if (zkClient.exists(myMasterNode)) {
                return;
            }
            myMasterNode = null;
        }
        myMasterNode = zkClient.createEphemeralSequential(diamondClient.getAppMasterNode() + "/", zkClient.toString());
        logger.info("创建我的master节点：{}", myMasterNode);
        procChildren(zkClient.getChildren(diamondClient.getAppMasterNode()));
    }

    @Override
    public synchronized void handleChildChange(String parentPath, List<String> currentChilds) {
        String masterNode = diamondClient.getAppMasterNode();
        if (!parentPath.equals(masterNode)) {
            throw new IllegalArgumentException("不是master节点 " + masterNode + "!=" + parentPath);
        }
        procChildren(currentChilds);
    }

    @Override
    public synchronized void handleStateChanged(KeeperState state) throws Exception {
        if (state == curState) {
            return;
        }

        KeeperState old = curState;
        curState = state;
        logger.info("zk状态转变：{} -> {}", old, state);
        if (state == KeeperState.Disconnected) {
            setMaster(false);
        } else if (state == KeeperState.SyncConnected && old == KeeperState.Disconnected) {
            // 断开连接后，重新建立链接，重新处理master信息
            ZkClient zkClient = getZkClient();
            procChildren(zkClient.getChildren(diamondClient.getAppMasterNode()));
        }
    }

    @Override
    public void handleNewSession() throws Exception {
    }

    @Override
    public void handleSessionEstablishmentError(Throwable error) throws Exception {
    }
}
