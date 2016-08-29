package com.qjq.data.process.zk;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;

import com.qjq.data.process.util.UtilCollection;
import com.qjq.data.process.util.UtilObj;
import com.qjq.data.process.util.UtilSys;



/**
 * 配置客户端接口
 * @since 0.1.0
 */
public abstract class AbstractDiamondClient implements AutoCloseable, DisposableBean {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	/** zk起始路径 */
	protected static final String zkRootPath = "/bubugao-diamond";
	protected String connectString;
	protected int connectionTimeout = 200;
	protected int sessionTimeout = 4000;
	protected Integer appId;
	protected String authInfo;
	protected final Map<String, String> userPwdMap = new LinkedHashMap<>();
	
	protected DiamondZkConnection zkConnection;
	protected ZkClient zkClient;
	
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public String getConnectString() {
		return connectString;
	}
	@Value("${diamond.zk.connectString}")
	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}
	@Value("${diamond.zk.connectionTimeout:200}")
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	@Value("${diamond.zk.sessionTimeout:4000}")
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	public int getSessionTimeout() {
		return sessionTimeout;
	}
	@Value("${diamond.zk.authInfo:}")
	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}
	public ZkClient getZkClient() {
		return zkClient;
	}
	
	@PostConstruct
	public void init() {
		logger.info("开始初始化zk客户端 connectString={}, connectionTimeout={}, sessionTimeout={}", connectString, connectionTimeout, sessionTimeout);
		if (authInfo != null && !authInfo.isEmpty()) {
			String[] tmp = authInfo.split(":", 2);
			userPwdMap.put(tmp[0], tmp.length < 2 ? "" : tmp[1]);
		}
		zkConnection = new DiamondZkConnection(connectString, sessionTimeout);
		zkConnection.setUserPwdMap(userPwdMap);
		zkClient = new ZkClient(zkConnection, connectionTimeout);
	}

	@PreDestroy
	@Override
	public void destroy() throws Exception {
		close();
	}
	@Override
	public void close() {
		logger.info("关闭zk连接：{}", zkClient);
		if (zkClient != null) zkClient.close();
	}
	
	
	public void addAuthInfo(final String user, final String pwd) {
		logger.info("增加认证信息：{}", user);
		zkClient.retryUntilConnected(new Callable<String>() {
			@Override
			public String call() throws Exception {
				byte[] auth = (user + ":" + (pwd == null ? "" : pwd)).getBytes(UtilSys.UTF_8);
				zkConnection.getZookeeper().addAuthInfo("digest", auth);
				return userPwdMap.put(user, pwd);
			}
		});
	}
	
	protected String createPersistent(final String path, final Object data, final ACL... acls) {
		logger.info("创建持久节点[{}], data={}", path, data);
		String result = zkClient.retryUntilConnected(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return zkConnection.getZookeeper().create(path, 
						getZkSerializer().serialize(data),
						UtilCollection.asList(acls), CreateMode.PERSISTENT);
			}
		});
		logger.info("创建持久节点[{}] 结果：{}", path, result);
		return result;
	}
	
	protected ZkSerializer getZkSerializer() {
		return UtilObj.getFieldValue(zkClient, "_zkSerializer");
	}
	protected String getAppNode(Integer appId) {
		AssertDiamondClient.checkApp(appId);
		return zkRootPath + "/app-" + appId;
	}
	protected String getAppEndpointNode(Integer appId) {
		return getAppNode(appId) + "/endpoint";
	}
	protected String getAppPropertiesNode(Integer appId) {
		return getAppNode(appId) + "/app.properties";
	}
	protected String getAppMasterNode(Integer appId) {
		return getAppNode(appId) + "/master";
	}
	public String getAppMasterNode() {
		return getAppMasterNode(appId);
	}
	
	/**
	 * 获取app的访问地址
	 * @param appId 目标app
	 * @param version 目标app对应的版本（x.y.z，x架构变化，y服务变更，z修复）自动寻找适配的最高版本
	 */
	public String getAppEndpoint(Integer appId, String version) {
		Map<String, List<String>> all = zkClient.readData(getAppEndpointNode(appId), true);
		if (all == null) return null;
		
		List<String> endpoints = all.get(version);
		return endpoints.isEmpty() ? null : endpoints.get(0);
	}
}
