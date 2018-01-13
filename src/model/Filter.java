package model;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
/**
 * 
 * @author leonardoroman
 *
 */
public class Filter {
	public static Image flipVertically(String image) throws IOException {
		File photoFile = new File(image);
		BufferedImage bi = ImageIO.read(photoFile);
		BufferedImage bi2 = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < bi.getHeight(); i++) {
			for(int j = 0, k = bi.getWidth()-1; j < bi.getHeight(); j++,k--) {
				int p = bi.getRGB(j, i);
				bi2.setRGB(j, i, p);
				bi2.setRGB(k, i, p);
			}
		}
		Image img =  SwingFXUtils.toFXImage(bi2, null);
		return img;
	}
	/**
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static Image flipHorizontally(String image) throws IOException {
		File photoFile = new File(image);
		BufferedImage bi = ImageIO.read(photoFile);
		BufferedImage bi2 = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < bi.getWidth(); i++) {
			for(int j = 0, k = bi.getHeight()-1; j < bi.getHeight()/2; j++,k--) {
				int p = bi.getRGB(i, j);
				bi2.setRGB(i, j, p);
				bi2.setRGB(i, k, p);
			}
		}
		Image img =  SwingFXUtils.toFXImage(bi2, null);
		return img;
	}
	/**
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static Image flipDiagonally(String image) throws IOException {
		File photoFile = new File(image);
		BufferedImage bi = ImageIO.read(photoFile);
		int width = bi.getWidth();
		int height = bi.getHeight();
		if(width>height) {
			width = height;
		}else {
			height = width;
		}
		BufferedImage bi2 = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < width; i++) {
			for(int j = i; j < height; j++) {
				int p = bi.getRGB(width - 1 -j, i);
				bi2.setRGB(height - 1 -j, i, p);
				bi2.setRGB(width -1 -i, j, p);
			}
		}
		Image img =  SwingFXUtils.toFXImage(bi2, null);
		return img;
	}
}
