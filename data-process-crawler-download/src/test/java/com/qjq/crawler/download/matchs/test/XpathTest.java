package com.qjq.crawler.download.matchs.test;

import java.util.List;

import com.qjq.crawler.download.service.impl.XpathServiceImpl;
import com.qjq.crawler.download.utils.HttpRequest;

public class XpathTest {
    public static void main(String[] args) {
        XpathServiceImpl impl = new XpathServiceImpl();
        String content = HttpRequest.sendGet("http://blog.eastmoney.com/week.html", "");
        List<String> urls = impl.xpath("//li[@class=\"w20_1\"]//@href", content);
        String s = "";
        for (String ss : urls) {
            s += ss + ";";
        }
        System.out.println(s);
    }
}
