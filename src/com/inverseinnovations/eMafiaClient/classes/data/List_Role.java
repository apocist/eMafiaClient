/* eMafiaClient - List_Role.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;

import javax.swing.JPanel;

public class List_Role extends JPanel{
	private static final long serialVersionUID = 1L;
	public int id;
	public String name;
	public String aff;
	public String hexcolor;


	//info can get later
	public String cat1;

	//private JLabel roleName;

	public List_Role(final int id, String name, String aff, String hexcolor){
		this.id = id;
		this.name = name;
		this.aff = aff;
		this.hexcolor = hexcolor;

		//roleName = new JLabel("<html><font color=\""+hexcolor+"\">"+aff+" "+name+"</font></html>");//playerName.setBackground(new Color(32,32,32));
		//roleName.setPreferredSize(new Dimension(163, 30));
		//roleName.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

		//add(roleName);

		//roleName.setVisible(true);
	}

	public String toString() {
		return name;
	}
}
