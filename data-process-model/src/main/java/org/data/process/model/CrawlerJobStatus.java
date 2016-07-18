package org.data.process.model;

public enum CrawlerJobStatus {
    ready("1", "准备中"), wait("2", "等待"),run("3", "运行中"),pause("4", "暂停"),finish("5", "完成");
    private String code;
    private String desc;

    private CrawlerJobStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
