package com.qjq.crawler.download.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qjq.crawler.download.service.XpathService;
@Service
public class XpathServiceImpl implements XpathService {

    private static Logger logger = LoggerFactory.getLogger(XpathServiceImpl.class);

    @Override
    public List<String> xpath(String xpath, String content) {
        List<String> result = new ArrayList<String>();
        TagNode document = htmlClear(content);
        try {

            Object[] objarrtr = document.evaluateXPath(xpath);
            if (objarrtr != null && objarrtr.length > 0) {
                for (Object obja : objarrtr) {
                    String str = obja.toString();
                    result.add(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public TagNode htmlClear(String pageData) {
        try {
            HtmlCleaner htmlCleaner = new HtmlCleaner();
            CleanerProperties props = htmlCleaner.getProperties();
            props.setUseCdataForScriptAndStyle(true);
            props.setRecognizeUnicodeChars(true);
            props.setUseEmptyElementTags(true);
            props.setAdvancedXmlEscape(true);
            props.setTranslateSpecialEntities(true);
            props.setBooleanAttributeValues("empty");
            TagNode node = htmlCleaner.clean(pageData);
            return node;
        } catch (Exception e) {
            logger.error("html clear error", e);
        }
        return null;
    }
}
