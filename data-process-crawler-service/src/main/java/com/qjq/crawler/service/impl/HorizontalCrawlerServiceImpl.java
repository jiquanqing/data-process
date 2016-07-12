package com.qjq.crawler.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.crawler.domain.HorizontalCrawlerConfig;
import com.qjq.crawler.domain.VariablesField;
import com.qjq.crawler.jms.MessageSender;
import com.qjq.crawler.service.DonwloadService;
import com.qjq.crawler.service.HorizontalCrawlerService;
import com.qjq.crawler.utils.UtilJson;

@Service
public class HorizontalCrawlerServiceImpl implements HorizontalCrawlerService {

    @Autowired
    DonwloadService donwloadService;
    @Autowired
    MessageSender messageSender;

    public void handle(HorizontalCrawlerConfig horizontalCrawlerConfig) {
        String baseUrl = horizontalCrawlerConfig.getBaseUrl();
        List<VariablesField> fields = horizontalCrawlerConfig.getVariablesFields();
        Map<String, Object> mqMap = new HashMap<String, Object>();
        for (VariablesField variablesField : fields) {
            Long start = variablesField.getStartPoint();
            Long end = variablesField.getEndPoint();
            for (Long var = start; var < end; var++) {
                String params = variablesField.getFieldName() + "=" + var;
                String url = creatUrl(baseUrl, params);
                mqMap.put("url", url);
                mqMap.put("jobId", horizontalCrawlerConfig.getJobId());
                mqMap.put("jobType", horizontalCrawlerConfig.getCrawlerType());
                String json = UtilJson.writerWithDefaultPrettyPrinter(mqMap);
                messageSender.hander(json); // 发送mq到队列里面 通知下载系统下载
                // 发送MQ String content = donwloadService.donwload(basUrl,
                // params);
            }
        }
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
