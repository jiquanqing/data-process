package com.qjq.parser.extractor.baike;

import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;

import com.qjq.parser.extractor.configuration.Configuration;
import com.qjq.parser.extractor.configuration.DefaultConfigurationParser;
import com.qjq.parser.extractor.exception.ConfWrappedException;

public class BaikeConfigurationParser extends DefaultConfigurationParser {
    public Configuration parse(String confName, XMLConfiguration xConf) {
        BaikeConfiguration configuration = new BaikeConfiguration();
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
                BaikeTemplate template = new BaikeTemplate();
                template.getUrlPatterns().addAll(patternList);
                template.setTitle(xConf.getString("template(" + k + ").title-xpath"));
                template.setParent(xConf.getString("template(" + k + ").parent-xpath"));
                template.setParentUrl(xConf.getString("template(" + k + ").parenturl-xpath"));
                template.setPolysemy(xConf.getString("template(" + k + ").polysemy-xpath"));
                template.setPolysemyUrl(xConf.getString("template(" + k + ").polysemyurl-xpath"));
                template.setRelated(xConf.getString("template(" + k + ").related-xpath"));
                template.setRelatedUrl(xConf.getString("template(" + k + ").relatedurl-xpath"));
                template.setSub(xConf.getString("template(" + k + ").sub-xpath"));
                template.setSubUrl(xConf.getString("template(" + k + ").suburl-xpath"));
                template.setSynonym(xConf.getString("template(" + k + ").synonym-xpath"));
                template.setSynonymUrl(xConf.getString("template(" + k + ").synonymurl-xpath"));
                template.setAbstractKey(xConf.getString("template(" + k + ").abstract-xpath"));
                
                template.setContent(xConf.getString("template(" + k + ").content-xpath"));
                template.setTag(xConf.getString("template(" + k + ").tag-html"));
                template.setH2(xConf.getString("template(" + k + ").contentH2-xpath"));
                template.setH3(xConf.getString("template(" + k + ").contentH3-xpath"));
                template.setSummary(xConf.getString("template(" + k + ").summary-xpath"));
                configuration.addTemplate(template);
            }
        } catch (Exception e) {
            throw new ConfWrappedException(e);
        }
        
        return configuration;
    }
}
