/**
 * 
 */
package com.enuminfo.gameoflife;

import java.awt.Dimension;
import java.util.Enumeration;

/**
 * @author Kumar
 */
@SuppressWarnings("rawtypes")
public interface CellGrid {

	public boolean getCell(int col, int row);
	public void setCell(int col, int row, boolean cell);
	public Dimension getDimension();
	public void resize(int col, int row);
	public Enumeration getEnum();
	public void clear();
}
