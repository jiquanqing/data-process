package com.qjq.crawler.download.timerjob;

import com.qjq.crawler.download.domain.TimerJobConfig;

public class DefaultTimerJobStrategy implements TimerJobStrategy {

    @Override
    public void onTimerJob(TimerJobConfig timerJobConfig) {
        System.out.println("我是定时任务");
    }

}
