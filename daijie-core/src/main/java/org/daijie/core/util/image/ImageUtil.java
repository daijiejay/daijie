package org.daijie.core.util.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.daijie.core.util.http.HttpUtil;


/**
 * <b> </b>
 * <p>
 * 功能:
 * </p>
 * 
 * @作者 stone
 * @创建时间 2016年7月19日 下午2:40:59
 * @修改内容
 * @修改时间
 */
public class ImageUtil {
    
    /**
     * 重新设置图像大小
     * @param sfile 图片源文件
     * @param tfile 目标文件
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @param quality 质量系数
     * @return 
     * @throws IOException
     */
    public static BufferedImage resizeImg(String sfile, int newWidth,
    		int newHeight, float quality) throws IOException {
    	Image srcImg = HttpUtil.downloadImage(sfile);
    	if(srcImg == null){
    		srcImg = new ImageIcon(sfile).getImage();
    	}
    	return resizeImg(srcImg, newWidth, newHeight, quality);
    }
    
    /**
     * 重新设置图像大小
     * @param image 文件
     * @param newWidth 新宽度
     * @param newHeight 新高度
     * @param quality 质量系数
     * @return 
     * @throws IOException
     */
    public static BufferedImage resizeImg(Image image, int newWidth,
    		int newHeight, float quality) throws IOException {
    	if(quality > 1) {  
    		throw new IllegalArgumentException(  
    				"Quality has to be between 0 and 1");  
    	}
    	if(newWidth <=0 || newHeight<=0) {
    		throw new IllegalArgumentException(  
    				"Width or Height must greater than zero");
    	}
    	//This code ensures that all the pixels in the image are loaded.
    	Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    	// This code ensures that all the pixels in the image are loaded.  
    	Image temp = new ImageIcon(resizedImage).getImage();  
    	// Create the buffered image.  
    	BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
    	// Copy image to buffered image.  
    	Graphics g = bufferedImage.createGraphics();  
    	// Clear background and paint the image.  
    	g.setColor(Color.white);  
    	g.fillRect(0, 0, newWidth, newHeight);  
    	g.drawImage(temp, 0, 0, null);  
    	g.dispose();  
    	// Soften.  
    	float softenFactor = 0.05f;  
    	float[] softenArray = { 0, softenFactor, 0, softenFactor,  
    			1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };  
    	Kernel kernel = new Kernel(3, 3, softenArray);  
    	ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);  
    	bufferedImage = cOp.filter(bufferedImage, null);
    	return bufferedImage;
    }

    /**
     * 
     * @param sfile 图片源文件
     * @param cornerRadius 圆角90/180/360
     * @return
     * @throws IOException
     */
    public static BufferedImage makeRoundedCornerImg(String sfile,
            int cornerRadius) throws IOException {
        BufferedImage image = ImageIO.read(new File(sfile));
        return makeRoundedCornerImg(image, cornerRadius);
    }
    
    /**
     * 
     * @param image 文件
     * @param cornerRadius 圆角90/180/360
     * @return
     * @throws IOException
     */
    public static BufferedImage makeRoundedCornerImg(BufferedImage image,
    		int cornerRadius) throws IOException {
    	int w = image.getWidth();
    	int h = image.getHeight();
    	BufferedImage output = new BufferedImage(w, h,
    			BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2 = output.createGraphics();
    	// This is what we want, but it only does hard-clipping, i.e. aliasing
    	// g2.setClip(new RoundRectangle2D ...)
    	// so instead fake soft-clipping by first drawing the desired clip shape
    	// in fully opaque white with antialiasing enabled...
    	g2.setComposite(AlphaComposite.Src);
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    			RenderingHints.VALUE_ANTIALIAS_ON);
    	g2.setColor(Color.WHITE);
    	g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
    			cornerRadius));
    	// ... then compositing the image on top,
    	// using the white shape from above as alpha source
    	g2.setComposite(AlphaComposite.SrcAtop);
    	g2.drawImage(image, 0, 0, null);
    	g2.dispose();
    	return output;
    }
	
	/**
	 * 
	 * @param image 文件
	 * @param tfile 写入本地文件路径
	 * @throws IOException
	 */
	public static void write(BufferedImage image, String tfile) throws IOException{
		try {
			ImageIO.write(image, "png", new File(tfile));
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

//    public static void main(String[] args) throws IOException {  
//        String filepath = "C:\\Users\\daijie\\Pictures\\";
//        ImageUtil.write(ImageUtil.resizeImg(filepath+"2.png", 256, 256, 1.0f), filepath+"1-256-256-1.png", 1.0f);
//        ImageUtil.write(ImageUtil.makeRoundedCornerImg(filepath+"1-256-256-1.png", 360), filepath+"2-256-256-360.png");
//
//    }  
}