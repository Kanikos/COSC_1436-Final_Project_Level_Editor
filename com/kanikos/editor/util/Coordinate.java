package com.kanikos.editor.util;

public class Coordinate {
	public int x, y;
	
	public Coordinate(int x, int y) {
		setCoordinate(x, y);
	}
	
	public void setCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isOutOfBounds() {
		return (x < 0 || y < 0);
	}
	
	public String toString() {
		return String.format("X: %03d\tY: %03d", x, y);
	}
}
