package com.kanikos.editor.graphics;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.kanikos.editor.level.Tile;
import com.kanikos.editor.util.Palette;

public class Sprite {
	// sprite standard width and height
	public static final int DIMENSIONS = 16;
	
	// variables determines how to transform the sprite
	public static final byte TRANSFORMATION_MASK = 0b111;
	public static final byte
		FLIP_X = 0b001,
		FLIP_Y = 0b010,
		FLIP_D = 0b100
	;
	
	private byte[] sprite;
	
	public Sprite(int[] spritesheet, int spritesheetWidth, int xOffset, int yOffset) {
		this.sprite = new byte[DIMENSIONS * DIMENSIONS];
		
		xOffset *= DIMENSIONS;
		yOffset *= DIMENSIONS;
		
		int mask = 0xFF;
		int xPos, yPos;
		for(int y = 0; y < DIMENSIONS; y++) {
			yPos = y + yOffset;
			
			for(int x = 0; x < DIMENSIONS; x++) {
				xPos = x + xOffset;
				
				sprite[(y * DIMENSIONS) + x] = (byte) (spritesheet[(yPos * spritesheetWidth) + xPos] & mask);
			}
		}
	}
	
	public Sprite() {
		sprite = new byte[DIMENSIONS * DIMENSIONS];
		
		for(int y = 0; y < DIMENSIONS; y++) {
			for(int x = 0; x < DIMENSIONS; x++) {
				if(x == 0 || y == 0) {
					sprite[(y * DIMENSIONS) + x] = -1;
				}
				else {
					sprite[(y * DIMENSIONS) + x] = 0;
				}
				
			}
		}
	}
	
	public void render(Palette palette, int[] pixels, int dimensions, Tile tile, int xPos, int yPos) {
		for(int y = 0; y < DIMENSIONS; y++) {
			int yPX = tile.flipY() ? DIMENSIONS - 1 - y : y;
			int viewportY = yPos + y;
			
			for(int x = 0; x < DIMENSIONS; x++) {
				int xPX = tile.flipX() ? DIMENSIONS - 1 - x : x;
				int viewportX = xPos + x;
				
				byte pixel;
				if(tile.flipD()) {
					pixel = sprite[(xPX * DIMENSIONS) + yPX];
				}
				else {
					pixel = sprite[(yPX * DIMENSIONS) + xPX];
				}
				
				pixels[(viewportY * dimensions) + viewportX] = palette.colorize(pixel);
			}
		}
	}
	
	public void render(int[] pixels, int dimensions, int xPos, int yPos) {
		for(int y = 0; y < DIMENSIONS; y++) {
			int viewportY = yPos + y;
			
			for(int x = 0; x < DIMENSIONS; x++) {
				int viewportX = xPos + x;
				
				pixels[(viewportY * dimensions) + viewportX] = sprite[(y * DIMENSIONS) + x];
			}
		}
	}
	
	public ImageIcon toImageIcon(int scale) {
		int scaledDimensions = DIMENSIONS * scale;
		
		BufferedImage image = new BufferedImage(scaledDimensions, scaledDimensions, BufferedImage.TYPE_INT_RGB);
		image.flush();
		
		int pixel;
		for(int y = 0; y < scaledDimensions; y++) {
			for(int x = 0; x < scaledDimensions; x++) {
				pixel = sprite[((y / scale) * DIMENSIONS) + (x / scale)];
				pixel = Palette.getStandardColor(pixel);
				
				image.setRGB(x, y, pixel);
			}
		}
		
		return new ImageIcon(image);
	}
}
