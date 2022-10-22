package info.itsthesky.util;

import info.itsthesky.TimelineMaker;
import info.itsthesky.events.Events;
import info.itsthesky.settings.AbstractSetting;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class FileManager {

	private static File folder;
	private static @Nullable File projectFile;
	private static JSONObject preferences;
	private static JSONObject currentProject;

	public static File getFolder() {
		if (folder == null)
			folder = new File(System.getProperty("user.home") + "/.timeline_maker");
		if (!folder.exists())
			folder.mkdirs();
		return folder;
	}

	public static void init() throws IOException {
		folder = getFolder();

		final File preferencesFile = new File(folder, "preferences.json");
		if (!preferencesFile.exists()) {
			try {
				preferencesFile.createNewFile();
				Files.write(preferencesFile.toPath(), FileManager.class.getClassLoader().getResourceAsStream("preferences.json").readAllBytes());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		preferences = new JSONObject(Files.readString(preferencesFile.toPath()));

		if (preferences.optString("current_project", "sample").equalsIgnoreCase("sample"))
			loadDefaultProject(FileManager.class.getClassLoader().getResourceAsStream("default.json"));
		else
			loadProject(new File(preferences.optString("current_project")));

		AbstractSetting.loadSettings(currentProject);
		Events.load(currentProject.getJSONArray("events"));
	}

	public static void loadDefaultProject(InputStream stream) throws IOException {
		currentProject = new JSONObject(new String(stream.readAllBytes()));
		projectFile = null;
		refreshTitle();
	}

	public static boolean isSampleProject() {
		return projectFile == null;
	}

	public static void loadProject(File file) throws IOException {
		projectFile = file;
		currentProject = new JSONObject(Files.readString(file.toPath()));
		AbstractSetting.loadSettings(currentProject);
		Events.load(currentProject.getJSONArray("events"));
		preferences.put("current_project", file.getAbsolutePath());
		refreshTitle();
	}

	private static void refreshTitle() {
		TimelineMaker.getScreen().setTitle("Timeline Maker - " + currentProject.getJSONObject("settings").optString("name", "Untitled"));
	}

	public static void saveProject() throws IOException {
		saveProject(projectFile);
	}

	public static void saveProject(File target) throws IOException {
		if (currentProject == null)
			return;

		currentProject.put("settings", AbstractSetting.saveSettings());
		currentProject.put("events", Events.save());

		Files.writeString(target.toPath(), currentProject.toString(4));
		Files.writeString(new File(getFolder(), "preferences.json").toPath(), preferences.toString(4));
	}

	public static JSONObject getCurrentProject() {
		return currentProject;
	}
}
