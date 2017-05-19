/**
 * 
 */
package com.enuminfo.gameoflife;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Kumar
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class GameOfLifeGrid implements CellGrid {

	private int cellRows;
	private int cellCols;
	private int generations;
	private static Shape[] shapes;
	private Hashtable currentShape;
	private Hashtable nextShape;
	private Cell[][] grid;

	public GameOfLifeGrid(int cellCols, int cellRows) {
		this.cellCols = cellCols;
		this.cellRows = cellRows;
		currentShape = new Hashtable();
		nextShape = new Hashtable();
		grid = new Cell[cellCols][cellRows];
		for (int c = 0; c < cellCols; c++)
			for (int r = 0; r < cellRows; r++)
				grid[c][r] = new Cell(c, r);
	}

	public synchronized void clear() {
		generations = 0;
		currentShape.clear();
		nextShape.clear();
	}

	public synchronized void next() {
		Cell cell;
		int col, row;
		int neighbours;
		Enumeration enumeration;
		generations++;
		nextShape.clear();
		enumeration = currentShape.keys();
		while (enumeration.hasMoreElements()) {
			cell = (Cell) enumeration.nextElement();
			cell.neighbour = 0;
		}
		enumeration = currentShape.keys();
		while (enumeration.hasMoreElements()) {
			cell = (Cell) enumeration.nextElement();
			col = cell.col;
			row = cell.row;
			addNeighbour(col - 1, row - 1);
			addNeighbour(col, row - 1);
			addNeighbour(col + 1, row - 1);
			addNeighbour(col - 1, row);
			addNeighbour(col + 1, row);
			addNeighbour(col - 1, row + 1);
			addNeighbour(col, row + 1);
			addNeighbour(col + 1, row + 1);
		}
		enumeration = currentShape.keys();
		while (enumeration.hasMoreElements()) {
			cell = (Cell) enumeration.nextElement();
			if (cell.neighbour != 3 && cell.neighbour != 2) {
				currentShape.remove(cell);
			}
		}
		enumeration = nextShape.keys();
		while (enumeration.hasMoreElements()) {
			cell = (Cell) enumeration.nextElement();
			if (cell.neighbour == 3) {
				setCell(cell.col, cell.row, true);
			}
		}
	}

	public synchronized void addNeighbour(int col, int row) {
		try {
			Cell cell = (Cell) nextShape.get(grid[col][row]);
			if (cell == null) {
				Cell c = grid[col][row];
				c.neighbour = 1;
				nextShape.put(c, c);
			} else {
				cell.neighbour++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
	}

	public Enumeration getEnum() {
		return currentShape.keys();
	}

	public synchronized boolean getCell(int col, int row) {
		try {
			return currentShape.containsKey(grid[col][row]);
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		return false;
	}

	public synchronized void setCell(int col, int row, boolean c) {
		try {
			Cell cell = grid[col][row];
			if (c) {
				currentShape.put(cell, cell);
			} else {
				currentShape.remove(cell);
			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
	}

	public int getGenerations() {
		return generations;
	}

	public Dimension getDimension() {
		return new Dimension(cellCols, cellRows);
	}

	public synchronized void resize(int cellColsNew, int cellRowsNew) {
		if (cellCols == cellColsNew && cellRows == cellRowsNew)
			return;
		Cell[][] gridNew = new Cell[cellColsNew][cellRowsNew];
		for (int c = 0; c < cellColsNew; c++)
			for (int r = 0; r < cellRowsNew; r++)
				if (c < cellCols && r < cellRows)
					gridNew[c][r] = grid[c][r];
				else
					gridNew[c][r] = new Cell(c, r);
		int colOffset = (cellColsNew - cellCols) / 2;
		int rowOffset = (cellRowsNew - cellRows) / 2;
		Cell cell;
		Enumeration enumeration;
		nextShape.clear();
		enumeration = currentShape.keys();
		while (enumeration.hasMoreElements()) {
			cell = (Cell) enumeration.nextElement();
			int colNew = cell.col + colOffset;
			int rowNew = cell.row + rowOffset;
			try {
				nextShape.put(gridNew[colNew][rowNew], gridNew[colNew][rowNew]);
			} catch (ArrayIndexOutOfBoundsException e) {

			}
		}
		grid = gridNew;
		currentShape.clear();
		enumeration = nextShape.keys();
		while (enumeration.hasMoreElements()) {
			cell = (Cell) enumeration.nextElement();
			currentShape.put(cell, cell);
		}
		cellCols = cellColsNew;
		cellRows = cellRowsNew;
	}
}
