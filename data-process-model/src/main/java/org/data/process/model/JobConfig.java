package org.data.process.model;

import java.util.List;

public class JobConfig {
    
    private List<VariablesField> variablesFields; // 纵向抓取时可变的字段
    
    private Integer crawlerType; //crawler类型 1 - 纵向  2 - 横向 (需要告诉url起止点) 
    
    private Integer sleepTime;  //抓取间隔时间

    public List<VariablesField> getVariablesFields() {
        return variablesFields;
    }

    public void setVariablesFields(List<VariablesField> variablesFields) {
        this.variablesFields = variablesFields;
    }

    public Integer getCrawlerType() {
        return crawlerType;
    }

    public void setCrawlerType(Integer crawlerType) {
        this.crawlerType = crawlerType;
    }

    public Integer getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }
    
    
}
