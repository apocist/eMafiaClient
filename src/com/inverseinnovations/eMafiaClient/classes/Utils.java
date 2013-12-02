/* eMafiaClient - Utils.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Utils {
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

	/** @author Richard Bair*/
	public static List<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
		  compList.add(comp);
		  if (comp instanceof Container) {
			compList.addAll(getAllComponents((Container) comp));
		  }
		}
		return compList;
	}

	public static String MD5(String md5) {
		try {
				java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
				byte[] array = md.digest(md5.getBytes("UTF-8"));
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < array.length; ++i) {
				  sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			   }
				return sb.toString();
			}
			catch (java.security.NoSuchAlgorithmException e) {}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}

	public static Image getSC2MImageCache(String SC2M_WEB_PATH, String loc){
		Image image = null;
		String defaultAva = "images/styles/DarkCore/misc/unknown.gif";
		if(loc == null){loc = defaultAva;}
		else if(loc.equals("null")){loc = defaultAva;}
		else if(loc.equals("")){loc = defaultAva;}
		//loc = "cache/"+loc;
		//File file = new File(System.getProperty("user.dir") + File.separator +loc);
		File file = new File(System.getProperty("user.dir") + "/cache/" +loc);
		//check if file exists
		if(file.isFile() && file.canRead()){
			try {
				//load the file
				//System.out.println("Found file...loading");
				image = ImageIO.read(file);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			//if file doesnt exist
			try {
				//load from web
				URL url = new URL(SC2M_WEB_PATH+loc);
				image = ImageIO.read(url);
			}
			catch (Exception e) {
				System.out.println(SC2M_WEB_PATH+loc);
				e.printStackTrace();
				file = new File(System.getProperty("user.dir") + File.separator + "/cache/" + defaultAva);
				try {
					image = ImageIO.read(file);
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			try {
				//save to cache
				//System.out.println("Loaded from web..saving to disk");
				file.getParentFile().mkdirs();
				ImageIO.write( (RenderedImage) image, getImageFormat(loc), file);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	public static String getImageFormat(String imageName){
	    String temp = imageName.toLowerCase();
	    String theReturn = "jpg";
	    if(temp.endsWith(".jpg"))theReturn = "jpg";
	    else if(temp.endsWith(".jpeg"))theReturn = "jpg";
	    else if(temp.endsWith(".png"))theReturn = "png";
	    else if(temp.endsWith(".gif"))theReturn = "gif";
	    return theReturn;
	}
}
