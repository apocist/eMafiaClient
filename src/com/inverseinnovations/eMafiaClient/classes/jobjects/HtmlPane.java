/* eMafiaClient - HtmlPane.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class HtmlPane extends JTextPane {
	private static final long serialVersionUID = 1L;
	public HTMLEditorKit kit = new HTMLEditorKit();
	public HTMLDocument doc = new HTMLDocument();
	public String lastMsg = "";

	public HtmlPane(){
		setEditorKit(kit);
		setDocument(doc);
		setEditable(false);
		setBackground(new Color(32,32,32));
		//setForeground(new Color(255,255,255));
		DefaultCaret caret = (DefaultCaret)this.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);//this will make it auto scroll to bottom
	}
	public void append(String s){
		try {
			if(lastMsg.equals("<font color=\"FFFFFF\"><hr></font>") && s.equals("<font color=\"FFFFFF\"><hr></font>")){}
			else{
				lastMsg = s;
				kit.insertHTML(doc, doc.getLength(), s, 0, 0, null);
			}
		}
		catch (BadLocationException e) {
		}catch (IOException e) {
		}
	}
	public void scrollToBottom(){
		this.setCaretPosition(doc.getLength());
	}
}
