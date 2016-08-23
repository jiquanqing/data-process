package com.qjq.crawler.download.zk;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class ZkParamHandler implements DataHandler {

    private static final Logger logger = LoggerFactory.getLogger(ZkParamHandler.class);

    @Override
    public void handler(Properties remotePro, Properties localPro) {
        ZkParam[] zkParams = ZkParam.values();
        synchronized (ZkParamHandler.class) {
            for (ZkParam zkParam : zkParams) {
                    String remoteValue = remotePro.getProperty(zkParam.getKey());
                    String localValue = ZkParamContext.getParamValue(zkParam);
                    if (StringUtils.isEmpty(remoteValue) && StringUtils.isEmpty(localValue)) {
                        logger.info("zkParam={}未配置", zkParam.getDesc());
                    } else if (StringUtils.isEmpty(remoteValue)) {
                        // zk服务器删除了该值,本地应该删除该值
                        ZkParamContext.deleteParam(zkParam);
                        logger.info("删除本地zkParam={}", zkParam.getDesc());
                    } else if (StringUtils.isEmpty(localValue)) {
                        // zk服务器新增该值，本地应该新增
                        ZkParamContext.updateParam(zkParam, remoteValue);
                        logger.info("新增本地zkParam={}；值为{}", zkParam.getDesc(), remoteValue);
                    } else {
                        // zk服务器与本地都配置了该值
                        if (localValue.equals(remoteValue)) {
                            logger.info("本地zkParam={}值为{}，保持不变。", zkParam.getDesc(), localValue, remoteValue);
                        } else {
                            ZkParamContext.updateParam(zkParam, remoteValue);
                            logger.info("本地zkParam={}；变更前为({})，变更后为({})", zkParam.getDesc(), localValue, remoteValue);
                        }
                    }
                }
        }
    }

}
