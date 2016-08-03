package com.qjq.economics.information.timerjob;

public class ParseSendJmsJobStrategy implements TimerJobStrategy {

    private Long delay = 5000l;

    @Override
    public void onTimerJob() {
        // TODO Auto-generated method stub

    }

    @Override
    public Long getDelay() {
        return delay;
    }

}
