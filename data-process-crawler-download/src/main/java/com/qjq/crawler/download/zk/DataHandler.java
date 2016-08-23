package com.qjq.crawler.download.zk;

import java.util.Properties;

public interface DataHandler {
	/**
	 * 数据处理
	 * @param properties 更新后的
	 * @param curPro 当前的
	 */
	public void handler(Properties properties,Properties curPro);
}
