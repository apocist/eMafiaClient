/* eMafiaClient - RoleDataDisplay.java
Copyright (C) 2013  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.inverseinnovations.eMafiaClient.classes.Utils;
import com.inverseinnovations.sharedObjects.RoleData;

public class RoleDataDisplay extends JPanel{
	private static final long serialVersionUID = 1L;
	private boolean editable = false;
	private String[] affiliations = {"TOWN","MAFIA","NEUTRAL"};
	private String[] categories = {"","CORE","BENIGN","POWER","SUPPORT","INVESTIGATIVE","PROTECTIVE","KILLING","DECEPTION"};
	private String[] targetables = {"No One","Everyone","Everyone Except Self","Self Only"};
	private String[] actionCats = {"Jail","Vest","Witch","Busdrive","Roleblock","Frame","Douse","Heal","Kill","Clean","Invest","Disguise","Recruit"};
	private TabbedPanel scriptsTabbed = new TabbedPanel(this.editable);;
	private JLabel roleIdData = new JLabel();
	private JTextField roleNameData = new JTextField(15);
	private JComboBox<String> roleAffData = new JComboBox<String>();
	private JComboBox<String> roleCat1Data = new JComboBox<String>();
	private JComboBox<String> roleCat2Data = new JComboBox<String>();
	private JComboBox<String> targetN1 = new JComboBox<String>();
	private JComboBox<String> targetN2 = new JComboBox<String>();
	private JComboBox<String> targetD1 = new JComboBox<String>();
	private JComboBox<String> targetD2 = new JComboBox<String>();
	private JComboBox<String> actionCat = new JComboBox<String>();
	private JCheckBox roleOnTeamData = new JCheckBox();
	private JTextField roleTeamNameData = new JTextField(15);
	private JCheckBox roleNightChatData = new JCheckBox();
	private JCheckBox roleVisibleTeamData = new JCheckBox();
	private JCheckBox roleTeamWinData = new JCheckBox();
	private JPanel roleTeamRightPanel = new JPanel();//to show turning inviso

	public RoleDataDisplay(){
		this(false);
	}

	public RoleDataDisplay(boolean editable){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.editable = editable;
		updateEditablilty();

		JPanel roleIdPanel = new JPanel();
		roleIdPanel.setLayout(new BoxLayout(roleIdPanel, BoxLayout.X_AXIS));
		roleIdPanel.add(new JLabel("Role ID: "));roleIdPanel.add(roleIdData);
		add(roleIdPanel);

		JPanel roleNamePanel = new JPanel();
		roleNamePanel.setLayout(new BoxLayout(roleNamePanel, BoxLayout.X_AXIS));
		roleNamePanel.add(new JLabel("Name: "));roleNamePanel.add(roleNameData);
		add(roleNamePanel);

		for(String aff : affiliations){
			roleAffData.addItem(aff);
		}
		roleAffData.setEditable(true);
		JPanel roleAffPanel = new JPanel();
		roleAffPanel.setLayout(new BoxLayout(roleAffPanel, BoxLayout.X_AXIS));
		roleAffPanel.add(new JLabel("Affliation: "));roleAffPanel.add(roleAffData);
		add(roleAffPanel);

		for(String cats : categories){
			roleCat1Data.addItem(cats);
			roleCat2Data.addItem(cats);
		}
		//roleCat1Data.setEditable(true);roleCat2Data.setEditable(true);
		add(new JLabel("Categories:"));
		JPanel roleCatPanel = new JPanel();
		roleCatPanel.setLayout(new BoxLayout(roleCatPanel, BoxLayout.X_AXIS));
		roleCatPanel.add(roleCat1Data);roleCatPanel.add(roleCat2Data);
		add(roleCatPanel);

		for(String toAdd : targetables){
			targetN1.addItem(toAdd);
			targetN2.addItem(toAdd);
			targetD1.addItem(toAdd);
			targetD2.addItem(toAdd);
		}
		JPanel roleNightDayTarPanel = new JPanel();
		roleNightDayTarPanel.setLayout(new BoxLayout(roleNightDayTarPanel, BoxLayout.X_AXIS));
		roleNightDayTarPanel.add(new JLabel("Night Targetables   "));
		roleNightDayTarPanel.add(new JLabel("   Day Targetables"));
		JPanel roleTarget1Panel = new JPanel();
		roleTarget1Panel.setLayout(new BoxLayout(roleTarget1Panel, BoxLayout.X_AXIS));
		roleTarget1Panel.add(new JLabel(" 1 "));
		roleTarget1Panel.add(targetN1);
		roleTarget1Panel.add(new JLabel(" 1 "));
		roleTarget1Panel.add(targetD1);
		JPanel roleTarget2Panel = new JPanel();
		roleTarget2Panel.setLayout(new BoxLayout(roleTarget2Panel, BoxLayout.X_AXIS));
		roleTarget2Panel.add(new JLabel(" 2 "));
		roleTarget2Panel.add(targetN2);
		roleTarget2Panel.add(new JLabel(" 2 "));
		roleTarget2Panel.add(targetD2);
		add(roleNightDayTarPanel);
		add(roleTarget1Panel);
		add(roleTarget2Panel);

		for(String actions : actionCats){
			actionCat.addItem(actions);
		}
		actionCat.setEditable(true);
		JPanel actionCatPanel = new JPanel();
		actionCatPanel.setLayout(new BoxLayout(actionCatPanel, BoxLayout.X_AXIS));
		actionCatPanel.add(new JLabel("Action Category: "));actionCatPanel.add(actionCat);
		add(actionCatPanel);

		JPanel roleTeamLeftPanel = new JPanel();
		roleTeamLeftPanel.setLayout(new BoxLayout(roleTeamLeftPanel, BoxLayout.X_AXIS));
		roleOnTeamData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				roleTeamRightPanel.setVisible(roleOnTeamData.isSelected());
			}

		});
		roleTeamLeftPanel.add(new JLabel("Team: "));roleTeamLeftPanel.add(roleOnTeamData);roleOnTeamData.setSelected(false);


		JPanel roleTeamNamePanel = new JPanel();
		roleTeamNamePanel.setLayout(new BoxLayout(roleTeamNamePanel, BoxLayout.X_AXIS));
		roleTeamNamePanel.add(new JLabel("Team Name: "));roleTeamNamePanel.add(roleTeamNameData);
		JPanel roleNightChatPanel = new JPanel();
		roleNightChatPanel.setLayout(new BoxLayout(roleNightChatPanel, BoxLayout.X_AXIS));
		roleNightChatPanel.add(new JLabel("Night Chat: "));roleNightChatPanel.add(roleNightChatData);roleNightChatData.setSelected(false);
		JPanel roleVisibleTeamPanel = new JPanel();
		roleVisibleTeamPanel.setLayout(new BoxLayout(roleVisibleTeamPanel, BoxLayout.X_AXIS));
		roleVisibleTeamPanel.add(new JLabel("Visible Teammates: "));roleVisibleTeamPanel.add(roleVisibleTeamData);roleVisibleTeamData.setSelected(false);
		JPanel roleTeamWinPanel = new JPanel();
		roleTeamWinPanel.setLayout(new BoxLayout(roleTeamWinPanel, BoxLayout.X_AXIS));
		roleTeamWinPanel.add(new JLabel("Win with Team: "));roleTeamWinPanel.add(roleTeamWinData);roleTeamWinData.setSelected(true);
		JPanel roleTeamRightLowerPanel = new JPanel();
		roleTeamRightLowerPanel.setLayout(new BoxLayout(roleTeamRightLowerPanel, BoxLayout.X_AXIS));
		roleTeamRightLowerPanel.add(roleNightChatPanel);roleTeamRightLowerPanel.add(roleVisibleTeamPanel);roleTeamRightLowerPanel.add(roleTeamWinPanel);
		roleTeamRightPanel.setLayout(new BoxLayout(roleTeamRightPanel, BoxLayout.Y_AXIS));
		roleTeamRightPanel.add(roleTeamNamePanel);roleTeamRightPanel.add(roleTeamRightLowerPanel);
		JPanel roleTeamAllPanels = new JPanel();
		roleTeamAllPanels.setLayout(new BoxLayout(roleTeamAllPanels, BoxLayout.X_AXIS));
		roleTeamAllPanels.add(roleTeamLeftPanel);roleTeamAllPanels.add(roleTeamRightPanel);
		add(roleTeamAllPanels);

		scriptsTabbed = new TabbedPanel(this.editable);
		//scriptsTabbed.setSize(new Dimension(300,200));
		add(scriptsTabbed);
	}

	/**
	 * Change current settings into a new RoleData
	 */
	public RoleData convertToRoleData(){
		RoleData data = new RoleData();
		if(Utils.isInteger(roleIdData.getText())){
			data.id = Integer.parseInt(roleIdData.getText());
		}
		data.name = roleNameData.getText();
		if(roleAffData.getSelectedItem() instanceof String){
			data.affiliation = (String) roleAffData.getSelectedItem();
		}
		data.category = new String[2];
		if(roleCat1Data.getSelectedItem() instanceof String){
			if(!((String) roleCat1Data.getSelectedItem()).isEmpty()){
				data.category[0] = (String) roleCat1Data.getSelectedItem();
			}
		}
		if(roleCat2Data.getSelectedItem() instanceof String){
			if(!((String) roleCat2Data.getSelectedItem()).isEmpty()){
				data.category[1] = (String) roleCat2Data.getSelectedItem();
			}
		}
		data.targetablesNight1 = targetN1.getSelectedIndex();
		data.targetablesNight2= targetN2.getSelectedIndex();
		data.targetablesDay1 = targetD1.getSelectedIndex();
		data.targetablesDay2 = targetD2.getSelectedIndex();
		if(actionCat.getSelectedItem() instanceof String){
			data.actionCat = (String) actionCat.getSelectedItem();
		}

		data.teamName = roleTeamNameData.getText();
		if(data.teamName.isEmpty()){data.teamName = null;}
		data.chatAtNight = roleNightChatData.isSelected();
		data.visibleTeam = roleVisibleTeamData.isSelected();
		data.teamWin = roleTeamWinData.isSelected();

		data.ersScript = scriptsTabbed.getErsScripts();
		return data;
	}

	public void updateData(RoleData data){
		if(data != null){
			roleIdData.setText(""+data.id);
			roleNameData.setText(data.name);
			roleAffData.setSelectedItem(data.affiliation);
			if(data.category != null){
				if(data.category.length >= 1){
					roleCat1Data.setSelectedItem(data.category[0]);
					if(data.category.length >= 2){
						roleCat2Data.setSelectedItem(data.category[1]);
					}
				}
			}
			targetN1.setSelectedIndex(0);
			targetN2.setSelectedIndex(0);
			targetD1.setSelectedIndex(0);
			targetD2.setSelectedIndex(0);
			if(targetN1.getItemCount() > data.targetablesNight1){
				targetN1.setSelectedIndex(data.targetablesNight1);
			}
			if(targetN2.getItemCount() > data.targetablesNight2){
				targetN2.setSelectedIndex(data.targetablesNight2);
			}
			if(targetD1.getItemCount() > data.targetablesDay1){
				targetD1.setSelectedIndex(data.targetablesDay1);
			}
			if(targetD2.getItemCount() > data.targetablesDay2){
				targetD2.setSelectedIndex(data.targetablesDay2);
			}
			actionCat.setSelectedItem(data.actionCat);

			roleOnTeamData.setSelected(data.onTeam);
			roleTeamRightPanel.setVisible(roleOnTeamData.isSelected());

			roleTeamNameData.setText(data.teamName);
			roleNightChatData.setSelected(data.chatAtNight);
			roleVisibleTeamData.setSelected(data.visibleTeam);
			roleTeamWinData.setSelected(data.teamWin);

			scriptsTabbed.removeAll();
			scriptsTabbed.addTabSet(data.ersScript);
		}
	}

	public void updateEditablilty(boolean editable){
		this.editable = editable;
		updateEditablilty();
	}

	public void updateEditablilty(){
		roleIdData.setEnabled(editable);
		roleNameData.setEditable(editable);
		roleAffData.setEnabled(editable);
		roleCat1Data.setEnabled(editable);
		roleCat2Data.setEnabled(editable);
		targetN1.setEnabled(editable);
		targetN2.setEnabled(editable);
		targetD1.setEnabled(editable);
		targetD2.setEnabled(editable);

		actionCat.setEnabled(editable);
		roleOnTeamData.setEnabled(editable);
		roleTeamNameData.setEditable(editable);
		roleNightChatData.setEnabled(editable);
		roleVisibleTeamData.setEnabled(editable);
		roleTeamWinData.setEnabled(editable);

		scriptsTabbed.updateEditablilty(editable);
	}


}
