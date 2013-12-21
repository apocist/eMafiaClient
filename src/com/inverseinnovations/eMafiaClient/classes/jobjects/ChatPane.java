/* eMafiaClient - ChatPane.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.inverseinnovations.eMafiaClient.*;


/**
 * Allows this component directly place on JLayeredPane to be auto size according to
 * current frame size.
 * Allows either Fullscreen or Center
 * Allows additional function of detecting preferred size correctly
 */
public class ChatPane extends JAutoPanel{
	private static final long serialVersionUID = 1L;

	public ChatPane(final Framework Framework){
		super(Framework.Window.desktop);

		// Outcome Pane

		//Framework.Data.chatOutput.setEditable(false);
		JScrollPane chatScrollpane = new JScrollPane(Framework.Data.chatOutput);
			//chatInput
		JTextPane chatInput = new JTextPane();
		chatInput.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 10){
					String output = ((JTextPane)e.getSource()).getText();
					output = output.substring(0, output.length() - 2).trim();//removes unneeded things
					((JTextPane)e.getSource()).setText("");
					if(!output.equals("")){
						Framework.Telnet.write(output);
					}
				}
			}
			public void keyTyped(KeyEvent e) {
			}
		});
		JScrollPane inputScrollpane = new JScrollPane(chatInput);
		inputScrollpane.setMaximumSize(new Dimension(3000, 30));
		//chatPanel - chatScrollPane on top of inputScrollPane

		//this.setBorder(BorderFactory.createLineBorder(Color.gray));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(chatScrollpane);
		this.add(inputScrollpane);
	}
}
