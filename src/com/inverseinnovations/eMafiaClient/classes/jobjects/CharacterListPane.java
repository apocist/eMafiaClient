/* eMafiaClient - CharacterListPane.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.border.EmptyBorder;

import com.inverseinnovations.eMafiaClient.*;
import com.inverseinnovations.eMafiaClient.classes.Comparator_PlayerLobby;


/**
 * Allows this component directly place on JLayeredPane to be auto size according to
 * current frame size.
 * Allows either Fullscreen or Center
 * Allows additional function of detecting preferred size correctly
 */
public class CharacterListPane extends JAutoPanel{
	private static final long serialVersionUID = 1L;

	public CharacterListPane(Framework Framework){
		super(Framework.Window.desktop);
		this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		this.setBorder(new EmptyBorder(0, 0, 0, 0) );

		Framework.Data.playerList.setComparator(new Comparator_PlayerLobby());
		Framework.Data.playerList.setAutoCreateRowSorter(true);
		Framework.Data.playerList.setSortOrder(SortOrder.ASCENDING);
		Framework.Data.playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Framework.Data.playerList.setLayoutOrientation(JList.VERTICAL);
		Framework.Data.playerList.setBackground(new Color(32,32,32));
		Framework.Data.playerList.setCellRenderer(Framework.Window.CellRenderer.new PlayerList());
		JScrollPane playerScrollpane = new JScrollPane(Framework.Data.playerList);

		this.add(playerScrollpane);
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(100, 4000));

	}
}
