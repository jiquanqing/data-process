package com.qjq.parser.extractor.cnki;

import java.util.ArrayList;
import java.util.List;

import com.qjq.parser.extractor.configuration.DefaultConfiguration;

/*
 * functions:ciki configuration
 * author:qingjiquan
 * date:2014-11-20
 */
public class CnKiConfiguration extends DefaultConfiguration {

	private List<CnKiTemplate> zhWiKiTemplates = new ArrayList<CnKiTemplate>();

	public void addTemplate(CnKiTemplate template) {
		this.zhWiKiTemplates.add(template);
	}
	public List<CnKiTemplate> getZhWiKiTemplates() {
		return zhWiKiTemplates;
	}

	public void setZhWiKiTemplates(List<CnKiTemplate> zhWiKiTemplates) {
		this.zhWiKiTemplates = zhWiKiTemplates;
	}

	public List<CnKiTemplate> getZhWiKiTemplateByUrl(String url) {
		List<CnKiTemplate> result = new ArrayList<CnKiTemplate>();
		for (int i = 0; i < zhWiKiTemplates.size(); i++) {
			CnKiTemplate template = zhWiKiTemplates.get(i);
			for (int j = 0; j < template.getUrlPatterns().size(); j++) {
				if (matchUrl(template.getUrlPatterns().get(j), url)) {
					result.add(template);
					break;
				}
			}
		}
		return result;
	}
}
