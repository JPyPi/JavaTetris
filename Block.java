package Tetris;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

//import java.awt.image.BufferedImage;

import java.awt.Rectangle;

public class Block{
	public static final int block_size=20;

	public int width,height;
	public int x,y;
	private int rel_x,rel_y;
	public Image image;
	//public BufferedImage image;
	
	public Block(int color_id, int relative_x, int relative_y){
		image=Board.tint_buffer.images[color_id];
		/*image=loadImage(this.getClass().getResource("./fancy4.png").getPath());
		tint(image,color);*/
		//ImageIcon ii=new ImageIcon(this.getClass().getResource("fancy4.png"));
		//image=ii.getImage();
		
		width=block_size;//image.getWidth(null);
		height=block_size;//image.getHeight(null);
		
		//this.color=color;
		x=relative_x*block_size;
		y=relative_y*block_size;
		rel_x=relative_x;
		rel_y=relative_y;
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x,y,width,height);
	}
	
	public int getRelX(){return rel_x;}
	public void setRelX(int new_rel_x){
		x+=(new_rel_x-rel_x)*block_size;
		rel_x=new_rel_x;
	}
	
	public int getRelY(){return rel_y;}
	public void setRelY(int new_rel_y){
		y+=(new_rel_y-rel_y)*block_size;
		rel_y=new_rel_y;
	}
	
	
	

}