/* eMafiaClient - ColorPane.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ColorPane extends JTextPane {
	private static final long serialVersionUID = 1L;
	public SimpleAttributeSet set;

	public void append(Color c,String s){
		Document doc = getStyledDocument();
		set = new SimpleAttributeSet();
		//StyleConstants.setItalic(set, true);
		StyleConstants.setForeground(set, c);
		try {
			doc.insertString(doc.getLength(), s, set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void append(Color c,String[] styles,String s){
		Document doc = getStyledDocument();
		set = new SimpleAttributeSet();
		for(String style : styles){
			switch(style){
				case "italic":
					StyleConstants.setItalic(set, true);
					break;
				case "bold":
					StyleConstants.setBold(set, true);
					break;
				case "underline":
					StyleConstants.setUnderline(set, true);
					break;
				case "strikethrough":
					StyleConstants.setStrikeThrough(set, true);
					break;
				case "subscript":
					StyleConstants.setSubscript(set, true);
					break;
				case "superscript":
					StyleConstants.setSuperscript(set, true);
					break;
			}
		}
		StyleConstants.setForeground(set, c);
		try {
			doc.insertString(doc.getLength(), s, set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
