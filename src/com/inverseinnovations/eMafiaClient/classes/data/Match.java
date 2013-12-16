/* eMafiaClient - Match.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXList;

import com.inverseinnovations.eMafiaClient.*;
import com.inverseinnovations.eMafiaClient.classes.Utils;


public class Match extends JPanel{
	private static final long serialVersionUID = 1L;
	public Framework Framework;
	//public DefaultListModel<List_Character> playerListModel = new DefaultListModel<List_Character>();
    //public JXList playerList = new JXList(playerListModel);
	public DefaultListModel<List_Role> rolesPossibleListModel = new DefaultListModel<List_Role>();
	public JXList rolesPossibleList = new JXList(rolesPossibleListModel);
	public DefaultListModel<List_Role> roleSetupListModel = new DefaultListModel<List_Role>();
	public JXList roleSetupList = new JXList(roleSetupListModel);
    public DefaultListModel<List_AlivePlayer> aliveListModel = new DefaultListModel<List_AlivePlayer>();
    public JXList aliveList = new JXList(aliveListModel);
    public DefaultListModel<List_DeadPlayer> deadListModel = new DefaultListModel<List_DeadPlayer>();
    public JXList deadList = new JXList(deadListModel);
    public DefaultListModel<String> orderOfOpsListModel = new DefaultListModel<String>();
	public JXList orderOfOpsList = new JXList(orderOfOpsListModel);

    public String id;
    public String match_name;
    public int host_id = 0;
    public String host_name;
    public String num_chars;
    public String max_chars;//the max num of chars for a game, 15 defualt
    public String start_game_at = "0";//0=day/1=day no lynch/2=night
    public String discussion = "1";//0=no discuss mode/1=discuss
    public String day_length = "30";//# in secs. 60-600 default 60
    public String night_length = "30";//30-120 default 30
    public String discuss_length = "30";//30-180 default 30
    public String trial_length = "30";//30-120 default 30
    public String trial_pause_day = "1";//0=no/1=yes
    public String trial_defense = "1";//0=no defense/1=plead
    public String choose_names = "1";//0=no/1=yes

    public int currentPlayerNum;
    public int[] targetsN1;
    public int[] targetsN2;
    public int[] targetsD1;
    public int[] targetsD2;
	//unimplemented settings
    public String day_type = "1";//0=majority/1=trial/2=ballot/3=ballot+trial
    public String pm_allowed = "1";//0=false/1=true


    public String last_will = "1";//0=no/1=show last will
    public String description = "1";//0=night seq/1=death desc/2=classic night

    public JLabel match_nameLab = new JLabel();
    public JLabel max_charsLab = new JLabel();
    public JLabel day_lengthLab = new JLabel();
    public JLabel day_typeLab = new JLabel();
    public JLabel night_lengthLab = new JLabel();
    public JLabel last_willLab = new JLabel();
    public JLabel discuss_lengthLab = new JLabel();
    public JLabel start_game_atLab = new JLabel();
    public JLabel descriptionLab = new JLabel();
    public JLabel pm_allowedLab = new JLabel();
    public JLabel discussionLab = new JLabel();
    public JLabel trial_pause_dayLab = new JLabel();
    public JLabel trial_defenseLab = new JLabel();
    public JLabel choose_namesLab = new JLabel();
    public JLabel trial_lengthLab = new JLabel();

    public JButton leaveBut = new JButton("Leave");
    public JButton rolesBut = new JButton("Setup");
    public JButton gameStartBut = new JButton("Game Start");

    public Match(Framework framework){///XXX:should set the size of this some time
    	//this.setBounds(0,0 , desktop.getWidth(),(int)(Math.round(desktop.getHeight()*0.6)));
    	this.Framework = framework;
    	match_nameLab.setFont(new Font("Book Antiqua", Font.BOLD, 24));

        leaveBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				Framework.Telnet.write("-leave");
        	}
        });
        gameStartBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				Framework.Telnet.write("-gamestart");
        	}
        });
        rolesBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				System.out.println("char = "+Framework.Data.characterId+" host = "+host_id);
				if(host_id == Framework.Data.characterId){
					Framework.Window.createIFrame("roleMenu");
				}
				else{
					Framework.Window.createIFrame("roleSetupNonHost");//
				}
        	}
        });

        aliveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		aliveList.setLayoutOrientation(JList.VERTICAL);
		aliveList.setBackground(new Color(32,32,32));
		aliveList.setCellRenderer(Framework.Window.CellRenderer.new PlayerList());
		MouseListener[] mouseListen = aliveList.getMouseListeners();//remove mouse listeners
		for(MouseListener current : mouseListen){
			aliveList.removeMouseListener(current);
		}
		aliveList.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent event){
				Framework.Window.clickButtonAtJXList(aliveList,event.getPoint());
			}
		});

        deadList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//TODO no need to select graveyard
		deadList.setLayoutOrientation(JList.VERTICAL);
		deadList.setBackground(new Color(32,32,32));
		deadList.setCellRenderer(Framework.Window.CellRenderer.new MatchDeadList());

		rolesPossibleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rolesPossibleList.setLayoutOrientation(JList.VERTICAL);
		rolesPossibleList.setBackground(new Color(32,32,32));
		rolesPossibleList.setCellRenderer(Framework.Window.CellRenderer.new RoleList());

		roleSetupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roleSetupList.setLayoutOrientation(JList.VERTICAL);
		roleSetupList.setBackground(new Color(32,32,32));
		roleSetupList.setCellRenderer(Framework.Window.CellRenderer.new RoleSetupList());

		orderOfOpsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		orderOfOpsList.setLayoutOrientation(JList.VERTICAL);
		orderOfOpsList.setBackground(new Color(32,32,32));

    	this.updateMatchSetupSetting();
    }

    /*public void telnetWrite(String string){
    	System.out.println("sending: '"+string+"'");
    	if(framework == null){System.out.println("framework null :O");}
    	else if(framework.Telnet == null){System.out.println("telnet null :O");}
    	this.framework.Telnet.write(string);
    }*/

    public void changeMatchSetupSetting(String setting, String value){
    	switch(setting){
    		case "id": 				this.id = value;		 		break;
    		case "match_name": 		this.match_name = value; 		break;
    		case "host_id": 		if(Utils.isInteger(value)){this.host_id = Integer.parseInt(value);}break;
    		case "host_name": 		this.host_name = value; 		break;
    		case "num_chars": 		this.num_chars = value; 		break;
    		case "max_chars": 		this.max_chars = value; 		break;
    		case "start_game_at": 	this.start_game_at = value; 	break;
    		case "discussion": 		this.discussion = value; 		break;
    		case "day_length": 		this.day_length = value; 		break;
    		case "night_length":	this.night_length = value; 		break;
    		case "discuss_length": 	this.discuss_length = value;	break;
    		case "trial_length": 	this.trial_length = value; 		break;
    		case "day_type": 		this.day_type = value; 			break;
    		case "pm_allowed": 		this.pm_allowed = value; 		break;
    		case "trial_pause_day": this.trial_pause_day = value; 	break;
    		case "trial_defense": 	this.trial_defense = value; 	break;
    		case "choose_names": 	this.choose_names = value; 		break;
    		case "last_will": 		this.last_will = value; 		break;
    		case "description": 	this.description = value; 		break;
    	}
    }

    public void updateMatchSetupSetting(){//this will change depending on the layout of the match settigns
    	/*for(Component comp : Utils.getAllComponents(this)){
			if(comp.getClass().getSimpleName().equals("JLabel")){
				switch((String) comp.getName()){
					case "match_name":((JLabel) comp).setText(this.match_name);break;
		  			case "max_chars":((JLabel) comp).setText(this.num_chars+"/"+this.max_chars+" Players");break;
		  			case "day_length":((JLabel) comp).setText("Day Length: "+this.day_length+" secs");break;
		  			case "day_type":((JLabel) comp).setText("Voting system: "+(this.day_type.equals("0") ? "Majority" : this.day_type.equals("1") ? "Trial" : this.day_type.equals("2") ? "Ballot" : "Ballot + Trial"));break;
		  			case "night_length":((JLabel) comp).setText("Night Length: "+this.night_length+" secs");break;
		  			case "last_will":((JLabel) comp).setText("Last Will Allowed: "+(this.last_will.equals("1") ? "Yes" : "No"));break;
		  			case "discuss_length":((JLabel) comp).setText("Discussion Time: "+this.discuss_length+" secs");break;
		  			case "start_game_at":((JLabel) comp).setText("Game Start At: "+(this.start_game_at.equals("0") ? "Day" : this.start_game_at.equals("1") ? "Day + No Lynch" : "Night"));break;
		  			case "description":((JLabel) comp).setText("Death Descriptions: "+(this.description.equals("0") ? "Night Sequence" : this.description.equals("1") ? "Death Description" : "Classic Night"));break;
		  			case "pm_allowed":((JLabel) comp).setText("PMs Allowed: "+(this.pm_allowed.equals("1") ? "Yes" : "No"));break;
		  			case "discussion":((JLabel) comp).setText("Discussion: "+(this.discussion.equals("1") ? "Yes" : "No"));break;
		  			case "trial_pause_day":((JLabel) comp).setText("Trial Pauses Day: "+(this.trial_pause_day.equals("1") ? "Yes" : "No"));break;
		  			case "trial_defense":((JLabel) comp).setText("Trial Defense: "+(this.trial_defense.equals("1") ? "Yes" : "No"));break;
		  			case "choose_names":((JLabel) comp).setText("Choose Names: "+(this.choose_names.equals("1") ? "Yes" : "No"));break;
		 			case "trial_length":((JLabel) comp).setText("Trial Length: "+this.trial_length+" secs");break;
		 		}
			}
    	}*/
    	match_nameLab.setText(this.match_name);
    	max_charsLab.setText(this.num_chars+"/"+this.max_chars+" Players");
    	day_lengthLab.setText("Day Length: "+this.day_length+" secs");
    	day_typeLab.setText("Voting system: "+(this.day_type.equals("0") ? "Majority" : this.day_type.equals("1") ? "Trial" : this.day_type.equals("2") ? "Ballot" : "Ballot + Trial"));
    	night_lengthLab.setText("Night Length: "+this.night_length+" secs");
    	last_willLab.setText("Last Will Allowed: "+(this.last_will.equals("1") ? "Yes" : "No"));
    	discuss_lengthLab.setText("Discussion Time: "+this.discuss_length+" secs");
    	start_game_atLab.setText("Game Start At: "+(this.start_game_at.equals("0") ? "Day" : this.start_game_at.equals("1") ? "Day + No Lynch" : "Night"));
    	descriptionLab.setText("Death Descriptions: "+(this.description.equals("0") ? "Night Sequence" : this.description.equals("1") ? "Death Description" : "Classic Night"));
    	pm_allowedLab.setText("PMs Allowed: "+(this.pm_allowed.equals("1") ? "Yes" : "No"));
    	discussionLab.setText("Discussion: "+(this.discussion.equals("1") ? "Yes" : "No"));
    	trial_pause_dayLab.setText("Trial Pauses Day: "+(this.trial_pause_day.equals("1") ? "Yes" : "No"));
    	trial_defenseLab.setText("Trial Defense: "+(this.trial_defense.equals("1") ? "Yes" : "No"));
    	choose_namesLab.setText("Choose Names: "+(this.choose_names.equals("1") ? "Yes" : "No"));
    	trial_lengthLab.setText("Trial Length: "+this.trial_length+" secs");
    }
}