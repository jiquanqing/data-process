package com.qjq.crawler.download.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.qjq.crawler.download.service.ExtendsHtmlUrlService;
import com.qjq.crawler.download.utils.UrlProcessUtils;

@Service
public class ExtendsHtmlUrlServiceImpl implements ExtendsHtmlUrlService {

    // 获取扩展url的正则 是否可以考虑用xpaths
    private static Pattern url_pattern = Pattern.compile("<a.*?/a>");
    private static Pattern href_pattern = Pattern.compile("href=(\"|')(.*?)(\"|'| )");

    @Override
    public List<String> handle(String content) {

        List<String> res = new ArrayList<String>();
        Matcher matcher = url_pattern.matcher(content);
        while (matcher.find()) {
            String aStr = matcher.group();

            Matcher urlMatcher = href_pattern.matcher(aStr);
            while (urlMatcher.find()) {
                String url = urlMatcher.group(2);
                res.add(filterUrl(url));
            }
        }
        return res;
    }

    // 判断扩展的url是站内还是站外
    public Boolean judeUrlIsHost(String baseUrl, String extendUrl) {
        
        baseUrl = UrlProcessUtils.getBaseUrl(baseUrl);
        extendUrl = UrlProcessUtils.getBaseUrl(extendUrl);
 
        if (baseUrl.equalsIgnoreCase(extendUrl)) {
            return true;
        } else {
            return false;
        }
    }

    public String filterUrl(String url) {
        url = url.replaceAll("\"", "");
        url = url.replaceAll("'", "");
        url = url.trim();
        return url;
    }
    public static void main(String[] args) {
        ExtendsHtmlUrlServiceImpl extendsHtmlUrlServiceImpl = new ExtendsHtmlUrlServiceImpl();
        System.out.println(extendsHtmlUrlServiceImpl.judeUrlIsHost("http://www.baidu.com", "www.baidu.com/blog"));
    }
}
