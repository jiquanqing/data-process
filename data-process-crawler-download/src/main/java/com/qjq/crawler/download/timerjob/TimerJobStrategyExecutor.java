package com.qjq.crawler.download.timerjob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.qjq.crawler.download.dao.mysql.CrawlerTimerJobMapper;
import com.qjq.crawler.download.domain.CrawlerTimerJob;
import com.qjq.crawler.download.domain.CrawlerTimerJobExample;
import com.qjq.crawler.download.domain.TimerJobConfig;
import com.qjq.crawler.download.domain.TimerJobStatus;

@EnableScheduling
public class TimerJobStrategyExecutor implements InitializingBean, ApplicationContextAware, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(TimerJobStrategyExecutor.class);

    ThreadPoolTaskScheduler scheduler;
    List<ScheduledFuture> futures = new ArrayList<ScheduledFuture>(); // 任务计划

    ApplicationContext applicationContext;
    @Autowired
    CrawlerTimerJobMapper crawlerTimerJobMapper;

    public void addStrategy() {
        logger.info("加载定时任务");
        // 读取数据库里面配置的定时任务
        CrawlerTimerJobExample crawlerTimerJobExample = new CrawlerTimerJobExample();
        crawlerTimerJobExample.createCriteria().andJobstatusEqualTo(TimerJobStatus.STOP.getCode());
        List<CrawlerTimerJob> crawlerTimerJob = crawlerTimerJobMapper.selectByExample(crawlerTimerJobExample);
        for (CrawlerTimerJob job : crawlerTimerJob) {
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

        /*
         * // 加载特定的定时任务 Map<String, TimerJobStrategy> map =
         * applicationContext.getBeansOfType(TimerJobStrategy.class);
         * 
         * for (Map.Entry<String, TimerJobStrategy> m : map.entrySet()) {
         * TimerJobStrategy strategy = m.getValue(); TimerJobConfig config2 =
         * new TimerJobConfig(); config2.setDelay(strategy.getDelay());
         * registerTimberJob(config2, strategy); }
         */

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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        addStrategy();
    }

    @Override
    public void destroy() throws Exception {
        for (ScheduledFuture future : futures) {
            future.cancel(true);
        }
        //更新定时任务在mysql中的状态
        CrawlerTimerJob record = new CrawlerTimerJob();
        record.setJobstatus(TimerJobStatus.STOP.getCode());
        CrawlerTimerJobExample example = new CrawlerTimerJobExample();
        example.createCriteria().andJobstatusEqualTo(TimerJobStatus.RUNNING.getCode());
        crawlerTimerJobMapper.updateByExampleSelective(record, example);

        logger.info("定时任务已经被注销");
    }

    public ThreadPoolTaskScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(ThreadPoolTaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

}
