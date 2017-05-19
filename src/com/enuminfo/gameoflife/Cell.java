/**
 * 
 */
package com.enuminfo.gameoflife;

/**
 * @author Kumar
 */
public class Cell {

	public final short col;
	public final short row;
	public byte neighbour;
	private final int HASHFACTOR = 5000;

	public Cell(int col, int row) {
		this.col = (short) col;
		this.row = (short) row;
		neighbour = 0;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Cell))
			return false;
		return col == ((Cell) o).col && row == ((Cell) o).row;
	}

	public int hashCode() {
		return HASHFACTOR * row + col;
	}

	public String toString() {
		return "Cell at (" + col + ", " + row + ") with " + neighbour + " neighbour" + (neighbour == 1 ? "" : "s");
	}
}
