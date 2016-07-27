package com.qjq.parser.extractor.cnki;

import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;

import com.qjq.parser.extractor.configuration.Configuration;
import com.qjq.parser.extractor.configuration.DefaultConfigurationParser;
import com.qjq.parser.extractor.exception.ConfWrappedException;
/*
 * functions:ciki configurationparser
 * author:qingjiquan
 * date:2014-11-20
 */
public class CnKiConfigurationParser extends DefaultConfigurationParser {
	public Configuration parse(String confName, XMLConfiguration xConf) {
		CnKiConfiguration configuration = new CnKiConfiguration();
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
				CnKiTemplate template = new CnKiTemplate();
				template.getUrlPatterns().addAll(patternList);
				template.setKeyWords(xConf.getString("template(" + k + ").keywords-xpath"));
				template.setClcNum(xConf.getString("template(" + k + ").clcnum-xpath"));
				template.setAbstractinfo(xConf.getString("template(" + k + ").abstract-xpath"));
				template.setContent(xConf.getString("template(" + k + ").content-xpath"));
				template.setAbstrachen(xConf.getString("template(" + k + ").abstracten-xpath"));
				template.setAbstrachzh(xConf.getString("template(" + k + ").abstractzh-xpath"));
				template.setAuthor(xConf.getString("template(" + k + ").author-xpath"));
				template.setAuthoren(xConf.getString("template(" + k + ").authoren-xpath"));
				template.setAuthorenhref(xConf.getString("template(" + k + ").authorenhref-xpath"));
				template.setAuthorhref(xConf.getString("template(" + k + ").authorhref-xpath"));
				template.setPublishinfo(xConf.getString("template(" + k + ").publishinfo-xpath"));
				template.setKeyworden(xConf.getString("template(" + k + ").keyworden-xpath"));
				template.setKeywordenhref(xConf.getString("template(" + k + ").keywordenhref-xpath"));
				template.setKeywordzh(xConf.getString("template(" + k + ").keywordzh-xpath"));
				template.setKeywordzhhref(xConf.getString("template(" + k + ").keywordzhhref-xpath"));
				template.setJigou(xConf.getString("template(" + k + ").jigou-xpath"));
				template.setJigouhref(xConf.getString("template(" + k + ").jigouhref-xpath"));
				template.setChTitle(xConf.getString("template(" + k + ").zhtitle-xpath"));
				template.setEnTitle(xConf.getString("template(" + k + ").entitle-xpath"));
				configuration.addTemplate(template);
			}
		} catch (Exception e) {
			throw new ConfWrappedException(e);
		}

		return configuration;
	}
}
