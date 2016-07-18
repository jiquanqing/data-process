package com.qjq.crawler.domain;

import java.util.List;

import org.data.process.model.VariablesField;

/**
 * 
 * Description: TODO 配置
 * 
 * @author qingjiquan@bubugao.com
 * @Date 2016年7月7日 下午4:21:07
 * @version 1.0
 * @since JDK 1.7
 */
public class CrawlerConfig {

    private String baseUrl;     //基础url

    private Integer crawlerType; //crawler类型 1 - 纵向  2 - 横向 (需要告诉url起止点) 
    
    private Integer extendDeep; //根据baseUrl 扩展深度
    
    
    private Integer threadNum;  //启的线程数据
    
    private String mongoCollectionName; //存储的集合名称
    
    private Long sleepMills;    //暂停时间 毫秒
    
    private String mqName;      //发送的mq name
    
    private String jobId;       //根据抓取配置生成一个唯一的ID
    
    private List<String> torrentUrl;    //入口种子url
    
    private Integer maxDomain;
    
    private List<VariablesField> variablesFields; // 纵向抓取时可变的字段

    public List<VariablesField> getVariablesFields() {
        return variablesFields;
    }

    public void setVariablesFields(List<VariablesField> variablesFields) {
        this.variablesFields = variablesFields;
    }

    
    
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public Integer getExtendDeep() {
        return extendDeep;
    }

    public void setExtendDeep(Integer extendDeep) {
        this.extendDeep = extendDeep;
    }

    public Integer getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }

    public String getMongoCollectionName() {
        return mongoCollectionName;
    }

    public void setMongoCollectionName(String mongoCollectionName) {
        this.mongoCollectionName = mongoCollectionName;
    }

    public Long getSleepMills() {
        return sleepMills;
    }

    public void setSleepMills(Long sleepMills) {
        this.sleepMills = sleepMills;
    }

    public Integer getCrawlerType() {
        return crawlerType;
    }

    public void setCrawlerType(Integer crawlerType) {
        this.crawlerType = crawlerType;
    }

    public String getMqName() {
        return mqName;
    }

    public void setMqName(String mqName) {
        this.mqName = mqName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<String> getTorrentUrl() {
        return torrentUrl;
    }

    public void setTorrentUrl(List<String> torrentUrl) {
        this.torrentUrl = torrentUrl;
    }

    public Integer getMaxDomain() {
        return maxDomain;
    }

    public void setMaxDomain(Integer maxDomain) {
        this.maxDomain = maxDomain;
    }
    
}
