package info.itsthesky.events;

import org.json.JSONObject;

import java.awt.*;

public class Event {

	private boolean isInstant;
	private String name;
	private int start;
	private int end;
	private EventPriority priority;
	private Color color;

	public static Event createInstant(String name, int date, Color color, EventPriority priority) {
		return new Event(name, date, date, color, priority);
	}

	public static Event create(String name, int start, int end, Color color, EventPriority priority) {
		return new Event(name, start, end, color, priority);
	}

	private Event(String name, int start, int end, Color color, EventPriority priority) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.color = color;
		this.priority = priority;
		this.isInstant = start == end;
	}

	public String getName() {
		return name;
	}

	public int getStart() {
		return start;
	}

	public Color getColor() {
		return color;
	}

	public int getEnd() {
		return end;
	}

	public EventPriority getPriority() {
		return priority;
	}

	public boolean isInstant() {
		return isInstant;
	}

	public int getYear() {
		return start;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStart(int start) {
		this.start = start;
		isInstant = start == end;
	}

	public void setEnd(int end) {
		this.end = end;
		isInstant = start == end;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public JSONObject toJSON() {
		final JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("start", start);
		json.put("end", end);
		json.put("color", color.getRGB());
		json.put("priority", priority.name());
		return json;
	}

	public static Event fromJSON(JSONObject json) {
		return new Event(
				json.getString("name"),
				json.getInt("start"),
				json.getInt("end"),
				new Color(json.getInt("color")),
				EventPriority.getPriority(json.getString("priority"))
		);
	}
}
