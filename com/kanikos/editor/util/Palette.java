package com.kanikos.editor.util;

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
}
