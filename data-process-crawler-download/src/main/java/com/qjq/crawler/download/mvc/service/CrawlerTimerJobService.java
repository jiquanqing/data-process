package com.qjq.crawler.download.mvc.service;

import java.util.List;

import com.qjq.crawler.download.domain.CrawlerTimerJob;

public interface CrawlerTimerJobService {

    public Boolean add(CrawlerTimerJob crawlerTimerJob);

    public List<CrawlerTimerJob> getByStatus(Integer status);
}
