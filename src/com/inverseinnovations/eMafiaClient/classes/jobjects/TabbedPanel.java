/* eMafiaClient - TabbedPanel.java
Copyright (C) 2013  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class TabbedPanel extends JTabbedPane{
	private static final long serialVersionUID = 1L;
	private boolean editable;

	public TabbedPanel(){
		this(false);
	}

	public TabbedPanel(boolean editable){
		this.editable = editable;
		setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
	}

	public void addTab(){
		addTab("New Tab");
	}

	public void addTab(String title){
		addTab(title, title+" entry");
	}

	public void addTab(String title, String content){
		RSyntaxTextArea textArea = new RSyntaxTextArea(content);
		textArea.setTabSize(3);
		textArea.setCaretPosition(0);
		textArea.setMarkOccurrences(true);
		textArea.setCodeFoldingEnabled(true);
		textArea.setClearWhitespaceLinesEnabled(true);
		textArea.setSyntaxEditingStyle(org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		textArea.setEditable(editable);
		RTextScrollPane scrollPane = new RTextScrollPane(textArea, true);//true/false is if line numbers should appear


		scrollPane.setPreferredSize(new Dimension(600,250));//This seems to be a quick fix for over expanding
		@SuppressWarnings("unused")
		Component comp = add(title, scrollPane);
	}

	public void addTabSet(Map<String, String> set){
		for(String setKey:set.keySet()){
			if(set.get(setKey) != null){
				if(!set.get(setKey).equals("")){
					addTab(setKey,set.get(setKey));
				}
			}
		}
	}

	/**
	 * Generates Map of all scripts within this role
	 */
	public Map<String, String> getErsScripts(){
		Map<String, String> ersScript = new LinkedHashMap<String, String>();
		int indexes = this.getTabCount()-1;
		if(indexes > 0){
			for(int index = 0;index <= indexes;index++){
				Component comp = this.getComponentAt(index);
				if(comp instanceof RTextScrollPane){
					String scriptName = this.getTitleAt(index);
					RTextArea textArea = ((RTextScrollPane) comp).getTextArea();
					if(textArea != null){
						ersScript.put(scriptName, textArea.getText());
					}
				}
			}
		}
		return ersScript;
	}

	/**
	 * Changes whether each Tab's content may be editted
	 */
	public void updateEditablilty(boolean editable){
		for (Component comp : this.getComponents()) {
			if (comp instanceof RTextScrollPane) {
				RTextArea textArea = ((RTextScrollPane) comp).getTextArea();
				if(textArea != null){
					textArea.setEditable(editable);
				}
			}
		}
	}

}
