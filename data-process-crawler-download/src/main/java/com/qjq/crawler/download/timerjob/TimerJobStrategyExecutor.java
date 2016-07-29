package com.qjq.crawler.download.timerjob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.qjq.crawler.download.domain.TimerJobConfig;

@EnableScheduling
public class TimerJobStrategyExecutor implements InitializingBean, ApplicationContextAware, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(TimerJobStrategyExecutor.class);

    ThreadPoolTaskScheduler scheduler;
    List<ScheduledFuture> futures = new ArrayList<ScheduledFuture>(); // 任务计划

    ApplicationContext applicationContext;

    public void addStrategy() {
        // 读取数据库里面配置的定时任务
        TimerJobConfig config = new TimerJobConfig();
        config.setDelay(1000l);
        TimerJobStrategy jobStrategy = new DefaultTimerJobStrategy();
        registerTimberJob(config, jobStrategy);

        // 加载特定的定时任务
        Map<String, TimerJobStrategy> map = applicationContext.getBeansOfType(TimerJobStrategy.class);

        for (Map.Entry<String, TimerJobStrategy> m : map.entrySet()) {
            TimerJobStrategy strategy = m.getValue();
            TimerJobConfig config2 = new TimerJobConfig();
            config2.setDelay(strategy.getDelay());
            registerTimberJob(config2, strategy);
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
        logger.info("定时任务已经被注销");
    }

    public ThreadPoolTaskScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(ThreadPoolTaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

}
