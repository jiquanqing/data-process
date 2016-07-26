package com.qjq.crawler.download.matchs.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchTest {

    public static void main(String[] args) {

        String str = " <li><a href=\"/articles/weibo_list/3e6r6zAlogin\">登录</a></li>";

        Pattern pattern = Pattern.compile("<a.*?href=[\"']?((https?://)?/?[^\"']+)[\"']?.*?>(.+)</a>");

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }

    }
}
