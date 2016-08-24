package com.qjq.crawler.download.jms.recvie;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.data.process.mqmodel.DownLoadMessage;
import org.data.process.utils.UtilJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.qjq.crawler.download.contier.DownLoadWorkQueue;
import com.qjq.crawler.download.contier.DownLoadWorkQueueManger;
import com.qjq.crawler.download.service.DownLoadService;

@Service
public class DownLoadJmsListenerStrategyImpl implements JmsListenerStrategy {

    private static Logger logger = LoggerFactory.getLogger(DownLoadJmsListenerStrategyImpl.class);

    private String jmsQueue = "Crawler_Service_DownLoad_Queue";

    @Autowired
    DownLoadService downLoadService;
    @Autowired
    DownLoadWorkQueueManger downLoadWorkQueueManger;

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
            Map<String, DownLoadWorkQueue> map = downLoadWorkQueueManger.getWorkQueue();
            if (!map.containsKey(downLoadMessage.getJobId())) { // 恢复内存下载队列数据
                downLoadWorkQueueManger.recoverWordQueue(downLoadMessage.getJobId());
            }
            DownLoadWorkQueue downLoadWorkQueue = map.get(downLoadMessage.getJobId());
            if (downLoadWorkQueue.getIsFinish()) {
                logger.info("此任务已经完成 jobId={}", downLoadMessage.getJobId());
                return;
            }
            Long sleep = downLoadMessage.getSleep();
            if (sleep == null)
                sleep = 1000l;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    logger.info("{}开始处理下载任务", Thread.currentThread().getName());
                    downLoadService.schudel(downLoadMessage);
                    logger.info("{}下载完成", Thread.currentThread().getName());
                }
            });
        }
    }

    public void setJmsQueue(String jmsQueue) {
        this.jmsQueue = jmsQueue;
    }

}
