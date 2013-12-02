/* eMafiaClient - List_DeadPlayer.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.inverseinnovations.eMafiaClient.Framework;


public class List_DeadPlayer extends JPanel{
	private static final long serialVersionUID = 1L;
	public Framework Framework;
	public int id;
    public String name;
    public String hexcolor;
    public String nameDisplay;
    public String death;//only for graveyard TODO need to remove and fix graveyard

    private JLabel playerName;

    public List_DeadPlayer(final Framework Framework, final int id, String name, String hexcolor){
    	this.id = id;
    	this.name = name;
    	this.hexcolor = hexcolor;
    	this.nameDisplay = hexcolor;

    	setSize(200, 30);

		playerName = new JLabel("<html><font color=\""+hexcolor+"\">"+name+"</font></html>");//playerName.setBackground(new Color(32,32,32));

		add(playerName);


		playerName.setVisible(true);
    }

    public String toString() {
    	return name;
    }
}