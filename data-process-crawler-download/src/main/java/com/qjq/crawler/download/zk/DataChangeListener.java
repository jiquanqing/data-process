package com.qjq.crawler.download.zk;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qjq.data.process.zk.AppPropertiesChangeListener;

public class DataChangeListener implements AppPropertiesChangeListener {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    ZkHandler zkHandler;

    public DataChangeListener(ZkHandler zkHandler) {
        super();
        this.zkHandler = zkHandler;
    }

    @Override
    public void onDataChange(Properties properties) {
        logger.info("properties data change : " + properties);
        Properties curPro = zkHandler.getProperties();
        zkHandler.refresh(properties);
        ZkParamContext.updateDynamicParams(properties);
        for (DataHandler handler : DataHandlerContext.getHandlers()) {
            handler.handler(properties, curPro);
        }
    }
}
