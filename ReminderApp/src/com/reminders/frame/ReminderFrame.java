package com.reminders.frame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ReminderFrame extends JFrame implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	public ReminderFrame(String appName) {
		super(appName);
		setExtendedState(MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(800, 700));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(createMenuBar());
	}

	public void showFrame() {
		this.setVisible(true);
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();

		// Build the first menu.
		menu = new JMenu("File");
		menuBar.add(menu);

		menuItem = new JMenuItem("New Reminder", KeyEvent.VK_N);
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu.addSeparator();

		menuItem = new JMenuItem("Exit");
		menu.add(menuItem);

		menu = new JMenu("Edit");
		menuBar.add(menu);

		menu = new JMenu("Settings");
		menuBar.add(menu);

		menu = new JMenu("Help");

		JMenuItem about = new JMenuItem("About");
		menu.add(about);

		menuBar.add(menu);

		return menuBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

}
