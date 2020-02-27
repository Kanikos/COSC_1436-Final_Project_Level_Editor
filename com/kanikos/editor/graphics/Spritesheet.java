package com.kanikos.editor.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Spritesheet {
	public static final Sprite NULL_SPRITE = new Sprite();
	
	private Sprite[] sprites;
	private int width, height, length;
	
	public Spritesheet(String path) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File(path));
			image.flush();
		}
		catch(Exception e) { e.printStackTrace(); }
		
		width = image.getWidth() / Sprite.DIMENSIONS;
		height = image.getHeight() / Sprite.DIMENSIONS;
		
		length = width * height;
		sprites = new Sprite[length];
		
		
		int[] spritesheet = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				sprites[(y * width) + x] = new Sprite(spritesheet, image.getWidth(), x, y);
			}
		}
	}
	
	public Sprite getSprite(int index) {
		if(index < 0 || index >= sprites.length) {
			return NULL_SPRITE;
		}
		
		return sprites[index];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getLength() {
		return length;
	}
}
