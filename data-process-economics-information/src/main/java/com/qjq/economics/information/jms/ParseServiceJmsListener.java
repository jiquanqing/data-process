package com.qjq.economics.information.jms;

import org.data.process.model.HtmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.economics.information.dao.HtmlRepository;
import com.qjq.economics.information.domain.ParseConfig;
import com.qjq.economics.information.domain.ParseInformation;
import com.qjq.economics.information.service.InformationDataProcessService;
import com.qjq.parser.extractor.ExtractorPage;

@Service
public class ParseServiceJmsListener implements JmsListenerStrategy {

    private static Logger logger = LoggerFactory.getLogger(ParseServiceJmsListener.class);

    private String jmsQueue = "Parse_Service_Job_Queue";

    @Autowired
    HtmlRepository htmlRepository;
    @Autowired
    InformationDataProcessService dataProcessService;

    @Override
    public String getJmsQueue() {
        return jmsQueue;
    }

    @Override
    public void onMessage(String text) {
        logger.info("接收到parse任务,text={}", text);

        String uid = text;
        if (uid != null) {
            HtmlObject htmlObject = htmlRepository.findOne(uid);
            if (htmlObject != null) {
                logger.info("开始parse uid={}", uid);
                ParseConfig config = new ParseConfig();
                config.setUid(uid);
                config.setUrl(htmlObject.getUrl());
                config.setContent(htmlObject.getContent());

                ExtractorPage extractorPage = dataProcessService.parse(config);
                ParseInformation information = dataProcessService.extractorPageConvert2ParseInformation(extractorPage);
                dataProcessService.insert(information);

                logger.info("uid={},parse成功已经插入到mysql中", uid);
            } else {
                logger.error("Mongo Not Find Uid={}", uid);
            }
        }
    }

}
