package com.kanikos.editor.serial;

import java.io.File;
import java.io.FileInputStream;

public class Deserializer {
	private byte[] data;
	private int pointer;
	
	public Deserializer(String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			
			// java 8 version
			File file = new File(filePath);
			data = new byte[(int) (file.getTotalSpace())];
			fis.read(data);
			// java 8 version end
			
//			data = fis.readAllBytes(); <- this does not work in java 8
			pointer = 0;
			
			fis.close();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	private long read(int size) {
		long readData = 0;
		
		for(int s = 0; s < size; s += 8) {
			long temp = Byte.toUnsignedLong(data[pointer]);
			temp <<= s;
			
			readData |= temp;
			pointer++;
		}
		
		return readData;
	} 
	
	// methods that read primitive integer types
	public byte readByte() {
		return (byte) (read(Byte.SIZE));
	}
	
	public short readShort() {
		return (short) (read(Short.SIZE));
	}
	
	public int readInt() {
		return (int) (read(Integer.SIZE));
	}
	
	public long readLong() {
		return read(Long.SIZE);
	}
	
	// write character data
	public char readChar() {
		return (char) (read(Byte.SIZE));
	}
	
	public String readString() {
		String string = "";
		int stringSize = readInt();
		
		for(int i = 0; i < stringSize; i++) {
			string += readChar();
		}
		
		return string;
	}
	
	// methods that read primitive integer array types
	public byte[] readBytes() {
		int arrayLength = readInt();
		byte[] data = new byte[arrayLength];
		
		for(int i = 0; i < arrayLength; i++) {
			data[i] = readByte();
		}
		
		return data;
	}
	
	public short[] readShorts() {
		int arrayLength = readInt();
		short[] data = new short[arrayLength];
		
		for(int i = 0; i < arrayLength; i++) {
			data[i] = readShort();
		}
		
		return data;
	}
	
	public int[] readInts() {
		int arrayLength = readInt();
		int[] data = new int[arrayLength];
		
		for(int i = 0; i < arrayLength; i++) {
			data[i] = readInt();
		}
		
		return data;
	}
	
	public long[] readLongs() {
		int arrayLength = readInt();
		long[] data = new long[arrayLength];
		
		for(int i = 0; i < arrayLength; i++) {
			data[i] = readLong();
		}
		
		return data;
	}
	
	// methods that read character array data
	public char[] readChars() {
		int arrayLength = readInt();
		char[] data = new char[arrayLength];
		
		for(int i = 0; i < arrayLength; i++) {
			data[i] = readChar();
		}
		
		return data;
	}
	
	public String[] readStrings() {
		int arrayLength = readInt();
		String[] data = new String[arrayLength];
		
		for(int i = 0; i < arrayLength; i++) {
			data[i] = readString();
		}
		
		return data;
	}
}
