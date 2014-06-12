package com.mlxy.poping;

import java.io.Serializable;

import android.graphics.Color;

/** 单个方块的实例。 */
public class Block implements Serializable {
	private static final long serialVersionUID = 6827165505563814335L;
	
	public static final int COLOR_RED = Color.RED;
	public static final int COLOR_BLUE = Color.BLUE;
	public static final int COLOR_GREEN = Color.GREEN;
	public static final int COLOR_YELLOW = Color.YELLOW;
	public static final int COLOR_MAGENTA = Color.MAGENTA;
	/** 可用颜色列表。*/
	public static final int[] COLOR_LIST = { COLOR_RED, COLOR_BLUE, COLOR_GREEN, COLOR_YELLOW, COLOR_MAGENTA };

	private int id;
	
	private int color;
	private int row;
	private int column;
	
	private boolean selected;

	public Block(int id, int color, int row, int column) {
		this.setSelected(false);
		this.id = id;
		this.color = color;
		this.row = row;
		this.column = column;
	}

	
	public int getId() {
		return id;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
