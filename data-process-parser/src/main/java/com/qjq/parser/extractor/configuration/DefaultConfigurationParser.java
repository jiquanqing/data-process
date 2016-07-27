package com.qjq.parser.extractor.configuration;

import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;

import com.qjq.parser.extractor.exception.ConfWrappedException;

/*
 *  function: parser xpath xml config this is default parser 
 *  author: qingjiquan 
 *  date:  2014-10-27
 */
public class DefaultConfigurationParser implements ConfParser {

    public Configuration parse(String confName, String fileName) {
        try {
            XMLConfiguration xConf = new XMLConfiguration(fileName);
            return parse(confName, xConf);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new ConfWrappedException(e);
        }
    }

    public Configuration parse(String confName, XMLConfiguration xConf) {
        DefaultConfiguration configuration = new DefaultConfiguration();
        try {
            configuration.setConfName(confName);

            xConf.setDelimiterParsingDisabled(true);
            xConf.setValidating(true);
            xConf.setThrowExceptionOnMissing(false);
            xConf.setThrowExceptionOnMissing(false);
            List<String> tList = (List<String>) xConf.getList("template-site.site");
            configuration.getTemplateSites().addAll(tList);
            for (int k = 0; true; k++) {
                List<String> patternList = (List<String>) xConf.getList("template(" + k + ").url.url-pattern");
                if (patternList == null || patternList.size() == 0) {
                    break;
                }
                DefaultTemplate template = new DefaultTemplate();
                template.getUrlPatterns().addAll(patternList);
                template.setTitle(xConf.getString("template(" + k + ").title-xpath"));
                template.setSource(xConf.getString("template(" + k + ").source-xpath"));
                template.setDate(xConf.getString("template(" + k + ").date-xpath"));
                template.setBody(xConf.getString("template(" + k + ").body-xpath"));
                template.setAuthor(xConf.getString("template(" + k + ").author-xpath"));
                configuration.addTemplate(template);
            }
        } catch (Exception e) {
            throw new ConfWrappedException(e);
        }

        return configuration;
    }
}
