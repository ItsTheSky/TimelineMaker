package info.itsthesky.settings;

import info.itsthesky.util.FileManager;
import org.json.JSONObject;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public abstract class AbstractSetting {

	private static final Map<String, AbstractSetting> SETTINGS = new HashMap<>();
	private static final Map<String, Object> VALUES = new HashMap<>();

	public static Component createSetting(String name, String description) {
		return setting(name, () -> new Setting(name, description)).toComponent();
	}

	public static Component createSpinnerSetting(String name, String description,
												 int min, int max) {
		return setting(name, () -> new NumberSetting(name, description, min, max)).toComponent();
	}

	public static Component createSpinnerSetting(String name, String description) {
		return setting(name, () -> new NumberSetting(name, description, Integer.MIN_VALUE, Integer.MAX_VALUE)).toComponent();
	}

	public static Component createColorSetting(String name, String description) {
		return setting(name, () -> new ColorSetting(name, description)).toComponent();
	}

	@SafeVarargs
	public static <T> Component createDropdownSetting(String name, String description,
													  DropdownSetting.Element<T>... values) {
		return setting(name, () -> new DropdownSetting<>(name, description, values)).toComponent();
	}

	private static AbstractSetting setting(String name, Supplier<AbstractSetting> creator) {
		if (SETTINGS.containsKey(format(name)))
			return SETTINGS.get(format(name));
		else {
			AbstractSetting setting = creator.get();
			SETTINGS.put(format(name), setting);
			return setting;
		}
	}

	private static String format(String name) {
		return name.toLowerCase().replace(" ", "_");
	}

	public static void loadSettings(JSONObject json) {
		for (AbstractSetting setting : SETTINGS.values())
			setting.load(json.getJSONObject("settings"));
	}

	public static JSONObject saveSettings() {
		final JSONObject json = new JSONObject();
		for (AbstractSetting setting : SETTINGS.values())
			setting.save(json);
		return json;
	}

	protected static void storeValue(String id, Object value) {
		VALUES.put(id, value);
	}

	private final String name;
	private final String description;

	protected AbstractSetting(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public static <T extends AbstractSetting> T getSetting(String name) {
		return (T) SETTINGS.get(format(name));
	}

	public static <T> T getSettingValue(AbstractSetting setting) {
		return VALUES.containsKey(setting.getId()) ? (T) VALUES.get(setting.getId()) : null;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return name.toLowerCase().replace(" ", "_");
	}

	public String getDescription() {
		return description;
	}

	protected void storeValue(Object value) {
		storeValue(getId(), value);
	}

	public abstract Component toComponent();

	public abstract void load(JSONObject json);

	public abstract void save(JSONObject json);
}
