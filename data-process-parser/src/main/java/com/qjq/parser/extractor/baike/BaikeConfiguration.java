package com.qjq.parser.extractor.baike;

import java.util.ArrayList;
import java.util.List;

import com.qjq.parser.extractor.configuration.DefaultConfiguration;

public class BaikeConfiguration extends DefaultConfiguration {

    private List<BaikeTemplate> baikeTemplates = new ArrayList<BaikeTemplate>();

    public void addTemplate(BaikeTemplate template) {
        baikeTemplates.add(template);
    }

    public List<BaikeTemplate> getBaikeTemplates() {
        return baikeTemplates;
    }

    public void setBaikeTemplates(List<BaikeTemplate> baikeTemplates) {
        this.baikeTemplates = baikeTemplates;
    }
    public List<BaikeTemplate> getBaikeTemplatesByUrl(String url) {
        List<BaikeTemplate> result = new ArrayList<BaikeTemplate>();
        for (int i = 0; i < baikeTemplates.size(); i++) {
            BaikeTemplate template = baikeTemplates.get(i);
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
