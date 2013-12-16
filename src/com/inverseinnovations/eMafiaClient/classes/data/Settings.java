/* eMafiaClient - RoleSearchPane.java
   Copyright (C) 2013  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes.data;

import java.util.Properties;

public class Settings {
	Properties p = new Properties();
	public int CLIENT_BUILD;

	public Settings(){
		try{
			if(this.getClass().getResourceAsStream("/settings.ini") != null){
				Properties prop = new Properties();
				prop.load(this.getClass().getResourceAsStream("/settings.ini"));
				CLIENT_BUILD = Integer.parseInt(prop.getProperty("version"));
			}
		}
		catch(Exception e){}
	}

}
