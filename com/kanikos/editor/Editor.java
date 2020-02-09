package com.kanikos.editor;

import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.kanikos.editor.comp.ControlPanel;
import com.kanikos.editor.comp.Viewer;

public class Editor {
	public Editor() {
		// initialize frame and the container panel
		JFrame frame = new JFrame("ASM 2 - Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.DARK_GRAY);
		frame.add(panel);
		
		// column 1: viewer
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		
		Viewer viewer = new Viewer(700, 700);
		panel.add(viewer, constraints);
		
		// column 2: level editor control panel
		constraints.insets = new Insets(0, 0, 0, 0);
		
		ControlPanel controlPanel = new ControlPanel(viewer);
		panel.add(controlPanel, constraints);
		
		// make frame visible
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Editor();
	}
}
