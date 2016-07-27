package com.qjq.parser.extractor.logger;

import java.io.File;
import java.lang.management.ManagementFactory;

import com.qjq.parser.extractor.utils.FileUtils;

public class SysLog {
	public static final String log="log"+ManagementFactory.getRuntimeMXBean().getName().split("@")[0]+".txt";
	public static final String path="log";
	private static void writeLog(String msg){
		File f = new File(path);
		if(!f.isDirectory()){
			f.mkdir();
		}
		FileUtils.appendFile(msg+"\r\n",path+"/"+log);
	}
	
	public static void logGetRecord(String info){
		String msg = InitSysLogEvent.getRecord(info);
		writeLog(msg);
	}
	
	public static void logGetRecordSuccess(String uid,String url){
		String msg = InitSysLogEvent.getRecordSuccess(uid,url);
		writeLog(msg);
	}
	
	public static void logExtractSuccess(String uid,int size){
		String msg = InitSysLogEvent.extractSuccess(uid, size);
		writeLog(msg);
	}
	
	public static void logWrittenBackSuccess(String uid){
		String msg = InitSysLogEvent.writtenBackSuccess(uid);
		writeLog(msg);
	}
	
	public static void logError(String uid,String emsg){
		String msg = InitSysLogEvent.errorMsg(uid,emsg);
		writeLog(msg);
	}
}
