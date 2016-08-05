package com.qjq.crawler.download.timerjob;

import org.data.process.model.HtmlObject;
import org.data.process.utils.UidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qjq.crawler.download.dao.mongo.HtmlRepository;
import com.qjq.crawler.download.domain.TimerJobConfig;
import com.qjq.crawler.download.jms.send.MessageSender;
import com.qjq.crawler.download.service.DownLoadService;
import com.qjq.crawler.download.utils.HttpRequest;

/**
 * 
 * Description: TODO 监听东财博客的定时任务
 * 
 * @author qingjiquan@bubugao.com
 * @Date 2016年8月5日 下午3:16:30
 * @version 1.0
 * @since JDK 1.7
 */
@Service
public class EastyMoneyBlogTimerJobStategy implements TimerJobStrategy {

    private static Logger logger = LoggerFactory.getLogger(EastyMoneyBlogTimerJobStategy.class);

    private Long delay = 1000 * 60 * 10l; // 10分钟抓一次

    @Value("${eastyBolgUrls:''}")
    private String eastyBolgUrls; // 暂时采用配置文件方式，后续需要考虑动态更新 采用数据库存储 或者其他方式

    @Value("${eastyNotifyMqName:'Parse_Service_Job_Queue'}")
    private String notifyMqName;

    @Autowired
    HtmlRepository htmlRepository;
    @Autowired
    MessageSender messageSender;
    @Autowired
    DownLoadService downLoadService;

    @Override
    public void onTimerJob(TimerJobConfig timerJobConfig) {
        logger.info("东财博客开始抓取");
        String strs[] = eastyBolgUrls.split(";");
        for (String string : strs) {
            try {
                String uid = downLoadService.downLoadByUrl(string);
                messageSender.hander(uid, notifyMqName); // 以后的扩展点，加入mq存储机制，怕数据丢失，进行一次备份
                logger.info("东财监听博客url={}刷新成功", string);
            } catch (Exception e) {
                logger.error("定时下载失败 url={}", string, e);
            }
        }
    }

    @Override
    public Long getDelay() {
        return delay;
    }

}
