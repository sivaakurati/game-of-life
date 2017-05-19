/**
 * 
 */
package com.enuminfo.util;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Kumar
 */
@SuppressWarnings("deprecation")
public class AboutDialog extends Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button okButton;

	public AboutDialog(Frame parent, String title, String[] lines, String imageName, int posX, int posY) {
		super(parent, title, false);
		Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(imageName));
		ImageComponent ic = new ImageComponent(image);
		okButton = new Button("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints contraints = new GridBagConstraints();
		setLayout(gridbag);
		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(okButton);
		Panel textPanel = new Panel();
		textPanel.setLayout(new GridLayout(lines.length, 1));
		for (int i = 0; i < lines.length; i++) {
			textPanel.add(new Label(lines[i]));
		}
		contraints.fill = GridBagConstraints.BOTH;
		contraints.weightx = 1;
		contraints.weighty = 1;
		contraints.gridx = GridBagConstraints.REMAINDER;
		contraints.gridy = 0;
		contraints.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(ic, contraints);
		add(ic);
		contraints.fill = GridBagConstraints.NONE;
		contraints.weightx = 0;
		contraints.weighty = 0;
		contraints.gridy = 1;
		gridbag.setConstraints(textPanel, contraints);
		add(textPanel);
		contraints.gridy = 2;
		gridbag.setConstraints(buttonPanel, contraints);
		add(buttonPanel);
		enableEvents(Event.WINDOW_DESTROY);
		setResizable(false);
		setModal(true);
		pack();
		setLocation(posX, posY);
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
