package info.itsthesky.events;

import java.awt.*;

public record EventPriority(String name) {

	public static final EventPriority LOW = new EventPriority("Low");
	public static final EventPriority NORMAL = new EventPriority("Normal");
	public static final EventPriority HIGH = new EventPriority("High");
	public static final EventPriority HIGHEST = new EventPriority("Highest");

	public static EventPriority getPriority(String name) {
		return switch (name.toLowerCase()) {
			case "low" -> LOW;
			case "normal" -> NORMAL;
			case "high" -> HIGH;
			case "highest" -> HIGHEST;
			default -> null;
		};
	}

	public static EventPriority[] values() {
		return new EventPriority[] {LOW, NORMAL, HIGH, HIGHEST};
	}
}
