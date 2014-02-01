/* eMafiaClient - Telnet.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.inverseinnovations.sharedObjects.*;
import com.inverseinnovations.sharedObjects.CharacterData;
import com.inverseinnovations.eMafiaClient.classes.DataPacket;
import com.inverseinnovations.eMafiaClient.classes.Utils;
import com.inverseinnovations.eMafiaClient.classes.data.*;

public class Telnet {
	public final Framework Framework;
	private Socket soc = null;
	private DataInputStream in;
	private DataOutputStream out;
	public boolean SERVERRUNNING = true;
	public boolean MANUELDC = false;
	public String var1, var2;

	public Telnet(final Framework framework) {
		this.Framework = framework;
	}

	public void connect(String server, String port) {
		try {
			int port_int = Integer.parseInt(port);
			// Connect to the specified server
			try {
				soc = new Socket(server, port_int);
			} catch (IOException e) {
				System.out.println("Can't connect to eMafia\n");
				SERVERRUNNING = false;
				Framework.Window.createIFrame("popup", new String[] {"eMafia Server is OFFLINE", "exit" });
			}
			// in = new BufferedReader(new
			// InputStreamReader(soc.getInputStream()));
			in = new DataInputStream(soc.getInputStream());
			//out = new PrintWriter(soc.getOutputStream());
			out = new DataOutputStream(soc.getOutputStream());

			new Thread(null, null, "Telnet Listener") {
				public void run() {
					this.setPriority(6);// above normal
					while (SERVERRUNNING) {
						try {
							readUntil();// Read until lext input
							// Thread.sleep(100);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		} catch (Exception e) {

		}
	}

	public void disconnect() {
		try {
			if (soc != null) {
				this.soc.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readUntil() {
		try {
			// String line;
			while (in.readUnsignedByte() == 0) {// gets 1 byte starter
				int control;
				int size;
				byte[] data = null;
				control = in.readUnsignedShort();// gets 2 byte control
				size = in.readUnsignedShort();// gets 2 byte size of data(may
												// increase size later..only
												// supports 65.5 kbs of data
												// transfer per command)

				// System.out.println("["+control+"] size "+size+" data: "+data);
				if (size > 0) {
					data = new byte[size];
				}
				in.readFully(data, 0, size);// gets data

				if (in.readUnsignedByte() == 255) {// gets 1 byte ender, if data
													// too big, this wont be
													// reached then theres error
					doCommand(new DataPacket(control, data));
					if(size > 0){//just for debugging purposes
						if(control < 1000){
							System.out.println("[" + control + "] "+ new String(data, "ISO-8859-1"));
						}
						else{System.out.println("[" + control + "] <Object> "+size+" bytes");}
					}
					else{System.out.println("[" + control + "]");}
				}
				else {
					System.out.println("****ERROR**NO COMMAND ENDER****");
				}
				return;
			}
		} catch (SocketException e) {
			System.out.println("Connection Socket Error\n");
			SERVERRUNNING = false;
			if(!MANUELDC){
				Framework.Window.createIFrame("popup", new String[] {"<html><b>You were Disconnected from eMafia</b></html>","exit","html" });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void write(String string){
		write(string, null);
	}
	public void write(String string, Object data) {
		try {
			//new
			byte[] commandBytes = null;
			int size = 0;
			if(string != null){if(string != ""){
				commandBytes = string.getBytes(Charset.forName("ISO-8859-1"));
				size = commandBytes.length;
			}}
			byte[] cmdSize = new byte[1];
			//for (int i=1; i>=0; i--) {
				cmdSize[0] = (byte) (size & 0xff);
				size >>= 4;//need to change this to one byte instead
			//}
			//so now got the size of Command and command
			byte[] dataBytes = null;
			size = 0;
			if(data != null){
				dataBytes = objectToByte(data);
				size = dataBytes.length;
			}
			byte[] dataSize = new byte[2];
			for (int i=1; i>=0; i--) {
				dataSize[i] = (byte) (size & 0xff);
				size >>= 8;
			}
			//now got data...kinda
			ByteArrayList array = new ByteArrayList(new byte[]{0x00});
			array.append(cmdSize);
			array.append(commandBytes);
			array.append(dataSize);
			if(data != null){array.append(dataBytes);}
			array.append(new byte[]{(byte) 0xff});
			//TODO more to go
			//{00}{cmdSize(1 byte)}{commandBytes(up to 255 bytes)}{FF}{FF}
			//new end
			//out.println(string);
			//out.flush();
			out.write(array.toByteArray());
			System.out.println("--> " + string);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wrapper class to provide ease of converting Strings, ints, and bytes
	 * into a series of bytes.
	 */
	public static class ByteArrayList extends ArrayList<Byte>{
		private static final long serialVersionUID = 1L;
		/**
		 * Wrapper class to provide ease of converting Strings, ints, and bytes
		 * into a series of bytes.
		 */
		public ByteArrayList(){
			super();
		}
		/**
		 * Wrapper class to provide ease of converting Strings, ints, and bytes
		 * into a series of bytes.
		 */
		public ByteArrayList(byte[] byteArray){
			super();
			for(byte b : byteArray){
				this.add(b);
			}
		}
		/**Appends a series of bytes*/
		public void append(byte[] byteArray){
			for(byte b : byteArray){
				this.add(b);
			}
		}
		/**Appends a ByteArrayList*/
		public void append(ByteArrayList arrayList){
			for(byte b : arrayList){
				this.add(b);
			}
		}
		/**Appends an int*/
		public void append(int i){
			append(BigInteger.valueOf(i).toByteArray());
		}
		/**Appends a String*/
		public void append(String i){
			append(String.valueOf(i).getBytes());
		}
		/**Converts the ByteArrayList into byte[]*/
		public byte[] toByteArray(){
			byte[] byteArray = new byte[this.size()];
			for(int i = 0; i<this.size(); i++){
				byteArray[i] = this.get(i);
			}
			return byteArray;
		}
	}
	/**
	 * Converts an Object to byte[]
	 * @param object Object to Convert to byte[]
	 * @return null on IOException
	 */
	public static byte[] objectToByte(Object object){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] yourBytes = null;
		try {
			try {
				out = new ObjectOutputStream(bos);
				out.writeObject(object);
			}
			catch (IOException e) {
			}
			yourBytes = bos.toByteArray();
		}
		finally{
			try{
				if (out != null){
				out.close();
				}
			}
			catch (IOException ex){
				// ignore close exception
			}
			try{
				bos.close();
			}
			catch(IOException ex){
				// ignore close exception
			}
		}
		return yourBytes;
	}
	public void chatRecieved(String[] detail) {
		if (detail[0].equals("<><hr>")) {
			if (Framework.Data.chatOutput.lastMsg
					.equals("<font color=\"FFFFFF\"<><hr></font>")) {
				return;
			}
		}// return if duplicate <hr>
		Framework.Data.chatOutput.append("<font color=\"FFFFFF\">" + detail[0]
				+ "</font>");
	}

	public void chatCharRecieved(String[] detail) {
		Framework.Data.chatOutput.append("<font color=\"" + detail[0]
				+ "\"><b>" + detail[1] + "</b></font><font color=\"FFFFFF\">: "
				+ detail[2] + "</font>");
	}
	public void chatCharRecieved2(String[] detail) {
		CharacterData chara = Framework.Data.characterData.get(Integer.parseInt(detail[0]));
		Framework.Data.chatOutput.append("<font color=\""+chara.hexcolor+"\"><b>"+chara.name+"</b></font>"+
				"<font color=\"FFFFFF\">: "+detail[1]+"</font>");
	}

	public void doCommand(DataPacket data) {
		if (data != null) {
			switch (data.getCommand()) {
			// -Connection-// 0 - 9
			case "Connection":// connected/handshake
				if(Integer.parseInt(data.getString()) > Framework.Settings.CLIENT_BUILD){
					//System.out.println("Latest version is "+data.getString()+", currently at "+Framework.Settings.CLIENT_BUILD);
					MANUELDC = true;
					disconnect();
					Framework.Window.createIFrame("popup", new String[] {"<center>Latest version is Build "+data.getString()+", currently at Build "+Framework.Settings.CLIENT_BUILD+"<br>Update required!</center>", "update","html"});
				}
				break;
			case "Password":// Asking for password..if FINAL, remove from client
				if (var1 == null)
					var1 = "0";
				write(Utils.MD5(var1));
				if (data.getString() != null)
					if (data.getString().equals("FINAL"))
						var1 = null;
				break;
			case "Password2"://
				write(var2);
				break;
			case "Login Prompt":// login prompt..Asking for username..handled differently later
				Framework.Window.createIFrame("login");
				break;
			case "Register Prompt":// register prompt
				Framework.Window.createIFrame("register");
				break;
			case "Verification Prompt":// verify prompt
				Framework.Window.createIFrame("verify");
				break;
			case "Disconnect":// manuel disconnect
				disconnect();
				break;
			// -Entering/Exiting screens-// 50 - 69
			case "Close Window":// close window(detail defines window layer)
				// Framework.Window.deleteIFrame(detail[0]);
				Framework.Window.deleteIFrame(data.getString());
				break;
			case "Lobby Window":// enter lobby
				Framework.Window.countdownTimer.start(0);
				Framework.Window.createIFrame("lobby");
				break;
			case "Match Setup Window":// join a match
				Framework.Window.deleteIFrame("lobby");// lobby
				Framework.Window.createIFrame("matchSetup");
				break;
			case "Match Inplay Window":// Match Started
				Framework.Window.deleteIFrame("matchSetup");// lobby
				Framework.Window.createIFrame("matchInplay");
				break;
			// -Chat functions-// 70 - 99
			case "Lobby Chat Received":// recieved lobby chat
				chatRecieved(data.getStringArray());
				break;
			case "Lobby Chat Received From Character":// recieved lobby chat
														// from character
				chatCharRecieved(data.getStringArray());
				break;
			case "Lobby Chat Received From Character2":// recieved lobby chat object based
				// from character
				chatCharRecieved2(data.getStringArray());
				break;
			case "Lobby Player List Refresh":// refresh lobby player list
				Framework.Data.playerListModel.clear();
				/*if (data.getStringArrayArray() != null) {
					for (String[] s : data.getStringArrayArray()) {
						if (s != null) {
							Framework.Data.playerListModel
									.addElement(new List_Character(Framework,
											Integer.parseInt(s[0]), s[1], s[2],
											s[3]));
						}
					}
				}*/
				if (data.getStringArray() != null) {
					for (String s : data.getStringArray()) {
						if (s != null){if(Utils.isInteger(s)){
							CharacterData chara = Framework.Data.characterData.get(Integer.parseInt(s));
							Framework.Data.playerListModel.addElement(
									new List_Character(Framework, chara.eid, chara.name, chara.hexcolor, chara.avatarUrl));
						}}
					}
				}
				Framework.Window.autoUpdateJAutoPanels();// XXX a temp fix
				break;
			case "Lobby Player List Add":// add player to lobby list
				/*if (data.getStringArray() != null) {
					Framework.Data.playerListModel
							.addElement(
									new List_Character(Framework, Integer.parseInt(data.getStringArray()[0]), data.getStringArray()[1],data.getStringArray()[2], data.getStringArray()[3])
							);
				}*/
				if(data.getString() != null){if(Utils.isInteger(data.getString())){
					CharacterData chara = Framework.Data.characterData.get(Integer.parseInt(data.getString()));
					Framework.Data.playerListModel
						.addElement(
								new List_Character(Framework, chara.eid, chara.name, chara.hexcolor, chara.avatarUrl)
						);
				}}
				break;
			case "Lobby Player List Remove":// remove player from lobby list
				if (data.getString() != null) {
					int eid = Integer.parseInt(data.getString());
					for (int loop = 0; loop < Framework.Data.playerListModel.getSize(); loop++) {
						if (eid == Framework.Data.playerListModel.get(loop).id) {
							Framework.Data.playerListModel.remove(loop);
							break;
						}
					}
				}
				break;
			// -Lobby functions-// 100 - 109
			case "Lobby Match List Refresh":// refresh match list
				Framework.Data.matchListModel.clear();
				if (data.getStringArrayArray() != null) {
					for (String[] s : data.getStringArrayArray()) {
						if (s != null) {
							Framework.Data.matchListModel
									.addElement(new List_Match(Framework, s));
						}
					}
				}
				Framework.Window.autoUpdateJAutoPanels();// XXX a temp fix?
				break;
			// TODO add/remove matches from match list
			// -Match functions-// 200+
			case "Match Targettable Players":// sets the targetable players
				if (data.getStringArray() != null) {
					Framework.Data.curMatch.targetsN1 = new int[] { Integer
							.parseInt(data.getStringArray()[0]) };
					Framework.Data.curMatch.targetsN2 = new int[] { Integer
							.parseInt(data.getStringArray()[1]) };
					Framework.Data.curMatch.targetsD1 = new int[] { Integer
							.parseInt(data.getStringArray()[2]) };
					Framework.Data.curMatch.targetsD2 = new int[] { Integer
							.parseInt(data.getStringArray()[3]) };
				}
				break;
			case "Match Time of Day":// change time of day
				switch (Integer.parseInt(data.getString())) {
				case 1:// disc
					for (int i = 0; i < Framework.Data.curMatch.aliveListModel
							.getSize(); i++) {
						if (Framework.Data.curMatch.aliveListModel.get(i) != null) {
							Framework.Data.curMatch.aliveListModel.get(i)
									.dayNoVote();
						}
					}
					break;
				case 2:// normal day
					for (int i = 0; i < Framework.Data.curMatch.aliveListModel
							.getSize(); i++) {
						if (Framework.Data.curMatch.aliveListModel.get(i) != null) {
							Framework.Data.curMatch.aliveListModel
									.get(i)
									.dayVoting(
											Framework.Data.curMatch.currentPlayerNum,
											Framework.Data.curMatch.targetsD1,
											Framework.Data.curMatch.targetsD2);
						}
					}
					break;
				case 3:// trial plead
					for (int i = 0; i < Framework.Data.curMatch.aliveListModel
							.getSize(); i++) {
						if (Framework.Data.curMatch.aliveListModel.get(i) != null) {
							Framework.Data.curMatch.aliveListModel.get(i)
									.dayNoVote();
						}
					}
					break;
				case 4:// trial vote
						// shouldnt need anything here
						// except a new window to vote inno/guilty
					break;
				case 6:// lynch
					break;
				case 8:// night
					for (int i = 0; i < Framework.Data.curMatch.aliveListModel
							.getSize(); i++) {
						if (Framework.Data.curMatch.aliveListModel.get(i) != null) {
							Framework.Data.curMatch.aliveListModel
									.get(i)
									.nightNorm(
											Framework.Data.curMatch.currentPlayerNum,
											Framework.Data.curMatch.targetsN1,
											Framework.Data.curMatch.targetsN2);
						}
					}
					break;
				default:
					break;
				}
				Framework.Data.curMatch.aliveList.validate();
				Framework.Window.autoUpdateJAutoPanels();
				break;
			case "Match Player Number":// set playerNum
				Framework.Data.curMatch.currentPlayerNum = Integer
						.parseInt(data.getString());
				break;
			case "Match Alive Player List Refresh":// refresh living player list
				if (data.getStringArrayArray() != null) {
					Framework.Data.curMatch.aliveListModel.clear();
					for (String[] s : data.getStringArrayArray()) {
						if (s != null) {
							Framework.Data.curMatch.aliveListModel
									.addElement(new List_AlivePlayer(Framework,
											Integer.parseInt(s[0]), s[1], s[2]));
						}
					}
				}
				break;
			// TODO add/remove players from alive list
			case "Match Dead Player List Refresh":// refresh graveyard player
													// list
				if (data.getStringArrayArray() != null) {
					Framework.Data.curMatch.deadListModel.clear();
					List_DeadPlayer player;
					for (String[] s : data.getStringArrayArray()) {
						if (s != null) {
							player = new List_DeadPlayer(Framework,
									Integer.parseInt(s[0]), s[1], s[2]);
							player.death = s[3];
							Framework.Data.curMatch.deadListModel
									.addElement(player);
						}
					}
				}
				break;
			// TODO add players to graveyard
			case "Timer Set":// timer set
				Framework.Window.countdownTimer.start(Integer.parseInt(data
						.getString()));
				break;
			case "Name Selection Window":// Name Selection
				Framework.Window.createIFrame("nameSelection");
				break;
			case "Match Settings":// recieves all match settings
				Framework.Data.curMatch.id = data.getStringArray()[0];
				Framework.Data.curMatch.match_name = data.getStringArray()[1];
				if (Utils.isInteger(data.getStringArray()[2])) {
					Framework.Data.curMatch.host_id = Integer.parseInt(data
							.getStringArray()[2]);
				}
				Framework.Data.curMatch.host_name = data.getStringArray()[3];
				Framework.Data.curMatch.num_chars = data.getStringArray()[4];
				Framework.Data.curMatch.max_chars = data.getStringArray()[5];
				Framework.Data.curMatch.start_game_at = data.getStringArray()[6];
				Framework.Data.curMatch.discussion = data.getStringArray()[7];
				Framework.Data.curMatch.day_length = data.getStringArray()[8];
				Framework.Data.curMatch.night_length = data.getStringArray()[9];
				Framework.Data.curMatch.discuss_length = data.getStringArray()[10];
				Framework.Data.curMatch.trial_length = data.getStringArray()[11];
				Framework.Data.curMatch.day_type = data.getStringArray()[12];
				Framework.Data.curMatch.pm_allowed = data.getStringArray()[13];
				Framework.Data.curMatch.trial_pause_day = data.getStringArray()[14];
				Framework.Data.curMatch.trial_defense = data.getStringArray()[15];
				Framework.Data.curMatch.choose_names = data.getStringArray()[16];
				Framework.Data.curMatch.last_will = data.getStringArray()[17];
				Framework.Data.curMatch.trial_pause_day = data.getStringArray()[18];
				Framework.Data.curMatch.description = data.getStringArray()[19];
				Framework.Data.curMatch.updateMatchSetupSetting();
				break;
			case "Votes Update":// updates the votes counter of player
				String[] info = data.getStringArray();
				if (info[0] != null) {
					if (Utils.isInteger(info[0])) {
						if (info.length < 2) {
							info = new String[] { info[0], "" };
						}
						for (int i = 0; i < Framework.Data.curMatch.aliveListModel
								.getSize(); i++) {
							if (Framework.Data.curMatch.aliveListModel.get(i) != null) {
								if (Framework.Data.curMatch.aliveListModel.get(
										i).getPlayerNum() == Integer
										.parseInt(info[0])) {
									Framework.Data.curMatch.aliveListModel.get(
											i).setVoteCount(info[1]);
								}
							}
						}
					}
				}
				break;
			case "Votes Remove":// removes all votes from players
				for (int i = 0; i < Framework.Data.curMatch.aliveListModel
						.getSize(); i++) {
					if (Framework.Data.curMatch.aliveListModel.get(i) != null) {
						Framework.Data.curMatch.aliveListModel.get(i)
								.setVoteCount("");
					}
				}
				break;
			// -Other-//
			case "Role Search Results":// role search results
				Framework.Data.roleSearchListModel.clear();
				if (data.getStringArrayArray() != null) {
					for (String[] s : data.getStringArrayArray()) {
						if (s != null) {
							if (s.length == 4) {
								Framework.Data.roleSearchListModel
										.addElement(new List_Role(Integer
												.parseInt(s[0]), s[1], s[2],
												s[3]));
							}
						}
					}
				}
				break;
			case "Role Possible List":// roles possible
				Framework.Data.curMatch.rolesPossibleListModel.clear();
				if (data.getStringArrayArray() != null) {
					for (String[] s : data.getStringArrayArray()) {
						if (s != null) {
							if (s.length == 4) {
								Framework.Data.curMatch.rolesPossibleListModel
										.addElement(new List_Role(Integer
												.parseInt(s[0]), s[1], s[2],
												s[3]));
							}
						}
					}
				}
				break;
			case "Role Setup List":// role setup
				Framework.Data.curMatch.roleSetupListModel.clear();
				if (data.getStringArrayArray() != null) {
					for (String[] s : data.getStringArrayArray()) {
						if (s != null) {
							if (s.length == 4) {
								Framework.Data.curMatch.roleSetupListModel
										.addElement(new List_Role(Integer
												.parseInt(s[0]), s[1], s[2],
												s[3]));
							}
						}
					}
				}
				break;
			case "Order of Operations List"://action cat order
				Framework.Data.curMatch.orderOfOpsListModel.clear();
				if (data.getStringArray() != null) {
					for (String s : data.getStringArray()) {
						if (s != null) {
							Framework.Data.curMatch.orderOfOpsListModel.addElement(s);
						}
					}
				}
				//Framework.Data.curMatch.orderOfOpsList.validate();
				break;
			case "Character ID Set":// set Character ID
				if (Utils.isInteger(data.getString())) {
					Framework.Data.characterId = Integer.parseInt(data
							.getString());
				}
				break;

			// -Popups/prompts-//

			case "Generic Popup":// generic popup
				Framework.Window.createIFrame("popup",
						new String[] { data.getString(1), "ok","" });
				break;
			case "Generic HTML Popup":// generic html-styled popup
				Framework.Window.createIFrame("popup",
						new String[] { data.getString(1), "ok", "html" });
				break;

			// -Object transfers-//

			case "Role Update":// roleView changes
				if(data.getObject() instanceof RoleData){
					RoleData role = (RoleData)data.getObject();
					Framework.Data.serializeObject("/cache/role/", ""+role.id, role);
					Framework.Data.roleView.updateData(role);
					Framework.Window.autoUpdateJAutoPanels();
				}
				break;
			case "Character Update":// character updates
				if (data.getObject() instanceof CharacterData) {
					CharacterData chara = (CharacterData) data.getObject();
					Framework.Data.characterData.put(chara.eid, chara);
				}
				break;
			default:
				System.out.println(" ||| ***Command Control " + data.getControl()+ ":" + data.getCommand() + " non-Existant***");
				break;
			}
		}
	}
}
