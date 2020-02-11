package com.kanikos.editor.comp.tab;

import java.io.File;
import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.kanikos.editor.comp.Viewer;
import com.kanikos.editor.graphics.Sprite;
import com.kanikos.editor.graphics.Spritesheet;
import com.kanikos.editor.level.Chunk;
import com.kanikos.editor.level.Tile;
import com.kanikos.editor.serial.Deserializer;
import com.kanikos.editor.serial.Serializer;
import com.kanikos.editor.util.Coordinate;
import com.kanikos.editor.util.Palette;

public class ChunkEditor extends Tab {
	private static final long serialVersionUID = 2994596565165783465L;
	
	// TODO - rewrite this class
	
	// chunk variables
	private Chunk chunk = new Chunk();
	
	// menu bar variables 
	private JMenuBar menuBar;
	
	private JMenuItem file_loadSpritesheet;
	private JMenuItem file_saveChunk;
	private JMenuItem file_loadChunk;
	
	private JMenuItem edit_clearChunk;
	
	// editor controls
	private JButton clearButton;
	
	// sprite selection variables
	private Spritesheet spritesheet;
	private Tile tile = new Tile();
	
	private JPanel scrollPanel;
	private JButton[] spriteButtons;
	
	// sprite information and properties variables
	private Viewer spritePreview;
	
	private BufferedImage[] colorPalettePreview;
	private JButton[] colorPaletteButtons;
	
	private JCheckBox prop_solid, prop_flipX, prop_flipY, prop_flipD;
	
	public ChunkEditor() {
		super("Chunk Editor");
		
		// set up chunk editor
		externalViewer.setResolution(Chunk.WIDTH * Sprite.DIMENSIONS, Chunk.HEIGHT * Sprite.DIMENSIONS);
		
		chunk.render(spritesheet, externalViewer.pixels, Chunk.WIDTH * Sprite.DIMENSIONS);
		externalViewer.repaint();
		
		// row 0 - Menu Bar
		setConstraints(INSETS_00, 0, 0, 5, 1, 1D, 0D, GridBagConstraints.HORIZONTAL);
		
		menuBar = new JMenuBar();
		add(menuBar, CONSTRAINTS);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		file_loadSpritesheet = createMenuItem(fileMenu, "Load Spritesheet");
		file_saveChunk = createMenuItem(fileMenu, "Save Chunk");
		file_loadChunk = createMenuItem(fileMenu, "Load Chunk");
		
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		edit_clearChunk = createMenuItem(editMenu, "Clear Chunk");
		
		// row 1 ~ 3 - sprite information
		// col 0 - sprite preview
		setConstraints(INSETS_05, 0, 1, 1, 3, 1D, 0D, GridBagConstraints.NONE);
		
		spritePreview = new Viewer(16, 16, 200, 200);
		add(spritePreview, CONSTRAINTS);
		
		// row 1:
		// col 1 ~ 4 editor controls
		setConstraints(INSETS_05, 0, 1, 1, 1, 1D, 0D, GridBagConstraints.NONE);
		
		CONSTRAINTS.gridx = 1;
		clearButton = createButton(this, "Clear Button");
		
		// row 2
		// col 1 ~ 4 - properties boxes
		setConstraints(INSETS_05, 0, 2, 1, 1, 1D, 0D, GridBagConstraints.NONE);
		
		CONSTRAINTS.gridx = 1;
		prop_solid = createCheckBox(this, "Solid");
		
		CONSTRAINTS.gridx = 2;
		prop_flipX = createCheckBox(this, "Flip X");
		
		CONSTRAINTS.gridx = 3;
		prop_flipY = createCheckBox(this, "Flip Y");
		
		CONSTRAINTS.gridx = 4;
		prop_flipD = createCheckBox(this, "Flip D"); 
		
		// row 3:
		// col 1 ~ 4 - color palette buttons
		setConstraints(INSETS_05, 0, 3, 1, 1, 1D, 0D, GridBagConstraints.NONE);
		
		colorPalettePreview = new BufferedImage[4];
		colorPaletteButtons = new JButton[4];
		
		for(int i = 0; i < Palette.LIMIT; i++) {
			CONSTRAINTS.gridx = i + 1;
			
			colorPalettePreview[i] = createSolidImage(32, chunk.palette.getColor(i));
			colorPaletteButtons[i] = createButton(this, colorPalettePreview[i]);
		}
		
		// row 4 - sprite selector pane
		setConstraints(INSETS_00, 0, 4, 5, 1, 1D, 1D, GridBagConstraints.BOTH);
		
		scrollPanel = new JPanel();
		scrollPanel.setBackground(Color.BLACK);
		scrollPanel.setLayout(new GridLayout(0, 8, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		add(scrollPane, CONSTRAINTS);
	}

	// utility
	private static final boolean OPEN_FILE_DIALOG = false;
	private static final boolean SAVE_FILE_DIALOG = true;
	
	public File browseForFile(String title, FileNameExtensionFilter filter, boolean dialog) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle(title);
		
		int choice;
		if(dialog) {
			choice = fileChooser.showSaveDialog(null);
		}
		else {
			choice = fileChooser.showOpenDialog(null);
		}
		
		if(choice == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} 
		
		return null;
	}
	
	private JButton createButton(JPanel panelContainer, Sprite sprite, int scale) {
		JButton button = new JButton();
		button.addActionListener(container);
		button.setPreferredSize(new Dimension(Sprite.DIMENSIONS * scale, Sprite.DIMENSIONS * scale));
		
		// create the icon
		BufferedImage image = new BufferedImage(Sprite.DIMENSIONS, Sprite.DIMENSIONS, BufferedImage.TYPE_INT_RGB);
		image.flush();
		
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		sprite.render(chunk.palette, pixels, Sprite.DIMENSIONS, tile, 0, 0);
		
		// scale the image;
		Image scaledImage = image.getScaledInstance(image.getWidth() * scale, image.getHeight() * scale, Image.SCALE_REPLICATE);
		scaledImage.flush();
		
		// create the icon, and set it to the button, and return the button
		button.setIcon(new ImageIcon(scaledImage));
		panelContainer.add(button);
		return button;
	}
	
	private JButton createButton(JPanel panelContainer, BufferedImage image) {
		JButton button = new JButton();
		button.addActionListener(container);
		button.setIcon(new ImageIcon(image));
		
		panelContainer.add(button, CONSTRAINTS);
		
		return button;
	}
	
	private JButton createButton(JPanel panelContainer, String label) {
		JButton button = new JButton(label);
		button.addActionListener(container);
		
		panelContainer.add(button, CONSTRAINTS);
		return button;
		
	}
	
	private BufferedImage createSolidImage(int dimensions, int color) {
		BufferedImage image = new BufferedImage(dimensions, dimensions, BufferedImage.TYPE_INT_RGB);
		fillImage(image, color);
		return image;
	}
	
	private void fillImage(BufferedImage image, int color) {
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				image.setRGB(x, y, color);
			}
		}
	}

	private JMenuItem createMenuItem(JMenu menuContainer, String label) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(container);
		
		menuContainer.add(item);
		return item;
	}
	
	private JCheckBox createCheckBox(JPanel panelContainer, String label) {
		JCheckBox checkBox = new JCheckBox(label);
		checkBox.addActionListener(container);
		
		panelContainer.add(checkBox, CONSTRAINTS);
		
		return checkBox;
	}
	
	private void updatePallet(int index, int color) {
		if(chunk.palette.getColor(index) != color) {
			chunk.palette.setColor(index, color);
		}
		
		
		fillImage(colorPalettePreview[index], color);
		colorPaletteButtons[index].repaint();
	}
	
	private void updateChunk() {
		chunk.render(spritesheet, externalViewer.pixels, Chunk.WIDTH * Sprite.DIMENSIONS);
		externalViewer.repaint();
	}
	
	private void updatePreview() {
		Sprite sprite = spritesheet.getSprite(tile.getID());
		sprite.render(chunk.palette, spritePreview.pixels, Sprite.DIMENSIONS, tile, 0, 0);
		
		spritePreview.repaint();
	}
	
	// listeners
	public void keyPressed(KeyEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		Object src = e.getSource();
		
		if(src == externalViewer && spritesheet != null) {
			Coordinate coordinate = externalViewer.getCoordinate(e.getX(), e.getY());
			
			if(coordinate == null) {
				return;
			}
			
			coordinate.x /= Sprite.DIMENSIONS;
			coordinate.y /= Sprite.DIMENSIONS;
			
			Tile coordinateTile = chunk.getTile(coordinate.x, coordinate.y);
			if(tile.equals(coordinateTile)) {
				return;
			}
			
			chunk.setTile(tile, coordinate.x, coordinate.y);
			
			coordinate.x *= Sprite.DIMENSIONS;
			coordinate.y *= Sprite.DIMENSIONS;
			
			Sprite sprite = spritesheet.getSprite(tile.getID());
			sprite.render(chunk.palette, externalViewer.pixels, Chunk.WIDTH * Sprite.DIMENSIONS, tile, coordinate.x, coordinate.y);
			externalViewer.repaint();
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		mousePressed(e);
	} 
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src == prop_solid) {
			tile.flipFlag(Tile.FLAG_SOLID);
		}
		else if(src == prop_flipX) {
			tile.flipFlag(Tile.FLAG_FLIPX);
		}
		else if(src == prop_flipY) {
			tile.flipFlag(Tile.FLAG_FLIPY);
		}
		else if(src == prop_flipD) {
			tile.flipFlag(Tile.FLAG_FLIPD);
		}
		else {
			if(spritesheet == null) {
				if(src == file_loadSpritesheet) {
					File file = browseForFile("Open - Spritesheet", new FileNameExtensionFilter(".png, .jpeg, .jpg", "png", "jpg", "jpeg"), OPEN_FILE_DIALOG);
					
					if(file == null) {
						return;
					}
					
					spritesheet = new Spritesheet(file.getAbsolutePath());
					spriteButtons = new JButton[spritesheet.getLength()];
					
					for(int i = 0; i < spritesheet.getLength(); i++) {
						spriteButtons[i] = createButton(scrollPanel, spritesheet.getSprite(i), 3);
					}
					
					scrollPanel.revalidate();
				}
			}
			else {
				if(src == file_saveChunk) {
					File file = browseForFile("Save - Chunk", new FileNameExtensionFilter(".lvl", "lvl"), SAVE_FILE_DIALOG);
					
					if(file == null) {
						return;
					}
					
					Serializer serializer = new Serializer();
					chunk.serialize(serializer);
					serializer.saveTo(file.getAbsolutePath());
				}
				else if(src == file_loadChunk) {
					File file = browseForFile("Open - Chunk", new FileNameExtensionFilter(".lvl", "lvl"), OPEN_FILE_DIALOG);
					
					if(file == null) {
						return;
					}
					
					Deserializer deserializer = new Deserializer(file.getAbsolutePath());
					chunk = new Chunk();
					chunk.deserialize(deserializer);
					
					for(int i = 0; i < 4; i++) {
						updatePallet(i, chunk.palette.getColor(i));
					}
				}	
				else {
					for(int i = 0; i < colorPaletteButtons.length; i++) {
						if(src == colorPaletteButtons[i]) {
							Color choosenColor = JColorChooser.showDialog(null, "Chooser Color", new Color(chunk.palette.getColor(i)), false);
							
							if(choosenColor == null) {
								return;
							}
							
							updatePallet(i, choosenColor.getRGB());
							break;
						}
					}
					
					for(short i = 0; i < spriteButtons.length; i++) {
						if(src == spriteButtons[i]) {
							tile.setID(i);
							break;
						}
					}
				}
			}
		}
		
		if(spritesheet != null) {
			updateChunk();
			updatePreview();
		}
		
		repaint();
	}
}
