/* eMafiaClient - RoleSearchPane.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.jobjects;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.inverseinnovations.eMafiaClient.*;
import com.inverseinnovations.eMafiaClient.classes.data.List_Role;


/**
 * Allows this component directly place on JLayeredPane to be auto size according to
 * current frame size.
 * Allows additional function of detecting preferred size correctly
 */
public class RoleSearchPane extends JPanel{
	private static final long serialVersionUID = 1L;
	private Framework Framework;
	private String aff = "ANY";
	private String cat = "ANY";
	private int page = 1;
	private JLabel pageLabelNum = new JLabel(""+page);
	private int maxNumOfRoles = 10;

	public RoleSearchPane(Framework framework){
		super();
		this.Framework = framework;

		/*Framework.Data.roleSearchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Framework.Data.roleSearchList.setLayoutOrientation(JList.VERTICAL);
		Framework.Data.roleSearchList.setBackground(new Color(32,32,32));
		Framework.Data.roleSearchList.setCellRenderer(Framework.Window.CellRenderer.new RoleList());*/
		JScrollPane roleSearchScrollpane = new JScrollPane(Framework.Data.roleSearchList);
		roleSearchScrollpane.setSize(115, 150);

		ActionListener affcatListListener = new ActionListener() {//add actionlistner to listen for change
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource().getClass().getSimpleName().equals("JComboBox")){
					@SuppressWarnings("unchecked")
					JComboBox<String> source = (JComboBox<String>) e.getSource();
					if(source.getName().equals("aff"))aff = (String) source.getSelectedItem();//get the selected item
					else if(source.getName().equals("cat"))cat = (String) source.getSelectedItem();//get the selected item
					page = 1;
					performSearch();
				}
			}
		};
		ActionListener pageButListener = new ActionListener() {//add actionlistner to listen for change
			@Override
			public void actionPerformed(ActionEvent e) {
				//@SuppressWarnings("unused")
				JButton source = (JButton) e.getSource();
				if(source.getName().equals("left")){
					if(page > 1){page--;performSearch();}
				}
				else if(source.getName().equals("right")){
					if(Framework.Data.roleSearchListModel.getSize() >= maxNumOfRoles){
						page++;performSearch();
					}
				}
			}
		};
		String[] affStrings = { "ANY", "TOWN", "MAFIA", "NEUTRAL"};
		JComboBox<String> affList = new JComboBox<String>(affStrings);affList.setName("aff");
		affList.setSelectedIndex(0);
		affList.addActionListener(affcatListListener);

		String[] catStrings = { "ANY", "CORE", "INVESTIGATIVE", "PROTECTIVE", "KILLING"};
		JComboBox<String> catList = new JComboBox<String>(catStrings);catList.setName("cat");
		catList.setSelectedIndex(0);
		catList.addActionListener(affcatListListener);
		JButton pageLeft = new JButton("<");pageLeft.setName("left");
		JButton pageRight = new JButton(">");pageRight.setName("right");
		pageLeft.addActionListener(pageButListener);
		pageRight.addActionListener(pageButListener);
		JAutoPanel roleSearchOps = new JAutoPanel(Framework.Window.desktop);
		roleSearchOps.setLayout(new BoxLayout(roleSearchOps, BoxLayout.X_AXIS));
		roleSearchOps.add(pageLeft);
		roleSearchOps.add(affList);
		roleSearchOps.add(catList);
		roleSearchOps.add(pageRight);
		JButton viewBut = new JButton("View");
		ActionListener viewButListener = new ActionListener() {//add actionlistner to listen for change
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getSelectedRole() != 0){
					//Framework.Telnet.write("-roleview "+getSelectedRole());
					//TODO check if cached Role is on client, if not -roleview getSelectedRole()
					//TODO if cached: -roleview getSelectedRole() roleVersion...server will update if client version out of date
					Framework.Window.createIFrame("roleView",new String[] {""+getSelectedRole()});
				}
			}
		};
		viewBut.addActionListener(viewButListener);
		JPanel pagePanel = new JPanel();
		pagePanel.setLayout(new BoxLayout(pagePanel, BoxLayout.X_AXIS));
		pagePanel.add(viewBut);
		pagePanel.add(new JLabel("Page "));
		pagePanel.add(pageLabelNum);

		//JAutoPanel roleSearchPanel = new JAutoPanel(Framework.Window.desktop);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(roleSearchOps);
		this.add(roleSearchScrollpane);
		this.add(pagePanel);
	}

	/**Returns the current role's id. If none selected, returns 0*/
	public int getSelectedRole(){
		int id = 0;
		List_Role role = (List_Role) Framework.Data.roleSearchList.getSelectedValue();
		if(role != null){id = role.id;}
		return id;
	}

	public void performSearch(){
		Framework.Telnet.write("-rolesearch "+aff+" "+cat+" "+page);
		pageLabelNum.setText(""+page);
	}

	public Dimension getPreferredSize(){
	Dimension pSize = super.getPreferredSize();
	Dimension mSize = getMinimumSize();
	int wid, ht;

	wid = pSize.width < mSize.width  ? mSize.width : pSize.width;
	ht = pSize.height < mSize.height ? mSize.height: pSize.height;
	return new Dimension(wid, ht);
	}
}
