package info.itsthesky.settings;

import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Properties;

public class Setting extends AbstractSetting {
	public Setting(String name, String description) {
		super(name, description);
	}

	private JTextField textField;

	@Override
	public Component toComponent() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		textField = new JTextField();
		textField.setMinimumSize(new Dimension(200, 30));
		textField.setToolTipText(getDescription());
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				storeValue(textField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				storeValue(textField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				storeValue(textField.getText());
			}
		});

		panel.add(new JLabel(getName()));
		panel.add(textField);

		return panel;
	}

	public JTextField getTextField() {
		return textField;
	}

	@Override
	public void load(JSONObject json) {
		textField.setText(json.optString(getId(), ""));
	}

	@Override
	public void save(JSONObject json) {
		json.put(getId(), textField.getText());
	}
}
