package org.data.process.utils;

import java.math.BigInteger;

import org.apache.commons.codec.digest.DigestUtils;

public class UidUtils {
    public static String getUid(String url) {
        String uid = "";
        String md5 = DigestUtils.md5Hex(url);

        String pre = md5.substring(0, 16);
        String after = md5.substring(16);

        BigInteger pr = new BigInteger(pre, 16);
        BigInteger af = new BigInteger(after, 16);

        BigInteger res = pr.xor(af);
        uid = res.toString(16);

        return uid;
    }
    
    
}
