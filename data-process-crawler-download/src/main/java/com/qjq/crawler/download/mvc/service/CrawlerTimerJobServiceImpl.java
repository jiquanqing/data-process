package com.qjq.crawler.download.mvc.service;

import java.util.List;

import org.data.process.utils.UidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.qjq.crawler.download.dao.mysql.CrawlerTimerJobMapper;
import com.qjq.crawler.download.domain.CrawlerTimerJob;
import com.qjq.crawler.download.domain.CrawlerTimerJobExample;

public class CrawlerTimerJobServiceImpl implements CrawlerTimerJobService {

    @Autowired
    CrawlerTimerJobMapper crawlerTimerJobMapper;

    @Override
    public Boolean add(CrawlerTimerJob crawlerTimerJob) {
        if (StringUtils.isEmpty(crawlerTimerJob.getJobid())) {
            crawlerTimerJob.setJobid(UidUtils.getUid(String.valueOf(System.currentTimeMillis()))); // 根据时间创建一个JobId
        }
        Integer res = crawlerTimerJobMapper.insert(crawlerTimerJob);
        if (res > 0)
            return true;
        else
            return false;
    }

    @Override
    public List<CrawlerTimerJob> getByStatus(Integer status) {
        CrawlerTimerJobExample example = new CrawlerTimerJobExample();
        if (status == null) { // 查询all
            example.createCriteria().andJobstatusIsNotNull();
        } else {
            example.createCriteria().andJobstatusEqualTo(status);
        }
        List<CrawlerTimerJob> jobs = crawlerTimerJobMapper.selectByExample(example);
        return jobs;
    }

}
