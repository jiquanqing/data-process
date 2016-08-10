package com.qjq.parser.extractor.builder;

import java.util.List;

import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qjq.parser.extractor.ExtractorPage;
import com.qjq.parser.extractor.configuration.ApplicationConfig;
import com.qjq.parser.extractor.configuration.DefaultConfiguration;
import com.qjq.parser.extractor.configuration.DefaultTemplate;
import com.qjq.parser.extractor.domain.PageDesc;
import com.qjq.parser.extractor.exception.WrappedException;
import com.qjq.parser.extractor.utils.CommonUtil;

/*
 *functions: default page extractor 
 *author: qingjiquan
 *date: 2014-10-30
 */
public class DefaultPageBuilder extends AbstractPageBuilder {
    final static String CONIF_NAME = "default";
    private static Logger LOG = LoggerFactory.getLogger(DefaultPageBuilder.class);

    public ExtractorPage extract(PageDesc desc, String rawContent) {
        ExtractorPage page = new ExtractorPage();
        page.setUrl(desc.getUrl());
        TagNode document = htmlClear(rawContent);
        processDoc(page, document);
        return page;
    }

    @Override
    protected boolean isTemplateSite(String url) {

        return false;
    }

    private void processDoc(ExtractorPage page, TagNode document) {
        String url = page.getUrl();
        DefaultConfiguration configuration = (DefaultConfiguration) ApplicationConfig.getConfigFromApplication().getConf(
                CONIF_NAME);
        List<DefaultTemplate> templates = configuration.getTemplatesByUrl(url);
        if (templates == null || templates.size() <= 0) {
            LOG.info("no templates find by url : " + url);
            return;
        }
        DefaultTemplate template = extractorTitle(page, document, templates);
        if (template == null)
            return;
        extractorAuthor(page, document, template);
        extractorDate(page, document, template);
        extractorSource(page, document, template);
        extractorBody(page, document, template);
    }

    private void extractorBody(ExtractorPage page, TagNode document, DefaultTemplate template) {
        String xpathBody = template.getBody();
        if (xpathBody == null || xpathBody.equals(""))
            return;
        try {
            StringBuffer value = new StringBuffer();
            Object[] objarrtr = document.evaluateXPath(xpathBody);
            if (objarrtr != null && objarrtr.length > 0) {
                for (Object obja : objarrtr) {
                    String str = "";
                    if (obja instanceof TagNode) {
                        TagNode tna = (TagNode) obja;
                        str = tna.getText().toString();
                    }else{
                        str = obja.toString();
                    }
                    value.append(CommonUtil.removeHuanHang(str));
                }
            }
            page.setContent(value.toString());
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }

    private void extractorAuthor(ExtractorPage page, TagNode document, DefaultTemplate template) {
        String xpathAuthor = template.getAuthor();
        if (xpathAuthor == null || xpathAuthor.equals(""))
            return;
        try {
            StringBuffer value = new StringBuffer();
            Object[] objarrtr = document.evaluateXPath(xpathAuthor);
            if (objarrtr != null && objarrtr.length > 0) {
                for (Object obja : objarrtr) {
                    String str = "";
                    if (obja instanceof TagNode) {
                        TagNode tna = (TagNode) obja;
                        str = tna.getText().toString();
                    }else{
                        str = obja.toString();
                    }
                    value.append(CommonUtil.removeHuanHang(str));
                }
            }
            page.setAuthorId(value.toString());
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }

    private void extractorDate(ExtractorPage page, TagNode document, DefaultTemplate template) {
        String xpathDate = template.getDate();
        if (xpathDate == null || xpathDate.equals(""))
            return;
        try {
            StringBuffer value = new StringBuffer();
            Object[] objarrtr = document.evaluateXPath(xpathDate);
            if (objarrtr != null && objarrtr.length > 0) {
                for (Object obja : objarrtr) {
                    String str = "";
                    if (obja instanceof TagNode) {
                        TagNode tna = (TagNode) obja;
                        str = tna.getText().toString();
                    }else{
                        str = obja.toString();
                    }
                    value.append(CommonUtil.removeHuanHang(str));
                }
            }
            page.setPublishDate(value.toString());
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }

    private void extractorSource(ExtractorPage page, TagNode document, DefaultTemplate template) {
        String xpathSource = template.getSource();
        if (xpathSource == null || xpathSource.equals(""))
            return;
        StringBuffer value = new StringBuffer();
        try {
            Object[] objarrtr = document.evaluateXPath(xpathSource);
            if (objarrtr != null && objarrtr.length > 0) {
                for (Object obja : objarrtr) {
                    String str = "";
                    if (obja instanceof TagNode) {
                        TagNode tna = (TagNode) obja;
                        str = tna.getText().toString();
                    }else{
                        str = obja.toString();
                    }
                    value.append(CommonUtil.removeHuanHang(str) + "\r\n");
                }
            }
            page.setPublishSource(value.toString());
        } catch (Exception e) {
            LOG.debug("xpath error and xpath = " + xpathSource, e);
        }
    }

    private DefaultTemplate extractorTitle(ExtractorPage page, TagNode document, List<DefaultTemplate> templates) {
        for (int i = 0; i < templates.size(); i++) {
            DefaultTemplate template = templates.get(i);
            String titleXpath = template.getTitle();
            if (titleXpath == null || titleXpath.equals(""))
                return null;
            StringBuffer value = new StringBuffer();
            try {
                Object[] objarrtr = document.evaluateXPath(titleXpath);
                if (objarrtr != null && objarrtr.length > 0) {
                    for (Object obja : objarrtr) {
                        String str = "";
                        if (obja instanceof TagNode) {
                            TagNode tna = (TagNode) obja;
                            str = tna.getText().toString();
                        }else{
                            str = obja.toString();
                        }
                        value.append(CommonUtil.removeHuanHang(str));
                    }
                }
            } catch (Exception e) {
                LOG.debug("xpath error and xpath = " + titleXpath);
            }
            if (value != null) {
                page.setTitle(value.toString());
                return template;
            }
        }
        return null;
    }
}
