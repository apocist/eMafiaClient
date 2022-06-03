/* eMafiaClient - CountdownTimer.java
Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes;


import javax.swing.*;
import java.lang.Thread;

/**
 * Attaches a timer to a label element
 * Timer starts to countdown when 'start' method is called and provided with seconds to countdown
 * @author Apocist
 *
 */
public class CountdownTimer {
	Thread timer;
	Runnable r;
	int secs = 0;
	boolean stopped = true;
	JLabel tf;
	//JFrame fr;
	//Font f=new Font("Algerian",0,18);
	//tf.setFont(f);

	/**
	 * Attaches a timer to a label element
	 * Timer starts to countdown when 'start' method is called and provided with seconds to countdown
	 * @param field
	 */
	public CountdownTimer(JLabel field) {
		r=new Runnable() {
			public void run() {
				timerTick();
			}
		};
		timer=new Thread(r,"Countdown Timer Thread");
		timer.setPriority(4);//below normal
		this.tf = field;
		timer.start();
	}

	/**
	 * Starts the timer with the inputted seconds
	 * @param secs
	 */
	public void start(final int secs) {
		/*try {
			stop();
			r=new Runnable() {
				public void run() {
					startTimer(secs);
				}
			};
			timer=new Thread(r,"Timer Thread");
			timer.start();
		}
		catch(Exception exc) {
			JOptionPane.showMessageDialog(new JFrame(),exc);
		}*/
		this.secs = secs;
		this.stopped = false;
		//System.out.println("start");
	}

	private void timerTick() {
		while(true){
			if(secs > 0){
				secs--;
				tf.setText(formatIntoMMSS(secs));
			}
			else if(!stopped){
				tf.setText("");
				stopped = true;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		/*try {
			for(int i=secs;i>=0;i--){
				//tf.setText(""+i);
				tf.setText(formatIntoMMSS(i));
				Thread.sleep(1000);
			}
		}
		catch(Exception exc) {
			JOptionPane.showMessageDialog(new JFrame(),exc);
		}*/
	}

	private static String formatIntoMMSS(int secsIn){

		int minutes = secsIn / 60,
		seconds = secsIn % 60;

		return ( (minutes < 10 ? "0" : "") + minutes + ":" + (seconds< 10 ? "0" : "") + seconds );

	}

	public static String formatIntoHHMMSS(int secsIn){

		int hours = secsIn / 3600,
		remainder = secsIn % 3600,
		minutes = remainder / 60,
		seconds = remainder % 60;

		return ( (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds< 10 ? "0" : "") + seconds );
	}
}
