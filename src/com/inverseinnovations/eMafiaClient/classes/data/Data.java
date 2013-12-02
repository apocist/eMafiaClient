/* eMafiaClient - Dava.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import org.jdesktop.swingx.JXList;
import com.inverseinnovations.eMafiaClient.*;
import com.inverseinnovations.eMafiaClient.classes.jobjects.*;


public class Data {
	public Framework Framework;
	public Match curMatch;// = new Match(Framework);
	public int characterId = 0;
	public HtmlPane chatOutput = new HtmlPane();
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
	}

	public boolean serializeCharacter(CharacterSerialize chara){
		boolean success = false;
		FileOutputStream fileOut = null;
		ObjectOutputStream outObj = null;
		try {
			fileOut = new FileOutputStream(System.getProperty("user.dir") + "/cache/users/" +chara.eid+".usr");
			outObj = new ObjectOutputStream(fileOut);
			outObj.writeObject(chara);

			success = true;
			System.out.println("Character "+chara.name+" was serialized!");
		}
		catch (IOException e) {e.printStackTrace();}
		finally{
			try {
				outObj.close();
				fileOut.close();
			}
			catch (IOException e){e.printStackTrace();}
		}
		return success;
	}

	public CharacterSerialize unserializeCharacter(final int eid){
		FileInputStream fileInput = null;
		ObjectInputStream inputObj = null;
		CharacterSerialize chara = null;
		try{
			fileInput = new FileInputStream(System.getProperty("user.dir") + "/cache/users/" +eid+".usr");
			inputObj = new ObjectInputStream(fileInput);
			chara = (CharacterSerialize) inputObj.readObject();
			System.out.println("Character "+chara.name+" was loaded!");
		}
		catch (Exception e) {e.printStackTrace();}
		finally{
			try {
				inputObj.close();
				fileInput.close();
			}
			catch (IOException e){e.printStackTrace();}
		}
		return chara;
	}
}
