package com.qjq.crawler.download.timerjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.download.domain.TimerJobConfig;

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
public class MysqlRefTimerJobStrategy implements TimerJobStrategy{

    private static Logger logger = LoggerFactory.getLogger(MysqlRefTimerJobStrategy.class);

    @Autowired
    TimerJobStrategyExecutor timerJobStrategyExecutor;
    
    private Long delay = 60 * 1000 * 10l;//10分钟刷新
    
    @Override
    public void onTimerJob(TimerJobConfig timerJobConfig) {
        logger.info("刷新mysql配置的定时任务信息");
        timerJobStrategyExecutor.addStrategy();
    }

    @Override
    public Long getDelay() {
        return delay;
    }

}
