package com.qjq.crawler.download.contier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class DownLoadWorkQueue {

    // 计数器失效时间默认为10天
    private int countTimeOut = 60 * 60 * 24 * 10;
    private String queueName;
    private String totKey = "";
    private String downKey = "";
    private Boolean isFinish = false;

    private int Max_Queue_Size = 10000000;

    public DownLoadWorkQueue() {

    }

    public DownLoadWorkQueue(String queueName, int queueSize, int countTimeOut) {
        this.queueName = queueName;
        Max_Queue_Size = queueSize;
        if (queueSize > Max_Queue_Size || queueSize <= 0)
            queueSize = Max_Queue_Size;
        if (countTimeOut > 0)
            this.setCountTimeOut(countTimeOut);

        this.setTotKey(queueName + "#tot");
        this.setDownKey(queueName + "#down");
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

    public int getCountTimeOut() {
        return countTimeOut;
    }

    public void setCountTimeOut(int countTimeOut) {
        this.countTimeOut = countTimeOut;
    }

    public String getTotKey() {
        return totKey;
    }

    public void setTotKey(String totKey) {
        this.totKey = totKey;
    }

    public String getDownKey() {
        return downKey;
    }

    public void setDownKey(String downKey) {
        this.downKey = downKey;
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Boolean isFinish) {
        this.isFinish = isFinish;
    }
}
