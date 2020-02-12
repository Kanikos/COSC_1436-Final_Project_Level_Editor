package com.kanikos.editor.comp.tab;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.kanikos.editor.comp.Viewer;
import com.kanikos.editor.graphics.Sprite;
import com.kanikos.editor.graphics.Spritesheet;
import com.kanikos.editor.level.Chunk;
import com.kanikos.editor.level.Tile;
import com.kanikos.editor.util.Palette;

public class ChunkEditor extends Tab {
	// menu items
	private JMenuItem
		file_loadSpritesheet
	;
	
	// level editor variables
	private Chunk chunk;
	private int width, height;
	
	// sprite properties variables
	private Viewer spritePreview;
	
	private JCheckBox prop_solid, prop_flipX, prop_flipY, prop_flipD;
	private JButton[] paletteButtons;
	
	// sprite selection variables
	private Spritesheet spritesheet;
	private Tile tile;
	
	private JPanel scrollPanel;
	private JButton[] spriteButtons;
	
	public ChunkEditor() {
		super("Chunk Editor");
		width = Chunk.WIDTH * Sprite.DIMENSIONS;
		height = Chunk.HEIGHT * Sprite.DIMENSIONS; 
		
		externalViewer.setResolution(width, height);
			
		/* initialize the selected tile, chunk, etc */
		chunk = new Chunk();
		tile = new Tile();
		
		chunk.render(spritesheet, externalViewer.pixels, width);
		
		/* row 0: toolbars */
		setConstraints(INSETS_00, 0, 0, 1, 1, 1D, 0, GridBagConstraints.HORIZONTAL);
		
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new GridLayout(0, 1, 0, 0));
		add(toolbar, CONSTRAINTS);
		
		JMenuBar menuBar = new JMenuBar();
		toolbar.add(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		file_loadSpritesheet = new JMenuItem("Load Spritesheet");
		file_loadSpritesheet.addActionListener(parent);
		fileMenu.add(file_loadSpritesheet);
		
		/* row 2: sprite preview and info panel */
		CONSTRAINTS.gridy = 1;
		
		JPanel spriteInfo = new JPanel();
		spriteInfo.setBackground(Color.DARK_GRAY);
		spriteInfo.setLayout(new GridBagLayout());
		add(spriteInfo, CONSTRAINTS);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = INSETS_05;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;

		constraints.weightx = 0D;
			
		// shows the current selected sprite with its current properties
		spritePreview = new Viewer(16, 16, 200, 200);
		spriteInfo.add(spritePreview, constraints);
		
		// properties buttons
		constraints.gridheight = 1;
		constraints.weightx = .25D;
		constraints.weighty = .50D;
		
		constraints.gridx = 1;
		prop_solid = new JCheckBox("Solid");
		prop_solid.addActionListener(parent);
		spriteInfo.add(prop_solid, constraints);
		
		constraints.gridx = 2;
		prop_flipX = new JCheckBox("Flip X");
		prop_flipX.addActionListener(parent);
		spriteInfo.add(prop_flipX, constraints);
		
		constraints.gridx = 3;
		prop_flipY = new JCheckBox("Flip Y");
		prop_flipY.addActionListener(parent);
		spriteInfo.add(prop_flipY, constraints);
		
		constraints.gridx = 4;
		prop_flipD = new JCheckBox("Flip D");
		prop_flipD.addActionListener(parent);
		spriteInfo.add(prop_flipD, constraints);
		
		// palette buttons
		constraints.gridy = 1;
		paletteButtons = new JButton[Palette.LIMIT];
		
		for(int i = 0; i < Palette.LIMIT; i++) {
			constraints.gridx = i + 1;
			
			JButton button = new JButton(chunk.palette.toImageIcon(i, 32));
			button.addActionListener(parent);
			spriteInfo.add(button, constraints);
			
			paletteButtons[i] = button;
		}
		
		/* row 3: sprite selection scroll panel */
		scrollPanel = new JPanel();
		scrollPanel.setBackground(Color.BLACK);
		scrollPanel.setLayout(new GridLayout(0, 8, 5, 5));
		
		setConstraints(INSETS_00, 0, 3, 1, 1, 1D, 1D, GridBagConstraints.BOTH);
		JScrollPane scrollPane = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, CONSTRAINTS);
	}
	
	private static final byte OPEN_FILE_DIALOG = 0;
	private static final byte SAVE_FILE_DIALOG = 1;
	
	public File browseForFile(byte dialog) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int choice;
		if(dialog == OPEN_FILE_DIALOG) {
			choice = fileChooser.showOpenDialog(null);
		}
		else {
			choice = fileChooser.showSaveDialog(null);
		}
		
		if(choice == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		
		return null;
	}
	
	private void updatePreview() {
		spritesheet.getSprite(tile.getID()).render(chunk.palette, spritePreview.pixels, Sprite.DIMENSIONS, tile, 0, 0);
		spritePreview.repaint();
	}
	
	// listeners
	public void keyPressed(KeyEvent e) {
		Object src = e.getSource();
	}
	
	public void mousePressed(MouseEvent e) {
		Object src = e.getSource();
	}
	
	public void mouseDragged(MouseEvent e) {
		Object src = e.getSource();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if(src == file_loadSpritesheet) {
			File spritesheetFile = browseForFile(OPEN_FILE_DIALOG);
			
			if(spritesheetFile == null) {
				return;
			}
			
			spritesheet = new Spritesheet(spritesheetFile.getAbsolutePath());
			spriteButtons = new JButton[spritesheet.getLength()];
			
			for(int i = 0; i < spritesheet.getLength(); i++) {
				JButton button = new JButton();
				button.addActionListener(parent);
				button.setIcon(spritesheet.getSprite(i).toImageIcon(2));
				scrollPanel.add(button);
				
				spriteButtons[i] = button;
			}
			
			scrollPanel.revalidate();
		}
		else {
			for(int i = 0; i < paletteButtons.length; i++) {
				if(src == paletteButtons[i]) {
					Color selectedColor = JColorChooser.showDialog(null, "Choose Color", new Color(chunk.palette.getColor(i)), false);
					
					if(selectedColor == null) {
						return;
					}
					
					chunk.palette.setColor(i, selectedColor.getRGB());
					paletteButtons[i].setIcon(chunk.palette.toImageIcon(i, 32));
				}
			}
		}
		
		updatePreview();
	}
}
