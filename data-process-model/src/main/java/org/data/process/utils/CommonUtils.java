package org.data.process.utils;

public class CommonUtils {
    public static String creatJobId(String baseUrl) {
        String base = baseUrl + System.currentTimeMillis();
        return UidUtils.getUid(base);
    }

}
