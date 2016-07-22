package com.qjq.crawler.download.service;

import org.data.process.mqmodel.DownLoadMessage;

public interface DownLoadService {

    public void addSeed(String url, String jobId);

    public void schudel(DownLoadMessage message);
}
