package com.qjq.crawler.service.impl;

import org.data.process.model.CrawlerJobStatus;
import org.data.process.utils.UidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.domain.CrawlerConfig;
import com.qjq.crawler.domain.CrawlerJob;
import com.qjq.crawler.service.CrawlerJobService;

@Service
public class BaseCrawlerService {
    
    @Autowired
    CrawlerJobService crawlerJobService;
    
    public Boolean insert(CrawlerJob crawlerJob) {
        int result = crawlerJobService.insertCrawlerJob(crawlerJob);
        if (result > 0)
            return true;
        return false;
    }

    public String creatJobId(String baseUrl) {
        String base = baseUrl + System.currentTimeMillis();
        return UidUtils.getUid(base);
    }

    public CrawlerJob creatByConfig(CrawlerConfig config) {

        CrawlerJob crawlerJob = new CrawlerJob();

        String jobId = creatJobId(config.getBaseUrl());
        crawlerJob.setJobname(jobId);
        crawlerJob.setJobid(jobId);
        crawlerJob.setMaxdepth(config.getExtendDeep());
        crawlerJob.setJobmaxdomainsize(config.getMaxDomain());
        crawlerJob.setIsvalid(1);
        crawlerJob.setJobmaxsize(config.getMaxDomain());
        crawlerJob.setJobstatus(CrawlerJobStatus.ready.getCode());

        return crawlerJob;
    }

}
