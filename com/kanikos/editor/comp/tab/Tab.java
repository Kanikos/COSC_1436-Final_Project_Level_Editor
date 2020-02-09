package com.kanikos.editor.comp.tab;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import com.kanikos.editor.comp.ControlPanel;
import com.kanikos.editor.comp.Viewer;

public abstract class Tab extends JPanel {
	private static final long serialVersionUID = 6279593733092286960L;

	protected static final Insets INSETS_00 = new Insets(0, 0, 0, 0);
	protected static final Insets INSETS_05 = new Insets(5, 5, 5, 5);
	
	protected static final GridBagConstraints CONSTRAINTS = new GridBagConstraints();
	protected static final Dimension DIMENSIONS = new Dimension(560, 720);
	
	public static ControlPanel container;
	public static Viewer externalViewer;
	
	public final String NAME;
	
	public Tab(String name) {
		NAME = name;
		
		setLayout(new GridBagLayout());
		setBackground(Color.DARK_GRAY);
		setPreferredSize(DIMENSIONS);
	}
	
	// utility 
	public void setConstraints(Insets insets, int x, int y, int width, int height, double weightX, double weightY, int fill) {
		CONSTRAINTS.insets = insets;
		CONSTRAINTS.gridx = x;
		CONSTRAINTS.gridy = y;
		CONSTRAINTS.gridwidth = width;
		CONSTRAINTS.gridheight = height;
		CONSTRAINTS.weightx = weightX;
		CONSTRAINTS.weighty = weightY;
		CONSTRAINTS.fill = fill;
	}
	
	// methods sub-classes have to implement
	public abstract void keyPressed(KeyEvent e);
	public abstract void mousePressed(MouseEvent e);
	public abstract void mouseDragged(MouseEvent e);
	public abstract void actionPerformed(ActionEvent e);
}
