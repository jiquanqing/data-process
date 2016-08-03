package com.qjq.economics.information.timerjob;


public interface TimerJobStrategy {
    
    public void onTimerJob();
    
    public Long getDelay();
    
}
