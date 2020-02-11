package com.kanikos.editor.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JPanel;
import com.kanikos.editor.util.Coordinate;

public class Viewer extends JPanel {
	private static final long serialVersionUID = -2978293310911041984L;
	
	public BufferedImage image;
	public int[] pixels;
	
	private Dimension d;
	private float xRatio, yRatio;
	private int x, y, width, height;
	
	public Viewer(int prefWidth, int prefHeight) {
		d = new Dimension(prefWidth, prefHeight);
		
		setMinimumSize(d);
		setPreferredSize(d);
		setBackground(Color.BLACK);
	}
	
	public Viewer(int resWidth, int resHeight, int prefWidth, int prefHeight) {
		this(prefWidth, prefHeight);
		setResolution(resWidth, resHeight);
	}
	
	public void setResolution(int resWidth, int resHeight) {
		image = new BufferedImage(resWidth, resHeight, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		
		// calculate the scale width / height of the image
		float width, height, scale;
		
		width = image.getWidth();
		height = image.getHeight();
		
		if(width > height) {
			scale = height / width;
			
			this.width = d.width;
			this.height = (int) (scale * d.width);
		}
		else {
			scale = width / height;
			
			this.width = (int) (scale * d.height);
			this.height = d.height;
		}
		
		// calculate the starting x and y
		x = (d.width / 2) - (this.width / 2);
		y = (d.height / 2) - (this.height / 2);
		
		// calculates the ratio between the scaled image and the actual image
		xRatio = resWidth / (float) (this.width);
		yRatio = resHeight / (float) (this.height);
	}
		
	public Coordinate getCoordinate(int x, int y) {
		if((x < this.x || x > this.x + width) || (y < this.y || y > this.y + height)) {
			return null;
		}
		
		int xCoord = x - this.x;
		int yCoord = y - this.y;
		
		return new Coordinate((int) (xCoord * xRatio), (int) (yCoord * yRatio));
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(image, x, y, width, height, null);
	}
}
