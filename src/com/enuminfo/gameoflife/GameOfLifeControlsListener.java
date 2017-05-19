/**
 * 
 */
package com.enuminfo.gameoflife;

/**
 * @author Kumar
 */
public interface GameOfLifeControlsListener {

	public void startStopButtonClicked(GameOfLifeControlsEvent e);
	public void nextButtonClicked(GameOfLifeControlsEvent e);
	public void speedChanged(GameOfLifeControlsEvent e);
	public void zoomChanged(GameOfLifeControlsEvent e);
	public void shapeSelected(GameOfLifeControlsEvent e);
}
