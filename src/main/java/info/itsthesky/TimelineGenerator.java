package info.itsthesky;

import info.itsthesky.events.Event;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

public final class TimelineGenerator {

	/**
	 * Generate a timeline image:
	 * - Use the background, text and structure color as reference
	 * - Draw the starting (minYear) and ending (maxYear) year at the top
	 * - Draw a line to separate years from events
	 * - Draw a vertical line for starting and ending year
	 * @param background The background color
	 * @param text The text color
	 * @param structure The structure color
	 * @param font The font to use
	 * @param minYear The starting year
	 * @param maxYear The ending year
	 * @param yearName The name of each year
	 * @return The generated image
	 */
	public static BufferedImage generate(float upscaling,
			Color background, Color text, Color structure,
			Font font, int minYear, int maxYear, int rows, String yearName,
			Event ... events
	) {

		// Constants
		final float multiplier = upscaling;
		final int width = (int) (1000 * multiplier);
		final int height = (int) (100 * multiplier);
		final int separation = (int) (5 * multiplier);
		final int padding = (int) (10 * multiplier);
		final int lineThickness = (int) (2 * multiplier);
		final int fontSize = (int) (20 * multiplier);

		int eventLevel = 0;
		Map<Integer, List<Event>> eventsByLevel = new HashMap<>();
		for (Event event : events) {
			if (event.getYear() < minYear || event.getYear() > maxYear)
				continue;

			if (!eventsByLevel.containsKey(eventLevel))
				eventsByLevel.put(eventLevel, new ArrayList<>());

			// If the event is too close to another one, we move it to the next level
			if (eventsByLevel.get(eventLevel).stream().anyMatch(e -> Math.abs(e.getYear() - event.getYear()) < 2 ||
					Math.abs(e.getYear() - event.getYear()) > maxYear - minYear - 2 ||
					event.getStart() < e.getStart() && e.getStart() < event.getEnd() ||
					event.getStart() < e.getEnd() && e.getEnd() < event.getEnd()
			)) {
				eventLevel++;
				if (!eventsByLevel.containsKey(eventLevel))
					eventsByLevel.put(eventLevel, new ArrayList<>());
				eventsByLevel.get(eventLevel).add(event);
			} else eventsByLevel.get(eventLevel).add(event);
		}
		final int maxEventsLevel = Math.max(eventLevel, rows);

		// Create a new image
		final BufferedImage image = new BufferedImage(width, (int) (height + maxEventsLevel * (fontSize + padding + 5) + separation * maxEventsLevel), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D graphics = image.createGraphics();

		// Enable anti-aliasing
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Set the font
		graphics.setFont(font.deriveFont((float) fontSize));

		// Draw the background
		graphics.setColor(background);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

		// Draw the structure (line to separate years from events)
		graphics.setColor(structure);
		graphics.fillRect(padding * 2, padding * 2 + fontSize + padding, width - padding * 4, lineThickness);

		// Draw the starting & ending year
		graphics.setColor(text);
		graphics.drawString(minYear + " " + yearName, padding, padding + fontSize);
		graphics.drawString(maxYear + " " + yearName, width - padding - graphics.getFontMetrics().stringWidth(maxYear + " " + yearName), padding + fontSize);

		// Draw the vertical line for starting and ending year
		graphics.setColor(structure);
		graphics.fillRect(padding * 2, padding * 2 + fontSize, lineThickness, image.getHeight() - padding * 4 - fontSize);
		graphics.fillRect(width - padding * 2 - lineThickness, padding * 2 + fontSize, lineThickness, image.getHeight() - padding * 4 - fontSize);

		eventsByLevel.forEach((level, lEvents) -> {
			// sort the lEvents list by if they are instant or not
			lEvents.sort(Comparator.comparingInt(Event::getYear));

			for (Event event : lEvents) {
				if (event.isInstant()) {
					// Draw the instant event
					graphics.setColor(event.getColor());
					graphics.fillRect(
							padding * 2 + (int) ((width - padding * 4) * ((float) (event.getYear() - minYear) / (float) (maxYear - minYear))),
							padding * 2 + fontSize + padding + lineThickness + padding + level * (fontSize + padding) + level * separation,
							lineThickness,
							fontSize + padding
					);
					graphics.setColor(text);
					graphics.drawString(
							event.getName(),
							padding * 2 + (int) ((width - padding * 4) * ((float) (event.getYear() - minYear) / (float) (maxYear - minYear))) + padding,
							padding * 2 + fontSize + padding + lineThickness + padding + level * (fontSize + padding) + fontSize + level * separation
					);
				} else {
					// Draw the event
					graphics.setColor(event.getColor());
					graphics.fillRoundRect(
							padding * 2 + (int) ((width - padding * 4) * ((float) (event.getYear() - minYear) / (float) (maxYear - minYear))),
							padding * 2 + fontSize + padding + lineThickness + padding + level * (fontSize + padding) + level * separation,
							(int) ((width - padding * 4) * ((float) (event.getEnd() - event.getYear()) / (float) (maxYear - minYear))),
							fontSize + padding, (int) (20 * multiplier), (int) (20 * multiplier)
					);
					graphics.setColor(text);
					graphics.drawString(
							event.getName(),
							padding * 2 + (int) ((width - padding * 4) * ((float) (event.getYear() - minYear) / (float) (maxYear - minYear))) + padding,
							padding * 2 + fontSize + padding + lineThickness + padding + level * (fontSize + padding) + fontSize + level * separation
					);
				}
			}
		});

		// Draw the years (with the lines) and the dates where an event start, stop or occur
		final List<Integer> years = new ArrayList<>();
		for (Event event : events)
			if (event.isInstant())
				years.add(event.getYear());
			else {
				years.add(event.getStart());
				years.add(event.getEnd());
			}
		Collections.sort(years);
		for (int year : years) {
			graphics.setColor(structure.brighter());
			graphics.fillRect(
					padding * 2 + (int) ((width - padding * 4) * ((float) (year - minYear) / (float) (maxYear - minYear))),
					padding * 2 + fontSize,
					lineThickness,
					fontSize
			);
			graphics.setColor(text.brighter().brighter());
			// The text is in the upper part
			graphics.drawString(
					year + "",
					padding * 2 + (int) ((width - padding * 4) * ((float) (year - minYear) / (float) (maxYear - minYear))) - graphics.getFontMetrics().stringWidth(year + "") / 2,
					padding * 2 + fontSize - padding
			);
		}

		graphics.dispose();
		return image;
	}

}
