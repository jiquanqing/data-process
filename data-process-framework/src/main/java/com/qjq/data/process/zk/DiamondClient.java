package com.qjq.data.process.zk;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.qjq.data.process.util.UtilSys;

/**
 * 配置客户端接口
 * 
 * @since 0.1.0
 */
public class DiamondClient extends AbstractDiamondClient {
    private final Map<String, AppEndpointZkDataListener> endpointChangeListenerMap = new ConcurrentHashMap<>();
    private final Map<String, AppPropertiesZkDataListener> propertiesChangeListenerMap = new ConcurrentHashMap<>();
    private AppMasterZkListener appMasterZkListener;

    @Override
    public void close() {
        logger.info("关闭DiamondClient appId={}, {}", getAppId(), this);

        endpointChangeListenerMap.clear();
        propertiesChangeListenerMap.clear();
        if (appMasterZkListener != null) {
            zkClient.unsubscribeStateChanges(appMasterZkListener);
            zkClient.unsubscribeChildChanges(getAppMasterNode(), appMasterZkListener);
            appMasterZkListener = null;
        }
        super.close();
    }

    /**
     * 设置app地址变更监听
     * 
     * @param appId 目标app
     * @param listener 监听器
     */
    public void setAppEndpointChangeListener(Integer appId, AppEndpointChangeListener listener) {
        String zkNode = getAppEndpointNode(appId);
        logger.info("设置app.endpoint改变监听器：appId={}, zkNode={}, {}", appId, zkNode, listener);
        AppEndpointZkDataListener zkListener = new AppEndpointZkDataListener(listener);
        AppEndpointZkDataListener old = endpointChangeListenerMap.put(zkNode, zkListener);
        if (old != null)
            zkClient.unsubscribeDataChanges(zkNode, old);
        zkClient.subscribeDataChanges(zkNode, zkListener);
    }

    /** app.properties */
    public Properties getAppProperties(Integer appId) {
        return zkClient.readData(getAppPropertiesNode(appId), true);
    }

    /** 获取app.properties的内容 */
    public String getAppPropertiesContent(Integer appId) {
        Properties data = zkClient.readData(getAppPropertiesNode(appId), true);
        logger.info("读取app.properties配置：{}", data == null ? null : data.size());
        if (data == null)
            return "";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            data.store(new BufferedWriter(new OutputStreamWriter(out, UtilSys.UTF_8), 2048), "diamond: " + appId);
            return out.toString("UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /** 设置app.properties内容变更监听 */
    public void setAppPropertiesChangeListener(Integer appId, AppPropertiesChangeListener listener) {
        String zkNode = getAppPropertiesNode(appId);
        logger.info("设置app.properties改变监听器：appId={}, zkNode={}, {}", appId, zkNode, listener);
        AppPropertiesZkDataListener zkListener = new AppPropertiesZkDataListener(listener);
        AppPropertiesZkDataListener old = propertiesChangeListenerMap.put(zkNode, zkListener);
        if (old != null)
            zkClient.unsubscribeDataChanges(zkNode, old);
        zkClient.subscribeDataChanges(zkNode, zkListener);
    }

    private synchronized void readAppMaster() {
        if (appMasterZkListener == null) {
            appMasterZkListener = new AppMasterZkListener(this);
            zkClient.subscribeStateChanges(appMasterZkListener);
            zkClient.subscribeChildChanges(getAppMasterNode(), appMasterZkListener);
            appMasterZkListener.createMyMasterNode();
        }
    }

    public boolean isAppMaster() {
        readAppMaster();
        return appMasterZkListener.isMaster();
    }

    public void setAppMasterChangeListener(AppMasterChangeListener listener) {
        readAppMaster();
        appMasterZkListener.setLitener(listener);
    }
}
