/* eMafiaClient - CellRenderer.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes;


import java.awt.Color;
import java.awt.Component;
import javax.swing.*;

import com.inverseinnovations.eMafiaClient.*;
import com.inverseinnovations.eMafiaClient.classes.data.*;
import com.inverseinnovations.eMafiaClient.classes.jobjects.*;


public class CellRenderer {
	public Framework Framework;
	public Color roleUnSelected = new Color(32,32,32);
	public Color roleSelected = Color.white;

	/**The visual display of JLists*/
	public CellRenderer(Framework Framework){
		this.Framework = Framework;
	}

	public class PlayerAccountList implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
			List_Character playerObj = (List_Character) value;
			JLabel playerName = new JLabel("<html><font color=\""+playerObj.hexcolor+"\">"+playerObj.name+"</font></html>");
			//playerName.setForeground(Color.red);
			JPanel playerPanel = new JPanel();

			playerPanel.add(playerName);

			Component component = playerPanel;
			//component.setBackground(isSelected ? Color.black : Color.white);
			//component.setForeground(isSelected ? Color.white : Color.black);
			component.setBackground(new Color(32,32,32));
			return component;
		}
	}
	/*
	public class MatchAliveDayDiscList implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
			Player playerObj = (Player) value;
			JLabel playerNum = new JLabel("<html><font size=\"1\" color=\"DDDDDD\">"+playerObj.id+" </font></html>");
			JLabel playerName = new JLabel("<html><font color=\""+playerObj.hexcolor+"\">"+playerObj.name+"</font></html>");
			//playerName.setForeground(Color.red);
			JPanel playerPanel = new JPanel();
			playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
			playerPanel.add(playerNum);
			playerPanel.add(playerName);

			Component component = playerPanel;
			component.setBackground(new Color(32,32,32));
			return component;
		}
	}

	public class MatchAliveDayVoteList implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
			final Player playerObj = (Player) value;
			JLabel playerNum = new JLabel("<html><font size=\"1\" color=\"DDDDDD\">"+playerObj.id+" </font></html>");
			JLabel playerName = new JLabel("<html><font color=\""+playerObj.hexcolor+"\">"+playerObj.name+"</font></html>");

			JPanel playerPanel = new JPanel();
			playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
			playerPanel.add(playerNum);
			playerPanel.add(playerName);
			if(playerObj.id != Framework.Data.curMatch.currentPlayerNum){//as long as you arent this player...add the vote button!
				JButton vote = new JButton(" ");
				Action voteAction = new AbstractAction(" ") {
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e) {
						Framework.Telnet.write("-vote "+playerObj.id);
					}
				};
				vote.setAction(voteAction);
				playerPanel.add(vote);
			}
			Component component = playerPanel;
			component.setBackground(new Color(32,32,32));
			return component;
		}
	}*/

	public class PlayerList implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,boolean cellHasFocus) {

			BackgroundPanel playerpanel = new BackgroundPanel(Framework.THEME_PATH+Framework.CURRENT_TEXTURE_PACK+"/PlayerList",2);
			//JPanel playerpanel = new JPanel();
			playerpanel.add((JComponent)value);
			Component component = playerpanel;
			return component;
		}
	}

	public class MatchDeadList implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
			List_DeadPlayer playerObj = (List_DeadPlayer) value;
			//JLabel playerNum = new JLabel("<html><font size=\"1\" color=\"DDDDDD\">"+playerObj.id+" </font></html>");
			JLabel playerName = new JLabel("<html><font color=\""+playerObj.hexcolor+"\">"+playerObj.name+" </font></html>");
			JLabel playerDeath = new JLabel("<html><font color=\"DDDDDD\"> "+playerObj.death+"</font></html>");
			//playerName.setForeground(Color.red);
			JPanel playerPanel = new JPanel();
			playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
			playerPanel.add(playerName);
			playerPanel.add(playerDeath);

			Component component = playerPanel;
			//component.setBackground(isSelected ? Color.black : Color.white);
			//component.setForeground(isSelected ? Color.white : Color.black);
			component.setBackground(new Color(32,32,32));
			return component;
		}
	}

	public class MatchList implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,boolean cellHasFocus) {

			BackgroundPanel matchPanel = new BackgroundPanel(Framework.THEME_PATH+Framework.CURRENT_TEXTURE_PACK+"/MatchList",3);
			matchPanel.add((JComponent)value);
			Component component = matchPanel;
			return component;
		}
	}

	public class RoleList implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
			List_Role role = (List_Role) value;
			JLabel roleName = new JLabel("<html><font color=\""+role.hexcolor+"\">"+role.name+" </font></html>");
			JLabel roleAff = new JLabel("<html><font color=\""+role.hexcolor+"\"> "+role.aff+"</font></html>");

			JPanel rolePanel = new JPanel();
			rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.X_AXIS));
			rolePanel.add(roleAff);
			rolePanel.add(roleName);

			Component component = rolePanel;
			component.setBackground(isSelected ? roleSelected : roleUnSelected );
			return component;
		}
	}

	public class RoleSetupList implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
			List_Role role = (List_Role) value;
			JLabel roleName = new JLabel("<html><font color=\""+role.hexcolor+"\">"+role.name+" </font></html>");
			roleName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			JPanel rolePanel = new JPanel();
			rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.X_AXIS));
			rolePanel.add(roleName);

			Component component = rolePanel;
			component.setBackground(isSelected ? roleSelected : roleUnSelected );
			return component;
		}
	}
}
