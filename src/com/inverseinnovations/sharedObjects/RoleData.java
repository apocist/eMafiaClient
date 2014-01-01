/* eMafiaClient - RoleDava.java
Copyright (C) 2013  Matthew 'Apocist' Davis */
package com.inverseinnovations.sharedObjects;
import java.util.LinkedHashMap;
import java.util.Map;
public class RoleData implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public int id;
	public int version;
	public String name;
	public String affiliation;
	public String[] category;
	public String actionCat;
	public int targetablesNight1;
	public int targetablesNight2;
	public int targetablesDay1;
	public int targetablesDay2;
	public boolean onTeam = false;
	public String teamName;
	public boolean teamWin = false;
	public Map<String, String> ersScript = new LinkedHashMap<String, String>();
}