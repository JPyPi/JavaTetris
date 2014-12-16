package Tetris;

/*
import java.awt.GraphicsDevice;
GraphicsDevice.getDefaultScreenDevice();
System.out.println(GraphicsDevice.isFullScreenSupported());
*/

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class Tetris extends JFrame{
	public Tetris(){
		FlowLayout layout=new FlowLayout(FlowLayout.LEADING,0,0);
		
		setLayout(layout);
		add(new Board());
		
		pack();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLocationRelativeTo(null);
		setTitle("Jetris");
		setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args){
		new Tetris();
	}
}