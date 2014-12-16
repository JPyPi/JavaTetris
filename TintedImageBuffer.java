package Tetris;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class TintedImageBuffer{
	public BufferedImage[] images;
	
	TintedImageBuffer(String image_path, Color[] tint_colors){
		images=new BufferedImage[tint_colors.length];
		for (int i=0;i<tint_colors.length;i++){
			images[i]=loadImage(image_path);
			tint(images[i],tint_colors[i]);
		}
	}
	
	public static void tint(BufferedImage img,Color tint_color) {
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color old_color = new Color(img.getRGB(x, y));
				Color new_color=new Color((int)((old_color.getRed()/255.0)*tint_color.getRed()),
										  (int)((old_color.getGreen()/255.0)*tint_color.getGreen()),
										  (int)((old_color.getBlue()/255.0)*tint_color.getBlue()),
										  old_color.getAlpha());
				img.setRGB(x, y, new_color.getRGB());
			}
		}
	}

	public static BufferedImage loadImage(String ref) {  
		BufferedImage bimg = null;  
		try {
			bimg = ImageIO.read(new File(ref));  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return bimg;  
	}  
}