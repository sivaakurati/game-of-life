/**
 * 
 */
package com.enuminfo.gameoflife;

import java.awt.Dimension;
import java.util.Enumeration;

/**
 * @author Kumar
 */
public class Shape {

	private final String name;
	private final int[][] shape;

	public Shape(String name, int[][] shape) {
		this.name = name;
		this.shape = shape;
	}

	public Dimension getDimension() {
		int shapeWidth = 0;
		int shapeHeight = 0;
		for (int cell = 0; cell < shape.length; cell++) {
			if (shape[cell][0] > shapeWidth)
				shapeWidth = shape[cell][0];
			if (shape[cell][1] > shapeHeight)
				shapeHeight = shape[cell][1];
		}
		shapeWidth++;
		shapeHeight++;
		return new Dimension(shapeWidth, shapeHeight);
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getCells() {
		return new Enumeration() {
			private int index = 0;

			public boolean hasMoreElements() {
				return index < shape.length;
			}

			public Object nextElement() {
				return shape[index++];
			}
		};
	}

	public String toString() {
		return name + " (" + shape.length + " cell" + (shape.length == 1 ? "" : "s") + ")";
	}
}
