package info.itsthesky.events;

import info.itsthesky.TimelineMaker;
import info.itsthesky.screens.CoreFrame;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public final class Events {

	public static final List<Event> events = new ArrayList<>();

	public static void addEvent(Event event) {
		events.add(event);
	}

	public static void clear() {
		events.clear();
	}

	public static void removeEvent(Event event) {
		events.remove(event);
	}

	public static JSONArray save() {
		final JSONArray array = new JSONArray();
		for (Event event : events)
			array.put(event.toJSON());
		return array;
	}

	public static void load(JSONArray array) {
		clear();
		for (int i = 0; i < array.length(); i++) {
			final Event event = Event.fromJSON(array.getJSONObject(i));
			events.add(event);
		}
		((CoreFrame) TimelineMaker.getScreen()).getEventsScreen().refreshEvents();
	}
}
