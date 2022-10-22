package info.itsthesky.settings;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class NumberSetting extends AbstractSetting {

	private final int min;
	private final int max;
	public NumberSetting(String name, String description, int min, int max) {
		super(name, description);

		this.min = min;
		this.max = max;
	}

	private JSpinner spinner;

	@Override
	public Component toComponent() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		spinner = new JSpinner(new SpinnerNumberModel(min, min, max, 1));
		spinner.setMinimumSize(new Dimension(200, 30));
		spinner.setToolTipText(getDescription());
		spinner.addChangeListener(e -> storeValue(spinner.getValue()));

		panel.add(new JLabel(getName()));
		panel.add(spinner);

		return panel;
	}

	public JSpinner getSpinner() {
		return spinner;
	}

	@Override
	public void load(JSONObject json) {
		spinner.setValue(json.optInt(getId(), min));
	}

	@Override
	public void save(JSONObject json) {
		json.put(getId(), spinner.getValue());
	}
}
