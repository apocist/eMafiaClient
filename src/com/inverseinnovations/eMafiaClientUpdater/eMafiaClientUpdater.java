/* eMafiaClientUpdater - eMafiaClientUpdater.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClientUpdater;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class eMafiaClientUpdater extends JFrame{
	private static final long serialVersionUID = 1L;
	private String updateURL = "http://inverseinnovations.com/eMafia/";
	private String rootFolder = System.getProperty("user.dir");
	private String updateFolder = rootFolder+"/_update";
	private int updateVersion;
	private int currentVersion;
	private Properties filesToUpdate;
	private boolean error = false;

	private Thread mainWorker;
	private Thread worker;
	private JPanel progressPanel;
	public JProgressBar progressBar;
	private JTextArea output;
	private JButton doneBut;
	private JScrollPane outputScroll;
	private JPanel mainPanel;
	private JPanel donePanel;

	public static void main(String[] args){
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new eMafiaClientUpdater().setVisible(true);
			}
		});
	}
	public eMafiaClientUpdater(){
		initWindow();
		mainWorker = new Thread(
			new Runnable(){
				public void run(){
					if(grabUpdateProp()){
						new File(updateFolder).mkdir();
						update(sortProperties());
						try{copyFiles(new File(updateFolder),rootFolder);}
						catch (Exception ex) {
							ex.printStackTrace();
							error = true;
							output.append("\n"+"An error occured while copying files!");
						}
						cleanup();
						if(!error){updateVersion(updateVersion);}

						if(error){
							output.append("\n"+"Errors have occured during the update process, please perform the update once more...");
							progressBar.setBackground(Color.RED);
						}
						else{
							//Change version here
							output.append("\n"+"Update complete!");
							doneBut.setEnabled(true);
							doneBut.setVisible(true);
						}
					}
				}
			});
		mainWorker.start();


	}
	private void initWindow(){
		progressPanel = new JPanel();
		progressPanel.setLayout(new FlowLayout());
		progressBar = new JProgressBar(0,100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressPanel.add(progressBar);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		donePanel = new JPanel();
		donePanel.setLayout(new FlowLayout());

		output = new JTextArea();
		outputScroll = new JScrollPane();
		outputScroll.setViewportView(output);

		doneBut = new JButton("Done");
		doneBut.setEnabled(false);
		doneBut.setVisible(false);
		doneBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String[] run = {"java","-jar","/eMafia.jar"};
		        try {
		            Runtime.getRuntime().exec(run);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		        System.exit(0);
			}
		});
		donePanel.add(doneBut);

		mainPanel.add(progressPanel,BorderLayout.NORTH);
		mainPanel.add(outputScroll,BorderLayout.CENTER);
		mainPanel.add(donePanel,BorderLayout.SOUTH);

		add(mainPanel);
		pack();
		this.setSize(500, 320);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		output.setText("Update Initiated");
	}
	private boolean grabUpdateProp(){
		boolean theReturn = false;
		try{

			if(this.getClass().getResourceAsStream("/settings.ini") != null){
				Properties prop = new Properties();
				prop.load(this.getClass().getResourceAsStream("/settings.ini"));
				currentVersion = Integer.parseInt(prop.getProperty("version"));
				prop = null;
			}
			else{
				output.append("\n"+"*Settings file not found.");
				currentVersion = 0;
			}

			try {
				URL url = new URL(updateURL+"update.ini");
				URLConnection conn = url.openConnection();
				InputStream is = conn.getInputStream();

				Reader reader = new InputStreamReader(is, "UTF-8"); // for example

				filesToUpdate = new Properties();
				try {
					filesToUpdate.load(reader);
				} finally {
					reader.close();
				}

				is.close();
				output.append("\n"+"Downloaded update list...");
				theReturn = true;
			}
			catch (Exception ex){
				ex.printStackTrace();
				output.append("\n"+"An error occured while downloading the update list... "+ex.toString()+" ...Cannot continue...");
				error = true;
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			output.append("\n"+"An error occured while downloading the update list... Cannot continue...");
			error = true;
		}
		return theReturn;
	}
	private void updateVersion(int currentVersion){
		Properties prop;
		try{
			prop = new Properties();
			if(this.getClass().getResourceAsStream("/settings.ini") != null){
				prop.load(this.getClass().getResourceAsStream("/settings.ini"));
			}
			else{
				output.append("\n"+"*Settings file not found.");
			}
			prop.put("version", ""+updateVersion);

			FileOutputStream fos = new FileOutputStream("settings.ini");
			prop.store(fos, "eMafia Settings");
			fos.flush();
			fos.close();

		}
		catch(Exception e){
			e.printStackTrace();
			output.append("\n"+"An error occured while updating the setting.ini file...");
			error = true;
		}
	}
	private List<String> sortProperties(){
		List<String> alist = new ArrayList<String>();
		updateVersion = Integer.parseInt(filesToUpdate.getProperty("version"));
		if(currentVersion < updateVersion){
			output.append("\n"+"Updating from build "+currentVersion+" to "+updateVersion);
			for(Object keys: filesToUpdate.keySet()){
				String key = (String)keys;
				if(isInteger(key)){//if the prop is a number
					if(currentVersion < Integer.parseInt(key)){//if the prop number is higher than the client's current version
						String value = filesToUpdate.getProperty(key);
						if(value.contains(",")){//if is an array
							for(String values : value.split(",")){
								alist.add(values);
							}
						}
						else{//if single item
							alist.add(value);
						}
					}
				}
			}
		}
		else{
			output.append("\n"+"Client is already up to date. Build "+updateVersion);
			progressBar.setValue(100);
		}
		return alist;
	}
	private void update(List<String> alist){
		for(final String file : alist){
			worker = new Thread(
			new Runnable(){
				public void run()
				{
					try {
						downloadFile(file);
						//outText.setText(outText.getText()+"\nUpdate Finished!");
					} catch (Exception ex) {
						ex.printStackTrace();
						//JOptionPane.showMessageDialog(null, "An error occured while downloading "+file+"!");
						output.append("\n"+"An error occured while downloading "+file+"!");
						error = true;
					}
				}
			});
			worker.start();
			while(worker.isAlive()){
				//do nothing
			}
		}
		output.append("\n"+"All files downloaded...");
	}
	private void downloadFile(String link) throws MalformedURLException, IOException{
		boolean error2 = false;
		progressBar.setValue(0);
		URL url = new URL(updateURL+link);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();

		long lenghtOfFile  = conn.getContentLength();
		output.append("\n"+"Downloding file "+link+"("+lenghtOfFile /1024+" KBs)...");
		try{
			int count;
			File file = new File(updateFolder+"/"+link);
			file.getParentFile().mkdirs();
			BufferedOutputStream fOut = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buffer = new byte[32 * 1024];
			long total = 0;
			//int in = 0;
			while ((count  = is.read(buffer)) != -1) {
				total += count;
				progressBar.setValue((int)((total*100)/lenghtOfFile));
				fOut.write(buffer, 0, count);
			}
			fOut.flush();
			fOut.close();
		}
		catch(Exception e){
			error2 = true;
			output.append("Error!");
			throw e;
		}
		is.close();
		if(!error2){output.append("Complete!");}
	}
	private void copyFiles(File f,String dir) throws IOException{
		File[]files = f.listFiles();
		for(File ff:files){
			if(ff.isDirectory()){
				new File(dir+"/"+ff.getName()).mkdir();
				copyFiles(ff,dir+"/"+ff.getName());
			}
			else{
				copy(ff.getAbsolutePath(),dir+"/"+ff.getName());
			}
		}
	}
	public void copy(String srFile, String dtFile) throws FileNotFoundException, IOException{

		  File f1 = new File(srFile);
		  File f2 = new File(dtFile);

		  InputStream in = new FileInputStream(f1);

		  OutputStream out = new FileOutputStream(f2);

		  byte[] buf = new byte[1024];
		  int len;
		  while ((len = in.read(buf)) > 0){
			out.write(buf, 0, len);
		  }
		  in.close();
		  out.close();
	  }
	private void cleanup(){
		output.append("\n"+"Performing clean up...");
		try {
			delete(new File(updateFolder));
		}
		catch (IOException e) {
			output.append("\n"+"Clean up caused an error, temp files may still exist...");
		}
	}
	public static void delete(File file) throws IOException{
	    	if(file.isDirectory()){
	    		if(file.list().length==0){
	    		   file.delete();
	    		}
	    		else{
	        	   String files[] = file.list();
	        	   for (String temp : files) {
	        	     File fileDelete = new File(file, temp);
	        	     delete(fileDelete);
	        	   }
	        	   if(file.list().length==0){
	           	     file.delete();
	        	   }
	    		}
	    	}
	    	else{
	    		file.delete();
	    	}
	    }

	public static boolean isInteger(String s){
		if(s != null && s != "")return isInteger(s,10);
		else return false;
	}
	private static boolean isInteger(String s, int radix){
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i),radix) < 0) return false;
		}
		return true;
	}



}
