package info.itsthesky.components;

import info.itsthesky.TimelineGenerator;
import info.itsthesky.events.Event;
import info.itsthesky.events.Events;
import info.itsthesky.settings.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PreviewComponent extends JPanel {

	public PreviewComponent() {
		setMaximumSize(new Dimension(getMaximumSize().width, Integer.MAX_VALUE));

		final JButton button = new JButton("Generate Preview");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(button);

		button.addActionListener(e -> {
			// Show the preview in another window
			final JFrame frame = new JFrame("Preview");
			final BufferedImage generated = generatePreview(1.0f);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(generated.getWidth() + 18, generated.getHeight() + 30);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);

			final ImageComponent imageComponent = new ImageComponent(generatePreview(1.0f));
			imageComponent.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
			imageComponent.setCursor(new Cursor(Cursor.HAND_CURSOR));
			imageComponent.addMouseListener(new MouseListener() {
				@Override public void mouseClicked(MouseEvent e) { }
				@Override public void mousePressed(MouseEvent e) { }
				@Override public void mouseEntered(MouseEvent e) { }
				@Override public void mouseExited(MouseEvent e) { }

				@Override
				public void mouseReleased(MouseEvent e) {
					// We ask the upscaling of the image
					final float upscaling = Float.parseFloat(JOptionPane.showInputDialog("How much do you want to upscale the image?"));
					// We ask the user to save the image
					final JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Save the preview image");
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.showSaveDialog(PreviewComponent.this);
					// We save the image
					if (fileChooser.getSelectedFile() != null) {
						try {
							if (!fileChooser.getSelectedFile().getName().endsWith(".png"))
								ImageIO.write(generatePreview(upscaling), "png", new File(fileChooser.getSelectedFile().getAbsolutePath() + ".png"));
							else
								ImageIO.write(generatePreview(upscaling), "png", fileChooser.getSelectedFile());
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
						JOptionPane.showMessageDialog(PreviewComponent.this, "The image has been saved successfully!");
					}
				}
			});

			frame.add(imageComponent);
			frame.setVisible(true);
		});
	}

	private BufferedImage generatePreview(float upscaling) {
		final ColorSetting background = AbstractSetting.getSetting("Background");
		final ColorSetting text = AbstractSetting.getSetting("Text");
		final ColorSetting structure = AbstractSetting.getSetting("Structure");

		final DropdownSetting<Font> font = AbstractSetting.getSetting("Font");

		final NumberSetting minYear = AbstractSetting.getSetting("start");
		final NumberSetting maxYear = AbstractSetting.getSetting("end");
		final NumberSetting rows = AbstractSetting.getSetting("rows");
		final Setting yearName = AbstractSetting.getSetting("year_name");

		return TimelineGenerator.generate(upscaling,
				background.getColorIcon().getColor(), text.getColorIcon().getColor(), structure.getColorIcon().getColor(),
				font.getSelected().getValue(), (int) minYear.getSpinner().getValue(), (int) maxYear.getSpinner().getValue(), (int) rows.getSpinner().getValue(), yearName.getTextField().getText(),
				Events.events.toArray(new Event[0])
		);
	}

	private static class ImageComponent extends Component {

		private BufferedImage image;
		public ImageComponent(BufferedImage image) {
			setImage(image);
		}

		public BufferedImage getImage() {
			return image;
		}

		public void setImage(BufferedImage image) {
			this.image = image;

			setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));
			setMaximumSize(new Dimension(image.getWidth(), image.getHeight()));
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			setSize(new Dimension(image.getWidth(), image.getHeight()));
		}

		@Override
		public void paint(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}
	}
}
