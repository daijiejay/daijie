package org.daijie.core.util.http;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.swetake.util.Qrcode;

/**
 * 生成二维码工具
 * 
 * @author daijie_jay
 * @since 2018年1月2日
 */
public class QrcodeUtil {
	
	public static final int SIZE_360=360;
	public static final int SIZE_315=315;
	public static final int SIZE_275=275;
	public static final int SIZE_220=220;
	public static final int SIZE_185=185;
	public static final int SIZE_140=140;
	public static final int SIZE_95=95;

	/** 
	 * 生成二维码(QRCode)图片 
	 * @param request 请求对象
	 * @param content 二维码图片的内容
	 * @param logoPath  二维码图片中间的logo路径
	 * @param recodeSize 通过QrcodeUtil.获取
	 * @param color 颜色对象
	 * @return BufferedImage
	 */  
	public static BufferedImage createQRCode(HttpServletRequest request, String content,String logoPath,int recodeSize,Color color) {  
		try {
			//背景
			int backgroundWidth = (int)(recodeSize*1.6);
			int backgroundHeight = backgroundWidth;
			int whiteCircleWidth = (int)(recodeSize*1.5);
			int whiteCircleHeight = whiteCircleWidth;
			BufferedImage background = new BufferedImage(backgroundWidth, backgroundHeight,BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = background.createGraphics();
			//画一个白色圆
			gs.setColor(Color.WHITE);
			gs.fillOval((backgroundWidth-whiteCircleWidth)/2, (backgroundHeight-whiteCircleHeight)/2, whiteCircleWidth, whiteCircleHeight);
			// 设定图像颜色
			gs.setColor(color);
			int qhX = recodeSize/45;
			int count = backgroundWidth/qhX;
			int begin = (backgroundWidth-recodeSize)/2/qhX;
			int end = ((backgroundWidth-recodeSize)/2+recodeSize)/qhX;
			for (int i = 0; i < count; i++) {
				for (int j = 0; j < count; j++) {
					if(i < begin || i > end || j < begin || j > end){
						//判断坐标是否在白色圆内
						int x = i*qhX==0 ? 0: i*qhX+4;
						int y = j*qhX==0 ? 0: j*qhX+4;
						int rgb = background.getRGB(x, y);
						if(((rgb >> 16) & 0xFF) > 240//红
								&& ((rgb >> 8) & 0xFF) > 240//绿
								&& (rgb & 0xFF) > 240){//蓝
							int nextInt = new Random().nextInt(20);
							if((j <= i && nextInt < 14) || (j > i && nextInt < 8)){
								gs.fillOval(j * qhX, i * qhX, qhX, qhX); 
							}
						}
					}
				}
			}
			for (int i = 0; i < backgroundWidth; i++) {
				for (int j = 0; j < backgroundHeight; j++) {
					int rgb = background.getRGB(i, j);
					if(((rgb >> 16) & 0xFF) == 0//红
							&& ((rgb >> 8) & 0xFF) == 0//绿
							&& (rgb & 0xFF) == 0){//蓝
						background.setRGB(i, j, -1);
					}
				}
			}
			gs.setBackground(Color.red);  
			BufferedImage bufImg = create1(request, content, logoPath, recodeSize, color);  
			gs.drawImage(bufImg, (backgroundWidth-recodeSize)/2,  (backgroundHeight-recodeSize)/2,  recodeSize, recodeSize, null);
			bufImg.flush();  
			gs.dispose();  
			return background;
		} catch (Exception e) {  
			e.printStackTrace();  
		}    
		return null;
	}
	
	/**
	 * 普通二维码
	 * @param request 请求对象
	 * @param content 二维码图片的内容
	 * @param logoPath  二维码图片中间的logo路径
	 * @param recodeSize 通过QrcodeUtil.获取
	 * @param color 颜色对象
	 * @return BufferedImage
	 */
	public static BufferedImage create(HttpServletRequest request, String content,String logoPath,int recodeSize,Color color){  
		try {
			int imgWidth=recodeSize;
			int imgHeight=imgWidth;
			int loginWidth=(int)(imgWidth/3);
			int loginHeight=loginWidth;
			
			Qrcode qrcodeHandler = new Qrcode();  
			qrcodeHandler.setQrcodeErrorCorrect('M');  
			qrcodeHandler.setQrcodeEncodeMode('B');  
			qrcodeHandler.setQrcodeVersion(7);  
			byte[] contentBytes = content.getBytes("gbk");  
			BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_INT_RGB);  
			Graphics2D gs = bufImg.createGraphics();  

			gs.setBackground(Color.WHITE);  
			gs.clearRect(0, 0, imgWidth, imgHeight);  

			// 设定图像颜色
			gs.setColor(color);  
			// 设置偏移量 不设置可能导致解析出错  
			int pixoff = 0;  
			int qhX=imgWidth/45;
			
			// 输出内容 > 二维码  
			if (contentBytes.length > 0 && contentBytes.length < 120) {  
				boolean[][] codeOut =               
						qrcodeHandler.calQrcode(contentBytes);  
				for (int i = 0; i < codeOut.length; i++) {  
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {  
							gs.fillRect(j * qhX + pixoff, i * qhX + pixoff, qhX, qhX);  
						}  
					}  
				}  
			} else {  
				System.err.println("QRCode content bytes length = "  
						+ contentBytes.length + " not in [ 0,120 ]. ");  
			}
			if(StringUtils.isNotEmpty(logoPath)){
				Image img = HttpUtil.downloadImage(logoPath);
				//实例化一个Image对象。
				gs.drawImage(img, (imgWidth-loginWidth)/2,  (imgHeight-loginHeight)/2,  loginWidth, loginHeight, null);
			}
			gs.dispose();  
			bufImg.flush();  
			return bufImg;
		} catch (Exception e) {  
			e.printStackTrace();  
		}    
		return null;
	}
	
	/**
	 * 定制二维码（风格1）
	 * @param request 请求对象
	 * @param content 二维码图片的内容
	 * @param logoPath  二维码图片中间的logo路径
	 * @param recodeSize 通过QrcodeUtil.获取
	 * @param color 颜色对象
	 * @return BufferedImage
	 */
	public static BufferedImage create1(HttpServletRequest request, String content,String logoPath,int recodeSize,Color color){  
		try {
			int imgWidth=recodeSize;
			int imgHeight=imgWidth;
			int loginWidth=(int)(imgWidth/3);
			int loginHeight=loginWidth;
			
			Qrcode qrcodeHandler = new Qrcode();  
			qrcodeHandler.setQrcodeErrorCorrect('M');  
			qrcodeHandler.setQrcodeEncodeMode('B');  
			qrcodeHandler.setQrcodeVersion(7);  
			byte[] contentBytes = content.getBytes("gbk");  
			BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_INT_RGB);  
			Graphics2D gs = bufImg.createGraphics();  
			
			gs.setBackground(Color.WHITE);  
			gs.clearRect(0, 0, imgWidth, imgHeight);  
			
			// 设定图像颜色
			gs.setColor(color);  
			// 设置偏移量 不设置可能导致解析出错  
			int pixoff = 0;  
			int qhX=imgWidth/45;
			int maxCircleDiameter = qhX*7-4;
			int minCircleDiameter = qhX*3;
			
			// 输出内容 > 二维码  
			if (contentBytes.length > 0 && contentBytes.length < 120) {  
				boolean[][] codeOut =               
						qrcodeHandler.calQrcode(contentBytes);  
				for (int i = 0; i < codeOut.length; i++) {  
					for (int j = 0; j < codeOut.length; j++) {
						if((i == 0 && j == 0)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+4, j*qhX+4, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 37 && j == 0)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+8, j*qhX+4, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 0 && j == 37)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+4, j*qhX+8, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 2 && j == 2)){
							gs.fillOval(i*qhX+2, j*qhX+2, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i == 39 && j == 2)){
							gs.fillOval(i*qhX+6, j*qhX+2, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i == 2 && j == 39)){
							gs.fillOval(i*qhX+2, j*qhX+6, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i < 7 && j < 7) || (i >= 37 && j < 7) || (i < 7 && j >= 37)){
							continue;
						}
						if (codeOut[j][i]) {  
							gs.fillOval(j * qhX + pixoff, i * qhX + pixoff, qhX, qhX);  
						}  
					}  
				}  
			} else {  
				System.err.println("QRCode content bytes length = "  
						+ contentBytes.length + " not in [ 0,120 ]. ");  
			}
			if(StringUtils.isNotEmpty(logoPath)){
				Image img = HttpUtil.downloadImage(logoPath);
				//实例化一个Image对象。
				gs.drawImage(img, (imgWidth-loginWidth)/2,  (imgHeight-loginHeight)/2,  loginWidth, loginHeight, null);
			}
			gs.dispose();  
			bufImg.flush();  
			return bufImg;
		} catch (Exception e) {  
			e.printStackTrace();  
		}    
		return null;
	}

	/**
	 * 定制二维码（风格2）
	 * @param request 请求对象
	 * @param content 二维码图片的内容
	 * @param logoPath  二维码图片中间的logo路径
	 * @param recodeSize 通过QrcodeUtil.获取
	 * @param color 颜色对象
	 * @return BufferedImage
	 */
	public static BufferedImage create2(HttpServletRequest request, String content,String logoPath,int recodeSize,Color color){  
		try {
			//背景尺寸
			int backgroundWidth = (int)(recodeSize*1.6);
			int backgroundHeight = backgroundWidth;
			//圆尺寸
			int whiteCircleWidth = (int)(recodeSize*1.5);
			int whiteCircleHeight = whiteCircleWidth;
			BufferedImage background = new BufferedImage(backgroundWidth, backgroundHeight,BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = background.createGraphics();
			//画一个白色圆
			gs.setColor(Color.WHITE);
			gs.fillOval((backgroundWidth-whiteCircleWidth)/2, (backgroundHeight-whiteCircleHeight)/2, whiteCircleWidth, whiteCircleHeight);
			gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//设定图像颜色
			gs.setColor(color);  
			//设定图像线性渐变色
//			gs.setPaint(new GradientPaint(whiteCircleWidth/2, 0, color, whiteCircleWidth/2, whiteCircleHeight, new Color(216, 172, 255)));
			gs.setPaint(new GradientPaint(whiteCircleWidth/2, 0, new Color(185, 104, 255), whiteCircleWidth/2, whiteCircleHeight, color));
			//每个小格的尺寸
			int qhX = recodeSize/45;
			int count = backgroundWidth/qhX;
			int begin = (backgroundWidth-recodeSize)/2/qhX;
			int end = ((backgroundWidth-recodeSize)/2+recodeSize)/qhX;
			for (int i = 0; i < count; i++) {
				for (int j = 0; j < count; j++) {
					if(i < begin || i > end || j < begin || j > end){
						//判断坐标是否在白色圆内
						int x = i*qhX==0 ? 0: i*qhX+4;
						int y = j*qhX==0 ? 0: j*qhX+4;
						int rgb = background.getRGB(x, y);
						if(((rgb >> 16) & 0xFF) > 240//红
								&& ((rgb >> 8) & 0xFF) > 240//绿
								&& (rgb & 0xFF) > 240){//蓝
							//随机绘制点
							int nextInt = new Random().nextInt(20);
							if((j <= i && nextInt < 14) || (j > i && nextInt < 8)){
								gs.fillOval(j * qhX, i * qhX, qhX, qhX); 
							}
						}
					}
				}
			}
			//整块画板背景设置白色
			for (int i = 0; i < backgroundWidth; i++) {
				for (int j = 0; j < backgroundHeight; j++) {
					int rgb = background.getRGB(i, j);
					if(((rgb >> 16) & 0xFF) == 0//红
							&& ((rgb >> 8) & 0xFF) == 0//绿
							&& (rgb & 0xFF) == 0){//蓝
						background.setRGB(i, j, -1);
					}
				}
			}
			//logo尺寸
			int loginWidth=(int)(recodeSize/3);
			int loginHeight=loginWidth;
			int start = (backgroundWidth-recodeSize)/2;
			
			Qrcode qrcodeHandler = new Qrcode();  
			qrcodeHandler.setQrcodeErrorCorrect('M');  
			qrcodeHandler.setQrcodeEncodeMode('B');  
			qrcodeHandler.setQrcodeVersion(7);  
			byte[] contentBytes = content.getBytes("gbk");
			
			// 设置偏移量 不设置可能导致解析出错  
			int pixoff = 0;  
			int maxCircleDiameter = qhX*7-4;
			int minCircleDiameter = qhX*3;
			
			// 输出内容 > 二维码  
			if (contentBytes.length > 0 && contentBytes.length < 120) {  
				boolean[][] codeOut =               
						qrcodeHandler.calQrcode(contentBytes);  
				for (int i = 0; i < codeOut.length; i++) {  
					for (int j = 0; j < codeOut.length; j++) {
						if((i == 0 && j == 0)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+2+start, j*qhX+3+start, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 37 && j == 0)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+9+start, j*qhX+3+start, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 0 && j == 37)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+2+start, j*qhX+9+start, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 2 && j == 2)){
							gs.fillOval(i*qhX+1+start, j*qhX+1+start, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i == 39 && j == 2)){
							gs.fillOval(i*qhX+8+start, j*qhX+1+start, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i == 2 && j == 39)){
							gs.fillOval(i*qhX+1+start, j*qhX+8+start, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i < 7 && j < 7) || (i >= 37 && j < 7) || (i < 7 && j >= 37)){
							continue;
						}
						if (codeOut[j][i]) {  
							gs.fillOval(j * qhX + pixoff+start, i * qhX + pixoff+start, qhX, qhX);  
						}  
					}  
				}  
			} else {  
				System.err.println("QRCode content bytes length = "  
						+ contentBytes.length + " not in [ 0,120 ]. ");  
			}
			if(StringUtils.isNotEmpty(logoPath)){
				Image img = HttpUtil.downloadImage(logoPath);
				//实例化一个Image对象。
				gs.drawImage(img, (backgroundWidth-loginWidth)/2,  (backgroundHeight-loginHeight)/2,  loginWidth, loginHeight, null);
			}
			background.flush();  
			gs.dispose();  
			return background;
		} catch (Exception e) {  
			e.printStackTrace();  
		}    
		return null;
	}
	
	/**
	 * 定制二维码（风格3）
	 * @param request 请求对象
	 * @param content 二维码图片的内容
	 * @param logoPath  二维码图片中间的logo路径
	 * @param recodeSize 通过QrcodeUtil.获取
	 * @param color 颜色对象
	 * @return BufferedImage
	 */
	public static BufferedImage create3(HttpServletRequest request, String content,String logoPath,int recodeSize,Color color){
		return null;
	}

	/**
	 * 定制二维码（风格4）
	 * @param request 请求对象
	 * @param content 二维码图片的内容
	 * @param logoPath  二维码图片中间的logo路径
	 * @param recodeSize 通过QrcodeUtil.获取
	 * @param color 颜色对象
	 * @return BufferedImage
	 */
	public static BufferedImage create4(HttpServletRequest request, String content,String logoPath,int recodeSize,Color color){  
		try {
			//背景尺寸
			int backgroundWidth = (int)(recodeSize*1.6);
			int backgroundHeight = backgroundWidth;
			//圆尺寸
			int whiteCircleWidth = (int)(recodeSize*1.5);
			int whiteCircleHeight = whiteCircleWidth;
			BufferedImage background = new BufferedImage(backgroundWidth, backgroundHeight,BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = background.createGraphics();
			//设置图片透明
			background = gs.getDeviceConfiguration().createCompatibleImage(backgroundWidth, backgroundHeight, Transparency.TRANSLUCENT); 
			gs.dispose();
			gs = background.createGraphics();
			//定制一个画二维码内容的区域
			BufferedImage shape = new BufferedImage(backgroundWidth, backgroundHeight,BufferedImage.TYPE_INT_RGB);
			Graphics2D shapeGs = shape.createGraphics();
			//画一个白色圆
			shapeGs.setColor(Color.WHITE);
			shapeGs.fillOval((backgroundWidth-whiteCircleWidth)/2, (backgroundHeight-whiteCircleHeight)/2, whiteCircleWidth, whiteCircleHeight);
			//设定图像颜色
			gs.setColor(color);  
			gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//设定图像线性渐变色
			gs.setPaint(new GradientPaint(0, 0, new Color(199, 96, 185), whiteCircleWidth, whiteCircleHeight, new Color(132, 100, 238)));
			//每个小格的尺寸
			int qhX = recodeSize/45;
			int count = backgroundWidth/qhX;
			int begin = (backgroundWidth-recodeSize)/2/qhX;
			int end = ((backgroundWidth-recodeSize)/2+recodeSize)/qhX;
			for (int i = 0; i < count; i++) {
				for (int j = 0; j < count; j++) {
					if(i < begin || i > end || j < begin || j > end){
						//判断坐标是否在白色圆内
						int x = i*qhX==0 ? 0: i*qhX+4;
						int y = j*qhX==0 ? 0: j*qhX+4;
						int rgb = shape.getRGB(x, y);
						if(((rgb >> 16) & 0xFF) > 240//红
								&& ((rgb >> 8) & 0xFF) > 240//绿
								&& (rgb & 0xFF) > 240){//蓝
							//随机绘制点
							int nextInt = new Random().nextInt(20);
							if((j <= i && nextInt < 10) || (j > i && nextInt < 10)){
								gs.fillOval(j * qhX, i * qhX, qhX, qhX); 
							}
						}
					}
				}
			}
			//logo尺寸
			int loginWidth=(int)(recodeSize/3);
			int loginHeight=loginWidth;
			int start = (backgroundWidth-recodeSize)/2;
			
			Qrcode qrcodeHandler = new Qrcode();  
			qrcodeHandler.setQrcodeErrorCorrect('M');  
			qrcodeHandler.setQrcodeEncodeMode('B');  
			qrcodeHandler.setQrcodeVersion(7);  
			byte[] contentBytes = content.getBytes("gbk");
			
			// 设置偏移量 不设置可能导致解析出错  
			int pixoff = 0;  
			int maxCircleDiameter = qhX*7-4;
			int minCircleDiameter = qhX*3;
			
			// 输出内容 > 二维码  
			if (contentBytes.length > 0 && contentBytes.length < 120) {  
				boolean[][] codeOut =               
						qrcodeHandler.calQrcode(contentBytes);  
				for (int i = 0; i < codeOut.length; i++) {  
					for (int j = 0; j < codeOut.length; j++) {
						if((i == 0 && j == 0)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+2+start, j*qhX+3+start, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 37 && j == 0)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+9+start, j*qhX+3+start, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 0 && j == 37)){
							Stroke stroke = new BasicStroke(7);
							gs.setStroke(stroke);
							gs.drawOval(i*qhX+2+start, j*qhX+9+start, maxCircleDiameter, maxCircleDiameter);
							continue;
						}
						if((i == 2 && j == 2)){
							gs.fillOval(i*qhX+1+start, j*qhX+1+start, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i == 39 && j == 2)){
							gs.fillOval(i*qhX+8+start, j*qhX+1+start, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i == 2 && j == 39)){
							gs.fillOval(i*qhX+1+start, j*qhX+8+start, minCircleDiameter, minCircleDiameter);
							continue;
						}
						if((i < 7 && j < 7) || (i >= 37 && j < 7) || (i < 7 && j >= 37)){
							continue;
						}
						if (codeOut[j][i]) {  
							gs.fillOval(j * qhX + pixoff+start, i * qhX + pixoff+start, qhX, qhX);  
						}  
					}  
				}  
			} else {  
				System.err.println("QRCode content bytes length = "  
						+ contentBytes.length + " not in [ 0,120 ]. ");  
			}
			if(StringUtils.isNotEmpty(logoPath)){
				Image img = HttpUtil.downloadImage(logoPath);
				//实例化一个Image对象。
				gs.drawImage(img, (backgroundWidth-loginWidth)/2,  (backgroundHeight-loginHeight)/2,  loginWidth, loginHeight, null);
			}
			background.flush();  
			gs.dispose(); 
			return background;
		} catch (Exception e) {  
			e.printStackTrace();  
		}    
		return null;
	}
}
