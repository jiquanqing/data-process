package com.qjq.data.process.zk;

import java.util.Properties;

/**
 * app地址变更监听器
 * @since 0.1.0
 */
public interface AppPropertiesChangeListener {

	void onDataChange(Properties properties);
}
