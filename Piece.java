package Tetris;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Piece{
	private static final char is_block_char='x';
	public Block[] blocks;
	private int width=0,height=0;
	private boolean locked_blocks;
	//private Color color;
	
	public Piece(String arrangement,int color_id, boolean lock_blocks){
		locked_blocks=lock_blocks;
		//this.color=new Color(color.getRed(),color.getGreen(),color.getBlue(),0);
		char[] chr_arrangement=arrangement.toLowerCase().toCharArray();
				
		int num_blocks=0;
		for (char c: chr_arrangement){
			if (c==is_block_char) num_blocks+=1;
		}
		blocks=new Block[num_blocks];
		
		
		int i=0,x=0,y=0;
		for (char c: chr_arrangement){
			if (c==is_block_char){
				blocks[i]=new Block(color_id,x,y);
				i++;
			}
			if (c=='\n'){
				y+=1;
				height+=1;
				x=0;
			}else{
				x+=1;
				if (x>width) width=x;
			}
		}
		
	}
	public Piece(String arrangement){this(arrangement,0,true);}
	
	public void draw(Graphics2D screen,JPanel p){
		//screen.setXORMode(color);
		for (Block b: blocks){
			if (b!=null)
				screen.drawImage(b.image,b.x,b.y,p);
		}
	}
	
	public void move(int x, int y){//Move the whole peice by moving all the blocks that make it up.
		for (Block b: blocks){
			if (b!=null){
				b.x+=x;
				b.y+=y;
			}
		}
	}
	
	public void rotateRight(){
		for (Block b: blocks){
			int old_rel_x=b.getRelX();
			b.setRelX(height-b.getRelY());
			b.setRelY(old_rel_x);
		}
	}
	
	public void rotateLeft(){
		for (Block b: blocks){
			int old_rel_x=b.getRelX();
			b.setRelX(b.getRelY());
			b.setRelY(width-old_rel_x);
		}
	}
	
	public int getTop(){
		int top=0;
		boolean found_top=false;
		for (Block b: blocks){
			if (b!=null && b.y<top || found_top==false){
				top=b.y;
				found_top=true;
			}
		}
		return top;
	}
	
	public int getBottom(){
		int bottom=0;
		for (Block b: blocks){
			if (b!=null && b.y+Block.block_size>bottom){
				bottom=b.y+Block.block_size;
			}
		}
		return bottom;
	}
	
	public int getLeft(){
		int left=0;
		boolean found_left=false;
		for (Block b: blocks){
			if (b!=null && b.x<left || found_left==false){
				left=b.x;
				found_left=true;
			}
		}
		return left;
	}
	
	public int getRight(){
		int right=0;
		for (Block b: blocks){
			if (b!=null && b.x+Block.block_size>right){
				right=b.x+Block.block_size;
			}
		}
		return right;
	}
	
	public boolean checkCollision(Piece other_piece){
		for (Block b: blocks){
			for (Block other_b: other_piece.blocks){
				if (b!=null && other_b!=null && b.getBounds().intersects(other_b.getBounds())){
					return true;
				}
			}
		}
		return false;
	}
	
	public int getWidth(){	
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}