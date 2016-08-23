package com.qjq.crawler.download.zk;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ZkParamContext {

    private static final Map<ZkParam, String> paramMap = new ConcurrentHashMap<ZkParam, String>();
    
    private static Properties properties = new Properties();
    
    /** 
     * 获取配置值，通常配置key已定义时调用该方法。 <br/> 
     * 
     * @author hdcheng
     * @date: 2016年2月16日 上午10:33:57
     * @version 1.0
     *
     * @param param  枚举类型，使用前请定义
     * @return
     */ 
    public static String getParamValue(ZkParam param){
        return paramMap.get(param);
    }
    
    /** 
     * 获取配置值，通常是在配置key无法提前定义的情况下调用该方法. <br/> 
     * 
     * @author hdcheng
     * @date: 2016年2月16日 上午10:34:31
     * @version 1.0
     *
     * @param param
     * @return
     */ 
    public static String getDynamicParamValue(String param){
        return properties.getProperty(param, "");
    }
    
    public static String getParamValue(String param){
        try {
            ZkParam zkParam = ZkParam.parseZkParam(param);
            return paramMap.get(zkParam);
        } catch (Exception e) {
            return "";
        }
    }
    
    public static Set<ZkParam> getAllParam(){
        return paramMap.keySet();
    }
    
    public static String updateParam(ZkParam param, String value){
        return paramMap.put(param, value);
    }
    
    public static String deleteParam(ZkParam param){
        return paramMap.remove(param);
    }
    
    public static void clearParam(){
        paramMap.clear();
    }

    public static void updateDynamicParams(Properties props) {
        properties = props;
    }
    
    public static Properties getDynamicParams(){
        return properties;
    }
}
