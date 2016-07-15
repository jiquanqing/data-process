package com.qjq.crawler.contier;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class DownLoadWorkQueue {

    private String queueName;
    private Long allTot;
    private Long downloadTot;

    private int Max_Queue_Size = 10000000;

    private Queue<String> workQueue;

    public DownLoadWorkQueue(String queueName, int queueSize) {
        this.queueName = queueName;
        Max_Queue_Size = queueSize;
        if (queueSize > Max_Queue_Size || queueSize <= 0)
            queueSize = Max_Queue_Size;

        workQueue = new ArrayBlockingQueue<String>(Max_Queue_Size);
    }

    public void incAllTot() {
        this.allTot++;
    }

    public void incDownloadTot() {
        this.downloadTot++;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Long getAllTot() {
        return allTot;
    }

    public void setAllTot(Long allTot) {
        this.allTot = allTot;
    }

    public Long getDownloadTot() {
        return downloadTot;
    }

    public void setDownloadTot(Long downloadTot) {
        this.downloadTot = downloadTot;
    }

    public int getMax_Queue_Size() {
        return Max_Queue_Size;
    }

    public void setMax_Queue_Size(int max_Queue_Size) {
        Max_Queue_Size = max_Queue_Size;
    }

    public Queue<String> getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(Queue<String> workQueue) {
        this.workQueue = workQueue;
    }

}
