package com.kanikos.editor.level;

import com.kanikos.editor.serial.Deserializer;
import com.kanikos.editor.serial.Serializer;

public class Tile {
	public static final short 
		FLAG_FLIPX = (short) 0b00000001_00000000,
		FLAG_FLIPY = (short) 0b00000010_00000000,
		FLAG_FLIPD = (short) 0b00000100_00000000,
		FLAG_SOLID = (short) 0b00001000_00000000
	;
	
	private short serializedForm = 1;
	
	public void deserialize(Deserializer deserializer) {
		serializedForm = deserializer.readShort();
	}
	
	public void serialize(Serializer serializer) {
		serializer.writeShort(serializedForm);
	}
	
	public void setID(short spriteID) {
		spriteID &= 0xFF;
		
		serializedForm &= 0xFF00;
		serializedForm |= spriteID;
	}
	
	public void flipFlag(short flag) {
		flag &= 0xFF00;
		serializedForm ^= flag;
	}
	
	public boolean isFlagSet(short flag) {
		return (serializedForm & flag) == flag;
	}
	
	public short getID() {
		return serializedForm &= 0xFF;
	}
	
	// wrappers
	public boolean flipX() {
		return isFlagSet(FLAG_FLIPX);
	}
	
	public boolean flipY() {
		return isFlagSet(FLAG_FLIPY);
	}
	
	public boolean flipD() {
		return isFlagSet(FLAG_FLIPD);
	}
	
	public boolean isSolid() {
		return isFlagSet(FLAG_SOLID);
	}
	
	//overloads and overwrites
	public boolean equals(Tile tile) {
		if(tile == null) {
			return false;
		}
		
		return serializedForm == tile.serializedForm;
	}
	
	public Tile clone() {
		Tile tile = new Tile();
		tile.serializedForm = serializedForm;
		
		return tile;
	}
}
