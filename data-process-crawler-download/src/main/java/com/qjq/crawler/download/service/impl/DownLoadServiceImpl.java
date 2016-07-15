package com.qjq.crawler.download.service.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.data.process.mqmodel.DownLoadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.contier.DownLoadWorkQueue;
import com.qjq.crawler.contier.DownLoadWorkQueueManger;
import com.qjq.crawler.download.service.DownLoadService;
import com.qjq.crawler.jms.MessageSender;
import com.qjq.crawler.utils.HttpRequest;
import com.qjq.crawler.utils.UidUtils;
import com.qjq.crawler.utils.UtilJson;

@Service
public class DownLoadServiceImpl implements DownLoadService {

    private static Logger logger = LoggerFactory.getLogger(DownLoadServiceImpl.class);

    @Autowired
    MessageSender messageSender;

    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(20, 100, 60, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory());

    @Autowired
    DownLoadWorkQueueManger workQueueManger;

    @Override
    public void addSeed(String url, String jobName) {
        DownLoadWorkQueue queue = null;
        if (workQueueManger.getWorkQueue().containsKey(jobName)) {
            queue = workQueueManger.getWorkQueue().get(jobName);
        } else {
            queue = workQueueManger.addWorkQueue(jobName, -1);
        }
        queue.incAllTot();
        DownLoadMessage downLoadMessage = new DownLoadMessage();
        downLoadMessage.setUrl(url);
        downLoadMessage.setJobName(jobName);
        messageSender.hander(UtilJson.writerWithDefaultPrettyPrinter(downLoadMessage));
    }

    public void schudel(DownLoadMessage message) {
        DownLoadWorkQueue downLoadWorkQueue = workQueueManger.getWorkQueue().get(message.getJobName());
        String uid = UidUtils.getUid(message.getUrl());
        String content = null;
        try {
            content = HttpRequest.sendGet(message.getUrl(), message.getParams());
        } catch (Exception e) {
            try {
                if (content == null || content.equals("")) {
                    content = HttpRequest.sendPost(message.getUrl(), message.getParams());
                }
            } catch (Exception e2) {
                content = "url" + message.getUrl() + "下载失败";
                logger.error("url={}，下载失败", message.getUrl());
            }
        }
        downLoadWorkQueue.incDownloadTot();
    }

    public void extendsUrl(String content, String jobName) {

    }
}
