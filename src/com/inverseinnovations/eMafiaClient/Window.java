/* eMafiaClient - Window.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient;

//TODO need to MD5 the registration/verify password

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.*;
import java.net.URI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.inverseinnovations.eMafiaClient.classes.*;
import com.inverseinnovations.eMafiaClient.classes.data.List_Role;
import com.inverseinnovations.eMafiaClient.classes.jobjects.*;

public class Window extends Frame {
	private static final long serialVersionUID = 1L;
	public Framework Framework;
	public CellRenderer CellRenderer;

	public JDesktopPane desktop;
	private Integer popupLayer = 30;
	public JLabel textTimer = new JLabel();
	public CountdownTimer countdownTimer = new CountdownTimer(textTimer);

	public Window(Framework framework) {
		super("eMafia");// client title
		this.Framework = framework;
		this.CellRenderer = new CellRenderer(Framework);
		setSize(800, 600);// client window size
		setMinimumSize(new Dimension(800, 600));
		this.desktop = new JDesktopPane();
		desktop.setOpaque(true);// defualt=true
		add(desktop, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Framework.Telnet.disconnect();
				System.exit(0);
			}
		});

		setVisible(true);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				autoUpdateJAutoPanels();
			}
		});
	}

	public void autoUpdateJAutoPanels() {
		for (Component comp : desktop.getComponents()) {
			if (comp instanceof JAutoPanel) {
				((JAutoPanel) comp).autoUpdate();
			}
		}
	}

	public Integer layerName(String layerName) {
		Integer layer;
		switch (layerName) {
		case "lobby":
			layer = 2;
			break;
		case "matchSetup":
		case "matchInplay":
			layer = 4;
			break;
		case "login":
			layer = 18;
			break;
		case "register":
			layer = 20;
			break;
		case "verify":
			layer = 22;
			break;
		case "nameSelection":
			layer = 24;
			break;
		case "popup":
			layer = popupLayer;
			popupLayer += 2;
			;
			break;
		// default:layer = 0;break;
		default:
			layer = popupLayer;
			popupLayer += 2;
			;
			break;
		}
		return layer;
	}

	public void deleteIFrame(String layerName) {
		Integer layer = layerName(layerName);
		for (Component comp : desktop.getComponentsInLayer(layer)) {
			desktop.remove(comp);
			desktop.repaint();
		}
	}

	public void deleteIFrame(Integer layerNum) {
		for (Component comp : desktop.getComponentsInLayer(layerNum)) {
			desktop.remove(comp);
			desktop.repaint();
		}
	}

	public void createIFrame(String windowType) {
		createIFrame(windowType, null);
	}

	/**
	 * Creates a frame in the desktop: <br>
	 * <br>
	 * createIFrame("login");
	 *
	 * @param windowType
	 *            (login,popup,matchSetup,ect)
	 * @return
	 */
	public void createIFrame(String windowType, String[] parameters) {
		Integer layer = layerName(windowType);
		JAutoPanel p = contentIFrame(windowType, parameters, layer);// grabs the
																	// content
																	// to place
																	// in frame
		if (p != null) {
			desktop.add(p, layer);
			p.setVisible(true);
		}
	}

	public void clickButtonAtJXList(org.jdesktop.swingx.JXList list,
			Point pointer) {
		int index = list.locationToIndex(pointer);
		if (index >= 0) {
			JPanel o = (JPanel) list.getModel().getElementAt(index);
			pointer.y -= (o.getHeight() * index);
			if (o.contains(pointer)) {
				Object oO = ((Component) o).getComponentAt(pointer);
				if (oO.getClass().getSimpleName().equals("JButton")) {
					((JButton) oO).doClick();
				} else if (oO.getClass().getSimpleName().equals("JPanel")) {
					if (((JPanel) oO).getName().equals("buttonHolder")) {
						pointer.y -= ((JPanel) oO).getY();
						pointer.x -= ((JPanel) oO).getX();
						if (((JPanel) oO).contains(pointer)) {
							Object oOo = ((JPanel) oO).getComponentAt(pointer);
							if (oOo.getClass().getSimpleName()
									.equals("JButton")) {
								((JButton) oOo).doClick();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Inputs content for IFrame based on what type of window is being created
	 *
	 * @param frame
	 *            JInternalFrame
	 * @param type
	 *            Type of Window
	 */
	private JAutoPanel contentIFrame(String windowType, String[] parameters,
			Integer layer) {
		JAutoPanel frame = null;
		if (parameters == null) {
			parameters = new String[] { "" };
		}
		switch (windowType) {
		case "matchSetup":
			frame = window_matchSetup();
			break;
		case "matchInplay":
			frame = window_matchInplay();
			break;
		case "lobby":
			frame = window_lobby();
			break;
		case "login":
			frame = window_login();
			break;
		case "register":
			frame = window_register();
			break;
		case "verify":
			frame = window_verify();
			break;
		case "nameSelection":
			frame = window_nameSelection();
			break;
		case "roleMenu":
			frame = window_roleMenu(layer);
			break;
		// case "rolesPossibleAndSetup":frame =
		// window_rolesPossibleAndSetup(layer);break;
		case "rolesPossible":
			frame = window_rolesPossibleHost(layer);
			break;
		case "roleSetupHost":
			frame = window_roleSetupHost(layer);
			break;
		case "roleSetupNonHost":
			frame = window_roleSetupNonHost(layer);
			break;
		case "roleView":
			frame = window_roleView(parameters, layer);
			break;
		case "popup":
			frame = window_popup(parameters[0], parameters[1], layer);
			break;
		}
		return frame;
	}

	private JAutoPanel window_matchSetup() {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		// Match Lobby
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(textTimer);

		JPanel matchData = new JPanel();
		JPanel row1 = new JPanel();
		row1.setLayout(new BoxLayout(row1, BoxLayout.X_AXIS));
		row1.add(Framework.Data.curMatch.match_nameLab);
		row1.add(Box.createRigidArea(new Dimension(10, 0)));
		row1.add(Framework.Data.curMatch.max_charsLab);
		JPanel row2 = new JPanel();
		row2.setLayout(new BoxLayout(row2, BoxLayout.X_AXIS));
		row2.add(Framework.Data.curMatch.day_lengthLab);
		row2.add(Box.createRigidArea(new Dimension(10, 0)));
		row2.add(Framework.Data.curMatch.day_typeLab);
		JPanel row3 = new JPanel();
		row3.setLayout(new BoxLayout(row3, BoxLayout.X_AXIS));
		row3.add(Framework.Data.curMatch.night_lengthLab);
		row3.add(Box.createRigidArea(new Dimension(10, 0)));
		row3.add(Framework.Data.curMatch.discussionLab);
		row3.add(Box.createRigidArea(new Dimension(10, 0)));
		row3.add(Framework.Data.curMatch.discuss_lengthLab);
		JPanel row4 = new JPanel();
		row4.setLayout(new BoxLayout(row4, BoxLayout.X_AXIS));
		row4.add(Framework.Data.curMatch.start_game_atLab);
		row4.add(Box.createRigidArea(new Dimension(10, 0)));
		row4.add(Framework.Data.curMatch.descriptionLab);
		row4.add(Box.createRigidArea(new Dimension(10, 0)));
		row4.add(Framework.Data.curMatch.pm_allowedLab);
		row4.add(Box.createRigidArea(new Dimension(10, 0)));
		row4.add(Framework.Data.curMatch.last_willLab);
		JPanel row5 = new JPanel();
		row5.setLayout(new BoxLayout(row5, BoxLayout.X_AXIS));
		row5.add(Framework.Data.curMatch.trial_defenseLab);
		row5.add(Box.createRigidArea(new Dimension(10, 0)));
		row5.add(Framework.Data.curMatch.trial_lengthLab);
		row5.add(Box.createRigidArea(new Dimension(10, 0)));
		row5.add(Framework.Data.curMatch.trial_pause_dayLab);
		row5.add(Box.createRigidArea(new Dimension(10, 0)));
		row5.add(Framework.Data.curMatch.choose_namesLab);
		JPanel row6 = new JPanel();
		row6.setLayout(new BoxLayout(row6, BoxLayout.X_AXIS));
		row6.add(Framework.Data.curMatch.leaveBut);
		row6.add(Box.createRigidArea(new Dimension(10, 0)));
		row6.add(Framework.Data.curMatch.gameStartBut);
		row6.add(Box.createRigidArea(new Dimension(10, 0)));
		row6.add(Framework.Data.curMatch.rolesBut);
		JPanel Yrow = new JPanel();
		Yrow.setLayout(new BoxLayout(Yrow, BoxLayout.Y_AXIS));
		Yrow.add(row1);
		Yrow.add(row2);
		Yrow.add(row3);
		Yrow.add(row4);
		Yrow.add(row5);
		Yrow.add(row6);

		matchData.add(Yrow);
		JPanel matchPanel = new JPanel();
		matchPanel.setLayout(new BoxLayout(matchPanel, BoxLayout.Y_AXIS));

		matchPanel.add(matchData);
		matchPanel.add(buttons);

		// mainPanel - Chatpanel + playerScrollePane (side by side)

		JPanel mainPanel = new JPanel();
		// mainPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(new ChatPane(Framework));
		mainPanel.add(new CharacterListPane(Framework));

		// ////////
		// Layout//
		// ////////

		frame.setBackground(Color.gray);
		frame.setName("matchSetup");
		GroupLayout layout = new GroupLayout(frame);
		frame.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(matchPanel).addComponent(mainPanel)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(matchPanel).addComponent(mainPanel));
		frame.setAutoFullscreen();
		// Framework.Telnet.write("-getsetting");
		Framework.Telnet.write("-refresh");// Fix to show players on entering

		return frame;
	}

	private JAutoPanel window_matchInplay() {
		JAutoPanel frame = new JAutoPanel(this.desktop);
		// Match screen

		// Graveyard List

		/*
		 * Framework.Data.curMatch.deadList.setSelectionMode(ListSelectionModel.
		 * SINGLE_SELECTION);//TODO no need to select graveyard
		 * Framework.Data.curMatch
		 * .deadList.setLayoutOrientation(JList.VERTICAL);
		 * Framework.Data.curMatch.deadList.setBackground(new Color(32,32,32));
		 * Framework.Data.curMatch.deadList.setCellRenderer(CellRenderer.new
		 * MatchDeadList());
		 */
		JScrollPane graveyardScrollpane = new JScrollPane(
				Framework.Data.curMatch.deadList);
		JAutoPanel graveyardPanel = new JAutoPanel(desktop);
		graveyardPanel.add(new JLabel("Graveyard"));
		graveyardPanel.add(graveyardScrollpane);
		graveyardPanel.setMinimumSize(new Dimension(100, 100));
		graveyardPanel.setMaximumSize(new Dimension(100, 1000));

		// match Panel

		JPanel matchPanel = new JPanel();

		matchPanel.setLayout(new BoxLayout(matchPanel, BoxLayout.X_AXIS));
		matchPanel.add(graveyardPanel);
		matchPanel.add(textTimer);

		// Player List

		/*
		 * Framework.Data.curMatch.aliveList.setSelectionMode(ListSelectionModel.
		 * SINGLE_SELECTION);
		 * Framework.Data.curMatch.aliveList.setLayoutOrientation
		 * (JList.VERTICAL); Framework.Data.curMatch.aliveList.setBackground(new
		 * Color(32,32,32));
		 * Framework.Data.curMatch.aliveList.setCellRenderer(CellRenderer.new
		 * PlayerList()); MouseListener[] mouseListen =
		 * Framework.Data.curMatch.aliveList.getMouseListeners();//remove mouse
		 * listeners for(MouseListener current : mouseListen){
		 * Framework.Data.curMatch.aliveList.removeMouseListener(current); }
		 * Framework.Data.curMatch.aliveList.addMouseListener(new
		 * MouseAdapter(){
		 *
		 * @Override public void mouseClicked(MouseEvent event){
		 * clickButtonAtJXList
		 * (Framework.Data.curMatch.aliveList,event.getPoint()); } });
		 */
		JScrollPane playerScrollpane = new JScrollPane(
				Framework.Data.curMatch.aliveList);
		JAutoPanel playerPanel = new JAutoPanel(desktop);
		playerPanel.add(playerScrollpane);
		playerPanel.setMinimumSize(new Dimension(200, 100));
		playerPanel.setMaximumSize(new Dimension(200, 4000));

		// chatOutput Pane

		Framework.Data.chatOutput.setEditable(false);
		JScrollPane chatScrollpane = new JScrollPane(Framework.Data.chatOutput);

		// chatInput

		JTextPane chatInput = new JTextPane();
		chatInput.addKeyListener(new KeyListener() {// potentail memory leak?
					public void keyPressed(KeyEvent e) {
					}

					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode() == 10) {
							String output = ((JTextPane) e.getSource())
									.getText();
							output = output.substring(0, output.length() - 2)
									.trim();// removes unneeded things
							((JTextPane) e.getSource()).setText("");
							if (!output.equals("")) {
								Framework.Telnet.write(output);
							}
						}
					}

					public void keyTyped(KeyEvent e) {
					}
				});
		JScrollPane inputScrollpane = new JScrollPane(chatInput);
		inputScrollpane.setMaximumSize(new Dimension(3000, 30));

		// chatPanel - chatScrollPane on top of inputScrollPane

		JPanel chatPanel = new JPanel();
		// chatPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.add(chatScrollpane);
		chatPanel.add(inputScrollpane);

		// mainPanel - Chatpanel + playerScrollePane (side by side)

		JPanel mainPanel = new JPanel();
		// mainPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(chatPanel);
		mainPanel.add(playerPanel);

		// ////////
		// Layout//
		// ////////

		frame.setBackground(Color.gray);
		frame.setName("matchSetup");
		GroupLayout layout = new GroupLayout(frame);
		frame.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(matchPanel).addComponent(mainPanel)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(matchPanel).addComponent(mainPanel));
		frame.setAutoFullscreen();
		// Framework.Telnet.write("-getsetting");
		Framework.Telnet.write("-refresh");// Fix to show players on entering

		return frame;
	}

	private JAutoPanel window_lobby() {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		Framework.Telnet.write("-refreshplist");
		// Match List
		Framework.Data.matchList
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Framework.Data.matchList.setLayoutOrientation(JList.VERTICAL);
		Framework.Data.matchList.setCellRenderer(CellRenderer.new MatchList());
		Framework.Data.matchList.setBackground(new Color(32, 32, 32));
		MouseListener[] mouseListen = Framework.Data.matchList
				.getMouseListeners();// remove mouse listeners
		for (MouseListener current : mouseListen) {
			Framework.Data.matchList.removeMouseListener(current);
		}
		Framework.Data.matchList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				clickButtonAtJXList(Framework.Data.matchList, event.getPoint());
			}
		});

		JScrollPane matchScrollpane = new JScrollPane(Framework.Data.matchList);
		matchScrollpane.setPreferredSize(new Dimension(this.desktop.getWidth(),
				(int) (Math.round(this.desktop.getHeight() * 0.6)) - 40));

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		JButton createBut = new JButton("Create");

		createBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Framework.Telnet.write("-match create");
			}
		});
		JButton refreshBut = new JButton("Refresh");
		refreshBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Framework.Telnet.write("-match list");
			}
		});
		buttons.add(refreshBut);
		buttons.add(createBut);

		JPanel matchPanel = new JPanel();
		matchPanel.setLayout(new BoxLayout(matchPanel, BoxLayout.Y_AXIS));
		matchPanel.add(matchScrollpane);
		matchPanel.add(buttons);

		// mainPanel - Chatpanel + playerScrollePane (side by side)

		JPanel mainPanel = new JPanel();
		// mainPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(new ChatPane(Framework));
		mainPanel.add(new CharacterListPane(Framework));

		// ////////
		// Layout//
		// ////////

		frame.setBackground(Color.lightGray);
		frame.setName("lobbyChatWindow");
		GroupLayout layout = new GroupLayout(frame);
		frame.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(matchPanel).addComponent(mainPanel)));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(matchPanel).addComponent(mainPanel));

		frame.setAutoFullscreen();

		Framework.Telnet.write("-match list");

		// Framework.Telnet.write("-refresh");

		return frame;
	}

	private JAutoPanel window_login() {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		JLabel eMafiaText = new JLabel("eMafia");
		JLabel usernameText = new JLabel("Username:");
		JTextField usernameField = new JTextField(15);
		usernameField.setName("username");
		JLabel passwordText = new JLabel("Password:");
		JPasswordField passwordField = new JPasswordField(15);
		passwordField.setName("password");
		JButton loginBut = new JButton("Login");
		Action loginAction = new AbstractAction("Login") {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String username = null;
				Component[] theList = ((Component) e.getSource()).getParent()
						.getComponents();
				for (Component comp : theList) {
					if (comp instanceof JTextField
							&& comp.getName().equals("username")) {
						((JTextField) comp).setEditable(false);
						username = ((JTextField) comp).getText();
					}
					if (comp instanceof JPasswordField
							&& comp.getName().equals("password")) {
						((JPasswordField) comp).setEditable(false);
						Framework.Telnet.var1 = ((JPasswordField) comp)
								.getText();
					}
					if (comp instanceof JButton) {
						if (((JButton) comp).getText().equals("Login")) {// can't
																			// use
																			// getName
																			// without
																			// errors..
							((JButton) comp).setEnabled(false);
						}
					}
				}
				Framework.Telnet.write(username);
			}
		};

		passwordField.setAction(loginAction);
		loginBut.setAction(loginAction);
		JButton registerBut = new JButton("Register");
		registerBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Framework.Telnet.write("new");
			}
		});
		// registerBut.setEnabled(false);//Disabled until there is a
		// registartion
		JPanel loginFieldsPanel = new JPanel();
		GroupLayout loginFieldsLayout = new GroupLayout(loginFieldsPanel);
		loginFieldsPanel.setLayout(loginFieldsLayout);
		loginFieldsLayout.setAutoCreateGaps(true);
		loginFieldsLayout.setAutoCreateContainerGaps(true);

		loginFieldsLayout.setHorizontalGroup(loginFieldsLayout
				.createSequentialGroup()
				.addComponent(eMafiaText)
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(usernameText)
								.addComponent(passwordText)
								.addComponent(loginBut))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(usernameField)
								.addComponent(passwordField)
								.addComponent(registerBut)));
		loginFieldsLayout.setVerticalGroup(loginFieldsLayout
				.createSequentialGroup()
				.addComponent(eMafiaText)
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(usernameText)
								.addComponent(usernameField))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(passwordText)
								.addComponent(passwordField))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(loginBut)
								.addComponent(registerBut)));
		frame.add(loginFieldsPanel);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();

		return frame;
	}

	private JAutoPanel window_register() {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		JLabel eMafiaText = new JLabel("Registration");
		JLabel usernameText = new JLabel("Forum Username:");
		JTextField usernameField = new JTextField(15);
		usernameField.setName("username");
		JLabel passwordText = new JLabel("eMafia Password:");
		JPasswordField passwordField = new JPasswordField(15);
		passwordField.setName("password");
		JButton regBut = new JButton("Create Account");
		Action regAction = new AbstractAction("Create Account") {// XXX:verify
																	// password
																	// is a
																	// certain
																	// length
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String username = null;
				Framework.Telnet.var1 = null;
				String error = null;
				Component[] theList = ((Component) e.getSource()).getParent()
						.getParent().getComponents();
				for (Component comp : theList) {
					if (comp instanceof JTextField
							&& comp.getName().equals("username")) {
						((JTextField) comp).setEditable(false);
						username = ((JTextField) comp).getText();
						if (username == null || username.length() == 0)
							error = "2";
					}
					if (comp instanceof JPasswordField
							&& comp.getName().equals("password")) {
						((JPasswordField) comp).setEditable(false);
						Framework.Telnet.var1 = ((JPasswordField) comp)
								.getText();
						if (Framework.Telnet.var1 == null
								|| Framework.Telnet.var1.length() < 5)
							error = "1";
					}
					if (comp instanceof JButton) {
						if (((JButton) comp).getText().equals("Create Account")) {
							((JButton) comp).setEnabled(false);
						}
					}
				}
				if (error == null)
					Framework.Telnet.write(username);
				else {
					deleteIFrame("register");
					createIFrame("register");
					if (error.equals("1")) {
						createIFrame(
								"popup",
								new String[] {
										"Password must consist of atleast 5 characters.",
										"ok" });
					} else if (error.equals("2")) {
						createIFrame(
								"popup",
								new String[] {
										"<center>Username is blank....WTF?!<br>Come on now.... Everyone has a name.....Unless you weren't born..<br><br><br>You were born...right......?</center>",
										"ok" });
					}
				}

			}
		};
		// passwordField.setAction(regAction);
		regBut.setAction(regAction);
		JButton cancelBut = new JButton("Cancel");
		cancelBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Framework.Telnet.write("<-");
			}
		});
		JButton verifyBut = new JButton("Verify");
		verifyBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Framework.Telnet.write("ver");
			}
		});
		JPanel cancelVerifyPanel = new JPanel();
		cancelVerifyPanel.add(cancelBut);
		cancelVerifyPanel.add(verifyBut);
		JPanel registerPanel = new JPanel();
		registerPanel.add(regBut);
		// cancelVerifyPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

		JPanel loginFieldsPanel = new JPanel();
		GroupLayout loginFieldsLayout = new GroupLayout(loginFieldsPanel);
		loginFieldsPanel.setLayout(loginFieldsLayout);
		loginFieldsLayout.setAutoCreateGaps(true);
		loginFieldsLayout.setAutoCreateContainerGaps(true);

		loginFieldsLayout.setHorizontalGroup(loginFieldsLayout
				.createSequentialGroup()
				.addComponent(eMafiaText)
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(usernameText)
								.addComponent(passwordText)
								.addComponent(registerPanel))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(usernameField)
								.addComponent(passwordField)
								.addComponent(cancelVerifyPanel)));
		loginFieldsLayout.setVerticalGroup(loginFieldsLayout
				.createSequentialGroup()
				.addComponent(eMafiaText)
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(usernameText)
								.addComponent(usernameField))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(passwordText)
								.addComponent(passwordField))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(registerPanel)
								.addComponent(cancelVerifyPanel)));
		frame.add(loginFieldsPanel);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();

		return frame;
	}

	private JAutoPanel window_verify() {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		JLabel eMafiaText = new JLabel("Verification");
		JLabel usernameText = new JLabel("eMafia Username:");
		JTextField usernameField = new JTextField(15);
		usernameField.setName("username");
		JLabel passwordText = new JLabel("eMafia Password:");
		JPasswordField passwordField = new JPasswordField(15);
		passwordField.setName("password");
		JLabel verifyText = new JLabel("Verification:");
		JTextField verifyField = new JTextField(15);
		verifyField.setName("verify");
		JButton verBut = new JButton("Verify Account");
		Action verAction = new AbstractAction("Verify Account") {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String username = null;
				Framework.Telnet.var1 = null;
				Framework.Telnet.var2 = null;
				String error = null;
				Component[] theList = ((Component) e.getSource()).getParent()
						.getComponents();
				for (Component comp : theList) {
					if (comp instanceof JTextField
							&& comp.getName().equals("username")) {
						((JTextField) comp).setEditable(false);
						username = ((JTextField) comp).getText();
						if (username == null || username.length() == 0)
							error = "2";
					}
					if (comp instanceof JPasswordField
							&& comp.getName().equals("password")) {
						((JPasswordField) comp).setEditable(false);
						Framework.Telnet.var1 = ((JPasswordField) comp)
								.getText();
						if (Framework.Telnet.var1 == null
								|| Framework.Telnet.var1.length() < 5)
							error = "1";
					}
					if (comp instanceof JTextField
							&& comp.getName().equals("verify")) {
						((JTextField) comp).setEditable(false);
						Framework.Telnet.var2 = ((JTextField) comp).getText();
						if (Framework.Telnet.var2 == null
								|| Framework.Telnet.var2.length() == 0)
							error = "3";
					}
					if (comp instanceof JButton) {
						if (((JButton) comp).getText().equals("Verify Account")) {
							((JButton) comp).setEnabled(false);
						}
					}
				}
				if (error == null)
					Framework.Telnet.write(username);
				else {
					deleteIFrame("verify");
					createIFrame("verify");
					if (error.equals("1")) {
						createIFrame(
								"popup",
								new String[] {
										"Password must consist of atleast 5 characters.",
										"ok" });
					} else if (error.equals("2")) {
						createIFrame(
								"popup",
								new String[] {
										"<center>Username is blank....WTF?!<br>Come on now.... Everyone has a name.....Unless you weren't born..<br><br><br>You were born...right......?</center>",
										"ok" });
					} else if (error.equals("3")) {
						createIFrame(
								"popup",
								new String[] {
										"<center>Missing Verification code!<br>Please check your <a href=\"http://www.sc2mafia.com/forum/private.php\">SC2Mafia Inbox</a>.</center>",
										"ok" });
					}
				}

			}
		};
		// passwordField.setAction(regAction);
		verBut.setAction(verAction);
		JButton cancelBut = new JButton("Cancel");
		cancelBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Framework.Telnet.write("<-");
			}
		});

		JPanel loginFieldsPanel = new JPanel();
		GroupLayout loginFieldsLayout = new GroupLayout(loginFieldsPanel);
		loginFieldsPanel.setLayout(loginFieldsLayout);
		loginFieldsLayout.setAutoCreateGaps(true);
		loginFieldsLayout.setAutoCreateContainerGaps(true);

		loginFieldsLayout.setHorizontalGroup(loginFieldsLayout
				.createSequentialGroup()
				.addComponent(eMafiaText)
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(usernameText)
								.addComponent(passwordText)
								.addComponent(verifyText).addComponent(verBut))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(usernameField)
								.addComponent(passwordField)
								.addComponent(verifyField)
								.addComponent(cancelBut)));
		loginFieldsLayout.setVerticalGroup(loginFieldsLayout
				.createSequentialGroup()
				.addComponent(eMafiaText)
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(usernameText)
								.addComponent(usernameField))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(passwordText)
								.addComponent(passwordField))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(verifyText)
								.addComponent(verifyField))
				.addGroup(
						loginFieldsLayout
								.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
								.addComponent(verBut).addComponent(cancelBut)));
		frame.add(loginFieldsPanel);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();

		return frame;
	}

	private JAutoPanel window_nameSelection() {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		JLabel dialogText = new JLabel("Please input the name you want to use:");
		dialogText.setBorder(new EmptyBorder(10, 10, 20, 10));
		JTextField nameField = new JTextField(20);
		nameField.setName("name");
		Action nameAction = new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				String name = "";
				Component[] theList = ((Component) evt.getSource()).getParent()
						.getComponents();
				for (Component comp : theList) {
					if (comp instanceof JTextField
							&& comp.getName().equals("name")) {
						name = ((JTextField) comp).getText();
						((JTextField) comp).setText("");
					}
				}
				if (name.length() > 0) {
					Framework.Telnet.write("-name " + name);// redo login
				}
			}
		};
		JButton dialogOKBut = new JButton("OK");// XXX need resize this button
		dialogOKBut.setAction(nameAction);
		nameField.setAction(nameAction);
		// this.dialogOKBut.addActionListener(new CloseListener());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(dialogText, BorderLayout.NORTH);
		panel.add(nameField, BorderLayout.CENTER);
		panel.add(dialogOKBut, BorderLayout.PAGE_END);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.add(panel);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();

		return frame;
	}

	private JAutoPanel window_roleMenu(final Integer layer) {// Lists off
																// 'roleSetup'
																// and
																// 'rolePossible'
																// for host only
		JAutoPanel frame = new JAutoPanel(this.desktop);
		JPanel mainPanel = new JPanel();
		JButton setupBut = new JButton("<html>Role Setup</html>");
		setupBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets self
				Framework.Window.createIFrame("roleSetupHost");
			}
		});
		JButton possibleBut = new JButton(
				"<html><font size=\"1\">Roles Possible</font></html>");
		possibleBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets self
				Framework.Window.createIFrame("rolesPossible");
			}
		});
		possibleBut.setPreferredSize(new Dimension(15, 10));
		JButton dialogOKBut = new JButton("DONE");// XXX need resize this button
		dialogOKBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets self
			}
		});

		// mainPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		// mainPanel.add(roleSearchPanel);
		mainPanel.add(setupBut);
		mainPanel.add(possibleBut);
		mainPanel.add(dialogOKBut);

		frame.add(mainPanel);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();
		return frame;
	}

	private JAutoPanel window_rolesPossibleHost(final Integer layer) {
		JAutoPanel frame = new JAutoPanel(this.desktop);
		final RoleSearchPane searchPane = new RoleSearchPane(Framework);
		JPanel roleSearchPanel = new JPanel();
		roleSearchPanel.setLayout(new BoxLayout(roleSearchPanel,
				BoxLayout.Y_AXIS));
		roleSearchPanel.add(new JLabel("Role Search"));
		roleSearchPanel.add(searchPane);

		Framework.Telnet.write("-rolespossible list");
		Framework.Data.curMatch.rolesPossibleList
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Framework.Data.curMatch.rolesPossibleList
				.setLayoutOrientation(JList.VERTICAL);
		Framework.Data.curMatch.rolesPossibleList.setBackground(new Color(32,
				32, 32));
		Framework.Data.curMatch.rolesPossibleList
				.setCellRenderer(Framework.Window.CellRenderer.new RoleList());
		JScrollPane rolesPossibleScrollpane = new JScrollPane(
				Framework.Data.curMatch.rolesPossibleList);
		rolesPossibleScrollpane.setSize(115, 150);
		JButton clearBut = new JButton("Clear");
		clearBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Framework.Telnet.write("-rolespossible clear");
			}
		});
		JPanel rolesPossible = new JPanel();
		rolesPossible.setLayout(new BoxLayout(rolesPossible, BoxLayout.Y_AXIS));
		rolesPossible.add(new JLabel("Roles Possible"));
		rolesPossible.add(rolesPossibleScrollpane);
		rolesPossible.add(clearBut);

		JButton addBut = new JButton("--->");
		JButton removeBut = new JButton("<---");
		addBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchPane.getSelectedRole() != 0) {
					Framework.Telnet.write("-rolespossible add "
							+ searchPane.getSelectedRole());
				}
			}
		});
		removeBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List_Role role = (List_Role) Framework.Data.curMatch.rolesPossibleList
						.getSelectedValue();
				if (role != null) {
					Framework.Telnet.write("-rolespossible remove " + role.id);
				}
			}
		});
		JPanel addRemovePanel = new JPanel();
		addRemovePanel
				.setLayout(new BoxLayout(addRemovePanel, BoxLayout.Y_AXIS));
		addRemovePanel.add(addBut);
		addRemovePanel.add(removeBut);

		JButton dialogDoneBut = new JButton("Done");// XXX need resize this
													// button
		dialogDoneBut.setAction(new AbstractAction("Done") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets the poplayer
			}
		});

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		mainPanel.add(roleSearchPanel);
		mainPanel.add(addRemovePanel);
		mainPanel.add(rolesPossible);

		frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
		frame.add(mainPanel);
		frame.add(dialogDoneBut);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();
		return frame;
	}

	private JAutoPanel window_roleSetupHost(final Integer layer) {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		// Roles Possible
		Framework.Telnet.write("-rolespossible list");
		/*
		 * Framework.Data.curMatch.rolesPossibleList.setSelectionMode(
		 * ListSelectionModel.SINGLE_SELECTION);
		 * Framework.Data.curMatch.rolesPossibleList
		 * .setLayoutOrientation(JList.VERTICAL);
		 * Framework.Data.curMatch.rolesPossibleList.setBackground(new
		 * Color(32,32,32));
		 * Framework.Data.curMatch.rolesPossibleList.setCellRenderer
		 * (Framework.Window.CellRenderer.new RoleList());
		 */
		JScrollPane rolesPossibleScrollpane = new JScrollPane(
				Framework.Data.curMatch.rolesPossibleList);
		rolesPossibleScrollpane.setSize(115, 150);

		String[] affStrings = { "RANDOM", "TOWN", "MAFIA", "NEUTRAL" };
		final JComboBox<String> affList = new JComboBox<String>(affStrings);
		affList.setName("aff");
		affList.setSelectedIndex(0);

		String[] catStrings = { "RANDOM", "CORE", "INVESTIGATIVE",
				"PROTECTIVE", "KILLING" };
		final JComboBox<String> catList = new JComboBox<String>(catStrings);
		catList.setName("cat");
		catList.setSelectedIndex(0);

		JButton addCat = new JButton("Add Cat");
		addCat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String aff = (String) affList.getSelectedItem();
				String cat = (String) catList.getSelectedItem();
				Framework.Telnet.write("-rolesetup add " + aff + " " + cat);
			}

		});

		JPanel roleCat = new JPanel();
		roleCat.setLayout(new BoxLayout(roleCat, BoxLayout.X_AXIS));
		roleCat.add(affList);
		roleCat.add(catList);
		roleCat.add(addCat);

		JPanel rolesPossible = new JPanel();
		rolesPossible.setLayout(new BoxLayout(rolesPossible, BoxLayout.Y_AXIS));
		rolesPossible.add(new JLabel("Roles Possible"));
		rolesPossible.add(rolesPossibleScrollpane);
		rolesPossible.add(roleCat);

		// Roles Setup
		Framework.Telnet.write("-rolesetup list");
		/*
		 * Framework.Data.curMatch.roleSetupList.setSelectionMode(ListSelectionModel
		 * .SINGLE_SELECTION);
		 * Framework.Data.curMatch.roleSetupList.setLayoutOrientation
		 * (JList.VERTICAL);
		 * Framework.Data.curMatch.roleSetupList.setBackground(new
		 * Color(32,32,32));
		 * Framework.Data.curMatch.roleSetupList.setCellRenderer
		 * (Framework.Window.CellRenderer.new RoleSetupList());
		 */
		JScrollPane roleSetupScrollpane = new JScrollPane(
				Framework.Data.curMatch.roleSetupList);
		roleSetupScrollpane.setSize(115, 150);
		JButton clearBut = new JButton("Clear");
		clearBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Framework.Telnet.write("-rolesetup clear");
			}
		});
		JPanel roleSetup = new JPanel();
		roleSetup.setLayout(new BoxLayout(roleSetup, BoxLayout.Y_AXIS));
		roleSetup.add(new JLabel("Role Setup"));
		roleSetup.add(roleSetupScrollpane);
		roleSetup.add(clearBut);

		JButton addBut = new JButton("--->");
		JButton removeBut = new JButton("<---");
		addBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List_Role role = (List_Role) Framework.Data.curMatch.rolesPossibleList
						.getSelectedValue();
				if (role != null) {
					Framework.Telnet.write("-rolesetup add " + role.id);
				}
			}
		});
		removeBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List_Role role = (List_Role) Framework.Data.curMatch.roleSetupList
						.getSelectedValue();
				if (role != null) {
					Framework.Telnet.write("-rolesetup remove " + role.id);
				}
			}
		});
		JPanel addRemovePanel = new JPanel();
		addRemovePanel
				.setLayout(new BoxLayout(addRemovePanel, BoxLayout.Y_AXIS));
		addRemovePanel.add(addBut);
		addRemovePanel.add(removeBut);

		JButton dialogDoneBut = new JButton("Done");// XXX need resize this
													// button
		dialogDoneBut.setAction(new AbstractAction("Done") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets the poplayer
			}
		});

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		mainPanel.add(rolesPossible);
		mainPanel.add(addRemovePanel);
		mainPanel.add(roleSetup);

		frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
		frame.add(mainPanel);
		frame.add(dialogDoneBut);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();
		return frame;
	}

	private JAutoPanel window_roleSetupNonHost(final Integer layer) {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		// Roles Possible
		Framework.Telnet.write("-rolespossible list");
		/*
		 * Framework.Data.curMatch.rolesPossibleList.setSelectionMode(
		 * ListSelectionModel.SINGLE_SELECTION);
		 * Framework.Data.curMatch.rolesPossibleList
		 * .setLayoutOrientation(JList.VERTICAL);
		 * Framework.Data.curMatch.rolesPossibleList.setBackground(new
		 * Color(32,32,32));
		 * Framework.Data.curMatch.rolesPossibleList.setCellRenderer
		 * (Framework.Window.CellRenderer.new RoleList());
		 */
		JScrollPane rolesPossibleScrollpane = new JScrollPane(
				Framework.Data.curMatch.rolesPossibleList);
		rolesPossibleScrollpane.setSize(115, 150);

		JPanel rolesPossible = new JPanel();
		rolesPossible.setLayout(new BoxLayout(rolesPossible, BoxLayout.Y_AXIS));
		rolesPossible.add(new JLabel("Roles Possible"));
		rolesPossible.add(rolesPossibleScrollpane);

		// Roles Setup
		Framework.Telnet.write("-rolesetup list");
		/*
		 * Framework.Data.curMatch.roleSetupList.setSelectionMode(ListSelectionModel
		 * .SINGLE_SELECTION);
		 * Framework.Data.curMatch.roleSetupList.setLayoutOrientation
		 * (JList.VERTICAL);
		 * Framework.Data.curMatch.roleSetupList.setBackground(new
		 * Color(32,32,32));
		 * Framework.Data.curMatch.roleSetupList.setCellRenderer
		 * (Framework.Window.CellRenderer.new RoleSetupList());
		 */
		JScrollPane roleSetupScrollpane = new JScrollPane(
				Framework.Data.curMatch.roleSetupList);
		roleSetupScrollpane.setSize(115, 150);

		JPanel roleSetup = new JPanel();
		roleSetup.setLayout(new BoxLayout(roleSetup, BoxLayout.Y_AXIS));
		roleSetup.add(new JLabel("Role Setup"));
		roleSetup.add(roleSetupScrollpane);

		JButton dialogDoneBut = new JButton("Done");// XXX need resize this
													// button
		dialogDoneBut.setAction(new AbstractAction("Done") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets the poplayer
			}
		});

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		mainPanel.add(rolesPossible);
		mainPanel.add(roleSetup);

		frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
		frame.add(mainPanel);
		frame.add(dialogDoneBut);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();
		return frame;
	}

	private JAutoPanel window_roleView(String[] data, final Integer layer) {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		JLabel nameText = new JLabel("<Role Name Not Set>");
		JLabel idText = new JLabel("<ID Not Set>");
		if (data[0] != null) {
			nameText.setText(data[0]);
		}
		if (data[1] != null) {
			idText.setText(data[1]);
		}

		JButton dialogOKBut = new JButton("OK");// XXX need resize this button
		dialogOKBut.setAction(new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets the poplayer
			}
		});

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(nameText, BorderLayout.NORTH);
		panel.add(idText, BorderLayout.CENTER);
		panel.add(dialogOKBut, BorderLayout.PAGE_END);

		frame.add(panel);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();
		return frame;
	}

	private JAutoPanel window_popup(String msg, String msgType,
			final Integer layer) {
		JAutoPanel frame = new JAutoPanel(this.desktop);

		// JLabel dialogText = new JLabel(msg);
		JEditorPane dialogText = new JEditorPane("text/html", msg);
		dialogText.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
				if (HyperlinkEvent.EventType.ACTIVATED.equals(arg0
						.getEventType())) {
					String data = arg0.getDescription();
					if (data != null) {
						if (data.startsWith("http://")) {
							if (Desktop.isDesktopSupported()) {
								Desktop compDesktop = Desktop.getDesktop();
								try {
									URI uri = new URI(data);
									compDesktop.browse(uri);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		});
		// dialogText.setText(msg);
		dialogText.setEditable(false);
		dialogText.setOpaque(false);
		dialogText.setBorder(new EmptyBorder(10, 10, 20, 10));
		Action okAction = new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets the poplayer
			}
		};
		Action reloginAction = new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				deleteIFrame(layer);// delets the poplayer
				Framework.Telnet.write("JAVA");// redo login
			}
		};
		Action exitAction = new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		};
		JButton dialogOKBut = new JButton("OK");// XXX need resize this button
		if (msgType.equals("relogin")) {
			dialogOKBut.setAction(reloginAction);
		} else if (msgType.equals("ok")) {
			dialogOKBut.setAction(okAction);
		} else if (msgType.equals("exit")) {
			dialogOKBut.setAction(exitAction);
		} else {
			dialogOKBut.setAction(okAction);
			dialogOKBut.setText("Bug in making this button!");
		}
		// this.dialogOKBut.addActionListener(new CloseListener());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(dialogText, BorderLayout.CENTER);
		panel.add(dialogOKBut, BorderLayout.PAGE_END);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.add(panel);
		frame.setSize(frame.getPreferredSize());
		frame.setAutoCenter();
		return frame;
	}
}
