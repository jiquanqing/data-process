package com.qjq.crawler.download.matchs.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qjq.crawler.download.utils.HttpRequest;

public class MatchTest {

    public static void main(String[] args) {

        String str = " <li><a href=\"www.baidu.com/articles/weibo_list/3e6r6zAlogin\">登录</a></li><li><a href=\"/articles/weibo_list/3e6r6zAlogin\">登录</a></li>";

        str = HttpRequest.sendGet("http://blog.eastmoney.com/TONYJXM/bloglist_0_1.html", "");
        
        Pattern pattern = Pattern.compile("<a.*?/a>");
        Pattern hrefPattern = Pattern.compile("href=(\"|')(.*?)(\"|'| )");
        
        
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String aStr = matcher.group();
            
            Matcher urlMatcher = hrefPattern.matcher(aStr);
            while(urlMatcher.find()){
                System.out.println(urlMatcher.group(2));
            }
        }
    }
}
