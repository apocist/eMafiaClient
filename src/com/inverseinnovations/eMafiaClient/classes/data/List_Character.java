/* eMafiaClient - List_Character.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.inverseinnovations.eMafiaClient.Framework;
import com.inverseinnovations.eMafiaClient.classes.Utils;


public class List_Character extends JPanel{
	private static final long serialVersionUID = 1L;
	public Framework Framework;
	public int id;
	public int eid;
	public String name;
	public String hexcolor;
	public String nameDisplay;
	public String avatarUrl;
	public Image avatar;

	private JLabel playerName;

	public List_Character(final Framework Framework, final int id, String name, String hexcolor, String avatarUrl){
		this.id = id;
		this.name = name;
		this.hexcolor = hexcolor;
		this.nameDisplay = hexcolor;
		this.avatarUrl = avatarUrl;

		//TODO need to have a Constants file....
		this.avatar = Utils.getSC2MImageCache(Framework.SC2M_WEB_PATH, avatarUrl).getScaledInstance(28, 28, Image.SCALE_FAST);

		//setSize(200, 30);
			setLayout(new FlowLayout(FlowLayout.LEADING, 3, 0));//remove padding
			setSize(200, 30);
			setPreferredSize(new Dimension(200, 30));
			setBorder(new EmptyBorder(0, 0, 0, 0) );

		JLabel avaLabel = new JLabel(new ImageIcon(avatar));
		avaLabel.setPreferredSize(new Dimension(28, 28));

		playerName = new JLabel("<html><font color=\""+hexcolor+"\">"+name+"</font></html>");//playerName.setBackground(new Color(32,32,32));
		playerName.setPreferredSize(new Dimension(163, 30));playerName.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

		add(avaLabel);
		add(playerName);

		avaLabel.setVisible(true);
		playerName.setVisible(true);
	}

	public String toString() {
		return name;
	}
}
