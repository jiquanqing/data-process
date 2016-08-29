package com.qjq.crawler.download.js.test;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import junit.framework.TestCase;

public class HelloHtmlUnit extends TestCase {

    @Test
    public void test1() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        String str;
        // 创建一个webclient
        WebClient webClient = new WebClient();
        // htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        // 获取页面
        HtmlPage page = webClient.getPage("http://www.baidu.com/");
        // 获取页面的TITLE
        str = page.getTitleText();
        System.out.println(str);
        // 获取页面的XML代码
        str = page.asXml();
        System.out.println(str);
        // 获取页面的文本
        str = page.asText();
        System.out.println(str);
        // 关闭webclient
        webClient.closeAllWindows();
    }

    @Test
    public void testClick() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        final HtmlPage page = webClient.getPage("http://blog.jrj.com.cn/i,xwtk.html");

        System.out.println(page.asXml());
        
        /*ScriptResult scriptResult = page.executeJavaScript("async_articleList(1,76351,2,'9534648948')");
        Page page2 = scriptResult.getNewPage();
        if (page2.isHtmlPage()) {
            System.out.println(scriptResult.toString());
            HtmlPage htmlPage = (HtmlPage) page2;
            System.out.println(htmlPage.asXml());
        }
*/
        webClient.closeAllWindows();
    }
}
