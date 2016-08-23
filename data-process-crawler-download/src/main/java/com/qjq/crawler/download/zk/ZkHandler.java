package com.qjq.crawler.download.zk;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bubugao.diamond.client.AssertDiamondClient;
import com.bubugao.diamond.client.DiamondAdminClient;
import com.bubugao.diamond.client.DiamondClient;

public class ZkHandler {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	DiamondAdminClient adminClient;
	
	@Autowired
	DiamondClient diamondClient;
	
	@Value("${server.appId}")
	Integer appId;
	
	String zkRootPath = "/data-process-diamond";
	
	String zkProperties = "zkData.properties";
	
	@Value("${diamond.zk.root}")
	String root;
	
	@Value("${diamond.zk.appAdmin}")
	String appAdmin;
	
	@Value("${diamond.zk.authInfo}")
	String appServer;
	
	Map<String,String> userPwdMap = null;
	
	Map<String,String> zkData = null;
	
	Properties properties = null;

	@PostConstruct
	public void init() {
		if (userPwdMap == null) {
			userPwdMap = new HashMap<>();
		}
		userPwdMap.put(root.split(":")[0], root.split(":")[1]);
		userPwdMap.put(appAdmin.split(":")[0], appAdmin.split(":")[1]);
		userPwdMap.put("appServer" + appId, appServer.split(":")[1]);
		adminClient.setUserPwdMap(userPwdMap);
		adminClient.addAuthInfo("root",userPwdMap.get("root"));
		adminClient.addAuthInfo(appAdmin + appId,userPwdMap.get(appAdmin + appId));
		adminClient.init();
		//properties = UtilIO.loadProperties(zkProperties);
		properties = GlobalUtilIO.loadProperties(zkProperties, "UTF-8");
		
		Properties currentZkData = adminClient.getZkClient().readData(getAppPropertiesNode(appId),true);
		if(currentZkData != null && !currentZkData.isEmpty() && !currentZkData.equals(properties)){
			properties.putAll(currentZkData);
		}
		adminClient.setAppProperties(appId, properties);
		//初始化ZkParamsContext
		initZkParams(properties);
		// 准备master节点
		adminClient.readyAppMasterNode(appId);
		
		diamondClient.setAppPropertiesChangeListener(appId, new DataChangeListener(this));
		logger.info("zk节点数据:" + properties);
	}
	
	public void set(String key,String value){
		properties.put(key, value);
		adminClient.setAppProperties(appId, properties);
	}
	
	public String get(String key){
		return properties.getProperty(key);
	}
	
	public void refresh(Properties properties){
		this.properties = properties;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
		adminClient.setAppProperties(appId, properties);
	}

	public String getAppNode(Integer appId) {
		AssertDiamondClient.checkApp(appId);
		return zkRootPath + "/app-" + appId;
	}
	public String getAppEndpointNode(Integer appId) {
		return getAppNode(appId) + "/endpoint";
	}
	public String getAppPropertiesNode(Integer appId) {
		return getAppNode(appId) + "/app.properties";
	}
	public String getAppMasterNode(Integer appId) {
		return getAppNode(appId) + "/master";
	}
	public String getAppMasterNode() {
		return getAppMasterNode(appId);
	}
	
	private void initZkParams(Properties properties){
	    if(properties == null){
	        return;
	    }
	    ZkParamContext.updateDynamicParams(properties);
	    ZkParamContext.clearParam();
	    ZkParam[] paramArray = ZkParam.values();
	    for (ZkParam zkParam : paramArray) {
	        String paramValue = properties.getProperty(zkParam.getKey());
	        if(StringUtils.isEmpty(paramValue)){
	            ZkParamContext.updateParam(zkParam, paramValue);
	            logger.info("初始化zkParam({})为{}", zkParam.getDesc(), paramValue);
	        }
            }
	}
}
