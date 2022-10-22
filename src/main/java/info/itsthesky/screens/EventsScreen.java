package info.itsthesky.screens;

import info.itsthesky.events.Event;
import info.itsthesky.events.EventPriority;
import info.itsthesky.events.Events;
import info.itsthesky.settings.AbstractSetting;
import info.itsthesky.settings.NumberSetting;
import info.itsthesky.util.ColorIcon;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class EventsScreen extends JPanel {

	public EventsScreen() {

		// This will list the timeline events.
		// The user will be able to add new events using an "Add" button at the top.
		// The user will be able to edit events because their input are directly shown in the list.

		final Box panel = Box.createVerticalBox();
		panel.setBorder(BorderFactory.createTitledBorder("Events"));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		add(panel);

		grid.setLayout(new GridLayout(1, 5));
		panel.add(grid);
	}

	private final JPanel grid = new JPanel();

	public void refreshEvents() {
		grid.removeAll();
		((GridLayout) grid.getLayout()).setRows(Events.events.size() + 2);
		((GridLayout) grid.getLayout()).setHgap(20);

		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;

		for (Event event : Events.events) {

			// An event will be displayed as a JPanel with a Jbutton to remove it and a JLabel to show its name.
			// It will also contain a JSpinner to edit the start & end year of the event.

			final JButton removeButton = new JButton("Remove");
			removeButton.addActionListener(e -> {
				Events.removeEvent(event);
				refreshEvents();
			});

			final JTextField nameField = new JTextField(event.getName());
			nameField.addActionListener(e -> event.setName(nameField.getText()));
			nameField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					event.setName(nameField.getText());
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					event.setName(nameField.getText());
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					event.setName(nameField.getText());
				}
			});

			final int minYear = (int) ((NumberSetting) AbstractSetting.getSetting("start")).getSpinner().getValue();
			final int maxYear = (int) ((NumberSetting) AbstractSetting.getSetting("end")).getSpinner().getValue();

			final JSpinner startSpinner = new JSpinner(new SpinnerNumberModel(event.getStart(), minYear, maxYear, 1));
			final JSpinner endSpinner = new JSpinner(new SpinnerNumberModel(event.getEnd(), minYear, maxYear, 1));
			startSpinner.addChangeListener(e -> event.setStart((int) startSpinner.getValue()));
			endSpinner.addChangeListener(e -> event.setEnd((int) endSpinner.getValue()));

			final ColorIcon colorIcon = new ColorIcon();
			colorIcon.setColor(event.getColor());
			final JButton button = new JButton(colorIcon);
			button.addActionListener(e -> {
				final Color color = JColorChooser.showDialog(null, "Choose a color", colorIcon.getColor());
				if (color != null) {
					colorIcon.setColor(color);
					button.repaint();
					event.setColor(color);
				}
			});

			grid.add(removeButton, constraints);
			constraints.gridx++;
			grid.add(nameField, constraints);
			constraints.gridx++;
			grid.add(startSpinner, constraints);
			constraints.gridx++;
			grid.add(endSpinner, constraints);
			constraints.gridx++;
			grid.add(button, constraints);

			constraints.gridx = 0;
			constraints.gridy++;
		}

		for (int i = 0; i < 5; i++) {
			grid.add(new JLabel(), constraints);
			constraints.gridx++;
		}

		constraints.gridy++;
		constraints.gridx = 0;

		for (EventPriority priority : EventPriority.values()) {
			final JButton addButton = new JButton("Add " + priority.name());
			addButton.addActionListener(e -> {
				Events.addEvent(Event.create("New event", 0, 0, Color.ORANGE, priority));
				refreshEvents();
			});
			grid.add(addButton, constraints);
			constraints.gridx++;
		}
	}

}
