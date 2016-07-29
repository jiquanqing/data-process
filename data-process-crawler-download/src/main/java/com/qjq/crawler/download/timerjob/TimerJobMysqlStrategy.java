package com.qjq.crawler.download.timerjob;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.download.contier.DownLoadWorkQueue;
import com.qjq.crawler.download.contier.DownLoadWorkQueueManger;
import com.qjq.crawler.download.dao.mysql.CrawlerJobMapper;
import com.qjq.crawler.download.dao.redis.RedisStoreManger;
import com.qjq.crawler.download.domain.CrawlerJob;
import com.qjq.crawler.download.domain.TimerJobConfig;

@Service
public class TimerJobMysqlStrategy implements TimerJobStrategy {

    @Autowired
    DownLoadWorkQueueManger workQueueManger;
    @Autowired
    CrawlerJobMapper crawlerJobMapper;
    @Autowired
    RedisStoreManger redisStoreManger;

    @Override
    public void onTimerJob(TimerJobConfig timerJobConfig) {
        // 定时将队列里面的内容写入到mysql中
        Map<String, DownLoadWorkQueue> map = workQueueManger.getWorkQueue();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            DownLoadWorkQueue downLoadWorkQueue = map.get(key);
            

            String downKey = downLoadWorkQueue.getDownKey();
            String downNum = redisStoreManger.get(downKey);
            if (downNum != null) {
                CrawlerJob record = new CrawlerJob();
                record.setJobid(downLoadWorkQueue.getQueueName());
                record.setCrawlednum(Integer.valueOf(downNum));
                
                crawlerJobMapper.updateByPrimaryKeySelective(record);
            }

        }
    }

}
