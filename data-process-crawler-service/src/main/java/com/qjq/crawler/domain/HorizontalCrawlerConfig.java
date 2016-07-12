package com.qjq.crawler.domain;

import java.util.List;

public class HorizontalCrawlerConfig extends CrawlerConfig {

    private List<VariablesField> variablesFields; // 纵向抓取时可变的字段

    public List<VariablesField> getVariablesFields() {
        return variablesFields;
    }

    public void setVariablesFields(List<VariablesField> variablesFields) {
        this.variablesFields = variablesFields;
    }

}

