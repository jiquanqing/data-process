package com.qjq.parser.extractor.wiki;

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
public class WiKiConfigurationParser extends DefaultConfigurationParser {
	public Configuration parse(String confName, XMLConfiguration xConf) {
		WiKiConfiguration configuration = new WiKiConfiguration();
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
				WiKiTemplate template = new WiKiTemplate();
				template.getUrlPatterns().addAll(patternList);
				template.setTitle(xConf.getString("template(" + k + ").title-xpath"));
				template.setContent(xConf.getString("template(" + k + ").content-xpath"));
				template.setTable(xConf.getString("template(" + k + ").table-xpath"));
				configuration.addTemplate(template);
			}
		} catch (Exception e) {
			throw new ConfWrappedException(e);
		}

		return configuration;
	}
}
