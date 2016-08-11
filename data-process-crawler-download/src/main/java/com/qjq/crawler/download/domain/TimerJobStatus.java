package com.qjq.crawler.download.domain;

public enum TimerJobStatus {

    // '0 已失效，1 停止，2 等待中，3正在进行',
    NOT(0, "已失效"), STOP(1, "停止"), WAIT(2, "等待中"), RUNNING(3, "正在进行");

    private int code;
    private String desc;

    private TimerJobStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
