/* eMafiaClient - Framework.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;

import com.inverseinnovations.eMafiaClient.classes.data.*;


public class Framework {
	public Window Window;
	public Telnet Telnet;
	public Data Data;
	public Settings Settings;

	// public final String BASE_PATH = "eMafiaClient/";
	public final String THEME_PATH = "theme/";
	public final String CACHE_PATH = "cache/";
	public final String SC2M_WEB_PATH = "http://sc2mafia.com/forum/";
	public String CURRENT_TEXTURE_PACK = "default";

	public Framework() {
		checkUpdater();
		this.Window = new Window(this);
		this.Telnet = new Telnet(this);
		this.Data = new Data(this);
		this.Settings = new Settings();
	}

	public void connect() {
		Telnet.connect(Settings.SERVER_URL, Settings.SERVER_PORT);
	}

	public void checkUpdater(){
		Path source = Paths.get("_updater.jar");
		if(source.toFile().exists()){
			Path newDir = Paths.get("updater.jar");
			try {
				Files.move(source, newDir, REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(){
		Telnet.disconnect();
		String[] run = {"java","-jar","update.jar"};
		ProcessBuilder pb = new ProcessBuilder(run);
		pb.directory(new File(System.getProperty("user.dir")));

		try {
			//Runtime.getRuntime().exec(run);
			pb.start();
			System.out.println("updater.jar from "+pb.directory().getPath());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		Framework gameClient = new Framework();
		gameClient.connect();
	}

}
