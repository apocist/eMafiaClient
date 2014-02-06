/* eMafiaClient - TabbedPanel.java
Copyright (C) 2013  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.inverseinnovations.eMafiaClient.Window;

public class TabbedPanel extends JTabbedPane{
	private static final long serialVersionUID = 1L;
	public Window Window;
	private String[] scriptNames = {"","onStartup","onDayStart","onDayTargetChoice","onDayEnd","onNightStart","onNightTargetChoice","onNightEnd","onVisit","onAttacked","onLynch","onDeath","victoryCon","mayGameEndCon"};
	private boolean editable;

	public TabbedPanel(Window window){
		this(window, false);
	}

	public TabbedPanel(Window window, boolean editable){
		this.Window = window;
		this.editable = editable;
		setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
	}

	/*public void addTab(){
		addTab("New Tab");
	}

	public void addTab(String title){
		addTab(title, title+" entry");
	}*/

	public void addTab(String title, String content){
		//remove the add tab button first
		if(editable){
			System.out.println("Should be removing the +'s");
			for(int i = 0;i < getTabCount(); i++){
				Component comp = getTabComponentAt(i);
				if(comp instanceof AddTabPanel){
					int r = indexOfTabComponent(comp);
					if (r != -1) {
						System.out.println("+ foudn adn removed");
						this.remove(i);
					}
				}
			}
		}

		//add the new tab
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
		add(title, scrollPane);

		//replaces tab title with UI
		TabPanel titleUI = new TabPanel(title);
		titleUI.setClosable(editable);
		this.setTabComponentAt(getTabCount()-1, titleUI);

		//re-add the addTab(this amkes surde the add tab is on the end)
		if(editable){
			add("+", null);
			setTabComponentAt(getTabCount()-1, new AddTabPanel());
		}
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

	private class TabPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		private JLabel closeBut = new JLabel("x");
		public TabPanel(String name){
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setOpaque(false);
			setBorder(javax.swing.BorderFactory.createEmptyBorder());
			JLabel title = new JLabel(name+" ");
			title.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			add(title);
			closeBut.setForeground(Color.red);
			closeBut.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			closeBut.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					int i = TabbedPanel.this.indexOfTabComponent(TabPanel.this);
					if (i != -1) {
						TabbedPanel.this.remove(i);
					}
				}
			});
			add(closeBut);//use as close button for now
			closeBut.setEnabled(false);
			closeBut.setVisible(false);
		}
		public void setClosable(boolean closable){
			closeBut.setEnabled(closable);
			closeBut.setVisible(closable);
		}
	}
	/**
	 * For use as a TabLabel, clicking will allow user to add an additional script tab
	 */
	private class AddTabPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		public AddTabPanel(){
			setOpaque(false);
			setBorder(javax.swing.BorderFactory.createEmptyBorder());
			JLabel label = new JLabel("+");
			label.setForeground(Color.green);
			label.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			setToolTipText("Add new script");
			add(label);
			this.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					final JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

					final JComboBox<String> scriptNamesList = new JComboBox<String>();
					for(String script : scriptNames){
						scriptNamesList.addItem(script);
					}
					scriptNamesList.setEditable(true);

					JButton addBut = new JButton("Add");
					JButton cancelBut = new JButton("Cancel");


					panel.add(scriptNamesList);
					panel.add(addBut);
					panel.add(cancelBut);
					panel.setVisible(true);

					final Integer layer = Window.createIFrame("popupcustom", null, panel);

					addBut.setAction(new AbstractAction("Add") {
						private static final long serialVersionUID = 1L;
						public void actionPerformed(ActionEvent evt){
							String name = ((String)scriptNamesList.getSelectedItem());
							if(!name.isEmpty() && !name.equals(" ")){
								TabbedPanel.this.addTab(name, "");
								//panel.setVisible(false);//TODO until easier method to close the popup
								Window.deleteIFrame(layer);
							}
						}
					});
					cancelBut.setAction(new AbstractAction("Cancel") {
						private static final long serialVersionUID = 1L;
						public void actionPerformed(ActionEvent evt){
							Window.deleteIFrame(layer);
						}
					});
				}
			});
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
					if(!scriptName.equals("+")){
						RTextArea textArea = ((RTextScrollPane) comp).getTextArea();
						if(textArea != null){
							ersScript.put(scriptName, textArea.getText());
						}
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
		this.editable = editable;
		for (Component comp : this.getComponents()) {
			if (comp instanceof RTextScrollPane) {
				RTextArea textArea = ((RTextScrollPane) comp).getTextArea();
				if(textArea != null){
					textArea.setEditable(editable);
				}
			}
		}
		for(int i = 0;i < this.getTabCount(); i++){
			Component comp = this.getTabComponentAt(i);
			if(comp instanceof TabPanel){
				((TabPanel) comp).setClosable(editable);
			}
			if(comp instanceof AddTabPanel){
				int r = TabbedPanel.this.indexOfTabComponent(comp);
				if (r != -1) {
					TabbedPanel.this.remove(i);
				}
			}
		}
		if(editable){
			add("+", null);
			setTabComponentAt(getTabCount()-1, new AddTabPanel());
		}
	}

}
