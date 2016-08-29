package com.qjq.data.process.zk;

import java.util.Properties;

import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * zk监听器
 * @since 0.1.0
 */
public class AppPropertiesZkDataListener implements IZkDataListener {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final AppPropertiesChangeListener listener;
	private Object lastData;
	
	public AppPropertiesZkDataListener(AppPropertiesChangeListener listener) {
		this.listener = listener;
	}

	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		logger.debug("发生节点删除事件: znode={}", dataPath);
		lastData = null;
		listener.onDataChange(new Properties());
	}
	
	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
		logger.debug("发生节点数据更新事件：znode={}, data={}", dataPath, data);
		if (lastData == data || (lastData != null && lastData.equals(data))) return;
		lastData = data;
		listener.onDataChange((Properties) data);
	}
	
}
