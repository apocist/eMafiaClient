/* eMafiaClient - Framework.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient;

import com.inverseinnovations.eMafiaClient.classes.data.*;

public class Framework {
	public Window Window;
	public Telnet Telnet;
	public Data Data;

	// public final String BASE_PATH = "eMafiaClient/";
	public final String THEME_PATH = "theme/";
	public final String CACHE_PATH = "cache/";
	public final String SC2M_WEB_PATH = "http://sc2mafia.com/forum/";
	public String CURRENT_TEXTURE_PACK = "default";

	public Framework() {
		this.Window = new Window(this);
		this.Telnet = new Telnet(this);
		this.Data = new Data(this);
	}

	public void connect() {
		//Telnet.connect("127.0.0.1", "3689");
		Telnet.connect("www.inverseinnovations.com", "3689");
	}

	public static void main(String[] args) {
		Framework gameClient = new Framework();
		gameClient.connect();
	}

}
