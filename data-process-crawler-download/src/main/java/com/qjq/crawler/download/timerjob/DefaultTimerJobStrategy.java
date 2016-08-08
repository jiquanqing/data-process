package com.qjq.crawler.download.timerjob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.data.process.utils.UidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.qjq.crawler.download.dao.mongo.HtmlRepository;
import com.qjq.crawler.download.dao.mysql.CrawlerUrlJobMapper;
import com.qjq.crawler.download.domain.CrawlerUrlJob;
import com.qjq.crawler.download.domain.TimerJobConfig;
import com.qjq.crawler.download.jms.send.MessageSender;
import com.qjq.crawler.download.service.DownLoadService;
import com.qjq.crawler.download.service.XpathService;

public class DefaultTimerJobStrategy implements TimerJobStrategy, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(DefaultTimerJobStrategy.class);

    ApplicationContext applicationContext;

    DownLoadService downLoadService;
    HtmlRepository htmlRepository;
    MessageSender messageSender;
    XpathService xpathService;
    CrawlerUrlJobMapper crawlerUrlJobMapper;

    public void init() {
        downLoadService = applicationContext.getBean("downLoadService", DownLoadService.class);
        htmlRepository = applicationContext.getBean("htmlRepository", HtmlRepository.class);
        messageSender = applicationContext.getBean("messageSender", MessageSender.class);
        xpathService = applicationContext.getBean("xpathService", XpathService.class);
        crawlerUrlJobMapper = applicationContext.getBean("crawlerUrlJobMapper", CrawlerUrlJobMapper.class);
    }

    public DefaultTimerJobStrategy() {
        init();
    }

    @Override
    public void onTimerJob(TimerJobConfig timerJobConfig) {
        logger.info("默认定时任务启动成功,jobId={}", timerJobConfig.getJobId());
        String strs[] = timerJobConfig.getUrl().split(";");
        for (String string : strs) {
            try {
                String content = downLoadService.downLoadByUrl(string);
                List<String> extendUrls = getAllArticUrl(content, timerJobConfig.getXpath());
                for (String url : extendUrls) {
                    String uid = UidUtils.getUid(url);
                    if (htmlRepository.findOne(uid) == null) { // 如果mongo里面没有存在url才进行抓取
                        content = downLoadService.downLoadByUrl(url);
                        if (timerJobConfig.getNoticeQueueName() != null) {
                            messageSender.hander(uid, timerJobConfig.getNoticeQueueName()); // 通知parse系统
                            // ： 以后的扩展点，加入mq存储机制，怕数据丢失，进行一次备份
                        }
                    }
                }
                logger.info("东财监听博客url={}刷新成功", string);
            } catch (Exception e) {
                logger.error("定时下载失败 url={}", string, e);
            }
        }
    }

    public List<String> getAllArticUrl(String content, List<String> xpaths) {
        List<String> result = new ArrayList<String>();
        for (String xpath : xpaths) {
            // String xpath = "//span[@class='title']//@href"; // 获取东财的url列表
            List<String> res = xpathService.xpath(xpath, content);
            if (res != null) {
                result.addAll(res);
            }
        }
        return result;
    }

    public void insertMySql(String jobId, String uid) {
        CrawlerUrlJob crawlerUrlJob = new CrawlerUrlJob();
        crawlerUrlJob.setCtime(new Date());
        crawlerUrlJob.setIsvalid(1);
        crawlerUrlJob.setJobid(jobId);
        crawlerUrlJob.setUid(uid);
        crawlerUrlJobMapper.insert(crawlerUrlJob);
    }

    @Override
    public Long getDelay() {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
