package com.qjq.crawler.download.zk;

import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class Log4jHandler implements DataHandler{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	static String LOGGER_LEVEL = "logger.level";

	void changeLevel(Level level) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		Map<String, LoggerConfig> loggerMap = config.getLoggers();
		for(Map.Entry<String, LoggerConfig> entry : loggerMap.entrySet()){
			LoggerConfig loggerConfig = entry.getValue();
			loggerConfig.setLevel(level);
		}
		ctx.updateLoggers();
	}

	@Override
	public void handler(Properties properties,Properties curPro) {
		String curLev = curPro.getProperty(LOGGER_LEVEL);
		String levelStr = properties.getProperty(LOGGER_LEVEL);
		logger.warn("日志级别更改,当前级别:{},更新级别:{}", curLev,levelStr);
		if(!StringUtils.isEmpty(levelStr) && !levelStr.equals(curLev)){
			Level level = Level.toLevel(levelStr);
			this.changeLevel(level);
		}
	}
	
}
