package me.jupdyke01.mazegenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Selection extends JFrame {

	private static final long serialVersionUID = -8289301712096706915L;
	private JTextField width;
	private JTextField dimension;

	public Selection() {
		setBackground(Color.DARK_GRAY);
		setResizable(false);
		setTitle("Selection");
		setPreferredSize(new Dimension(300, 100));
		setLocationRelativeTo(null);
		
		JButton go = new JButton("Go!");
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Main(Integer.parseInt(dimension.getText()), Integer.parseInt(width.getText()));
			}
		});
		getContentPane().add(go, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		
		dimension = new JTextField();
		dimension.setText("X,Y Dimension");
		dimension.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(dimension);
		dimension.setColumns(10);
		
		width = new JTextField();
		width.setText("Column Width");
		width.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(width);
		width.setColumns(10);
		pack();
		setVisible(true);
	}
	

	public static void main(String[] args) {
		new Selection();
	}
	
}
