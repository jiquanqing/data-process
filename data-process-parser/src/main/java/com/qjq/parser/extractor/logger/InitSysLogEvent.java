package com.qjq.parser.extractor.logger;
/*
 * function: system init logger
 * author: qingjiquan
 * date: 2014-10-27
 */
public class InitSysLogEvent extends AbstractLogEvent {

	public static final String LOAD_CONFIG = "LOAD_CONFIG";
	public static final String LOAD_CONFIG_SUCCESS = "CONFIG_LOAD_SUCCESS";
	public static final String LOAD_CONFIGURATION_SUCCESS = "CONFIGURATION_LOAD_SUCCESS";
	public static final String LOAD_XPATH_TEMPALTE = "LOAD_XPATH_TEMPLATE";
	public static final String GET_RECORD = "GET_RECORD";
	public static final String GET_RECORD_SUCCESS = "GET_RECORD_SUCCESS";
	public static final String EXTRACT_SUCCESS = "ETXRACT_SUCCESS";
	public static final String WRITTEN_BACK_SUCCESS = "WRITTEN_BACK_SUCCESS";
	public static final String ERROR = "ERROR";

	public static String loadConfigMessage(String baseDir) {
		time = String.valueOf(System.currentTimeMillis());
		String msg = logDefine + "	" + sysName + "	" + time + "	" + LOAD_CONFIG + "load properties from " + baseDir;
		return msg;
	}
	public static String loadConfigSuccess(int confSize){
	    time = String.valueOf(System.currentTimeMillis());
        String msg = logDefine + "  " + sysName + " " + time + "    " + LOAD_CONFIGURATION_SUCCESS + " ,xmlsize=" + confSize;
        return msg;
	}

	public static String loadXpathConfigMessage(String xpathConfigName) {
		time = String.valueOf(System.currentTimeMillis());
		String msg = logDefine + "	" + sysName + "	" + time + "	" + LOAD_CONFIG + "load xpathconfig from " + xpathConfigName;
		return msg;
	}

	public static String loadConfigSuccess(int jobSize, int xpathSize) {
		time = String.valueOf(System.currentTimeMillis());
		String msg = logDefine + "	" + sysName + "	" + time + "	" + LOAD_CONFIG_SUCCESS + "	" + "jobsize=" + jobSize + ",xpathsize=" + xpathSize;
		return msg;
	}
	
	public static String getRecord(String info) {
		time = String.valueOf(System.currentTimeMillis());
		String msg = logDefine + "	" + sysName + "	" + time + "	" + GET_RECORD + "	" + "sqsInfo=" + info;
		return msg;
	}
	
	public static String getRecordSuccess(String uid,String url) {
		time = String.valueOf(System.currentTimeMillis());
		String msg = logDefine + "	" + sysName + "	" + time + "	" + GET_RECORD_SUCCESS + "	" + "uid=" + uid+",url="+url;
		return msg;
	}
	
	public static String extractSuccess(String uid,int contSize) {
		time = String.valueOf(System.currentTimeMillis());
		String msg = logDefine + "	" + sysName + "	" + time + "	" + EXTRACT_SUCCESS + "	" + "uid=" + uid+",contSize="+contSize;
		return msg;
	}
	
	public static String writtenBackSuccess(String uid) {
		time = String.valueOf(System.currentTimeMillis());
		String msg = logDefine + "	" + sysName + "	" + time + "	" + WRITTEN_BACK_SUCCESS + "	" + "uid=" + uid;
		return msg;
	}
	
	public static String errorMsg(String uid,String emsg) {
		time = String.valueOf(System.currentTimeMillis());
		String msg = logDefine + "	" + sysName + "	" + time + "	" + ERROR + "	" + "uid=" + uid+", msg="+emsg;
		return msg;
	}
}
