package me.jupdyke01.mazegenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

	Main main;

	public ButtonListener(Main main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Main.speed) {
			if (main.framerate == 5.0) {
				main.framerate = 10.0;
				Main.speed.setText("Speed - 10");
				main.delta = 1;
			} else if (main.framerate == 10.0) {
				main.framerate = 20.0;
				Main.speed.setText("Speed - 20");
				main.delta = 1;
			} else if (main.framerate == 20.0) {
				main.framerate = 50.0;
				Main.speed.setText("Speed - 50");
				main.delta = 1;
			} else if (main.framerate == 50.0) {
				main.framerate = 100.0;
				Main.speed.setText("Speed - 100");
				main.delta = 1;
			} else if (main.framerate == 100.0) {
				main.framerate = 100000000.0;
				Main.speed.setText("Speed - Max");
				main.delta = 1;
			} else if (main.framerate == 100000000.0) {
				main.framerate = 10.0;
				Main.speed.setText("Speed - 5");
				main.delta = 1;
			}
		} else if (e.getSource() == Main.export) {
			main.export();
			//main.running = false;
			//main.frame.dispose();
			//new Main();
		}
	}

}
