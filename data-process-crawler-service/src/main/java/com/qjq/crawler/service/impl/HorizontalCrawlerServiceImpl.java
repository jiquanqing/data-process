package com.qjq.crawler.service.impl;

import java.util.Date;
import java.util.List;

import org.data.process.model.JobConfig;
import org.data.process.model.VariablesField;
import org.data.process.mqmodel.CrawlerMessage;
import org.data.process.utils.UtilJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.domain.CrawlerConfig;
import com.qjq.crawler.domain.CrawlerJob;
import com.qjq.crawler.jms.MessageSender;
import com.qjq.crawler.service.DonwloadService;
import com.qjq.crawler.service.HorizontalCrawlerService;

@Service
public class HorizontalCrawlerServiceImpl extends BaseCrawlerService implements HorizontalCrawlerService {

    @Autowired
    DonwloadService donwloadService;
    @Autowired
    MessageSender messageSender;

    public void handle(CrawlerConfig horizontalCrawlerConfig) {
        String baseUrl = horizontalCrawlerConfig.getBaseUrl();
        /*List<VariablesField> fields = horizontalCrawlerConfig.getVariablesFields();
        
       
        JobConfig jobConfig = new JobConfig();
        CrawlerJob crawlerJob = creatByConfig(horizontalCrawlerConfig);
      
        jobConfig.setVariablesFields(fields);
        jobConfig.setCrawlerType(horizontalCrawlerConfig.getCrawlerType());
        crawlerJob.setJobconfig(UtilJson.writerWithDefaultPrettyPrinter(jobConfig));
        crawlerJob.setUrllistid("");
        crawlerJob.setCtime(new Date());
        crawlerJob.setMtime(new Date());*/

        CrawlerMessage crawlerMessage = new CrawlerMessage();
        crawlerMessage.setBaseUrl(baseUrl);
        crawlerMessage.setJobId(horizontalCrawlerConfig.getJobId());
        messageSender.hander(UtilJson.writerWithDefaultPrettyPrinter(crawlerMessage)); // 发送mq到队列里面
                                                                                       // 通知下载系统下载
       // crawlerJobService.insertCrawlerJob(crawlerJob);           //任务插入数据库
        
     /*   for (VariablesField variablesField : fields) {
            Long start = variablesField.getStartPoint();
            Long end = variablesField.getEndPoint();
            
            for (Long var = start; var < end; var++) {
                String params = variablesField.getFieldName() + "=" + var;
                String url = creatUrl(baseUrl, params);
                crawlerMessage.setBaseUrl(url);
                crawlerMessage.setJobName(creatJobName(horizontalCrawlerConfig));
                messageSender.hander(UtilJson.writerWithDefaultPrettyPrinter(crawlerMessage)); // 发送mq到队列里面
                                                                                               // 通知下载系统下载
                insertCrawlerJob(crawlerJob);           //任务插入数据库
            }
        }*/
    }

   

    public String creatUrl(String baseUrl, String params) {
        StringBuffer url = new StringBuffer();
        url.append(baseUrl);
        if (baseUrl.contains("?")) { // http://www.baidu.com?yunh=XX
            if (baseUrl.contains("=")) {
                url.append("&").append(params);
            } else {
                url.append("?").append(params);
            }
        } else {
            url.append("?").append(params);
        }
        return url.toString();
    }

}
