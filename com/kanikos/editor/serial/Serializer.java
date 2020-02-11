package com.kanikos.editor.serial;

import java.util.ArrayList;
import java.io.FileOutputStream;

public class Serializer {
	private ArrayList<Byte> data = new ArrayList<Byte>();

	public void write(long writeData, int size) {
		for(int s = 0; s < size; s += 8) {
			data.add((byte) (writeData >> s));
		}
	}
	
	public void saveTo(String filePath)  {
		try {
			FileOutputStream fos = new FileOutputStream(filePath, false);
			
			for(int i = 0; i < data.size(); i++) {
 				fos.write(data.get(i));
			}
			
			fos.flush();
			fos.close();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	// methods that write primitive integer data types
	public void writeByte(byte writeData) {
		write(writeData, Byte.SIZE);
	}
	
	public void writeShort(short writeData) {
		write(writeData, Short.SIZE);
	}
	
	public void writeInt(int writeData) {
		write(writeData, Integer.SIZE);
	}
	
	public void writeLong(long writeData) {
		write(writeData, Long.SIZE);
	}
	
	// methods that write character data
	public void writeChar(char writeData) {
		write(writeData, Byte.SIZE);
	}
	
	public void writeString(String writeData) {
		char[] charArray = writeData.toCharArray();
		writeInt(charArray.length);
		
		for(int i = 0; i < charArray.length; i++) {
			writeChar(charArray[i]);
		}
	}
	
	// methods that write primitive integer array types 
	public void writeBytes(byte[] writeData) {
		writeInt(writeData.length);
		
		for(byte data: writeData) {
			writeByte(data);
		}
	}
	
	public void writeShorts(short[] writeData) {
		writeInt(writeData.length);
		
		for(short data: writeData) {
			writeShort(data);
		}
	}
	
	public void writeInts(int[] writeData) {
		writeInt(writeData.length);
		
		for(int data: writeData) {
			writeInt(data);
		}
	}
	
	public void writeLongs(long[] writeData) {
		writeInt(writeData.length);
		
		for(long data: writeData) {
			writeLong(data);
		}
	}
	
	// methods that write character data arrays
	public void writeCharacters(char[] writeData) {
		writeInt(writeData.length);
		
		for(char data: writeData) {
			writeChar(data);
		}
	}
	
	public void writeStrings(String[] writeData) {
		writeInt(writeData.length);
		
		for(String data: writeData) {
			writeString(data);
		}
	}
}
