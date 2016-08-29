package com.qjq.data.process.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @since 0.1.0
 */
public class UtilIO {
	protected static final Logger logger = LoggerFactory.getLogger(UtilIO.class);
	/** 请作为常量使用此对象 */
	public static final FileAttribute<Set<PosixFilePermission>> ATTR_EXEC;
	static {
		Set<PosixFilePermission> perms = new HashSet<>();
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.OWNER_EXECUTE);
		ATTR_EXEC = PosixFilePermissions.asFileAttribute(perms);
	}
	
	private static final long KB = 1024, MB = 1024 * KB;
	public static final long GB = 1024 * MB;
	private static final long TB = 1024 * GB;
	private static final long PB = 1024 * TB;
	private static final long ZB = 1024 * PB;
	public static String prettyByte(Number sizeOfByte) {
		if (sizeOfByte == null) return null;
		
		long size = sizeOfByte.longValue();
		if (size == 0) return "0 KB";
		if (size < KB) return sizeOfByte + " B";
		
		DecimalFormat format = new DecimalFormat("#.##");
		if (size < MB) return format.format(1.0 * size / KB) + "KB";
		if (size < GB) return format.format(1.0 * size / MB) + "MB";
		if (size < TB) return format.format(1.0 * size / GB) + "GB";
		if (size < PB) return format.format(1.0 * size / TB) + "TB";
		if (size < ZB) return format.format(1.0 * size / PB) + "PB";
		return format.format(1.0 * size / ZB) + "ZB";
	}
	
	public static Path mkdirs(Path path) throws IOException {
		return Files.createDirectories(path);
	}
	/** 删除文件或目录 */
	public static void delete(File file) throws IOException {
		delete(file.toPath());
	}
	/** 删除文件或目录 */
	public static synchronized void delete(Path path) throws IOException {
		if (!Files.exists(path)) return;
		
		if (Files.isDirectory(path) && !Files.isSymbolicLink(path)) {
			for (Path child : path) {
				delete(child);
			}
		}
		Files.delete(path);
	}

	public static Path createTempDir(String prefix) throws IOException {
		return Files.createTempDirectory(prefix);
	}
	
	
	public static BufferedReader newBufferedReader(File file, String charsetName) throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(charsetName)));	
	}
	/** 复写目标文件 */
	public static BufferedWriter newBufferedWriter(File target, String charsetName) throws FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), Charset.forName(charsetName)));
	}
	
	/** 改变文件编码 */
	public static void changeCharset(File file, File target, FilenameFilter filter, String from, String to) throws IOException {
		if (from.equalsIgnoreCase(to)) return;
		
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				changeCharset(child, new File(target, child.getName()), filter, from, to);
			}
		} else if (filter == null || filter.accept(file.getParentFile(), file.getName())) {
			if (target.isDirectory()) target = new File(target, file.getName());

			File parent = target.getParentFile();
			if (!parent.exists()) parent.mkdirs();
			
			byte[] text = read(file).toString(from).getBytes(to);
			if (text.length == file.length()) {
				if (!file.equals(target)) Files.copy(file.toPath(), target.toPath());
			} else {
				try (FileOutputStream fout = new FileOutputStream(target)) {
					fout.write(text);
				}
			}
		}
	}
	
	public static void close(AutoCloseable... closeables) {
		for (AutoCloseable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (Exception e) {
					logger.warn("忽略异常 {}", closeable, e);
				}
			}
		}
	}
	
	/**
	 * 读取流为字节
	 * @param in 输入流，本函数不负责关闭
	 * @param size 预计大小
	 */
	public static ByteArrayOutputStream read(InputStream in, Integer size) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(size == null || size < 8192 ? 8192 : size)) {
			byte[] buf = new byte[8192];
			for (int len; (len = in.read(buf)) != -1;) {
				out.write(buf, 0, len);
			}
			return out;
		}
	}
	
	public static ByteArrayOutputStream read(File file) throws IOException {
		long length = file.length();
		if (length > Integer.MAX_VALUE) throw new IllegalArgumentException("文件太大无法读取：" + length);
		
		try (FileInputStream in = new FileInputStream(file)) {
			return read(in, (int) length);
		}
	}
	
	/**
	 * 读取为字符串
	 * @param name 默认按资源读取，如果不存在按文件读取
	 */
	public static ByteArrayOutputStream readText(String name) throws IOException {
		InputStream in = null;
		try {
			in = UtilObj.getClassLoader().getResourceAsStream(name);
			if (in == null) in = new FileInputStream(name);
			return read(in, null);
		} finally {
			close(in);
		}
	}
	public static Properties loadProperties(String name) {
		Properties props = new Properties();
		try (InputStream in = UtilObj.getClassLoader().getResourceAsStream(name)) {
			if (in != null) props.load(in);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return props;
	}
	
	/** 从in读取写入到out，直到in结束（in和out并不关闭） */
	public static long write(InputStream in, OutputStream out) throws IOException {
		long size = 0;
		byte[] buf = new byte[8192];
		for (int len; (len = in.read(buf)) != -1;) {
			size += len;
			out.write(buf, 0, len);
		}
		return size;
	}
	
}
