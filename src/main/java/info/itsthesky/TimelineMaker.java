package info.itsthesky;

import com.formdev.flatlaf.FlatDarculaLaf;
import info.itsthesky.screens.CoreFrame;
import info.itsthesky.util.FileManager;

import javax.swing.*;
import java.io.IOException;

public class TimelineMaker {

	private static CoreFrame screen;
	public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException {
		FlatDarculaLaf.setup();
		UIManager.setLookAndFeel(new FlatDarculaLaf());

		screen = new CoreFrame();
		screen.setSize(800, 620);

		FileManager.init();

		screen.setVisible(true);
	}

	public static JFrame getScreen() {
		return screen;
	}
}
