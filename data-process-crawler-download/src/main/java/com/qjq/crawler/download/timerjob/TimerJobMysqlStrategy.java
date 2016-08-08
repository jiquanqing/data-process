package com.qjq.crawler.download.timerjob;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.broker.scheduler.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qjq.crawler.download.contier.DownLoadWorkQueue;
import com.qjq.crawler.download.contier.DownLoadWorkQueueManger;
import com.qjq.crawler.download.dao.mysql.CrawlerJobMapper;
import com.qjq.crawler.download.dao.redis.RedisStoreManger;
import com.qjq.crawler.download.domain.CrawlerJob;
import com.qjq.crawler.download.domain.TimerJobConfig;

@Service
public class TimerJobMysqlStrategy implements TimerJobStrategy {

    private static Logger logger = LoggerFactory.getLogger(TimerJobMysqlStrategy.class);

    @Value("${QueueWrtieMysqlDelayTime:3000}")
    private Long delay; //延时时间

    @Autowired
    DownLoadWorkQueueManger workQueueManger;
    @Autowired
    CrawlerJobMapper crawlerJobMapper;
    @Autowired
    RedisStoreManger redisStoreManger;

    @Override
    public void onTimerJob(TimerJobConfig timerJobConfig) {
        // 定时将队列里面的内容写入到mysql中 以及打出log 供后台查看
        System.out.println("我也是定时");
        Map<String, DownLoadWorkQueue> map = workQueueManger.getWorkQueue();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            DownLoadWorkQueue downLoadWorkQueue = map.get(key);

            String downKey = downLoadWorkQueue.getDownKey();
            String totKey = downLoadWorkQueue.getTotKey();
            String downNum = redisStoreManger.get(downKey);
            if (downNum != null) {
                CrawlerJob record = new CrawlerJob();
                record.setJobid(downLoadWorkQueue.getQueueName());
                record.setCrawlednum(Integer.valueOf(downNum));

                crawlerJobMapper.updateByPrimaryKeySelective(record);
            }

            logger.info("JobId={},tot={},down={}", key, redisStoreManger.get(totKey), downNum);
            System.out.println(key + ":" + redisStoreManger.get(totKey) + " " + downNum);
        }
    }

    @Override
    public Long getDelay() {
        return delay;
    }

}
