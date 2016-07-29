package com.qjq.crawler.download.timerjob;

import com.qjq.crawler.download.domain.TimerJobConfig;

public interface TimerJobStrategy {
    
    public void onTimerJob(TimerJobConfig timerJobConfig);
    
    public Long getDelay();
    
}
