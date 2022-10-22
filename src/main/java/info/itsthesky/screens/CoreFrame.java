package info.itsthesky.screens;

import info.itsthesky.components.MenuBar;

import javax.swing.*;
import java.awt.*;

public class CoreFrame extends JFrame {

	public CoreFrame() {
		super("Timeline Maker");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(new MenuBar());
		setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Make a tab system
		final JTabbedPane tabbedPane = new JTabbedPane();

		// Add the tabs
		tabbedPane.addTab("Settings", creationScreen = new CreationScreen());
		tabbedPane.addTab("Events", eventsScreen = new EventsScreen());

		// Add the tab system to the frame
		add(tabbedPane);
	}

	private final CreationScreen creationScreen;
	private final EventsScreen eventsScreen;

	public EventsScreen getEventsScreen() {
		return eventsScreen;
	}

	public CreationScreen getCreationScreen() {
		return creationScreen;
	}
}
