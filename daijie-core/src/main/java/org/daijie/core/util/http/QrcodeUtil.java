package org.daijie.core.util.http;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.swetake.util.Qrcode;

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
	 * @param content 二维码图片的内容
	 * @param recodeSize 通过QrcodeUtil.获取
	 */  
	public static BufferedImage createQRCode(String content,int recodeSize){
		try {
			int imgWidth=recodeSize;
			int imgHeight=imgWidth;
			Qrcode qrcodeHandler = new Qrcode();  
			qrcodeHandler.setQrcodeErrorCorrect('M');  
			qrcodeHandler.setQrcodeEncodeMode('B');  
			qrcodeHandler.setQrcodeVersion(7);  
			byte[] contentBytes = content.getBytes("gbk");  
			BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_BYTE_BINARY);  
			Graphics2D gs = bufImg.createGraphics();  

			gs.setBackground(Color.WHITE);  
			gs.clearRect(0, 0, imgWidth, imgHeight);  

			// 设定图像颜色 > BLACK  
			gs.setColor(Color.BLACK);  
			// 设置偏移量 不设置可能导致解析出错  
			int pixoff = 2;  
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
			gs.dispose();  
			bufImg.flush();  
			return bufImg;
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return null;
	}

	/** 
	 * 生成二维码(QRCode)图片 
	 * @param content 二维码图片的内容
	 * @param ccbpath  二维码图片中间的logo路径
	 * @param recodeSize 通过QrcodeUtil.获取
	 */  
	public static BufferedImage createQRCode(HttpServletRequest request, String content,String logoPath,int recodeSize) {  
		try {
			int imgWidth=recodeSize;
			int imgHeight=imgWidth;
			int loginWidth=(int)(imgWidth/3.5);
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

			// 设定图像颜色 > BLACK  
			gs.setColor(Color.BLACK);  
			// 设置偏移量 不设置可能导致解析出错  
			int pixoff = 2;  
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
			Image img = HttpUtil.downloadImage(logoPath, request);
			//实例化一个Image对象。
			gs.drawImage(img, (imgWidth-loginWidth)/2,  (imgHeight-loginHeight)/2,  loginWidth, loginHeight, null);
			gs.dispose();  
			bufImg.flush();  
			return bufImg;
		} catch (Exception e) {  
			e.printStackTrace();  
		}    
		return null;
	}



	/**
	 * 生成二维码(QRCode)图片
	 * @param content 二维码图片的内容
	 * @param ccbpath  二维码图片中间的logo路径
	 * @param recodeSize 通过QrcodeUtil.获取
	 */
	public static BufferedImage createQRCodeSmall(String content,String logoPath,int recodeSize) {
		try {
			int imgWidth=recodeSize;
			int imgHeight=imgWidth;
			int loginWidth=(int)(imgWidth/5.5);
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

			// 设定图像颜色 > BLACK
			gs.setColor(Color.BLACK);
			// 设置偏移量 不设置可能导致解析出错
			int pixoff = 2;
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
			File logoFile = new File(logoPath);
			Image img = ImageIO.read(logoFile);
			//实例化一个Image对象。
			gs.drawImage(img, (imgWidth-loginWidth)/2,  (imgHeight-loginHeight)/2,  loginWidth, loginHeight, null);
			gs.dispose();
			bufImg.flush();
			return bufImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
