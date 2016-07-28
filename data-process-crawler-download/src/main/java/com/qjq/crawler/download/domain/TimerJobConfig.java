package com.qjq.crawler.download.domain;

import java.util.List;

/**
 * 
 * Description: TODO 定时任务配置
 * 
 * @author qingjiquan@bubugao.com
 * @Date 2016年7月28日 下午3:16:56
 * @version 1.0
 * @since JDK 1.7
 */
public class TimerJobConfig {

    private Long delay; // 延时周期
    private String url;
    private List<String> xpath; // 解析url的xpath
    private String noticeQueueName;

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getXpath() {
        return xpath;
    }

    public void setXpath(List<String> xpath) {
        this.xpath = xpath;
    }

    public String getNoticeQueueName() {
        return noticeQueueName;
    }

    public void setNoticeQueueName(String noticeQueueName) {
        this.noticeQueueName = noticeQueueName;
    }

}
