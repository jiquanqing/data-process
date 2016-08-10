package com.qjq.economics.information.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qjq.economics.information.dao.ParseInformationMapper;
import com.qjq.economics.information.domain.ParseConfig;
import com.qjq.economics.information.domain.ParseInformation;
import com.qjq.economics.information.service.InformationDataProcessService;
import com.qjq.parser.extractor.ExtractorPage;
import com.qjq.parser.extractor.builder.PageBuilder;
import com.qjq.parser.extractor.builder.PageBuilderFactory;
import com.qjq.parser.extractor.domain.PageDesc;

/**
 * 
 * Description: TODO 一般默认的咨询信息处理service
 * 
 * @author qingjiquan@bubugao.com
 * @Date 2016年8月2日 下午3:08:56
 * @version 1.0
 * @since JDK 1.7
 */
@Service
public class InformationDataProcessServiceImpl implements InformationDataProcessService {

    @Autowired
    ParseInformationMapper informationMapper;

    @Override
    public ExtractorPage parse(ParseConfig parseConfig) {

        PageDesc desc = new PageDesc();

        PageBuilder builder = PageBuilderFactory.factory("");

        desc.setUrl(parseConfig.getUrl()); // 用来指定解析的模板

        ExtractorPage extractorPage = builder.extract(desc, parseConfig.getContent());

        return extractorPage;
    }

    /**
     * 
     * TODO 将网页中得到的parse信息进行入库的组装.
     * 
     * @see com.qjq.economics.information.service.InformationDataProcessService#extractorPageConvert2ParseInformation(com.qjq.parser.extractor.ExtractorPage)
     */
    @Override
    public ParseInformation extractorPageConvert2ParseInformation(ExtractorPage extractorPage) {
        
        ParseInformation information = new ParseInformation();
        
        information.setAuthor(extractorPage.getAuthorId());
        information.setContent(extractorPage.getContent());
        information.setCtime(new Date());
        information.setIsvalid(1);
        information.setSources(extractorPage.getPublishSource());
        information.setTime(extractorPage.getPublishDate());
        information.setTitle(extractorPage.getTitle());

        return information;
    }

    /**
     * 
     * TODO 完整的一条咨询信息入库
     * 
     * @see com.qjq.economics.information.service.InformationDataProcessService#insert(com.qjq.economics.information.domain.ParseInformation)
     */
    @Override
    public void insert(ParseInformation information) {
        informationMapper.insert(information);
    }

}
