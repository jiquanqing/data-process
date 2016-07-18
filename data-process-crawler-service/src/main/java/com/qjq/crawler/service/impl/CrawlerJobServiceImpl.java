package com.qjq.crawler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.dao.mysql.CrawlerJobMapper;
import com.qjq.crawler.domain.CrawlerJob;
import com.qjq.crawler.service.CrawlerJobService;

@Service
public class CrawlerJobServiceImpl implements CrawlerJobService {

    @Autowired
    CrawlerJobMapper crawlerJobMapper;

    @Override
    public int insertCrawlerJob(CrawlerJob crawlerJob) {
        return crawlerJobMapper.insert(crawlerJob);
    }

}
