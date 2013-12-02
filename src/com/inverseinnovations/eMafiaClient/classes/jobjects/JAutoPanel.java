/* eMafiaClient - JAutoPanel.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;
import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;


/**
 * Allows this component directly place on JLayeredPane to be auto size according to
 * current frame size.
 * Allows either Fullscreen or Center
 * Allows additional function of detecting preferred size correctly
 */
public class JAutoPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLayeredPane pane;
	private boolean fullscreen = false;
	private boolean center = false;

	public JAutoPanel(JLayeredPane pane){
		super();
		this.pane = pane;
	}

	public void autoUpdate(){
		if(fullscreen){//if should resize to all of JLayer
			autoUpdateFullscreen();
		}
		else if(center){//if should center in the Layer
			autoUpdateCenter();
		}
		validate();
	}

	private void autoUpdateCenter(){
		int X = (this.pane.getWidth() / 2) - (this.getWidth() / 2); // Center horizontally.
		int Y = (this.pane.getHeight() / 2) - (this.getHeight() / 2); // Center vertically.
		setLocation(X, Y);
	}

	private void autoUpdateFullscreen(){
		setLocation(0, 0);
		setSize(new Dimension(pane.getWidth(), pane.getHeight()));
	}

	public void setAutoCenter(){
		this.center = true;
		this.fullscreen = false;
		autoUpdateCenter();
	}

	public void setAutoFullscreen(){
		this.fullscreen = true;
		this.center = false;
		autoUpdateFullscreen();
	}

	public Dimension getPreferredSize(){
	  Dimension pSize = super.getPreferredSize();
	  Dimension mSize = getMinimumSize();
	  int wid, ht;

	  wid = pSize.width < mSize.width  ? mSize.width : pSize.width;
	  ht = pSize.height < mSize.height ? mSize.height: pSize.height;
	  return new Dimension(wid, ht);
	}
}
