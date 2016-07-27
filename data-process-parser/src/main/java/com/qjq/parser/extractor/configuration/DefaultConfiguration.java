package com.qjq.parser.extractor.configuration;

import java.util.ArrayList;
import java.util.List;

import com.qjq.parser.extractor.autotemplate.AutoTemplate;

/*
 * function: configuration entry
 * author: qingjiquan
 * date: 2014-10-27
 */
public class DefaultConfiguration implements Configuration {

    public String confName;
    private List<DefaultTemplate> templates = new ArrayList<DefaultTemplate>();
    private List<AutoTemplate> autoTemplates = new ArrayList<AutoTemplate>();
    public List<String> templateSites = new ArrayList<String>();

    public void addTemplate(DefaultTemplate template) {
        templates.add(template);
    }

    public void addAutoTemplate(AutoTemplate autoTemplate) {
        this.autoTemplates.add(autoTemplate);
    }

    public void addTemplateSite(String string) {
        templateSites.add(string);
    }

    public List<DefaultTemplate> getTemplatesByUrl(String url) {
        List<DefaultTemplate> result = new ArrayList<DefaultTemplate>();
        for (int i = 0; i < templates.size(); i++) {
            DefaultTemplate template = templates.get(i);
            for (int j = 0; j < template.getUrlPatterns().size(); j++) {
                if (matchUrl(template.getUrlPatterns().get(j), url)) {
                    result.add(template);
                    break;
                }
            }
        }
        return result;
    }

    public boolean matchUrl(String pattern, String url) {
        if (pattern == null || url == null) {
            return false;
        }
        // trim http://
        String[] patternItems = null;
        if (pattern.length() > 9) {
            int idx = pattern.indexOf("//", 0);
            if (idx > 0) {
                String patternPath = pattern.substring(idx + 2);
                patternItems = patternPath.split("/");
            }
        }

        String[] urlItems = null;
        if (url.length() > 9) {
            int idx = url.indexOf("//", 0);
            if (idx > 0) {
                String urlPath = url.substring(idx + 2);
                urlItems = urlPath.split("/");
            }
        }
        for (int i = 0; i < patternItems.length; i++) {
            String patSeg = patternItems[i];
            String urlSeg = "";

            if (urlItems.length > i) {
                urlSeg = urlItems[i];
            } else {
                if (patSeg.equals("*") && i == patternItems.length - 1) {
                    return true;
                } else {
                    return false;
                }
            }

            do {
                int nextStarPos = patSeg.indexOf("*", 1);
                String word = "";
                if (nextStarPos < 0) {
                    word = patSeg;
                    patSeg = "";
                } else {
                    word = patSeg.substring(0, nextStarPos - 1);
                    patSeg = patSeg.substring(nextStarPos);
                }

                if (word.charAt(0) == '*') {
                    word = word.substring(1);
                    int uIdx = urlSeg.indexOf(word);
                    if (uIdx >= 0) {
                        urlSeg = urlSeg.substring(uIdx + word.length());
                    } else {
                        return false;
                    }
                } else {
                    int uIdx = urlSeg.indexOf(word);
                    if (uIdx == 0) {
                        urlSeg = urlSeg.substring(word.length());
                    } else {
                        return false;
                    }
                }
            } while (patSeg.length() > 0);

        }

        return true;
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public List<DefaultTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<DefaultTemplate> templates) {
        this.templates = templates;
    }

    public List<String> getTemplateSites() {
        return templateSites;
    }

    public void setTemplateSites(List<String> templateSites) {
        this.templateSites = templateSites;
    }

    public List<AutoTemplate> getAutoTemplates() {
        return autoTemplates;
    }

    public void setAutoTemplates(List<AutoTemplate> autoTemplates) {
        this.autoTemplates = autoTemplates;
    }

}
