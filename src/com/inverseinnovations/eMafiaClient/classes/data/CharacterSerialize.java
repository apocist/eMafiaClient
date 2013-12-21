/* eMafiaClient - CharacterSerialize.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;

public class CharacterSerialize implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	public int eid;//this is the database id, not the normal game state id
	public String name;
	public String hexcolor;
	public String nameDisplay;
	public String avatarUrl;


	public CharacterSerialize(final int eid, String name, String hexcolor, String avatarUrl){
		this.eid = eid;
		this.name = name;
		this.hexcolor = hexcolor;
		this.nameDisplay = hexcolor;
		this.avatarUrl = avatarUrl;
	}
}
