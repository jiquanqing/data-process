package com.qjq.crawler.service;

import java.util.Map;

public interface DonwloadService {
    
    public String donwload(String url);
    
    public String donwload(String url,Map<String, Object> params);
    
}
