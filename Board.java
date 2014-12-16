package Tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.Dimension;
import javax.swing.BorderFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.lang.Math;

public class Board extends JPanel implements ActionListener, KeyListener{
	private List<Piece> pieces=new ArrayList<Piece>();
	private Piece active_piece=null;
	private long last_active_move=0;
	private int normal_move_delay=900,fast_move_delay=50;
	private int piece_move_delay=normal_move_delay;
	
	private final int FPS=40;
	private final int DELAY=1000/FPS;
	private Timer timer;
	
	private final int GAME_WIDTH=240;
	private final int GAME_HEIGHT=320;
	
	private boolean kUP=false,kDOWN=false,kLEFT=false,kRIGHT=false,kSPACE=false;
	private long last_horizontal_move=0;
	private int horizontal_move_delay=200;
	
	private boolean rotate_debouce=false;
	
	private String[] piece_types={"XXXX",//light blue
								  "X\nXXX",//orange
								  " X\nXXX",//purple
								  "  X\nXXX",//blue
								  "xx\n xx",//green
								  " xx\nxx",//red
								  "XX\nXX"};//yellow
	private Color[] piece_colors={new Color(0,213,255),//
								  new Color(255,106,0),//
								  new Color(151,0,206),//
								  new Color(0,45,226),//
								  new Color(0,225,0),//
								  new Color(224,0,0),//
								  new Color(255,216,0)};//
								  
	public static TintedImageBuffer tint_buffer;
	
	public static int randRange(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	public Board(){
		setSize(new Dimension(GAME_WIDTH,GAME_HEIGHT)); //Pocket pc screen resolution is 240x320
		setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
		System.out.println(getSize());
		//addKeyListener(new TAdapter());
		addKeyListener(this);
		setBackground(Color.black);//new Color(180,240,180));
		setDoubleBuffered(true);
		
		String p=this.getClass().getResource("fancy4.png").getPath().replace("%20"," ");
		System.out.println(p);
		tint_buffer=new TintedImageBuffer(p,piece_colors);
		addNewPiece();
		
		setFocusable(true);
		requestFocus();
		timer=new Timer(DELAY,this);
		timer.start();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D screen=(Graphics2D) g;
		for (int i=0;i<pieces.size();i++){
			Piece p=(Piece) pieces.get(i);
			p.draw(screen,this);
		}
		g.dispose();
	}
	
	public void checkKeyEvents(long current_time){
		if (kLEFT && active_piece.getLeft()>0 && last_horizontal_move<current_time){
			active_piece.move(-Block.block_size,0);
			last_horizontal_move=current_time+horizontal_move_delay;
		}
		if (kRIGHT && active_piece.getRight()<GAME_WIDTH && last_horizontal_move<current_time) {
			active_piece.move(Block.block_size,0);
			last_horizontal_move=current_time+horizontal_move_delay;
		}
		if (kUP){
			if (rotate_debouce==false){
				active_piece.rotateRight();
				if (active_piece.getLeft()<0 || active_piece.getRight()>GAME_WIDTH)
					active_piece.rotateLeft();
				rotate_debouce=true;
			}
		}else if (rotate_debouce){
			rotate_debouce=false;
		}
		if (kSPACE){
			if (piece_move_delay==normal_move_delay) last_active_move=0;
			piece_move_delay=fast_move_delay;
			
		}else if (piece_move_delay==fast_move_delay){
			piece_move_delay=normal_move_delay;
		}
	}
	
 	public void actionPerformed(ActionEvent e){
		long current_time=System.currentTimeMillis();
		checkKeyEvents(current_time);
		
		boolean piece_stopped=false;
		if (e.getSource().equals(timer) && active_piece!=null){
			if (last_active_move<current_time){//Move the active piece
				active_piece.move(0,Block.block_size);
				last_active_move=current_time+piece_move_delay;
			}
			if (active_piece.getBottom()>GAME_HEIGHT){//Use greater than so that we get enough time to slide the piece under another one (aka: move past the bound first then put us back)
				active_piece.move(0,-(active_piece.getBottom()-GAME_HEIGHT));
				piece_stopped=true;
			}
			for (Piece p: pieces){
				if (p!=active_piece && active_piece.checkCollision(p)){
					active_piece.move(0,-Block.block_size);
					piece_stopped=true;
					break;
				}
			}
			if (piece_stopped){
				if (active_piece.getTop()<=0){
					endGame();
				}else{
					addNewPiece();
				}
			}
		}
		repaint();
	}
	
	public void endGame(){
		System.out.println("Game over!");
		timer.stop();
	}
	
	private void addNewPiece(){
		clearLine();
		int piece_type_id=randRange(0,piece_types.length-1);
		Piece p=new Piece(piece_types[piece_type_id],piece_type_id,false);
		p.move(((int)((GAME_WIDTH/Block.block_size)-p.getWidth())/2)*Block.block_size,-p.getHeight()*Block.block_size);//center the brick
		pieces.add(p);
		active_piece=p;
	}
	
	private void hardDropActivePiece(){
		boolean no_collision=true;
		while (no_collision){
			active_piece.move(0,Block.block_size);
			if (active_piece.getBottom()>GAME_HEIGHT || checkActivePieceCollision())
				no_collision=false;
		}
		active_piece.move(0,-Block.block_size);
		addNewPiece();
	}
	
	private void clearLine(){
		if (active_piece!=null){
			int[] y_vals=new int[4];
			int i=0,blocks_found;
			for (Block b: active_piece.blocks){
				y_vals[i]=b.y;
				i++;
			}
			Arrays.sort(y_vals);
		
			for (int y:y_vals){
				blocks_found=0;
				for (Piece p: pieces){
					for (Block b: p.blocks){
						if (b!=null && b.y==y){
							blocks_found+=1;
						}
					}
				}
				if (blocks_found==GAME_WIDTH/Block.block_size){
					System.out.println("Found a line");
					for (Piece p: pieces){
						for (int bi=0;bi<p.blocks.length;bi++){
							if (p.blocks[bi]!=null && p.blocks[bi].y==y){
								p.blocks[bi]=null;
							}else if (p.blocks[bi]!=null && p.blocks[bi].y<y){
								p.blocks[bi].y+=Block.block_size;
							}
						}
					}
				}
			}
		}
	}
	
	private boolean checkActivePieceCollision(){
		for (Piece p: pieces){
			if (p!=active_piece && active_piece.checkCollision(p))
				return true;
		}
		return false;
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		//System.out.println(key);
		switch(key){
			case KeyEvent.VK_UP: kUP=true;
				break;
			case KeyEvent.VK_DOWN: hardDropActivePiece();
				break;
			case KeyEvent.VK_LEFT: kLEFT=true;
				break;
			case KeyEvent.VK_RIGHT: kRIGHT=true;
				break;
			case KeyEvent.VK_SPACE: kSPACE=true;
				break;
		}
		
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
			case KeyEvent.VK_UP: kUP=false;
				break;
			case KeyEvent.VK_DOWN: kDOWN=false;
				break;
			case KeyEvent.VK_LEFT: kLEFT=false;
				break;
			case KeyEvent.VK_RIGHT: kRIGHT=false;
				break;
			case KeyEvent.VK_SPACE: kSPACE=false;
				break;
		}
	}
	
	public void keyTyped(KeyEvent e){
	/*bla bla bla. I don't need this method. It's just here to satisfy the KeyListener interface.*/}
}