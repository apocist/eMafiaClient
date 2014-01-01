/* eMafiaClient - TabbedPanel.java
Copyright (C) 2013  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TabbedPanel extends JTabbedPane {
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
		JTextArea text = new JTextArea(content);

		text.setEditable(editable);
		JScrollPane scroll = new JScrollPane(text);
		scroll.setPreferredSize(new Dimension(600,250));//This seems to be a quick fix for over expanding
		@SuppressWarnings("unused")
		Component comp = add(title, new JScrollPane(scroll));
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
}
