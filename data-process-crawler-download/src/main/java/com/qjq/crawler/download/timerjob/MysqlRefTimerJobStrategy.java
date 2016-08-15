package com.qjq.crawler.download.timerjob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.qjq.crawler.download.dao.mysql.CrawlerTimerJobMapper;
import com.qjq.crawler.download.domain.CrawlerTimerJob;
import com.qjq.crawler.download.domain.CrawlerTimerJobExample;
import com.qjq.crawler.download.domain.TimerJobConfig;
import com.qjq.crawler.download.domain.TimerJobStatus;

/**
 * 
 * Description: TODO 定时刷新mysql配置信息
 * 
 * @author qingjiquan@bubugao.com
 * @Date 2016年8月11日 下午3:31:42
 * @version 1.0
 * @since JDK 1.7
 */
@Service
public class MysqlRefTimerJobStrategy implements TimerJobStrategy, DisposableBean, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(MysqlRefTimerJobStrategy.class);
    ApplicationContext applicationContext;

    ThreadPoolTaskScheduler scheduler;
    List<ScheduledFuture> futures = new ArrayList<ScheduledFuture>(); // 任务计划
    private Long delay = 60 * 1000 * 10l;// 10分钟刷新
    CrawlerTimerJobMapper crawlerTimerJobMapper;

    @Override
    public void onTimerJob(TimerJobConfig timerJobConfig) {
        logger.info("刷新mysql配置的定时任务信息");
        crawlerTimerJobMapper = applicationContext.getBean("crawlerTimerJobMapper", CrawlerTimerJobMapper.class);
        // 读取数据库里面配置的定时任务
        CrawlerTimerJobExample crawlerTimerJobExample = new CrawlerTimerJobExample();
        crawlerTimerJobExample.createCriteria().andJobstatusEqualTo(TimerJobStatus.STOP.getCode());
        List<CrawlerTimerJob> crawlerTimerJob = crawlerTimerJobMapper.selectByExample(crawlerTimerJobExample);
        for (CrawlerTimerJob job : crawlerTimerJob) {
            logger.info("检测到有新的定时抓取任务更新 jobId={}",job.getJobid());
            TimerJobConfig config = new TimerJobConfig();
            config.setDelay(Long.valueOf(job.getDelay()));
            config.setExpendsType(job.getExpendstype());
            config.setJobId(job.getJobid());
            config.setNoticeQueueName(job.getNoticequeuename());
            config.setUrl(job.getUrl());
            String xpaths[] = job.getXpath().split(";");
            List<String> xpathLists = new ArrayList<String>();
            for (String x : xpaths) {
                xpathLists.add(x);
            }
            config.setXpath(xpathLists);
            TimerJobStrategy jobStrategy = new DefaultTimerJobStrategy();
            registerTimberJob(config, jobStrategy);

            job.setJobstatus(TimerJobStatus.RUNNING.getCode());
            crawlerTimerJobMapper.updateByPrimaryKey(job); // 更新mysql
                                                           // 此定时任务已经在运行当中
        }
    }

    // 注册定时任务
    private void registerTimberJob(final TimerJobConfig timerJobConfig, final TimerJobStrategy jobStrategy) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                jobStrategy.onTimerJob(timerJobConfig);
            }
        };
        if (timerJobConfig.getDelay() != null) {
            long delay = timerJobConfig.getDelay();
            Date startTime = new Date(System.currentTimeMillis() + 15000);
            futures.add(scheduler.scheduleWithFixedDelay(task, startTime, delay));
        }
    }

    @Override
    public Long getDelay() {
        return delay;
    }

    @Override
    public void destroy() throws Exception {
        for (ScheduledFuture future : futures) {
            future.cancel(true);
        }
        // 更新定时任务在mysql中的状态
        CrawlerTimerJob record = new CrawlerTimerJob();
        record.setJobstatus(TimerJobStatus.STOP.getCode());
        CrawlerTimerJobExample example = new CrawlerTimerJobExample();
        example.createCriteria().andJobstatusEqualTo(TimerJobStatus.RUNNING.getCode());
        crawlerTimerJobMapper.updateByExampleSelective(record, example);

        logger.info("定时任务已经被注销");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
