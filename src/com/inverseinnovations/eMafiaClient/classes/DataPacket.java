/* eMafiaClient - DataPacket.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes;

import java.io.UnsupportedEncodingException;

public class DataPacket {
	private static final String CMDVARDIVIDER = ""; //(27)
	private static final String CMDVARSUBDIVIDER = ""; //(03)
	private int control;
	private byte[] data;
	private Object string;

	public DataPacket(int control, byte[] data){
		this.control = control;
		this.data = data;
		if(control < 1000){//all these commands only use arrays
			try {
				parseStrings();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	/**Return the numerical value that represents the command being sent*/
	public int getControl(){
		return control;
	}
	/**Returns the name of the command being sent*/
	public String getCommand(){//TODO should make this a map instead
		String r = "Unknown";
		switch(control){
		//-Connection-// 0 - 9
		case 0:r="Connection";break;
		case 1:r="Password";break;
		case 2:r="Password2";break;
		case 3:r="Login Prompt";break;
		case 4:r="Register Prompt";break;
		case 5:r="Verification Prompt";break;
		case 6:r="Disconnect";break;
		//-Entering/Exiting screens-// 50 - 69
		case 50:r="Close Window";break;
		case 51:r="Lobby Window";break;
		case 52:r="Match Setup Window";break;
		case 53:r="Match Inplay Window";break;
		//-Chat functions-// 70 - 99
		case 70:r="Lobby Chat Received";break;
		case 71:r="Lobby Chat Received From Character";break;
		case 75:r="Lobby Player List Refresh";break;
		case 76:r="Lobby Player List Add";break;
		case 77:r="Lobby Player List Remove";break;
		//-Lobby functions-// 100 - 109
		case 100:r="Lobby Match List Refresh";break;
		//-Match functions-// 200+
		case 200:r="Match Targettable Players";break;
		case 201:r="Match Time of Day";break;
		case 202:r="Match Player Number";break;
		case 203:r="Match Alive Player List Refresh";break;
		//TODO add/remove players from alive list
		case 204:r="Match Dead Player List Refresh";break;
		//TODO add players to graveyard
		case 205:r="Timer Set";break;
		case 206:r="Name Selection Window";break;
		case 207:r="Match Settings";break;
		case 208:r="Votes Update";break;
		case 209:r="Votes Remove";break;
		//-Other-//
		case 800:r="Role Search Results";break;
		case 801:r="Role Possible List";break;
		case 802:r="Role Setup List";break;
		case 803:r="Character ID Set";break;
		case 804:r="Character Data Update";break;//TODO updates serialized character
		//-Popups/prompts-//
		case 998:r="Role View Window";break;
		case 999:r="Generic Window";break;
		default: r="Unknown";break;
		}
		return r;
	}
	private void parseStrings() throws UnsupportedEncodingException {
		if(data != null){
			String fullDetail = new String(data,"ISO-8859-1");//XXX may need to enforce charset later
			String[] detail;
			String[][] detail2;

			if(fullDetail.contains(CMDVARDIVIDER)){
				detail = fullDetail.split(CMDVARDIVIDER);
				int hit = 0;
				for (String s : detail) {
					if(s != null){
						if(s.contains(CMDVARSUBDIVIDER)){
							hit = s.split(CMDVARSUBDIVIDER).length;
							break;
						}
					}
				}
				if(hit > 0){
					detail2 = new String[detail.length][hit];
					for (int i = 0; i < detail.length; i++) {
						if(detail[i] != null){
							if(detail[i].contains(CMDVARSUBDIVIDER)){//fullDetail.contains(CMDVARDIVIDER)
								detail2[i] = detail[i].split(CMDVARSUBDIVIDER);
							}
						}
					}
					string = detail2;
				}
				else{
					string = detail;
				}
			}
			else if(fullDetail.contains(CMDVARSUBDIVIDER)){
				detail = fullDetail.split(CMDVARSUBDIVIDER);
				detail2 = new String[1][detail.length];
				detail2[0] = detail;
				string = detail2;
			}
			else{
				detail = new String[]{fullDetail};
				string = detail;
			}
		}
	}
	/**Returns the unparsed details of the dataPacket*/
	public Object getData(){
		return data;
	}
	/**If data contains a String Array, returns the array element.*/
	public String getString(int num){
		String r = null;
		num--;
		if(string instanceof String[]){
			if(((String[]) string).length >= num){
				r = ((String[])string)[num];
			}
		}
		else if(string instanceof String){
			r = (String) string;
		}
		return r;
	}
	/**If data contains only a single String, returns it*/
	public String getString(){
		String r = null;
		if(string instanceof String[]){
			if(((String[]) string).length >= 1){
				r = ((String[])string)[0];
			}
		}
		else if(string instanceof String){
			r = (String) string;
		}
		return r;
	}
	/**If data is a String Array, returns it*/
	public String[] getStringArray(){
		return (String[]) string;
	}
	/**If data is a String Array of Arrays, returns it*/
	public String[][] getStringArrayArray(){
		String[][] r = null;
		if(string instanceof String[][]){
			r = (String[][]) string;
		}
		else if(string instanceof String[]){
			System.out.println("getStringArrayArray is actually just a singel Array....hrm..");
		}
		return r;
	}

}
