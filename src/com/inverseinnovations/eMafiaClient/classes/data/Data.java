/* eMafiaClient - Dava.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

import org.jdesktop.swingx.JXList;
import com.inverseinnovations.sharedObjects.*;
import com.inverseinnovations.eMafiaClient.*;
import com.inverseinnovations.eMafiaClient.classes.Comparator_PlayerLobby;
import com.inverseinnovations.eMafiaClient.classes.jobjects.*;


public class Data {
	public Framework Framework;
	public Match curMatch;// = new Match(Framework);
	public int characterId = 0;
	public Map<Integer, CharacterData> characterData = new LinkedHashMap<Integer, CharacterData>();
	public HtmlPane chatOutput = new HtmlPane();
	public RoleDataDisplay roleView;
	public DefaultListModel<List_Character> playerListModel = new DefaultListModel<List_Character>();
	//public JList<Object> playerList = new JList<Object>(playerListModel);
	public JXList playerList = new JXList(playerListModel);
	public DefaultListModel<List_Match> matchListModel = new DefaultListModel<List_Match>();
	//public JList<Matchlisted> matchList = new JList<Matchlisted>(matchListModel);
	public JXList matchList = new JXList(matchListModel);
	public DefaultListModel<List_Role> roleSearchListModel = new DefaultListModel<List_Role>();
	public JXList roleSearchList = new JXList(roleSearchListModel);


	public Data(Framework framework){
		this.Framework = framework;
		this.curMatch = new Match(Framework);
		roleView = new RoleDataDisplay(Framework.Window, false);

		playerList.setComparator(new Comparator_PlayerLobby());
		playerList.setAutoCreateRowSorter(true);
		playerList.setSortOrder(SortOrder.ASCENDING);
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerList.setLayoutOrientation(JXList.VERTICAL);
		playerList.setBackground(new Color(32,32,32));
		playerList.setCellRenderer(Framework.Window.CellRenderer.new PlayerList());

		roleSearchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roleSearchList.setLayoutOrientation(JList.VERTICAL);
		roleSearchList.setBackground(new Color(32,32,32));
		roleSearchList.setCellRenderer(Framework.Window.CellRenderer.new RoleList());
	}

	public boolean serializeObject(String location, String fileName, Object object){
		boolean success = false;
		FileOutputStream fileOut = null;
		ObjectOutputStream outObj = null;
		try {
			File dir = new File(System.getProperty("user.dir") + location);
			dir.mkdirs();//Errors if the dir doesn't exist
			fileOut = new FileOutputStream(System.getProperty("user.dir") + location +fileName);
			outObj = new ObjectOutputStream(fileOut);
			outObj.writeObject(object);

			success = true;
		}
		catch (IOException e) {e.printStackTrace();}
		finally{
			try {
			    	//pretty hacky but it wasn't going to work anyway if either is null
			    	/*if (outObj == null || fileOut == null) {
			    	    throw new NullPointerException();
			    	}*/
				outObj.close();
				fileOut.close();
			}
			catch (IOException e){e.printStackTrace();}
			catch (NullPointerException e) {e.printStackTrace();}
		}
		return success;
	}

	public Object unserializeObject(String location, String fileName){
		FileInputStream fileInput = null;
		ObjectInputStream inputObj = null;
		Object object = null;
		try{
			fileInput = new FileInputStream(System.getProperty("user.dir") + location +fileName);
			inputObj = new ObjectInputStream(fileInput);
			object = inputObj.readObject();
		}
		catch (Exception e) {e.printStackTrace();}
		finally{
			try {
			    //pretty hacky but it wasn't going to work anyway if either is null
			    	/*if (inputObj == null || fileInput == null) {
			    	    throw new NullPointerException();
			    	}*/
				inputObj.close();
				fileInput.close();
			}
			catch (IOException e){e.printStackTrace();}
			catch (NullPointerException e) {e.printStackTrace();}
		}
		return object;
	}

}
