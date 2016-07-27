package com.qjq.parser.extractor.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.qjq.parser.extractor.exception.WrappedException;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/*
 * functions: create document and clearn html
 * author: qingjiquan
 * date: 2014-10-29
 */
public class HtmlPageParser {
    private static HtmlPageParser singleParser = null;

    public static HtmlPageParser getHtmlPageParser() {
        if (singleParser == null) {
            singleParser = new HtmlPageParser();
        }
        return singleParser;
    }

    private HtmlPageParser() {

    }

    public Document parser(String url, String pageData) {
        String newPageData = htmlClear(pageData);
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new WrappedException(e);
        }
        Document document = null;
        StringReader reader = new StringReader(newPageData);
        try {
            // document = builder.parse(new InputSource(reader));
            DOMParser parser = new DOMParser();
            InputSource is = new InputSource(reader);
            parser.parse(is);
            document = parser.getDocument();
            
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new WrappedException(e);
        }
        return document;
    }

    private String htmlClear(String pageData) {
        String result = "";
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
            result = new PrettyXmlSerializer(props).getXmlAsString(node);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WrappedException(e);
        }
        return result;
    }
}
