package com.qjq.crawler.download.utils;

import com.qjq.crawler.download.service.impl.DownLoadServiceImpl;

public class UrlProcessUtils {
    public static int getUrlDeep(String url) {
        char s[] = url.toCharArray();
        int deep = 0;
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '/')
                deep++;
        }
        return deep;
    }

    public static String getBaseUrl(String url) {
        String base = "";
        if (url.startsWith("http://")) {
            url = url.replaceAll("http://", "");
        }
        if (url.contains("/"))
            base = url.substring(0, url.indexOf("/"));
        else
            base = url;
        return base;
    }

    public static void main(String[] args) {
        System.out.println(getBaseUrl("www.baidu.com"));
        System.out.println(getBaseUrl("http://www.baidu.com/p/fsd"));
        System.out.println(getBaseUrl("http://www.baidu.com/"));
    }
}
