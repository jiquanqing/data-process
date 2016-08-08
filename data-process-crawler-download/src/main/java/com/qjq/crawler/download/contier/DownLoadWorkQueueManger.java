package com.qjq.crawler.download.contier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qjq.crawler.download.dao.mysql.CrawlerJobMapper;
import com.qjq.crawler.download.dao.redis.RedisStoreManger;
import com.qjq.crawler.download.domain.CrawlerJob;

@Component
public class DownLoadWorkQueueManger {

    private Map<String, DownLoadWorkQueue> workQueue = new HashMap<String, DownLoadWorkQueue>();

    @Autowired
    CrawlerJobMapper crawlerJobMapper;
    @Autowired
    RedisStoreManger redisStoreManger;

    public DownLoadWorkQueue addWorkQueue(String queueName, int queueSize) {
        DownLoadWorkQueue downLoadWorkQueue = new DownLoadWorkQueue(queueName, queueSize, -1);
        workQueue.put(queueName, downLoadWorkQueue);

        redisStoreManger.hset(downLoadWorkQueue.getTotKey(), 0, downLoadWorkQueue.getCountTimeOut());
        redisStoreManger.hset(downLoadWorkQueue.getDownKey(), 0, downLoadWorkQueue.getCountTimeOut());

        return downLoadWorkQueue;
    }

    // 从redis中恢复内存数据
    public void recoverWordQueue(String jobId) {
        DownLoadWorkQueue downLoadWorkQueue = new DownLoadWorkQueue(jobId, -1, -1);
        workQueue.put(jobId, downLoadWorkQueue);

        Integer tot = Integer.valueOf(redisStoreManger.get(downLoadWorkQueue.getTotKey()));
        Integer down = Integer.valueOf(redisStoreManger.get(downLoadWorkQueue.getDownKey()));

        redisStoreManger.hset(downLoadWorkQueue.getTotKey(), tot, downLoadWorkQueue.getCountTimeOut());
        redisStoreManger.hset(downLoadWorkQueue.getDownKey(), down, downLoadWorkQueue.getCountTimeOut());

    }

    // 从mysql中恢复数据 但是此种方式会有问题，因为redis中的数据是延时5s写入数据库的 不到万不得已不采用
    public void recoverWordQueueByMysql(String jobId) {
        DownLoadWorkQueue downLoadWorkQueue = new DownLoadWorkQueue(jobId, -1, -1);
        workQueue.put(jobId, downLoadWorkQueue);

        CrawlerJob crawlerJob = crawlerJobMapper.selectByPrimaryKey(jobId);

        Integer down = crawlerJob.getCrawlednum();
        Integer tot = down + 1;
        
        redisStoreManger.hset(downLoadWorkQueue.getTotKey(), tot, downLoadWorkQueue.getCountTimeOut());
        redisStoreManger.hset(downLoadWorkQueue.getDownKey(), down, downLoadWorkQueue.getCountTimeOut());
    }

    public Map<String, DownLoadWorkQueue> getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(Map<String, DownLoadWorkQueue> workQueue) {
        this.workQueue = workQueue;
    }

    public void incAllTot(String queueName) {
        DownLoadWorkQueue downLoadWorkQueue = workQueue.get(queueName);
        if (downLoadWorkQueue != null)
            redisStoreManger.incr(downLoadWorkQueue.getTotKey());
    }

    public void incDownloadTot(String queueName) {
        DownLoadWorkQueue downLoadWorkQueue = workQueue.get(queueName);
        if (downLoadWorkQueue != null)
            redisStoreManger.incr(downLoadWorkQueue.getDownKey());
    }

    public int getAllTot(String queueName) {
        DownLoadWorkQueue downLoadWorkQueue = workQueue.get(queueName);
        if (downLoadWorkQueue != null) {
            String value = redisStoreManger.get(downLoadWorkQueue.getTotKey());
            if (value != null)
                return Integer.valueOf(value);
        }
        return -1;
    }

    public int getDownloadTot(String queueName) {
        DownLoadWorkQueue downLoadWorkQueue = workQueue.get(queueName);
        if (downLoadWorkQueue != null) {
            String value = redisStoreManger.get(downLoadWorkQueue.getDownKey());
            if (value != null)
                return Integer.valueOf(value);
        }
        return -1;
    }

}
