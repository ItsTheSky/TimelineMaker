package info.itsthesky.components;

import info.itsthesky.TimelineMaker;
import info.itsthesky.util.FileManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class MenuBar extends JMenuBar {

	public MenuBar() {
		super();

		add("File", "New", "Open", "Save", "Save As", "Exit");

		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	private void add(String name, String... items) {
		final JMenu menu = new JMenu(name);
		for (String item : items) {
			final JMenuItem menuItem = new JMenuItem(item);
			menu.add(menuItem);
			menuItem.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					switch (menuItem.getText().toLowerCase()) {
						case "exit" -> System.exit(0);
						case "open" -> {
							boolean success = false;
							final JFileChooser chooser = new JFileChooser();
							chooser.setFileFilter(new FileNameExtensionFilter("TimelineMaker Files", "tm"));
							if (chooser.showOpenDialog(TimelineMaker.getScreen()) == JFileChooser.APPROVE_OPTION) {
								try {
									FileManager.loadProject(chooser.getSelectedFile());
									success = true;
								} catch (IOException ex) {
									throw new RuntimeException(ex);
								}
							}
							if (success)
								JOptionPane.showMessageDialog(TimelineMaker.getScreen(), "Project '"+
										FileManager.getCurrentProject().getJSONObject("settings").getString("name") +"' successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
						}
						case "save" -> {
							boolean success = false;
							try {
								if (FileManager.isSampleProject()) {
									final JFileChooser chooser = new JFileChooser();
									chooser.setName("Save Project");
									chooser.setApproveButtonText("Save");
									final FileNameExtensionFilter filter = new FileNameExtensionFilter(
											"Timeline Maker & JSON", "tm", "json");
									chooser.setFileFilter(filter);
									int returnVal = chooser.showSaveDialog(TimelineMaker.getScreen());
									if (returnVal == JFileChooser.APPROVE_OPTION) {
										FileManager.saveProject(chooser.getSelectedFile());
										FileManager.loadProject(chooser.getSelectedFile());
										success = true;
									}
								} else {
									FileManager.saveProject();
									success = true;
								}
							} catch (Exception exception) {
								exception.printStackTrace();
							}
							if (success)
								JOptionPane.showMessageDialog(TimelineMaker.getScreen(), "File saved successfully!",
										"Success", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}
			});
		}
		add(menu);
	}

}
