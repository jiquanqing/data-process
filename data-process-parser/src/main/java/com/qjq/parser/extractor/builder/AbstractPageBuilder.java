package com.qjq.parser.extractor.builder;

import java.io.Reader;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

import com.qjq.parser.extractor.ExtractorPage;
import com.qjq.parser.extractor.domain.PageDesc;
import com.qjq.parser.extractor.exception.WrappedException;

abstract public class AbstractPageBuilder implements PageBuilder {
    private String contentGroup;

    public ExtractorPage extract(PageDesc desc, Reader rawContentReader) {
        return null;
    }

    protected abstract boolean isTemplateSite(String url);

    public String getContentGroup() {
        return contentGroup;
    }

    public void setContentGroup(String contentGroup) {
        this.contentGroup = contentGroup;
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
            e.printStackTrace();
            throw new WrappedException(e);
        }
    }
}
