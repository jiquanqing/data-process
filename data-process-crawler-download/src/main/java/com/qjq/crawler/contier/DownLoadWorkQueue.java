package com.qjq.crawler.contier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qjq.crawler.dao.redis.RedisStoreManger;

@Component
public class DownLoadWorkQueue {

    // 计数器失效时间默认为10天
    private int countTimeOut = 60 * 60 * 24 * 10;
    @Autowired
    RedisStoreManger redisStoreManger;

    private String queueName;
    private String totKey = queueName + "#tot";
    private String downKey = queueName + "#down";

    private int Max_Queue_Size = 10000000;

    public DownLoadWorkQueue(String queueName, int queueSize, int countTimeOut) {
        this.queueName = queueName;
        Max_Queue_Size = queueSize;
        if (queueSize > Max_Queue_Size || queueSize <= 0)
            queueSize = Max_Queue_Size;
        if (countTimeOut > 0)
            this.countTimeOut = countTimeOut;

        redisStoreManger.hset(totKey, 0, countTimeOut);

    }

    public void incAllTot() {
        redisStoreManger.incr(totKey);
    }

    public void incDownloadTot() {
        redisStoreManger.incr(downKey);
    }

    public int getAllTot() {
        String value = redisStoreManger.get(totKey);
        if (value != null)
            return Integer.valueOf(value);
        return -1;
    }

    public int getDownloadTot() {
        String value = redisStoreManger.get(downKey);
        if (value != null)
            return Integer.valueOf(value);
        return -1;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public int getMax_Queue_Size() {
        return Max_Queue_Size;
    }

    public void setMax_Queue_Size(int max_Queue_Size) {
        Max_Queue_Size = max_Queue_Size;
    }

}
