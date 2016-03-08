package chihane.poping;

import android.graphics.Color;

import java.io.Serializable;

/** 单个方块的实例。 */
public class Block implements Serializable {
	private static final long serialVersionUID = 6827165505563814335L;

	public static final int COLOR_ONE = Color.parseColor("#5FD9CD");
	public static final int COLOR_TWO = Color.parseColor("#FFCC33");
	public static final int COLOR_THREE = Color.parseColor("#F0ADA1");
	public static final int COLOR_FOUR = Color.parseColor("#66CC66");
	public static final int COLOR_FIVE = Color.parseColor("#FC7FEE");
	/** 可用颜色列表。*/
	public static final int[] COLOR_LIST = { COLOR_ONE, COLOR_TWO, COLOR_THREE, COLOR_FOUR, COLOR_FIVE };

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
