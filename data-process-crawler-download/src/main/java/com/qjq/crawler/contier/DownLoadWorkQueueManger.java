package com.qjq.crawler.contier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DownLoadWorkQueueManger {

    private Map<String, DownLoadWorkQueue> workQueue = new HashMap<String, DownLoadWorkQueue>();

    public DownLoadWorkQueue addWorkQueue(String queueName, int queueSize) {
        DownLoadWorkQueue downLoadWorkQueue = new DownLoadWorkQueue(queueName, queueSize);
        workQueue.put(queueName, downLoadWorkQueue);
        return downLoadWorkQueue;
    }

    public Map<String, DownLoadWorkQueue> getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(Map<String, DownLoadWorkQueue> workQueue) {
        this.workQueue = workQueue;
    }

}
