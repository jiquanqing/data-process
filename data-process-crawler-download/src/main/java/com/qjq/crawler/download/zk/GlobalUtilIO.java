package com.qjq.crawler.download.zk;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;


public class GlobalUtilIO {

    /** 
     * 加载classpath中的properties文件，可以指定字符集. <br/> 
     * 
     * @author hdcheng
     * @date: 2016年1月15日 下午4:18:59
     * @version 1.0
     *
     * @param name  文件名
     * @param charset 例如"UTF-8"
     * @return
     */ 
    public static Properties loadProperties(String name, String charset){
        Properties props = new Properties();
        try {
            InputStream reader = GlobalUtilIO.class.getClassLoader().getResourceAsStream(name);
            InputStreamReader r = new InputStreamReader(reader,  Charset.forName(charset));
            props.load(r);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return props;
    }
/*    public static void main(String[] args){
        Properties p =  GlobalUtilIO.loadProperties("zkData.properties", "UTF-8");
        System.out.println(p);
    }*/
}
