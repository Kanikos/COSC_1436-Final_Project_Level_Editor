package com.kanikos.editor.util;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.kanikos.editor.serial.Deserializer;
import com.kanikos.editor.serial.Serializer;

public class Palette {
	public static final int LIMIT = 4;
	private static final int MASK = 0xFF;
	private static final int INCREMENT = 0x55;
	
	// default palette
	public static final int ERROR_COLOR = 0xFFFF00FF;
	public static final int[] GRAY_SCALE 	= {
			0xFF000000,
			0xFF555555,
			0xFFAAAAAA,
			0xFFFFFFFF
	};
	
	private static int[] palette;
	
	public Palette() {
		palette = GRAY_SCALE.clone();
	}
	
	public int colorize(int grayScale) {
		int index = (grayScale & MASK) / INCREMENT;
		
		if(index < 0 || index > LIMIT) {
			return ERROR_COLOR;
		}
		
		return palette[index];
	}
	
	public static int getStandardColor(int grayScale) {
		int index = (grayScale & MASK) / INCREMENT;
		
		if(index < 0 || index > LIMIT) {
			return ERROR_COLOR;
		}
		
		return GRAY_SCALE[index];
	}
	
	public void setColor(int index, int color) {
		palette[index] = color;
	}
	
	public int getColor(int index) {
		return palette[index];
	}
	
	public void deserialize(Deserializer deserializer) {
		palette = deserializer.readInts();
	}
	
	public void serialize(Serializer serializer) {
		serializer.writeInts(palette);
	}
	
	public ImageIcon toImageIcon(int index, int dimensions) {
		BufferedImage image = new BufferedImage(dimensions, dimensions, BufferedImage.TYPE_INT_RGB);
		image.flush();
		
		for(int y = 0; y < dimensions; y++) {
			for(int x = 0; x < dimensions; x++) {
				image.setRGB(x, y, palette[index]);
			}
		}
		
		return new ImageIcon(image);
	}
}
