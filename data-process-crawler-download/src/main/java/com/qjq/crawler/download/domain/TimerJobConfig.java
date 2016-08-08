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
    private String noticeQueueName; // 如果通知的mqName不为空 则表示需要通知相应的mq进行parse
    private Integer expendsType = 1;// 1 为不扩展只需要下载 2为下载后还需要根绝xpath在进行一次下载
    private String jobId; // 定时任务的JobId，用来记录下载情况

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

    public Integer getExpendsType() {
        return expendsType;
    }

    public void setExpendsType(Integer expendsType) {
        this.expendsType = expendsType;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

}
