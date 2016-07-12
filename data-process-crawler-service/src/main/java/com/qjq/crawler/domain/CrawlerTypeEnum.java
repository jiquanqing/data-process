package com.qjq.crawler.domain;

public enum CrawlerTypeEnum {
    vertical(1,"纵向"),horizontal(2,"横向");
    
    private Integer id;
    private String desc;
    
    private CrawlerTypeEnum(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
    
}
