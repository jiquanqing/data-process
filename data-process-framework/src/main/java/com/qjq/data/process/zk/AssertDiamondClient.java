package com.qjq.data.process.zk;

/**
 * 
 * @since 0.1.0
 */
public class AssertDiamondClient {
	
	public static void checkApp(Integer appId) {
		if (appId == null || appId < 1) throw new IllegalArgumentException("appId=" + appId);
	}
	public static void checkAppVer(Integer appId, String version, int verNum) {
		checkApp(appId);
		
		int pos = version.indexOf('.'), pos2 = version.lastIndexOf('.');
		int mark = 1;
		if (pos > 0) mark++;
		if (pos2 > pos) mark++;
		if (mark < verNum) throw new IllegalArgumentException("app.ver=" + version + ", 版本段数=" + verNum);
	}
	
}
