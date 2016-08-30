package com.qjq.data.process.zk;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.DataTree;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import com.qjq.data.process.util.UtilCollection;

/**
 * 配置管理接口
 * @since 0.1.0
 */
public class DiamondAdminClient extends AbstractDiamondClient {
	private Map<String, String> userPwdMap = new HashMap<>();
	
	public void setUserPwdMap(Map<String, String> userPwdMap) {
		this.userPwdMap = userPwdMap;
	}
	
	
	private Id getIdByUser(String user) {
		String pwd = userPwdMap.get(user);
		if (pwd == null) throw new IllegalArgumentException("无法找到用户/密码信息, user=" + user);
		try {
			return new Id("digest", DigestAuthenticationProvider.generateDigest(user + ':' + pwd));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	private ACL getRootACL() {
		return new ACL(ZooDefs.Perms.ALL, getIdByUser("root"));
	}
	private ACL getAppAdminACL(Integer appId) {
		return new ACL(ZooDefs.Perms.ALL ^ ZooDefs.Perms.ADMIN, getIdByUser("appAdmin" + appId));
	}
	private ACL getAppServerACL(Integer appId) {
		return new ACL(ZooDefs.Perms.READ, getIdByUser("appServer" + appId));
	}
	/** 任何通过认证的只读权限 */
	private ACL getClientACL() {
		return new ACL(ZooDefs.Perms.READ, getIdByUser("client"));
	}
	
	public List<ACL> getACL(final String path, final Stat stat) {
		return zkClient.retryUntilConnected(new Callable<List<ACL>>() {
			@Override
			public List<ACL> call() throws Exception {
				return zkConnection.getZookeeper().getACL(path, stat);
			}
		});
	}
	public void setACL(final String path, final List<ACL> acls, final Stat stat) {
		Stat ret = zkClient.retryUntilConnected(new Callable<Stat>() {
			@Override
			public Stat call() throws Exception {
				return zkConnection.getZookeeper().setACL(path, acls, stat.getAversion());
			}
		});
		DataTree.copyStat(ret, stat);
	}
	
	public void readyRootNode() {
		if (!zkClient.exists(zkRootPath)) createPersistent(zkRootPath, null, getRootACL());
	}
	public void readyAppNode(Integer appId) {
		final String node = getAppNode(appId);
		if (!zkClient.exists(node)) {
			readyRootNode();
			createPersistent(node, null, getRootACL(), getAppAdminACL(appId));
		}
	}
	public void readyAppMasterNode(Integer appId) {
		final String node = getAppMasterNode(appId);
		if (!zkClient.exists(node)) {
			readyAppNode(appId);
			createPersistent(node, null, getRootACL(), getAppAdminACL(appId),
					new ACL(ZooDefs.Perms.CREATE | ZooDefs.Perms.READ, getIdByUser("appServer" + appId)),
					getClientACL());
		}
	}
	
	/**
	 * 设置应用的app.properties内容<br>
	 * ACL: root(ALL), appAdmin${appId}(rw), 本服务读appServer${appId}(r), 公共读client(r)
	 * @param appId
	 * @param properties
	 */
	public void setAppProperties(Integer appId, final Properties properties) {
		logger.info("设置appId={}, properties={}", appId, properties);
		
		final String node = getAppPropertiesNode(appId);
		if (zkClient.exists(node)) {
			final Stat stat = new Stat();
			Properties currentProps = zkClient.readData(node, stat);
			if (currentProps.equals(properties)) return;
			
			zkClient.writeData(node, properties, stat.getVersion());
			logger.info("appId={}, app.properties=\n{}\n替换为：\n{}\n", appId, currentProps, properties);
		} else {
			readyAppNode(appId);
			createPersistent(node, properties, getRootACL(), getAppAdminACL(appId), getAppServerACL(appId));
		}
	}
	
	/**
	 * 增加app版本对应地址
	 * @param version 必须是完整的x.y.z
	 */
	public void setAppEndpoints(Integer appId, String version, String... endpoints) {
		AssertDiamondClient.checkAppVer(appId, version, 3);
		String node = getAppEndpointNode(appId);
		List<String> list = UtilCollection.asList(endpoints);
		logger.info("设置endpoint：appId={}, ver={}, endpoints={}", appId, version, list);
		
		if (zkClient.exists(node)) {
			Stat stat = new Stat();
			Map<String, List<String>> all = zkClient.readData(node, stat);
			List<String> old = all.put(version, list);
			if (list.equals(old)) return;
			
			zkClient.writeData(node, all, stat.getVersion());
		} else {
			Map<String, List<String>> all = new LinkedHashMap<>();
			all.put(version, list);
			readyAppNode(appId);
			createPersistent(node, all, getRootACL(), getAppAdminACL(appId), getAppServerACL(appId), getClientACL());
		}
	}
	
	/**
	 * 删除app版本对应的所有地址
	 * @param version 必须是完整的x.y.z
	 */
	public void delAppEndpoint(Integer appId, String version) {
		AssertDiamondClient.checkAppVer(appId, version, 3);
		logger.info("删除endpoint：appId={}, version={}", appId, version);
		setAppEndpoints(appId, version);
	}
	
}
