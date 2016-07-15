package com.qjq.crawler.jms.recvie;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.data.process.mqmodel.DownLoadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.qjq.crawler.download.service.DownLoadService;
import com.qjq.crawler.utils.UtilJson;

@Service
public class DownLoadJmsListenerStrategyImpl implements JmsListenerStrategy {

    private static Logger logger = LoggerFactory.getLogger(DownLoadJmsListenerStrategyImpl.class);

    private String jmsQueue = "down-queue";

    @Autowired
    DownLoadService downLoadService;

    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(20, 100, 60, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory());

    @Override
    public String getJmsQueue() {
        return jmsQueue;
    }

    @Override
    public void onMessage(String text) {
        logger.info(jmsQueue + "接收到下载mq消息:", text);
        if (!StringUtils.isEmpty(text)) {
            final DownLoadMessage downLoadMessage = UtilJson.readValue(text, DownLoadMessage.class);
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    downLoadService.schudel(downLoadMessage);
                }
            });
        }
    }

    public void setJmsQueue(String jmsQueue) {
        this.jmsQueue = jmsQueue;
    }

}
