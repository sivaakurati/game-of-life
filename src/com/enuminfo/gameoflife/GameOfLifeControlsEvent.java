/**
 * 
 */
package com.enuminfo.gameoflife;

import java.awt.Event;

/**
 * @author Kumar
 */
public class GameOfLifeControlsEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int speed;
	private int zoom;
	private String shapeName;

	public GameOfLifeControlsEvent(Object source) {
		super(source, 0, null);
	}

	public static GameOfLifeControlsEvent getSpeedChangedEvent(Object source, int speed) {
		GameOfLifeControlsEvent event = new GameOfLifeControlsEvent(source);
		event.setSpeed(speed);
		return event;
	}

	public static GameOfLifeControlsEvent getZoomChangedEvent(Object source, int zoom) {
		GameOfLifeControlsEvent event = new GameOfLifeControlsEvent(source);
		event.setZoom(zoom);
		return event;
	}

	public static GameOfLifeControlsEvent getShapeSelectedEvent(Object source, String shapeName) {
		GameOfLifeControlsEvent event = new GameOfLifeControlsEvent(source);
		event.setShapeName(shapeName);
		return event;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public String getShapeName() {
		return shapeName;
	}

	public void setShapeName(String shapeName) {
		this.shapeName = shapeName;
	}
}
