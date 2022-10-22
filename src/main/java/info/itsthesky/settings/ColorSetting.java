package info.itsthesky.settings;

import info.itsthesky.util.ColorIcon;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class ColorSetting extends AbstractSetting{
	protected ColorSetting(String name, String description) {
		super(name, description);
	}

	private ColorIcon colorIcon;

	@Override
	public Component toComponent() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		colorIcon = new ColorIcon();
		final JButton button = new JButton(colorIcon);
		button.addActionListener(e -> {
			final Color color = JColorChooser.showDialog(null, "Choose a color", colorIcon.getColor());
			if (color != null) {
				colorIcon.setColor(color);
				button.repaint();
				storeValue(color);
			}
		});

		panel.add(new JLabel(getName()));
		panel.add(button);

		return panel;
	}

	public ColorIcon getColorIcon() {
		return colorIcon;
	}

	@Override
	public void load(JSONObject json) {
		colorIcon.setColor(Color.decode(json.optString(getId(), "#000000")));
	}

	@Override
	public void save(JSONObject json) {
		json.put(getId(), colorIcon.getColor().getRGB());
	}
}
