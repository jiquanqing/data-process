package com.qjq.crawler.download.service.impl;

import java.util.Date;

import org.data.process.model.HtmlObject;
import org.data.process.mqmodel.DownLoadMessage;
import org.data.process.utils.UidUtils;
import org.data.process.utils.UtilJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.download.contier.DownLoadWorkQueue;
import com.qjq.crawler.download.contier.DownLoadWorkQueueManger;
import com.qjq.crawler.download.dao.mongo.HtmlRepository;
import com.qjq.crawler.download.dao.mysql.CrawlerUrlJobMapper;
import com.qjq.crawler.download.dao.redis.RedisStoreManger;
import com.qjq.crawler.download.domain.CrawlerUrlJob;
import com.qjq.crawler.download.jms.send.MessageSender;
import com.qjq.crawler.download.service.DownLoadService;
import com.qjq.crawler.download.utils.HttpRequest;

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
    @Autowired
    CrawlerUrlJobMapper crawlerUrlJobMapper;

    private int uidTimeOut = 60 * 60 * 24 * 10;

    @Override
    public void addSeed(String url, String jobId) {

        String uid = UidUtils.getUid(url);
        try {
            if (redisStoreManger.existByRedis(uid, null)) {
                CrawlerUrlJob crawlerUrlJob = new CrawlerUrlJob();
                crawlerUrlJob.setCtime(new Date());
                crawlerUrlJob.setIsvalid(1);
                crawlerUrlJob.setJobid(jobId);
                crawlerUrlJob.setUid(uid);
                crawlerUrlJobMapper.insert(crawlerUrlJob);
                workQueueManger.incDownloadTot(jobId);
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
        workQueueManger.incAllTot(jobId);
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

            CrawlerUrlJob crawlerUrlJob = new CrawlerUrlJob();
            crawlerUrlJob.setCtime(new Date());
            crawlerUrlJob.setIsvalid(1);
            crawlerUrlJob.setJobid(message.getJobId());
            crawlerUrlJob.setUid(uid);
            crawlerUrlJobMapper.insert(crawlerUrlJob);
            workQueueManger.incDownloadTot(message.getJobId());
        } catch (Exception e) {
            logger.error("下载失败 url={}", message.getUrl());
        }
    }

    public void extendsUrl(String content, String jobName) {

    }
}
