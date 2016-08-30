package com.qjq.crawler.download.utils;


public class UrlProcessUtils {
    public static int getUrlDeep(String url) {
        char s[] = url.toCharArray();
        int deep = 0;
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '/')
                deep++;
        }
        if (url.startsWith("http://"))
            deep -= 2;
        if (url.endsWith("/"))
            deep -= 1;
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

        System.out.println(getUrlDeep("blog.sina.com.cn/lm/stock/"));
        System.out.println(getUrlDeep("http://blog.sina.com.cn/u/1092849864"));
    }
}
