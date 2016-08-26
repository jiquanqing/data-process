package com.qjq.crawler.download.service;

import java.util.List;

public interface ExtendsHtmlUrlService {

    public List<String> handle(String content);

    public Boolean judeUrlIsHost(String baseUrl, String extendUrl);
}
