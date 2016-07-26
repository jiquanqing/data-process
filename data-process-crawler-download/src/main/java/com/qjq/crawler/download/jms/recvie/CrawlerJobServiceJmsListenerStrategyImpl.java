package com.qjq.crawler.download.jms.recvie;

import org.data.process.model.CrawlerTypeEnum;
import org.data.process.model.JobConfig;
import org.data.process.model.VariablesField;
import org.data.process.mqmodel.CrawlerMessage;
import org.data.process.utils.UtilJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.qjq.crawler.download.dao.mysql.CrawlerJobMapper;
import com.qjq.crawler.download.domain.CrawlerJob;
import com.qjq.crawler.download.service.DownLoadService;

/**
 * 
 * Description: TODO Job监听<br/>
 * 
 * @author qingjiquan@bubugao.com
 * @Date 2016年7月22日 下午5:23:04
 * @version 1.0
 * @since JDK 1.7
 */

@Service
public class CrawlerJobServiceJmsListenerStrategyImpl implements JmsListenerStrategy {

    private static Logger logger = LoggerFactory.getLogger(CrawlerJobServiceJmsListenerStrategyImpl.class);

    private String jmsQueue = "Crawler_Service_Job_Queue";

    @Autowired
    CrawlerJobMapper crawlerJobMapper;
    @Autowired
    DownLoadService downLoadService;

    @Override
    public String getJmsQueue() {
        return jmsQueue;
    }

    @Override
    public void onMessage(String text) {
        logger.info(jmsQueue + "接收到mq消息:", text);
        // 监听到Job的mq 组装信息 调用 seed方法 产生下载的种子
        CrawlerMessage crawlerMessage = UtilJson.readValue(text, CrawlerMessage.class);
        if (crawlerMessage != null) {
            logger.info("开始处理JobId={}", crawlerMessage.getJobId());
            CrawlerJob crawlerJob = crawlerJobMapper.selectByPrimaryKey(crawlerMessage.getJobId());

            String configJson = crawlerJob.getJobconfig();
            if (!StringUtils.isEmpty(configJson)) {
                JobConfig config = UtilJson.readValue(configJson, JobConfig.class);
                if (config.getCrawlerType() == CrawlerTypeEnum.horizontal.getId()) { // 纵向下载

                    for (VariablesField variablesField : config.getVariablesFields()) {
                        Long start = variablesField.getStartPoint();
                        Long end = variablesField.getEndPoint();

                        for (Long var = start; var < end; var++) {
                            String params = variablesField.getFieldName() + "=" + var;
                            String url = creatUrl(crawlerMessage.getBaseUrl(), params);
                            downLoadService.addSeed(url, crawlerJob.getJobid(), 0, config.getSleepTime());
                        }
                    }
                } else {
                    downLoadService.addSeed(crawlerMessage.getBaseUrl(), crawlerJob.getJobid(), crawlerJob.getMaxdepth(),
                            config.getSleepTime());
                }
            }

        }
        System.out.println(text);
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

    public void setJmsQueue(String jmsQueue) {
        this.jmsQueue = jmsQueue;
    }

}
