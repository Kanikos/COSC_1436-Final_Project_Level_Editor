package com.kanikos.editor.util;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.kanikos.editor.serial.Deserializer;
import com.kanikos.editor.serial.Serializer;

public class Palette {
	public static final int   LIMIT 		= 4; 
	public static final int[] GRAY_SCALE 	= {
			0xFFFFFFFF,
			0xFF7E7E7E,
			0xFF3F3F3F,
			0xFF000000
	};
	
	private static int[] palette;
	
	public Palette() {
		palette = GRAY_SCALE.clone();
	}
	
	public int colorize(int grayscalePixel) {
		int colorizedPixel = 0;
		
		for(int i = 0; i < LIMIT; i++) {
			if(grayscalePixel == GRAY_SCALE[i]) {
				colorizedPixel = palette[i];
				break;
			}
		}
		
		return colorizedPixel;
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
