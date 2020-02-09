package com.kanikos.editor.comp;

import java.awt.Component;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JTabbedPane;
import com.kanikos.editor.comp.tab.*;


public class ControlPanel extends JTabbedPane implements KeyListener, MouseListener, ActionListener, MouseMotionListener {
	private static final long serialVersionUID = -8887205039527493579L;

	public ControlPanel(Viewer externalViewer) {
		// externalViewer setup 
		externalViewer.addMouseListener(this);
		externalViewer.addMouseMotionListener(this);
		
		// Tab class setup
		Tab.container = this;
		Tab.externalViewer = externalViewer;
		
		// level editor
		addTab(new ChunkEditor());
	}
	
	// utility 
	public Tab getSelectedTab() {
		Component comp = getSelectedComponent();
		
		if(comp instanceof Tab) {
			return (Tab) comp;
		}
		
		return null;
	}
	
	public void addTab(Tab tab) {
		addTab(tab.NAME, tab);
	}
	
	// listeners 
	public void keyPressed(KeyEvent e) {
		Tab selectedTab = getSelectedTab();
		
		if(selectedTab == null) {
			return;
		}
		
		selectedTab.keyPressed(e);
	} 
	
	public void mousePressed(MouseEvent e) {
		Tab selectedTab = getSelectedTab();
		
		if(selectedTab == null) {
			return;
		}
		
		selectedTab.mousePressed(e);
	}
	
	public void mouseDragged(MouseEvent e) {
		Tab selectedTab = getSelectedTab();
		
		if(selectedTab == null) {
			return;
		}
		
		selectedTab.mouseDragged(e);
	}
	
	public void actionPerformed(ActionEvent e) {
		Tab selectedTab = getSelectedTab();
		
		if(selectedTab == null) {
			return;
		}
		
		selectedTab.actionPerformed(e);
	}
	
	// unused methods that had to be implemented
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void mouseMoved(MouseEvent e) {}
}
