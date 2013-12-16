/* eMafiaClient - Telnet.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import com.inverseinnovations.eMafiaClient.classes.DataPacket;
import com.inverseinnovations.eMafiaClient.classes.Utils;
import com.inverseinnovations.eMafiaClient.classes.data.*;

//import eMafiaClient.classes.jobjects.*;

//import eMafiaFramework.Matchlisted;

public class Telnet {
	public final Framework Framework;
	private Socket soc = null;
	private DataInputStream in;
	private PrintWriter out;
	public boolean SERVERRUNNING = true;
	public String var1, var2;

	// (01)startoftext = 
	// (03)endoftext = 
	// (24)cancel = 
	// (25)endofmedium = 
	// (26)substitue = 
	// (27)esc = 
	// control will always be 3 chars in length 000 to 999
	// command is made of (26)['control']'details'(25)
	// extended command is made of
	// (26)['control']'detail1''detail2''detail2''detail2'(25)
	// full data command
	// (26)['control']sub1sub1sub2sub2sub3sub3sub4sub4(25)
	// (26) will state the beginning of the command
	// (25) will state the end of the command
	// (27) divides details
	// (03) divides sub details
	// example: [127]Now connecting makes popup box with text 'Now Connecting'
	// details can be NULL

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
				Framework.Window.createIFrame("popup", new String[] {
						"eMafia Server is OFFLINE", "exit" });
			}
			// in = new BufferedReader(new
			// InputStreamReader(soc.getInputStream()));
			in = new DataInputStream(soc.getInputStream());
			out = new PrintWriter(soc.getOutputStream());

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
					if (size > 0) {
						System.out.println("[" + control + "] "
								+ new String(data, "ISO-8859-1"));
					} else {
						System.out.println("[" + control + "]");
					}
				} else {
					System.out.println("****ERROR**NO COMMAND ENDER****");
				}
				return;
			}
		} catch (SocketException e) {
			System.out.println("Connection Socket Error\n");
			SERVERRUNNING = false;
			Framework.Window.createIFrame("popup", new String[] {
					"<html><b>You were Disconnected from eMafia</b></html>",
					"exit" });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void write(String value) {
		try {
			out.println(value);
			out.flush();
			System.out.println("--> " + value);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void doCommand(DataPacket data) {
		if (data != null) {
			switch (data.getCommand()) {
			// -Connection-// 0 - 9
			case "Connection":// connected/handshake
				if(Integer.parseInt(data.getString()) >= Framework.Settings.CLIENT_BUILD){
					Framework.update();
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
			case "Login Prompt":// login prompt..Asking for username..handled
								// differently later
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
			case "Lobby Player List Refresh":// refresh lobby player list
				Framework.Data.playerListModel.clear();
				if (data.getStringArrayArray() != null) {
					for (String[] s : data.getStringArrayArray()) {
						if (s != null) {
							Framework.Data.playerListModel
									.addElement(new List_Character(Framework,
											Integer.parseInt(s[0]), s[1], s[2],
											s[3]));
						}
					}
				}
				Framework.Window.autoUpdateJAutoPanels();// XXX a temp fix
				break;
			case "Lobby Player List Add":// add player to lobby list
				if (data.getStringArray() != null) {
					Framework.Data.playerListModel
							.addElement(new List_Character(Framework, Integer
									.parseInt(data.getStringArray()[0]), data
									.getStringArray()[1],
									data.getStringArray()[2], data
											.getStringArray()[3]));
				}
				break;
			case "Lobby Player List Remove":// remove player from lobby list
				if (data.getStringArray() != null) {
					int eid = Integer.parseInt(data.getStringArray()[0]);
					String name = data.getStringArray()[1];
					for (int loop = 0; loop < Framework.Data.playerListModel
							.getSize(); loop++) {
						if (eid == ((List_Character) Framework.Data.playerListModel
								.get(loop)).id) {
							if (name.equals(((List_Character) Framework.Data.playerListModel
									.get(loop)).name)) {
								Framework.Data.playerListModel.remove(loop);
								break;
							}
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
				/*
				 * if(detail != null){ //playerList.setListData(detail);//parse
				 * detail later when needed(when data attached to names)
				 * Framework.Data.curMatch.aliveListModel.clear(); for (String s
				 * : detail) { if(s != null){
				 * //playerListModel.addElement(s);//parse detail later when
				 * needed(when data attached to names)
				 * if(s.contains(CMDVARSUBDIVIDER
				 * )){//fullDetail.contains(CMDVARDIVIDER) String[] sub =
				 * s.split(CMDVARSUBDIVIDER);
				 * Framework.Data.curMatch.aliveListModel.addElement(new
				 * List_AlivePlayer(Framework,
				 * Integer.parseInt(sub[0]),sub[1],sub[2])); }
				 *
				 * } } }
				 */

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
			case "Character ID Set":// set Character ID
				if (Utils.isInteger(data.getString())) {
					Framework.Data.characterId = Integer.parseInt(data
							.getString());
				}
				break;
			case "Character Data Update":// TODO updates serialized character
				if (data.getStringArray() != null) {
					Framework.Data
							.serializeCharacter(new CharacterSerialize(Integer
									.parseInt(data.getStringArray()[0]), data
									.getStringArray()[1],
									data.getStringArray()[2], data
											.getStringArray()[3]));
					Framework.Data.unserializeCharacter(Integer.parseInt(data
							.getStringArray()[0]));
				}
				break;
			// -Popups/prompts-//
			case "Role View Window":// roleView popup
				Framework.Window
						.createIFrame("roleView", data.getStringArray());
				break;
			case "Generic Window":// generic popup
				Framework.Window.createIFrame("popup",
						new String[] { data.getString(1), "ok" });
				break;

			default:
				System.out.print(" ||| ***Command Control " + data.getControl()
						+ ":" + data.getCommand() + " non-Existant***");
				break;
			}
		}
	}
}
