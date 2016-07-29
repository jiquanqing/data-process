package com.qjq.crawler.download.contier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qjq.crawler.download.dao.redis.RedisStoreManger;

@Component
public class DownLoadWorkQueueManger {

    private Map<String, DownLoadWorkQueue> workQueue = new HashMap<String, DownLoadWorkQueue>();

    @Autowired
    RedisStoreManger redisStoreManger;

    public DownLoadWorkQueue addWorkQueue(String queueName, int queueSize) {
        DownLoadWorkQueue downLoadWorkQueue = new DownLoadWorkQueue(queueName, queueSize, -1);
        workQueue.put(queueName, downLoadWorkQueue);

        redisStoreManger.hset(downLoadWorkQueue.getTotKey(), 0, downLoadWorkQueue.getCountTimeOut());
        redisStoreManger.hset(downLoadWorkQueue.getDownKey(), 0, downLoadWorkQueue.getCountTimeOut());

        return downLoadWorkQueue;
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
