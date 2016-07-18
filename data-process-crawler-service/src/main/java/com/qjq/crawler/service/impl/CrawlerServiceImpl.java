package com.qjq.crawler.service.impl;

import org.data.process.model.CrawlerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.domain.CrawlerConfig;
import com.qjq.crawler.domain.CrawlerJob;
import com.qjq.crawler.service.CrawlerJobService;
import com.qjq.crawler.service.CrawlerService;
import com.qjq.crawler.service.DonwloadService;

@Service
public class CrawlerServiceImpl implements CrawlerService {

    private static Logger logger = LoggerFactory.getLogger(CrawlerServiceImpl.class);

    @Autowired
    DonwloadService donwloadService;
    @Autowired
    CrawlerJobService crawlerJobService;

    public void crawler(CrawlerConfig crawlerConfig) {
        logger.info("开始进行");
        if (crawlerConfig != null) {
            if (crawlerConfig.getCrawlerType() == CrawlerTypeEnum.horizontal.getId()) {

            }
        }
    }

    public Boolean insert(CrawlerJob crawlerJob) {
        int result = crawlerJobService.insertCrawlerJob(crawlerJob);
        if (result > 0)
            return true;
        return false;
    }

}
