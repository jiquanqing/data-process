package com.qjq.crawler.download.service.impl;

import org.data.process.model.HtmlObject;
import org.data.process.mqmodel.DownLoadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.contier.DownLoadWorkQueue;
import com.qjq.crawler.contier.DownLoadWorkQueueManger;
import com.qjq.crawler.dao.mongo.HtmlRepository;
import com.qjq.crawler.dao.redis.RedisStoreManger;
import com.qjq.crawler.download.service.DownLoadService;
import com.qjq.crawler.jms.send.MessageSender;
import com.qjq.crawler.utils.HttpRequest;
import com.qjq.crawler.utils.UidUtils;
import com.qjq.crawler.utils.UtilJson;

@Service
public class DownLoadServiceImpl implements DownLoadService {

    private static Logger logger = LoggerFactory.getLogger(DownLoadServiceImpl.class);

    @Autowired
    MessageSender messageSender;
    @Autowired
    HtmlRepository htmlRepository;

    @Autowired
    DownLoadWorkQueueManger workQueueManger;
    @Autowired
    RedisStoreManger redisStoreManger;

    private int uidTimeOut = 60 * 60 * 24 * 10;

    @Override
    public void addSeed(String url, String jobId) {

        String uid = UidUtils.getUid(url);
        try {
            if (redisStoreManger.existByRedis(uid, null)) {
                logger.info("uid = {},已经存在,skip", uid);
                return;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        DownLoadWorkQueue queue = null;
        if (workQueueManger.getWorkQueue().containsKey(jobId)) {
            queue = workQueueManger.getWorkQueue().get(jobId);
        } else {
            queue = workQueueManger.addWorkQueue(jobId, -1);
        }
        queue.incAllTot();
        DownLoadMessage downLoadMessage = new DownLoadMessage();
        downLoadMessage.setUrl(url);
        downLoadMessage.setJobId(jobId);
        messageSender.hander(UtilJson.writerWithDefaultPrettyPrinter(downLoadMessage));

        try {
            redisStoreManger.putToRedis(uid, null, 1, uidTimeOut);
        } catch (Exception e) {
            logger.error("uid = {},put to redis error", uid, e);
        }

    }

    public void schudel(DownLoadMessage message) {
        try {
            DownLoadWorkQueue downLoadWorkQueue = workQueueManger.getWorkQueue().get(message.getJobId());
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
            HtmlObject htmlObject = new HtmlObject();
            htmlObject.setContent(content);
            htmlObject.setDowntime(System.currentTimeMillis() + "");
            htmlObject.setUid(uid);
            htmlObject.setUrl(message.getUrl());

            htmlRepository.insert(htmlObject);
            downLoadWorkQueue.incDownloadTot();
        } catch (Exception e) {
            logger.error("下载失败 url={}", message.getUrl());
        }
    }

    public void extendsUrl(String content, String jobName) {

    }
}
