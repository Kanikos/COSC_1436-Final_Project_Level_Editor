package com.kanikos.editor.level;

import com.kanikos.editor.graphics.Sprite;
import com.kanikos.editor.graphics.Spritesheet;
import com.kanikos.editor.serial.Deserializer;
import com.kanikos.editor.serial.Serializer;
import com.kanikos.editor.util.Palette;

public class Chunk {
	public static final int WIDTH = 16, HEIGHT = 11, COUNT = WIDTH * HEIGHT;
	
	public Palette palette;
	public Tile[] tiles;
	
	public Chunk() {
		palette = new Palette();
		tiles = new Tile[COUNT];
	}
	
	public void render(Spritesheet spritesheet, int[] pixels, int dimensions) {
		for(int y = 0; y < HEIGHT; y++) {
			int yPos = y * Sprite.DIMENSIONS;
			
			for(int x = 0; x < WIDTH; x++) {
				int xPos = x * Sprite.DIMENSIONS;
				
				Tile tile = tiles[(y * WIDTH) + x];
				if(tile == null || spritesheet == null) {
					Spritesheet.NULL_SPRITE.render(pixels, dimensions, xPos, yPos);
					continue;
				}
				
				Sprite sprite = spritesheet.getSprite(tile.getID());
				sprite.render(palette, pixels, dimensions, tile, xPos, yPos);
			}
		}
	}
	
	public void deserialize(String path) {
		Deserializer deserializer = new Deserializer(path);
		palette.deserialize(deserializer);
		
		for(int i = 0; i < COUNT; i++) {
			Tile tile = new Tile();
			tile.deserialize(deserializer);
			
			tiles[i] = tile;
		}
	}
	
	public void serialize(String path) {
		Serializer serializer = new Serializer();
		palette.serialize(serializer);
		
		for(int i = 0; i < COUNT; i++) {
			tiles[i].serialize(serializer);
		}
		
		serializer.saveTo(path);
	}

	public void setTile(Tile tile, int x, int y) {
		tiles[(y * WIDTH) + x] = tile.clone();
	}
	
	public Tile getTile(int x, int y) {
		return tiles[(y * WIDTH) + x];
	}
}
