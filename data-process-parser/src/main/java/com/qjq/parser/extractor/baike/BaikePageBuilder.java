package com.qjq.parser.extractor.baike;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qjq.parser.extractor.ExtractorPage;
import com.qjq.parser.extractor.builder.DefaultPageBuilder;
import com.qjq.parser.extractor.configuration.ApplicationConfig;
import com.qjq.parser.extractor.domain.PageDesc;
import com.qjq.parser.extractor.html.HtmlPageParser;
import com.qjq.parser.extractor.logger.SysLog;
import com.qjq.parser.extractor.utils.CommonUtil;

/*
 * functions:baike page extractor
 * author:qingjiquan
 * date:2014-11-07
 */
public class BaikePageBuilder extends DefaultPageBuilder {
    final static String CONIF_NAME = "baike";

    public ExtractorPage extract(PageDesc desc, String rawContent) {
        BaikeExtractorPage page = new BaikeExtractorPage();
        page.setUrl(desc.getUrl());
        rawContent = preProcess(rawContent,null);
        HtmlPageParser htmlPageParser = HtmlPageParser.getHtmlPageParser();
        Document document = htmlPageParser.parser(desc.getUrl(), rawContent);
        page.setXmlDoc(document);
        processDoc(page, document);
        return page;
    }

    private String preProcess(String rawContent,BaikeTemplate template){
    	//System.out.println(rawContent);
    	String tags = "P*p|/P*/p|p*p|/p*/p|br*br|BR*br";
    	if(template!=null){
    		tags = template.getTag();
    	}       
    	if (tags == null || tags.equals(""))
            return rawContent;
    	String[] tagList = tags.split("\\|");
    	for(int i=0;i<tagList.length;i++){
    	//	System.out.println("tag="+tagList[i]);
    		String[] list = tagList[i].split("\\*");
    		//System.out.println("~~~"+list[1]);
    		//Pattern pattern = Pattern.compile("\\<"+list[0]+".*\\>");
    		rawContent=rawContent.replaceAll("\\<"+list[0]+"[^\\<]*\\>", "-"+list[1]+"-");
    	}
    	rawContent=rawContent.replaceAll("\\<table[^\\<]*\\>[ ]*.*\\</table\\>", "");
    	rawContent=rawContent.replaceAll("\\<TABLE[^\\<]*\\>[ ]*.*\\</TABLE\\>", "");
    	//System.out.println(rawContent);
    	return rawContent;
    }
    private void processDoc(BaikeExtractorPage page, Document document) {
        String url = page.getUrl();
        BaikeConfiguration configuration = (BaikeConfiguration) ApplicationConfig.getConfigFromApplication().getConf(CONIF_NAME);
        List<BaikeTemplate> templates = configuration.getBaikeTemplatesByUrl(url);
        if (templates == null || templates.size() <= 0) {
            return;
        }
        BaikeTemplate baikeTemplate = extractorTitle(page, document, templates);
        if (baikeTemplate == null)
            return;
        extractAbstract(page, document, baikeTemplate);
        extractParent(page, document, baikeTemplate);
        extractorContent(page, document, baikeTemplate);
        extractorSummary(page, document, baikeTemplate);
        extractorH2(page, document, baikeTemplate);
        extractorH3(page, document, baikeTemplate);
    }

    private void extractParent(BaikeExtractorPage page, Document document, BaikeTemplate template) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String parentXpath = template.getParent();
        String parentUrlPath = template.getParentUrl();
        if (parentXpath == null || parentXpath.equals(""))
            return;
        List<String> strings = new ArrayList<String>();
        List<String> urlLists = new ArrayList<String>();
        try {
            NodeList lists = (NodeList) xPath.evaluate(parentXpath, document, XPathConstants.NODESET);
            for (int i = 0; i < lists.getLength(); i++) {
                Node node = lists.item(i);
                strings.add(node.getTextContent());
            }
            lists = (NodeList) xPath.evaluate(parentUrlPath, document, XPathConstants.NODESET);
            for (int i = 0; i < lists.getLength(); i++) {
                Node node = lists.item(i);
                urlLists.add(node.getTextContent());
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        page.setParentEntries(strings);
        page.setParentUrls(urlLists);
    }

    private void extractAbstract(BaikeExtractorPage page, Document document, BaikeTemplate template) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String abstractXpath = template.getAbstractKey();
        if (abstractXpath == null || abstractXpath.equals(""))
            return;
        StringBuffer value = new StringBuffer();
        try {
            String str = xpath.evaluate(abstractXpath, document);
            value.append(CommonUtil.removeHuanHang(str));
        } catch (XPathExpressionException e) {
        }
        if (value != null && !value.toString().equals("")) {
            page.setAbstractStr(value.toString());
        }
    }

    private BaikeTemplate extractorTitle(BaikeExtractorPage page, Document document, List<BaikeTemplate> templates) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        for (int i = 0; i < templates.size(); i++) {
            BaikeTemplate template = templates.get(i);
            String titleXpath = template.getTitle();
            if (titleXpath == null || titleXpath.equals(""))
                return null;
            StringBuffer value = new StringBuffer();
            try {
                String str = xpath.evaluate(titleXpath, document);
                value.append(CommonUtil.removeHuanHang(str));
            } catch (XPathExpressionException e) {
            }
            if (value != null && !value.toString().equals("")) {
                page.setTitle(value.toString());
                return template;
            }
        }
        return null;
    }
    
    private void extractorContentP(BaikeExtractorPage page, Document document, BaikeTemplate template) {
    	XPath xpath = XPathFactory.newInstance().newXPath();
        String contXpath = template.getContent();
        
        if (contXpath == null || contXpath.equals(""))
            return;
        StringBuffer value = new StringBuffer();
        try {
            String str = xpath.evaluate(contXpath, document);
            if(str.length()<100){
            	return;
            }
            str=str.replaceAll(" ", "");
            String[] list = str.split("\n");
            
            for(int i=0;i<list.length;i++){
            	boolean isText=true;
            	if(list[i].length()<30){
            		if(list[i].indexOf('，')==-1 && list[i].indexOf('。')==-1 && list[i].indexOf('；')==-1){
            			isText=false;
            		}
            	}
            	if(isText){
            		if(value.length()==0){
            			value.append(list[i].trim());
            		}
            		else{
            			value.append("\n"+list[i].trim());
            		}
            	}
            }
            
        } catch (XPathExpressionException e) {
        	SysLog.logError("", "XPathExpressionException::"+e.getMessage());
        }
        if (value != null && !value.toString().equals("")) {
            page.setAllContent(value.toString());
        }
    }
    
    private void extractorContent(BaikeExtractorPage page, Document document, BaikeTemplate template) {
    	XPath xpath = XPathFactory.newInstance().newXPath();
        String contXpath = template.getContent();
        if (contXpath == null || contXpath.equals(""))
            return;
        StringBuffer value = new StringBuffer();
        try {
            String str = xpath.evaluate(contXpath, document);
            str = CommonUtil.removeHuanHang(str);
            str = str.replaceAll("-p-", "");
            String[] list = str.split("-/p-");
            System.out.println("P:"+list.length);
            StringBuffer strBuf = new StringBuffer();
            for(int i=0;i<list.length;i++){
            	if(list[i].equals("") || (list[i].length()<30 && list[i].indexOf("。")==-1 && list[i].indexOf("？")==-1 && list[i].indexOf("！")==-1 && list[i].indexOf("，")==-1&& list[i].indexOf("；")==-1)){
            	    continue;
            	}
            	if(strBuf.length()==0){
            		strBuf.append(list[i].trim());
            	}
            	else{
            		strBuf.append("\r\n"+list[i].trim());
            	}
            }
            str = strBuf.toString();
            String[] brs = str.split("-br-");
            StringBuffer buf = new StringBuffer();
            boolean isP = true;
            for(int i=0;i<brs.length;i++){
            	
            	if(brs[i].equals("") || (brs[i].length()<30 && brs[i].indexOf("。")==-1 && brs[i].indexOf("？")==-1 && brs[i].indexOf("！")==-1 && brs[i].indexOf("，")==-1&& brs[i].indexOf("；")==-1)){
            	    continue;
            	}
            	if(buf.length()==0){
            		buf.append(brs[i].trim());
            	}
            	else if(isP){
            		buf.append("\r\n"+brs[i].trim());
            	}
            	else{
            		buf.append(brs[i].trim());
            	}
            }
            str = buf.toString();
            value.append(str);
            
        } catch (XPathExpressionException e) {
        	SysLog.logError("", "XPathExpressionException::"+e.getMessage());
        }
        if (value != null && !value.toString().equals("")) {
            page.setAllContent(value.toString());
        }
    }
    
    private void extractorSummary(BaikeExtractorPage page, Document document, BaikeTemplate template) {
    	XPath xpath = XPathFactory.newInstance().newXPath();
        String sumXpath = template.getSummary();
        if (sumXpath == null || sumXpath.equals(""))
            return;
        StringBuffer value = new StringBuffer();
        try {
            String str = xpath.evaluate(sumXpath, document);
            str=str.replaceAll(" ", "");
            str = CommonUtil.removeHuanHang(str);
            str=str.replaceAll("请用一段简单的话描述该词条，马上添加摘要。", "");
            str = str.replaceAll("-p-", "");
            String[] list = str.split("-/p-");
            System.out.println("P:"+list.length);
            StringBuffer strBuf = new StringBuffer();
            for(int i=0;i<list.length;i++){
            	if(list[i].equals("") || (list[i].length()<30 && list[i].indexOf("。")==-1 && list[i].indexOf("？")==-1 && list[i].indexOf("！")==-1 && list[i].indexOf("，")==-1&& list[i].indexOf("；")==-1)){
            	    continue;
            	}
            	if(strBuf.length()==0){
            		strBuf.append(list[i].trim());
            	}
            	else{
            		strBuf.append("\r\n"+list[i].trim());
            	}
            }
            str = strBuf.toString();
            String[] brs = str.split("-br-");
            StringBuffer buf = new StringBuffer();
            boolean isP = true;
            for(int i=0;i<brs.length;i++){
            	if(brs[i].equals("") || (brs[i].length()<30 && brs[i].indexOf("。")==-1 && brs[i].indexOf("？")==-1 && brs[i].indexOf("！")==-1 && brs[i].indexOf("，")==-1&& brs[i].indexOf("；")==-1)){
            	    continue;
            	}
            	if(buf.length()==0){
            		buf.append(brs[i].trim());
            	}
            	else if(isP){
            		buf.append("\r\n"+brs[i].trim());
            	}
            	else{
            		buf.append(brs[i].trim());
            	}
            	/*if(brs[i].indexOf("。")==-1 && brs[i].indexOf("？")==-1 && brs[i].indexOf("！")==-1){
            		isP=false;
            	}
            	else{
            		isP=true;
            	}*/
            }
            str = buf.toString();
            value.append(str);
        } catch (XPathExpressionException e) {
        	SysLog.logError("", "XPathExpressionException::"+e.getMessage());
        }
        if (value != null && !value.toString().equals("")) {
            page.setSummary(value.toString());
        }
    }
    
    private void extractorH2(BaikeExtractorPage page, Document document, BaikeTemplate template) {
    	XPath xpath = XPathFactory.newInstance().newXPath();
        String h2Xpath = template.getH2();
        if (h2Xpath == null || h2Xpath.equals(""))
            return;
        StringBuffer value = new StringBuffer();
        try {
            String str = xpath.evaluate(h2Xpath, document);
            str=str.replaceAll(" ", "");
            value.append(str.trim());
        } catch (XPathExpressionException e) {
        	SysLog.logError("", "XPathExpressionException::"+e.getMessage());
        }
        if (value != null && !value.toString().equals("")) {
            page.setH2(value.toString());
        }
    }
    
    private void extractorH3(BaikeExtractorPage page, Document document, BaikeTemplate template) {
    	XPath xpath = XPathFactory.newInstance().newXPath();
        String h3Xpath = template.getH3();
        if (h3Xpath == null || h3Xpath.equals(""))
            return;
        StringBuffer value = new StringBuffer();
        try {
            String str = xpath.evaluate(h3Xpath, document);
            value.append(str);
        } catch (XPathExpressionException e) {
        	SysLog.logError("", "XPathExpressionException::"+e.getMessage());
        }
        if (value != null && !value.toString().equals("")) {
            page.setH3(value.toString());
        }
    }
}
