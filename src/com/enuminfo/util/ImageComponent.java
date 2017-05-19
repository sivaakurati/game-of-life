/**
 * 
 */
package com.enuminfo.util;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.ImageObserver;

/**
 * @author Kumar
 */
public class ImageComponent extends Canvas implements ImageObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image;

	public ImageComponent(Image image) {
		this.image = image;
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(image, 0);
		try {
			tracker.waitForID(0, 3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics graphics) {
		graphics.drawImage(image, 0, 0, this);
	}

	public Dimension getPreferredSize() {
		return new Dimension(image.getWidth(this), image.getHeight(this));
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		boolean isImageRead = (infoflags & ImageObserver.ALLBITS) != 0;
		repaint();
		return !isImageRead;
	}
}
