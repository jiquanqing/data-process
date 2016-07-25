package com.qjq.crawler.service.test;

import java.util.ArrayList;
import java.util.List;

import org.data.process.model.CrawlerTypeEnum;
import org.data.process.model.VariablesField;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.qjq.crawler.dao.test.AbstractTest;
import com.qjq.crawler.domain.CrawlerConfig;
import com.qjq.crawler.service.CrawlerService;

public class HorCrawlerTest extends AbstractTest {

    @Autowired
    CrawlerService crawlerService;

    @Test
    public void test() {
        CrawlerConfig crawlerConfig = new CrawlerConfig();
        crawlerConfig.setBaseUrl("http://guba.astmoe.com/list,code,f.html");
        crawlerConfig.setCrawlerType(CrawlerTypeEnum.horizontal.getId());
        crawlerConfig.setExtendDeep(0);
        crawlerConfig.setMaxDomain(100);
        VariablesField variablesField = new VariablesField();
        variablesField.setFieldName("code");
        variablesField.setStartPoint(1L);
        variablesField.setEndPoint(300L);
        List<VariablesField> fields = new ArrayList<VariablesField>();
        fields.add(variablesField);
        crawlerConfig.setVariablesFields(fields);
        crawlerService.crawler(crawlerConfig);
    }
}
