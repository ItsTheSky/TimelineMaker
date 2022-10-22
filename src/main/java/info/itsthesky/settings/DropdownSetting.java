package info.itsthesky.settings;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Function;

public class DropdownSetting<T> extends AbstractSetting {

	private final Element<T>[] values;

	@SafeVarargs
	protected DropdownSetting(String name, String description,
							  Element<T>... values) {
		super(name, description);
		this.values = values;
	}

	private JComboBox<Element<T>> comboBox;

	@Override
	public Component toComponent() {
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		panel.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		comboBox = new JComboBox<>(values);
		comboBox.setMinimumSize(new Dimension(200, 30));
		comboBox.setToolTipText(getDescription());
		comboBox.addItemListener(e -> storeValue(((Element<T>) e.getItem()).getValue()));

		panel.add(new JLabel(getName()));
		panel.add(comboBox);

		return panel;
	}

	public Element<T> getSelected() {
		return values[comboBox.getSelectedIndex()];
	}

	@Override
	public void load(JSONObject json) {
		final String value = json.optString(getId(), null);
		for (int i = 0; i < values.length; i++) {
			if (values[i].getId().equals(value)) {
				comboBox.setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public void save(JSONObject json) {
		json.put(getId(), getSelected().getId());
	}

	public static class Element<T> {

		private final String id;
		private final T value;
		private final String name;

		@SuppressWarnings("unchecked")
		public static <T> Element<T>[] allOf(Function<T, String> toString, T... values) {
			return Arrays.stream(values)
					.map(value -> new Element<>(value, toString.apply(value)))
					.toArray(Element[]::new);
		}

		public Element(T value, String name) {
			this.id = name.toLowerCase().replace(" ", "_");
			this.value = value;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public T getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
