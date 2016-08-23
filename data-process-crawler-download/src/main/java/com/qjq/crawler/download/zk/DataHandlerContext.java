package com.qjq.crawler.download.zk;

import java.util.ArrayList;
import java.util.List;

/**
 * hanlder上下文
 * @author bbgds
 *
 */
public class DataHandlerContext {
	final static List<DataHandler> handlerList = new ArrayList<>();
	static{
		handlerList.add(new Log4jHandler());
		handlerList.add(new ZkParamHandler());
	}
	
	public static List<DataHandler> getHandlers(){
		return handlerList;
	}
}
