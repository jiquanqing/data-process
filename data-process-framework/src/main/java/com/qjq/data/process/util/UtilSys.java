package com.qjq.data.process.util;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * 与系统有关，与环境有关的工具类
 * @since 0.1.0
 */
public class UtilSys {
	public static final Charset UTF_8 = Charset.forName("UTF-8"), ISO8859_1 = Charset.forName("ISO8859-1"),
			GBK = Charset.forName("GBK");
	public static Set<String> BASE_SHELL_VAR = Collections.unmodifiableSet(new HashSet<>(UtilString.split(
			"SSH_CLIENT, MAIL, NLSPATH, XDG_SESSION_ID, SSH_AGENT_PID, XFILESEARCHPATH, SSH_TTY,"
			+ "SSH_CONNECTION, XDG_RUNTIME_DIR, GLADE_PIXMAP_PATH, TERM, SHELL, XDG_MENU_PREFIX, XDG_SESSION_COOKIE,"
			+ "WINDOWID, USER, LS_COLORS, XDG_SESSION_PATH, GLADE_MODULE_PATH, XDG_SEAT_PATH,"
			+ "SSH_AUTH_SOCK, SESSION_MANAGER, DEFAULTS_PATH, XDG_CONFIG_DIRS, PATH, DESKTOP_SESSION,"
			+ "QT_IM_MODULE, PWD, XMODIFIERS, LANG, MANDATORY_PATH, GDMSESSION, SHLVL, HOME, LANGUAGE,"
			+ "LOGNAME, XDG_DATA_DIRS, DBUS_SESSION_BUS_ADDRESS, LESSOPEN, DISPLAY, GLADE_CATALOG_PATH,"
			+ "LIBGLADE_MODULE_PATH, XDG_CURRENT_DESKTOP, GTK_IM_MODULE, LESSCLOSE, COLORTERM, XAUTHORITY,"
			+ "OLDPWD, _", ',')));
	
	/** 获取配置器，优先顺序：env &gt; -D &gt; appProperties */
	public static Properties getSysProp(String appProperties) {
		Properties appProps = UtilIO.loadProperties(appProperties);
		
		Properties jvmProps = new Properties(appProps);
		jvmProps.putAll(System.getProperties());
		
		Properties envProps = new Properties(jvmProps);
		envProps.putAll(System.getenv());
		return envProps;
	}

	public static Map<String, Object> getJvmInfo(boolean includeStack) {
		Map<String, Object> info = new LinkedHashMap<>();
		Runtime runtime = Runtime.getRuntime();
		info.put("processors", runtime.availableProcessors());
		

		long total = runtime.totalMemory(), free = runtime.freeMemory(), used = total - free, max = runtime.maxMemory();
		Map<String, Object> mem = new LinkedHashMap<>();
		mem.put("max", max);
		mem.put("total", total);
		mem.put("free", free);
		mem.put("used", used);
		info.put("mem", mem);
		
		Map<String, Object> memHuman = new LinkedHashMap<>();
		memHuman.put("max", UtilIO.prettyByte(max));
		memHuman.put("total", UtilIO.prettyByte(total));
		memHuman.put("free", UtilIO.prettyByte(free));
		memHuman.put("used", UtilIO.prettyByte(used));
		info.put("memHuman", memHuman);
		
		
		if (includeStack) {
			Map<String, List<String>> allStackTraces = new HashMap<>();
			for (Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
				List<String> traces = new ArrayList<>(entry.getValue().length);
				for (StackTraceElement el : entry.getValue()) {
					traces.add(el.getClassName() + "." + el.getMethodName() + "(" + el.getFileName() + ":" + el.getLineNumber() + ")");
				}
				allStackTraces.put(entry.getKey().toString(), traces);
			}
			info.put("allStackTraces", allStackTraces);
		}
		return info;
	}
	
	private static Integer jvmPid;
	public static Integer getJvmPid() {
		if (jvmPid == null) jvmPid = Integer.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@", 2)[0]);
		return jvmPid;
	}

    /**
     * @return <pre>{
     *  host: 'hostname',
     *  ip: 'hostname's ip'
     * }</pre>
     */
    public static Map<String, String> getHostIp() {
        Map<String, String> result = getNets().get(0);
        result.put("host", result.remove("hostName"));
        return result;
    }

    /**
     * 返回所有网卡信息<br>
     * 主机ip应该是有意义的，且是当前网卡列表中的ip<br>
     * 如果是127，那么取第一个物理网卡的地址
     * 
     * @return <pre>
     * {
     * 	host: 'hostname',
     * 	ip: 'hostname's ip',
     *  nets: [{
     *      name: ''
     *  }]
     * }
     * </pre>
     */
    public static List<Map<String, String>> getNets() {
        List<Map<String, String>> result = new ArrayList<>();
        final Map<String, String> local;

        try {
            InetAddress localNet = InetAddress.getLocalHost();
            String hostName = localNet.getHostName();
            local = new HashMap<>();
            local.put("name", "lo");
            local.put("hostName", hostName);
            local.put("ip", localNet.getHostAddress());

            for (Enumeration<NetworkInterface> iter = NetworkInterface.getNetworkInterfaces(); iter.hasMoreElements();) {
                NetworkInterface net = iter.nextElement();
                if (!net.isUp() || net.isLoopback() || net.isPointToPoint() || net.isVirtual()) {
                    continue;
                }

                for (Enumeration<InetAddress> iter2 = net.getInetAddresses(); iter2.hasMoreElements();) {
                    InetAddress addr = iter2.nextElement();
                    if (addr instanceof Inet4Address) {
                        Map<String, String> one = new LinkedHashMap<>();
                        one.put("name", net.getName());
                        one.put("hostName", addr.getHostName().equals(addr.getHostAddress()) ? hostName : addr.getHostAddress());
                        one.put("ip", addr.getHostAddress());
                        result.add(one);
                    }
                }
            }
        } catch (UnknownHostException | SocketException e) {
			throw new IllegalArgumentException(e);
		}

        if (result.isEmpty()) {
            result.add(local);
            return result;
        }

        // 先匹配(local.hostName + local.ip); 匹配(local.hostName)[name, ip]; 最后(name, ip)
        Collections.sort(result, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                if (UtilObj.compare(o1.get("hostName"), local.get("hostName")) == 0) { // o1匹配local
                    if (UtilObj.compare(o1.get("ip"), local.get("ip")) == 0) { // o1完整匹配local
                        return -1;
                    }
                    
                    if (UtilObj.compare(o2.get("hostName"), local.get("hostName")) != 0) { // o2不匹配local
                        return -1;
                    }
                    
                    if (UtilObj.compare(o2.get("ip"), local.get("ip")) == 0) {// o2完整匹配local
                        return 1;
                    }
                } else if (UtilObj.compare(o2.get("hostName"), local.get("hostName")) == 0) { // o1不匹配，o2匹配local
                    return 1;
                }
                
                return (o1.get("name") + "=" + o1.get("ip")).compareTo(o2.get("name") + "=" + o2.get("ip"));
            }
        });
        return result;
	}
}
