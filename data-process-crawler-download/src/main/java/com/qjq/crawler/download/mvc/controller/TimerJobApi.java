package com.qjq.crawler.download.mvc.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.qjq.crawler.download.domain.CrawlerTimerJob;
import com.qjq.crawler.download.domain.TimerJobStatus;
import com.qjq.crawler.download.mvc.service.CrawlerTimerJobService;

@Controller
@RequestMapping("/timerJob")
public class TimerJobApi {

    @Autowired
    CrawlerTimerJobService timerJobService;

    @RequestMapping("/queryAll")
    public ModelAndView queryAll() {
        ModelAndView modelAndView = new ModelAndView("timerjoblist");
        ModelMap modelMap = modelAndView.getModelMap();

        List<CrawlerTimerJob> jobs = timerJobService.getByStatus(null);
        if (jobs != null) {
            modelMap.put("tot", jobs.size());
            modelMap.put("lists", jobs);
        }

        return modelAndView;
    }

    @RequestMapping("/add")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("timerjoblist");
        ModelMap modelMap = modelAndView.getModelMap();

        CrawlerTimerJob crawlerTimerJob = new CrawlerTimerJob();
        crawlerTimerJob.setDelay(request.getAttribute("delay").toString());
        crawlerTimerJob.setCtime(new Date());
        crawlerTimerJob.setExpendstype(Integer.valueOf(request.getAttribute("expendstype").toString()));
        crawlerTimerJob.setJobdesc(request.getAttribute("jobdesc").toString());
        crawlerTimerJob.setJobstatus(TimerJobStatus.STOP.getCode());
        crawlerTimerJob.setNoticequeuename(request.getAttribute("noticequeuename").toString());
        crawlerTimerJob.setUrl(request.getAttribute("url").toString());
        crawlerTimerJob.setXpath(request.getAttribute("xpath").toString());
        
        Boolean res = timerJobService.add(crawlerTimerJob);
        if (res) {
            modelMap.put("addRes", "ok");
        } else {
            modelMap.put("addRes", "error");
        }

        return modelAndView;
    }
}
