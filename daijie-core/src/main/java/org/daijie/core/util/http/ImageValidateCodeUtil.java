package org.daijie.core.util.http;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 图形验证码工具类
 * @author daijie
 * @date 2017年7月6日
 */
public class ImageValidateCodeUtil {

	public final static String CODE_KEY = "code_key";
	private Random random = new Random();
	private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";// 随机产生的字符串

	private int width = 80;// 图片宽
	private int height = 26;// 图片高
	private int lineSize = 40;// 干扰线数量
	private int stringNum = 4;// 随机产生字符数量
	private String validateCode = "";// 验证码
	
	public ImageValidateCodeUtil(){
		
	}
	
	public ImageValidateCodeUtil(int width, int height, int lineSize, int stringNum){
		this.width = width;
		this.height = height;
		this.lineSize = lineSize;
		this.stringNum = stringNum;
	}

	/*
	 * 获得字体
	 */
	private Font getFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
	}

	/*
	 * 获得颜色
	 */
	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 18);
		return new Color(r, g, b);
	}

	/*
	 * 绘制字符串
	 */
	private String drowString(Graphics g, String randomString, int i) {
		g.setFont(getFont());
		g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
				.nextInt(121)));
		String rand = String.valueOf(getRandomString(random.nextInt(randString
				.length())));
		randomString += rand;
		g.translate(random.nextInt(3), random.nextInt(3));
		g.drawString(rand, 13 * i, 16);
		return randomString;
	}
	
	/**
	 * 获取验证码
	 * @return
	 */
	public String getValidateCode() {
		return this.validateCode;
	}

	/*
	 * 绘制干扰线
	 */
	private void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.drawLine(x, y, x + xl, y + yl);
	}

	/*
	 * 获取随机的字符
	 */
	public String getRandomString(int num) {
		return String.valueOf(randString.charAt(num));
	}

	/**
	 * 生成随机图片
	 */
	public String getRandcode(HttpServletRequest request,HttpServletResponse response) {
		// BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
		g.setColor(getRandColor(110, 133));
		// 绘制干扰线
		for (int i = 0; i <= lineSize; i++) {
			drowLine(g);
		}
		// 绘制随机字符
		for (int i = 1; i <= stringNum; i++) {
			validateCode = drowString(g, validateCode, i);
		}
		g.dispose();
		try {
			ByteArrayOutputStream tmp = new ByteArrayOutputStream();
			ImageIO.write(image, "png", tmp);
			tmp.close();
			Integer contentLength = tmp.size();
			response.setHeader("content-length", contentLength + "");
			response.getOutputStream().write(tmp.toByteArray());// 将内存中的图片通过流动形式输出到客户端
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return validateCode;
	}
}
