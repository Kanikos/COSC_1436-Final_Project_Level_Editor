package com.kanikos.editor.graphics;

import java.awt.image.BufferedImage;
import com.kanikos.editor.level.Tile;
import com.kanikos.editor.util.Palette;

public class Sprite {
	public static final int DIMENSIONS = 16;
	
	private final int[] SPRITE;
	
	public Sprite(BufferedImage image, int imageWidth, int xPos, int yPos) {
		SPRITE = new int[DIMENSIONS * DIMENSIONS];
		image.getRGB(xPos, yPos, DIMENSIONS, DIMENSIONS, SPRITE, 0, DIMENSIONS);
	}
	
	public Sprite() {
		SPRITE = new int[DIMENSIONS * DIMENSIONS];
		
		for(int y = 0; y < DIMENSIONS; y++) 
			for(int x = 0; x < DIMENSIONS; x++)
				SPRITE[(y * DIMENSIONS) + x] = (x == 0 || y == 0) ? 0xFFFF00FF : 0xFF000000;
	}

	public void render(Palette palette, int[] pixels, int dimensions, Tile tile, int xPos, int yPos) {
		for(int y = 0; y < DIMENSIONS; y++) {
			int yPX = tile.flipY() ? DIMENSIONS - 1 - y : y;
			int viewportY = yPos + y;
			
			for(int x = 0; x < DIMENSIONS; x++) {
				int xPX = tile.flipX() ? DIMENSIONS - 1 - x : x;
				int viewportX = xPos + x;
				
				int pixel;
				if(tile.flipD()) {
					pixel = SPRITE[(xPX * DIMENSIONS) + yPX];
				}
				else {
					pixel = SPRITE[(yPX * DIMENSIONS) + xPX];
				}
				
				pixel = palette.colorize(pixel);
				pixels[(viewportY * dimensions) + viewportX] = pixel;
			}
		}
	}
	
	public void render(int[] pixels, int dimensions, int xPos, int yPos) {
		for(int y = 0; y < DIMENSIONS; y++) {
			int viewportY = yPos + y;
			
			for(int x = 0; x < DIMENSIONS; x++) {
				int viewportX = xPos + x;
				
				pixels[(viewportY * dimensions) + viewportX] = SPRITE[(y * DIMENSIONS) + x];
			}
		}
	}
}
