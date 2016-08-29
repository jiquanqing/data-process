package com.qjq.data.process.zk;

import java.util.List;
import java.util.Map;

/**
 * app地址变更监听器
 * 
 * @since 0.1.0
 */
public interface AppEndpointChangeListener {

    /**
     * @param endpointsByVer Map&lt;版本, List&lt;endpoint&gt;&gt;
     */
    void onDataChange(Map<String, List<String>> endpointsByVer);
}
