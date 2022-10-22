package info.itsthesky.screens;

import info.itsthesky.components.MenuBar;
import info.itsthesky.components.PreviewComponent;
import info.itsthesky.settings.DropdownSetting;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static info.itsthesky.settings.AbstractSetting.*;

public class CreationScreen extends JPanel {

	public CreationScreen() {

		final Box panel = Box.createVerticalBox();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, Integer.MAX_VALUE));
		add(panel);

		panel.add(createSection("Core Settings", createSetting("Name", "Timeline name")));
		panel.add(createSection("Date Format",
				createSpinnerSetting("Start", "Input the most ancient time of your timeline"),
				createSpinnerSetting("End", "Input the most recent time of your timeline"),
				createSetting("Year Name", "The name of each year between the ancient and recent time"),
				null
		));
		panel.add(createSection("Timeline Theme",
				createColorSetting("Background", "The background color of the timeline"),
				createColorSetting("Text", "The text color of the timeline"),
				createColorSetting("Structure", "The color for the structure of the timeline, including top-line separator, year separator, and more."),
				createSpinnerSetting("Rows", "The amount of rows in the timeline", 0, 10),
				null,
				createDropdownSetting("Font", "The font of the timeline",
						DropdownSetting.Element.allOf(Font::getName, GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())),
				null,
				new PreviewComponent()
		));
	}

	/**
	 * Create a new setting section with the input name.
	 * <br> A small gray border will be drawn around the section, with the name on the top middle.
	 * <br> The input components will be added to the section.
	 * @param name The name of the section
	 * @param components The components to add to the section
	 * @return The created section
	 */
	private JPanel createSection(String name, Component ... components) {
		final JPanel section = new JPanel();
		JPanel currentLine = new JPanel();
		section.add(currentLine);

		section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
		section.setBorder(BorderFactory.createTitledBorder(name));
		for (Component component : components)
			if (component == null) {
				currentLine = new JPanel();
				section.add(currentLine);
			}
			else currentLine.add(component);
		section.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) section.getMaximumSize().getHeight()));
		return section;
	}

}
