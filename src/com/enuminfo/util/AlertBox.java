/**
 * 
 */
package com.enuminfo.util;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

/**
 * @author Kumar
 */
@SuppressWarnings("deprecation")
public class AlertBox extends Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button okButton;

	public AlertBox(Frame parent, String title, String message) {
		super(parent, title, false);
		Image alertImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("alert.gif"));
		ImageComponent alertImageComponent = new ImageComponent(alertImage);
		okButton = new Button("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(okButton);
		StringTokenizer stringTokenizer = new StringTokenizer(message, "\n");
		Panel messagePanel = new Panel(new GridLayout(stringTokenizer.countTokens(), 1));
		while (stringTokenizer.hasMoreTokens()) {
			messagePanel.add(new Label(stringTokenizer.nextToken()));
		}
		add("West", alertImageComponent);
		add("Center", messagePanel);
		add("South", buttonPanel);
		enableEvents(Event.WINDOW_DESTROY);
		setResizable(false);
		setModal(true);
		pack();
		Point point = parent.getLocation();
		Dimension dimension = parent.getSize();
		setLocation(point.x + dimension.width / 2 - 150, point.y + dimension.height / 2 - 75);
		show();
	}

	private void close() {
		this.hide();
		this.dispose();
	}

	public void processEvent(AWTEvent e) {
		if (e.getID() == Event.WINDOW_DESTROY)
			close();
	}
}
