/* eMafiaClient - List_AlivePlayer.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.inverseinnovations.eMafiaClient.Framework;

//import eMafiaClient.classes.PlayerLayout;;
//TODO need to make voting and target buttons diff locations or they click each other
public class List_AlivePlayer extends JPanel{
	private static final long serialVersionUID = 1L;
	public Framework Framework;
	public int id;
	public String name;
	public String hexcolor;
	public String nameDisplay;
	//public String death;//only for graveyard

	private JLabel playerNum;
	private JLabel playerName;
	private JButton voteBut;
	private JButton target1But;
	private JButton target2But;

	public List_AlivePlayer(final Framework Framework, final int id, String name, String hexcolor){
		this.id = id;
		this.name = name;
		this.hexcolor = hexcolor;
		this.nameDisplay = hexcolor;

		//this.setBackground(new Color(32,32,32));
		//XXX testing layout
		//setLayout(null);
		//setLayout(new PlayerLayout());
		setLayout(new FlowLayout(FlowLayout.LEADING, 2, 0));//remove padding
		setSize(200, 30);
		setPreferredSize(new Dimension(200, 30));
		setBorder(new EmptyBorder(0, 0, 0, 0) );

		playerNum = new JLabel("<html><font size=\"1\" color=\"DDDDDD\">"+id+" </font></html>");
		//playerNum.setBorder(new EmptyBorder(0, 2, 0, 2) );
		playerNum.setPreferredSize(new Dimension(12, 24));playerNum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);playerNum.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		playerName = new JLabel("<html><font color=\""+hexcolor+"\">"+name+"</font></html>");//playerName.setOpaque(true);//.setBackground(new Color(32,32,32));
		//playerName.setBorder(new EmptyBorder(0, 0, 0, 2) );
		playerName.setPreferredSize(new Dimension(147, 30));playerName.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
		voteBut = new JButton();
		Action voteAction = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				Framework.Telnet.write("-vote "+id);
			}
		};
		voteBut.setAction(voteAction);voteBut.setMargin(new Insets(0,0,0,0));voteBut.setBackground(Color.GREEN);voteBut.setBorderPainted(false);voteBut.setPreferredSize(new Dimension(10, 23));//voteBut.setFont(voteBut.getFont().deriveFont(2));
		JPanel votePanel = new JPanel();votePanel.setLayout(new BorderLayout(0, 0));votePanel.setPreferredSize(new Dimension(10, 23));votePanel.setName("buttonHolder");votePanel.setOpaque(false);
		votePanel.add(voteBut);

		target1But = new JButton();
		Action target1Action = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				Framework.Telnet.write("-target1 "+id);
			}
		};
		target1But.setAction(target1Action);target1But.setBackground(Color.BLUE);target1But.setBorderPainted(false);target1But.setPreferredSize(new Dimension(10, 23));
		JPanel target1Panel = new JPanel();target1Panel.setLayout(new BorderLayout(0, 0));target1Panel.setPreferredSize(new Dimension(10, 23));target1Panel.setName("buttonHolder");target1Panel.setOpaque(false);
		target1Panel.add(target1But);

		target2But = new JButton();
		Action target2Action = new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				Framework.Telnet.write("-target2 "+id);
			}
		};
		target2But.setAction(target2Action);target2But.setBackground(Color.BLUE);target2But.setBorderPainted(false);target2But.setPreferredSize(new Dimension(10, 23));
		JPanel target2Panel = new JPanel();target2Panel.setLayout(new BorderLayout(0, 0));target2Panel.setPreferredSize(new Dimension(10, 23));target2Panel.setName("buttonHolder");target2Panel.setOpaque(false);
		target2Panel.add(target2But);

		//this.add(playerName,BorderLayout.CENTER);
		add(playerNum);
		add(playerName);
		add(votePanel);
		add(target1Panel);
		add(target2Panel);

		//playerNum.setBounds(25, 50, playerNum.getPreferredSize().width, playerNum.getPreferredSize().height);
		playerNum.setVisible(true);
		//playerName.setBounds(40, 50, 100, 20);
		playerName.setVisible(true);
		//voteBut.setBounds(170, 50, 7, 14);
		voteBut.setVisible(false);
		//target1But.setBounds(170, 50, 7, 14);
		target1But.setVisible(false);
		//target2But.setBounds(185, 50, 7, 14);
		target2But.setVisible(false);
	}

	public void dayVoting(int curPlayerNum, int[] targetsD1, int[] targetsD2){
		//this.removeAll();
		//this.add(playerNum,BorderLayout.WEST);
		//this.add(playerName,BorderLayout.CENTER);
		setAllNotVisible();
		playerName.setVisible(true);
		playerNum.setVisible(true);
		if(curPlayerNum != id){
			//this.add(voteBut,BorderLayout.EAST);
			voteBut.setVisible(true);
		}
		if(targetsD1[0] == -1 || ((targetsD1[0] == -3) && (curPlayerNum == id)) || ((targetsD1[0] == -2) && (curPlayerNum != id))){//if everyone or self
			//this.add(target1But,BorderLayout.EAST);
			target1But.setVisible(true);
		}
		if(targetsD2[0] == -1 || ((targetsD2[0] == -3) && (curPlayerNum == id)) || ((targetsD2[0] == -2) && (curPlayerNum != id))){//if everyone or self
			//this.add(target2But,BorderLayout.EAST);
			target2But.setVisible(true);
		}
	}
	public void nightNorm(int curPlayerNum, int[] targetsN1, int[] targetsN2){
		//this.removeAll();
		//this.add(playerNum,BorderLayout.WEST);
		//this.add(playerName,BorderLayout.CENTER);
		setAllNotVisible();
		playerName.setVisible(true);
		playerNum.setVisible(true);
		if(targetsN1[0] == -1 || ((targetsN1[0] == -3) && (curPlayerNum == id)) || ((targetsN1[0] == -2) && (curPlayerNum != id))){//if everyone or self
			//this.add(target1But,BorderLayout.EAST);
			target1But.setVisible(true);
		}
		if(targetsN2[0] == -1 || ((targetsN2[0] == -3) && (curPlayerNum == id)) || ((targetsN2[0] == -2) && (curPlayerNum != id))){//if everyone or self
			//this.add(target2But,BorderLayout.EAST);
			target2But.setVisible(true);
		}
	}
	public void dayNoVote(){
		//this.removeAll();
		//this.add(playerNum,BorderLayout.EAST);
		//this.add(playerName,BorderLayout.CENTER);
		setAllNotVisible();
		playerName.setVisible(true);
		playerNum.setVisible(true);
	}
	public void setVoteCount(String vote){
		if(!vote.equals("")){
			voteBut.setText("<html><font size=\"4\"><b>"+vote+"</b></font></html>");
		}
		else{
			voteBut.setText("");
		}
		//voteBut.setText(vote);
	}
	/** Makes all non-JPanels turn inviso*/
	public void setAllNotVisible(){
		for(Component c : this.getComponents()){
			if(c.getClass().getSimpleName().equals("JPanel")){
				for(Component c2 : ((JPanel) c).getComponents()){
					c2.setVisible(false);
				}
			}
			else{
				c.setVisible(false);
			}
		}
	}
	public int getPlayerNum(){
		return id;
	}
	public String toString() {
		return name;
	}
}
