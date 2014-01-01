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

import com.inverseinnovations.sharedObjects.RoleData;

public class RoleDataDisplay extends JPanel{
	private static final long serialVersionUID = 1L;
	private boolean editable = false;
	private String[] targetables = { "No One","Everyone","Everyone Except Self","Self Only" };
	private TabbedPanel scriptsTabbed;
	private JLabel roleIdData = new JLabel();
	private JTextField roleNameData = new JTextField(15);
	private JTextField roleAffData = new JTextField(15);
	private JComboBox<String> targetN1 = new JComboBox<String>();
	private JComboBox<String> targetN2 = new JComboBox<String>();
	private JComboBox<String> targetD1 = new JComboBox<String>();
	private JComboBox<String> targetD2 = new JComboBox<String>();
	private JCheckBox roleOnTeamData = new JCheckBox();
	private JTextField roleTeamNameData = new JTextField(15);
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

		JPanel roleAffPanel = new JPanel();
		roleAffPanel.setLayout(new BoxLayout(roleAffPanel, BoxLayout.X_AXIS));
		roleAffPanel.add(new JLabel("Affliation: "));roleAffPanel.add(roleAffData);
		add(roleAffPanel);

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
		JPanel roleTeamWinPanel = new JPanel();
		roleTeamWinPanel.setLayout(new BoxLayout(roleTeamWinPanel, BoxLayout.X_AXIS));
		roleTeamWinPanel.add(new JLabel("Win with Team: "));roleTeamWinPanel.add(roleTeamWinData);roleTeamWinData.setSelected(true);
		roleTeamRightPanel.setLayout(new BoxLayout(roleTeamRightPanel, BoxLayout.Y_AXIS));
		roleTeamRightPanel.add(roleTeamNamePanel);roleTeamRightPanel.add(roleTeamWinPanel);
		JPanel roleTeamAllPanels = new JPanel();
		roleTeamAllPanels.setLayout(new BoxLayout(roleTeamAllPanels, BoxLayout.X_AXIS));
		roleTeamAllPanels.add(roleTeamLeftPanel);roleTeamAllPanels.add(roleTeamRightPanel);
		add(roleTeamAllPanels);

		scriptsTabbed = new TabbedPanel(this.editable);
		//scriptsTabbed.setSize(new Dimension(300,200));
		add(scriptsTabbed);
	}

	public void updateData(RoleData data){
		if(data != null){
			roleIdData.setText(""+data.id);
			roleNameData.setText(data.name);
			roleAffData.setText(data.affiliation);
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
			roleOnTeamData.setSelected(data.onTeam);
			roleTeamRightPanel.setVisible(roleOnTeamData.isSelected());

			roleTeamNameData.setText(data.teamName);
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
		roleAffData.setEditable(editable);
		targetN1.setEnabled(editable);
		targetN2.setEnabled(editable);
		targetD1.setEnabled(editable);
		targetD2.setEnabled(editable);
		roleOnTeamData.setEnabled(editable);
		roleTeamNameData.setEditable(editable);
		roleTeamWinData.setEnabled(editable);

		//scriptsTabbed.setEditable(editable);
	}


}
