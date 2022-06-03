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
	public int targetablesNight1 = 0;
	public int targetablesNight2 = 0;
	public int targetablesDay1 = 0;
	public int targetablesDay2 = 0;
	public boolean onTeam = false;
	public String teamName;
	public boolean teamWin = false;
	public boolean visibleTeam = false;
	public boolean chatAtNight = false;
	public Map<String, String> ersScript = new LinkedHashMap<String, String>();
}