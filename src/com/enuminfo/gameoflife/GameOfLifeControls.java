/**
 * 
 */
package com.enuminfo.gameoflife;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author Kumar
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class GameOfLifeControls extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Label genLabel;
	private final String genLabelText = "Generations: ";
	private final String nextLabelText = "Next";
	private final String startLabelText = "Start";
	private final String stopLabelText = "Stop";
	public static final String SLOW = "Slow";
	public static final String FAST = "Fast";
	public static final String HYPER = "Hyper";
	public static final String BIG = "Big";
	public static final String MEDIUM = "Medium";
	public static final String SMALL = "Small";
	public static final int SIZE_BIG = 11;
	public static final int SIZE_MEDIUM = 7;
	public static final int SIZE_SMALL = 3;
	private Button startstopButton;
	private Button nextButton;
	private Vector listeners;
	private Choice shapesChoice;
	private Choice zoomChoice;

	public GameOfLifeControls() {
		listeners = new Vector();
		shapesChoice = new Choice();
		Shape[] shapes = ShapeCollection.getShapes();
		for (int i = 0; i < shapes.length; i++)
			shapesChoice.addItem(shapes[i].getName());
		shapesChoice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				shapeSelected((String) e.getItem());
			}
		});
		Choice speedChoice = new Choice();
		speedChoice.addItem(SLOW);
		speedChoice.addItem(FAST);
		speedChoice.addItem(HYPER);
		speedChoice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String arg = (String) e.getItem();
				if (SLOW.equals(arg))
					speedChanged(1000);
				else if (FAST.equals(arg))
					speedChanged(100);
				else if (HYPER.equals(arg))
					speedChanged(10);
			}
		});
		zoomChoice = new Choice();
		zoomChoice.addItem(BIG);
		zoomChoice.addItem(MEDIUM);
		zoomChoice.addItem(SMALL);
		zoomChoice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String arg = (String) e.getItem();
				if (BIG.equals(arg))
					zoomChanged(SIZE_BIG);
				else if (MEDIUM.equals(arg))
					zoomChanged(SIZE_MEDIUM);
				else if (SMALL.equals(arg))
					zoomChanged(SIZE_SMALL);
			}
		});
		genLabel = new Label(genLabelText + "         ");
		startstopButton = new Button(startLabelText);
		startstopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startStopButtonClicked();
			}
		});
		nextButton = new Button(nextLabelText);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextButtonClicked();
			}
		});
		this.add(shapesChoice);
		this.add(nextButton);
		this.add(startstopButton);
		this.add(speedChoice);
		this.add(zoomChoice);
		this.add(genLabel);
		this.validate();
	}

	public void addGameOfLifeControlsListener(GameOfLifeControlsListener listener) {
		listeners.addElement(listener);
	}

	public void removeGameOfLifeControlsListener(GameOfLifeControlsListener listener) {
		listeners.removeElement(listener);
	}

	public void setGeneration(int generations) {
		genLabel.setText(genLabelText + generations + "         ");
	}

	public void start() {
		startstopButton.setLabel(stopLabelText);
		nextButton.disable();
		shapesChoice.disable();
	}

	public void stop() {
		startstopButton.setLabel(startLabelText);
		nextButton.enable();
		shapesChoice.enable();
	}

	public void startStopButtonClicked() {
		GameOfLifeControlsEvent event = new GameOfLifeControlsEvent(this);
		for (Enumeration e = listeners.elements(); e.hasMoreElements();)
			((GameOfLifeControlsListener) e.nextElement()).startStopButtonClicked(event);
	}

	public void nextButtonClicked() {
		GameOfLifeControlsEvent event = new GameOfLifeControlsEvent(this);
		for (Enumeration e = listeners.elements(); e.hasMoreElements();)
			((GameOfLifeControlsListener) e.nextElement()).nextButtonClicked(event);
	}

	public void speedChanged(int speed) {
		GameOfLifeControlsEvent event = GameOfLifeControlsEvent.getSpeedChangedEvent(this, speed);
		for (Enumeration e = listeners.elements(); e.hasMoreElements();)
			((GameOfLifeControlsListener) e.nextElement()).speedChanged(event);
	}

	public void zoomChanged(int zoom) {
		GameOfLifeControlsEvent event = GameOfLifeControlsEvent.getZoomChangedEvent(this, zoom);
		for (Enumeration e = listeners.elements(); e.hasMoreElements();)
			((GameOfLifeControlsListener) e.nextElement()).zoomChanged(event);
	}

	public void shapeSelected(String shapeName) {
		GameOfLifeControlsEvent event = GameOfLifeControlsEvent.getShapeSelectedEvent(this, shapeName);
		for (Enumeration e = listeners.elements(); e.hasMoreElements();)
			((GameOfLifeControlsListener) e.nextElement()).shapeSelected(event);
	}

	public void setZoom(String n) {
		zoomChoice.select(n);
	}
}
