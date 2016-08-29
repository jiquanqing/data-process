package com.qjq.crawler.download.zk.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bubugao.diamond.client.DiamondClient;
import com.qjq.crawler.download.jms.test.AbstractTest;
import com.qjq.crawler.download.zk.ZkHandler;

public class ZkTest extends AbstractTest {

    @Autowired
    ZkHandler handler;
    @Autowired
    DiamondClient diamondClient;
    
    @Test
    public void test() {
        System.out.println(diamondClient.isAppMaster());
    }
}
