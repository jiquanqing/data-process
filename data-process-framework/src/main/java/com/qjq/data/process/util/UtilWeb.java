package com.qjq.data.process.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContext;

import freemarker.ext.beans.HashAdapter;
import freemarker.ext.servlet.HttpRequestHashModel;

/**
 * web相关工具方法
 * 
 * @since 0.1.0
 */
public class UtilWeb {
    protected static final Logger logger = LoggerFactory.getLogger(UtilWeb.class);

    public static HttpServletRequest getRequest(Object request) {
        HttpServletRequest req;
        if (request instanceof HttpServletRequest) {
            req = (HttpServletRequest) request;
        } else if (request instanceof HashAdapter) {
            req = ((HttpRequestHashModel) ((HashAdapter) request).getTemplateModel()).getRequest();
        } else if (request instanceof RequestContext) {
            req = UtilObj.getFieldValue(request, "request");
        } else {
            throw new IllegalArgumentException("无法解析HttpServletRequest: " + request);
        }
        return req;
    }

    /**
     * 获取数据从web环境中, 数据来源顺序:
     * <ol>
     * <li>{@linkplain HttpServletRequest#getAttribute(String)}</li>
     * <li>{@linkplain HttpServletRequest#getParameter(String)}</li>
     * <li>{@linkplain HttpSession#getAttribute(String)}</li>
     * <li>{@linkplain ServletContext#getAttribute(String)}</li>
     * </ol>
     */
    private static Object getValue(HttpServletRequest req, String name) {
        Object value = req.getAttribute(name);
        if (UtilString.notEmpty(value, false))
            return value;

        value = req.getParameter(name);
        if (UtilString.notEmpty(value))
            return value;

        value = req.getSession().getAttribute(name);
        if (UtilString.notEmpty(value, false))
            return value;

        return req.getServletContext().getAttribute(name);
    }

    public static Object getValue(Object request, String name) {
        return getValue(getRequest(request), name);
    }

    static Date getDate(HttpServletRequest req, String name, Date defaultValue) {
        Object value = getValue(req, name);
        Date date = defaultValue;
        if (value instanceof Date) {
            date = (Date) value;
        } else if (value instanceof String) {
            date = UtilDate.parseDate(value.toString().trim());
        }
        if (date != null)
            req.getSession().setAttribute(name, date);
        return date;
    }

    /**
     * 签名算法：md5(secret, timestamp, md5(name1, value1, ……, nameDup1, dupV1,
     * dupV2), secret)
     */
    public static String sign(String secret, Object timestamp, String paramsMd5) {
        String sign = UtilString.md5(secret, timestamp.toString(), paramsMd5, secret);
        logger.trace("timestamp={}, paramsMd5={}, sign={}", timestamp, paramsMd5, sign);
        return sign;
    }

    public static Map<String, Object> getParameterMap(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String[]> parameters = request.getParameterMap();
        Map<String, Object> params = new HashMap<String, Object>();
        String value = "";
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            if (null == obj) {
                value = "";
            } else if (obj instanceof String[]) {
                String[] values = (String[]) obj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = value.toString();
            }
            params.put(key, value);
        }
        return params;
    }

}
