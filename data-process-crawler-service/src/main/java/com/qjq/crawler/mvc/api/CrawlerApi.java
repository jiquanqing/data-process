package com.qjq.crawler.mvc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.data.process.model.CrawlerJobStatus;
import org.data.process.model.VariablesField;
import org.data.process.utils.CommonUtils;
import org.data.process.utils.UtilJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.qjq.crawler.domain.CrawlerConfig;
import com.qjq.crawler.domain.CrawlerJob;
import com.qjq.crawler.service.CrawlerJobService;
import com.qjq.crawler.service.CrawlerService;

@Controller
@RequestMapping("/crawlerApi")
public class CrawlerApi {

    private static Logger logger = LoggerFactory.getLogger(CrawlerApi.class);

    @Autowired
    CrawlerJobService crawlerJobService;
    @Autowired
    CrawlerService crawlerService;

    @RequestMapping("/creatJob")
    public ModelAndView creatJob() {
        ModelAndView modelAndView = new ModelAndView("creatJob");

        ModelMap map = modelAndView.getModelMap();
        map.put("status", "");
        return modelAndView;
    }

    @RequestMapping("/add")
    public ModelAndView addJob(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("creatJob");
        ModelMap map = modelAndView.getModelMap();
        try {

            CrawlerJob crawlerJob = new CrawlerJob();
            CrawlerConfig jobConfig = new CrawlerConfig();

            String jobName = request.getParameter("jobName");
            String urlListId = request.getParameter("urlList");
            String maxDepth = request.getParameter("maxDeep");
            String jobMaxSize = request.getParameter("maxSize");

            crawlerJob.setCtime(new Date());
            crawlerJob.setMtime(new Date());
            crawlerJob.setIsvalid(1);
            crawlerJob.setJobmaxsize(Integer.valueOf(jobMaxSize));
            crawlerJob.setJobmaxdomainsize(Integer.valueOf(jobMaxSize));
            crawlerJob.setUrllistid(urlListId);
            crawlerJob.setJobname(jobName);
            crawlerJob.setMaxdepth(Integer.valueOf(maxDepth));

            // ===jobconfig===
            String baseUrl = request.getParameter("baseUrl");
            String crawlerType = request.getParameter("crawlerType");
            String extendDeep = request.getParameter("extendDeep");
            String threadNum = request.getParameter("threadNum");
            String mongoCollectionName = request.getParameter("mongoCollectionName");
            String sleepMills = request.getParameter("sleepMills");
            String mqName = request.getParameter("mqName");
            String torrentUrl = request.getParameter("torrentUrl");
            String maxDomain = request.getParameter("maxDomain");
            jobConfig.setBaseUrl(baseUrl);
            jobConfig.setCrawlerType(Integer.valueOf(crawlerType));
            jobConfig.setExtendDeep(Integer.valueOf(extendDeep));
            jobConfig.setMongoCollectionName(mongoCollectionName);
            jobConfig.setMqName(mqName);
            jobConfig.setMaxDomain(Integer.valueOf(maxDomain));
            jobConfig.setSleepMills(Long.valueOf(sleepMills));
            jobConfig.setThreadNum(Integer.valueOf(threadNum));

            List<String> torrentUrls = new ArrayList<String>();
            String str[] = torrentUrl.split(";");
            torrentUrls = Arrays.asList(str);
            jobConfig.setTorrentUrl(torrentUrls);

            // ==VariablesField==
            String startPoint = request.getParameter("startPoint");
            String endPoint = request.getParameter("endPoint");
            String fieldName = request.getParameter("fieldName");
            VariablesField variablesField = new VariablesField();
            variablesField.setEndPoint(Long.valueOf(endPoint));
            variablesField.setFieldName(fieldName);
            variablesField.setStartPoint(Long.valueOf(startPoint));
            List<VariablesField> variablesFields = new ArrayList<VariablesField>();
            variablesFields.add(variablesField);
            jobConfig.setVariablesFields(variablesFields);
            crawlerJob.setJobconfig(UtilJson.writerWithDefaultPrettyPrinter(crawlerJob));
            crawlerJob.setJobid(CommonUtils.creatJobId(baseUrl));
            crawlerJob.setJobstatus(CrawlerJobStatus.ready.getCode());
            crawlerJobService.insertCrawlerJob(crawlerJob); // 插入到数据库中

            jobConfig.setJobId(crawlerJob.getJobid());
            crawlerService.crawler(jobConfig); // 执行任务

            map.put("status", "ok");
        } catch (Exception e) {
            logger.error("Job创建失败", e);
            map.put("status", "error");
        }


        return modelAndView;
    }
}
