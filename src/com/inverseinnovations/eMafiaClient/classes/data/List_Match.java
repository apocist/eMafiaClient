/* eMafiaClient - List_Match.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.inverseinnovations.eMafiaClient.*;

public class List_Match extends JPanel{
	private static final long serialVersionUID = 1L;
	public Framework Framework;
	public int id;
	public String name;
	public int cur_players;
	public int max_players;

	public List_Match(final Framework Framework, String[] param){//int id, String name, int cur_players, int max_players){
		this.Framework = Framework;
		this.id = Integer.parseInt(param[0]);
		this.name = param[1];
		this.cur_players = Integer.parseInt(param[2]);
		this.max_players = Integer.parseInt(param[3]);

		//this.setSize(new Dimension(300,250));//size seems to make no difference
		this.setBackground(Color.white);
		//this.setSize(this.getParent().getSize());
		JLabel matchName = new JLabel(this.name);
		JLabel playerCount = new JLabel(this.cur_players + "/" + this.max_players + " players");
		//JButtonData joinBut = new JButtonData();
		JButton joinBut = new JButton();

		//joinBut.int1 = this.id;
		joinBut.setText("Join");
		joinBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int matchID = id;//((JButtonData)evt.getSource()).int1;
				   Framework.Telnet.write("-match join "+matchID);
				//System.out.print("------Button fired! Matchid "+matchID+"\n");
			}
		});
		matchName.setForeground(Color.red);
		matchName.setFont(new Font("Default",1,18));
		playerCount.setForeground(Color.pink);
		//JPanel matchPanel = new JPanel();
		//BackgroundPanel matchPanel = new BackgroundPanel(new ImageIcon("texture/50BulletM.png").getImage(),3,new ImageIcon("texture/50BulletL.png").getImage(),new ImageIcon("texture/50BulletR.png").getImage());
		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		textPanel.add(matchName);
		textPanel.add(playerCount);
		this.add(textPanel,BorderLayout.CENTER);
		this.add(joinBut,BorderLayout.LINE_END);
		//this.add(matchPanel);

	}
	public String toString() {
		return name;
	}
}
