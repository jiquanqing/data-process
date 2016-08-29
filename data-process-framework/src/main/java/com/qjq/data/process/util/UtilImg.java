package com.qjq.data.process.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 图形工具
 * @since 0.1.0
 */
public class UtilImg {
	private static Random random = new Random();
	/**
	 * 产生一个随机的颜色
	 * @param fc 颜色分量最小值
	 * @param bc 颜色分量最大值
	 */
	private static Color getRandColor(int fc, int bc) {
		if (fc > 255) fc = 255;
		if (bc > 255) bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	/**
	 * @param width 宽度
	 * @param height 高度
	 * @param code 字符串
	 * @param output 输出流
	 */
	public static void writerImg(int width, int height, String code, OutputStream output) throws IOException {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		try {
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			for (int i = 0; i < 5; i++) {
				g.setColor(getRandColor(50, 100));
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				g.drawOval(x, y, 4, 4);
			}
			g.setFont(new Font("", Font.PLAIN, 36));
			for (int i = 0, len = code.length(); i < len; i++) {
				Character ch = code.charAt(i);
				g.setColor(new Color(20 + random.nextInt(80), 20 + random.nextInt(100), 20 + random.nextInt(90)));
				g.drawString(ch.toString(), (20 + random.nextInt(3)) * i, 34);
				for (int k = 0; k < 12; k++) { // 生成干扰线
					int x = random.nextInt(width);
					int y = random.nextInt(height);
					int xl = random.nextInt(9);
					int yl = random.nextInt(9);
					g.drawLine(x, y, x + xl, y + yl);
				}
			}
		} finally {
			g.dispose();
		}
		ImageIO.write(img, "JPEG", output); 
	}
}
