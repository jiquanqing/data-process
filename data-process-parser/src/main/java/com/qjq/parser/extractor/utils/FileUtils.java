package com.qjq.parser.extractor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileUtils {
	public static String readFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			return "";

		BufferedReader bd = null;
		StringBuffer buffer = new StringBuffer();
		try {
			FileInputStream fileInput = new FileInputStream(file);
			InputStreamReader inputStrReader = new InputStreamReader(fileInput,
					"UTF-8");
			bd = new BufferedReader(inputStrReader);
			String temp = ""; //
			while ((temp = bd.readLine()) != null) {
				buffer.append(temp);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (bd != null) {
				try {
					bd.close();
				} catch (IOException e) {
				}
			}
		}
		return buffer.toString();
	}
	public static void writeTofile(String content, String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileChannel fc = null;
		try {
			fc = new FileOutputStream(file).getChannel();
			try {
				fc.write(ByteBuffer.wrap(content.getBytes()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} finally {
			try {
				fc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
	
	public static void appendFile( String content, String fileName) {
		FileWriter fc =null;
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
        	fc = new FileWriter(fileName, true);
            fc.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
			try {
				fc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
	public static void appendMethodFile(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
