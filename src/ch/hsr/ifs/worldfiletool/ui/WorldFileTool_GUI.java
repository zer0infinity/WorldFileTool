/**
 * This file is part of WorldFileTool.
 * 
 * JMapDesk is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMapDesk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JMapDesk.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ch.hsr.ifs.worldfiletool.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.hsr.ifs.worldfiletool.logic.Data;
import ch.hsr.ifs.worldfiletool.logic.KML;
import ch.hsr.ifs.worldfiletool.logic.WorldFile;

/**
 * The Class GUI_Swing.
 */
public class WorldFileTool_GUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JTextField name;
	private JTextField image;
	private JTextField north;
	private JTextField west;
	private JTextField south;
	private JTextField east;
	private JTextField floor;
	private JComboBox maptype;
	private JTextField priority;
	private JLabel icon;
	private File path = new File(System.getProperty("user.dir"));
	private File file;
	private Properties properties = new Properties();
	private final String lastpath = "lastpath";
	private KML kml;
	private WorldFile wf;
	private Data data;
	private Components comp;
	
	/**
	 * Instantiates a new GUI_Swing.
	 */
	public WorldFileTool_GUI() {
		String input = "";
		data = new Data();
		comp = new Components();
		kml = new KML(this, data, input);
		wf = new WorldFile(this, data, input);
	}
	
	/**
	 * Show GUI.
	 */
	public void show_gui(String version) {
		comp.load(properties);
		setTitle("WorldFileTool " + version);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setSize(285, 430);
	    setIconImage(new ImageIcon(getClass().getResource("/ch/hsr/ifs/worldfiletool/ui/images/icon.jpg")).getImage());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - 250)/2, (dim.height - 600)/2);
		
		panel = new JPanel();
		panel.setLayout(null);
		
		// import kml
	    JButton button_openkml = comp.create_button("Import File...", new Rectangle(15, 310, 120, 25), KeyEvent.VK_I);
	    button_openkml.setMnemonic(KeyEvent.VK_I);
		button_openkml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				import_file();
			}
		});
		// export kml
		JButton button_savekml = comp.create_button("Export as KML...", new Rectangle(145, 310, 120, 25), KeyEvent.VK_K);
		button_savekml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				export_kml();
			}
		});
		// export worldfile
		JButton button_openwf = comp.create_button("Export as World File", new Rectangle(145, 340, 120, 25), KeyEvent.VK_W);
		button_openwf.setFont(new Font("Arial", Font.PLAIN, 9));
		button_openwf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				export_worldfile();
			}
		});
		// exit
		JButton button_exit = comp.create_button("Exit", new Rectangle(15, 340, 120, 25), KeyEvent.VK_E);
		button_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save_and_exit();
			}
		});
		
		JMenuBar menuBar = comp.menu_bar(this);
		setJMenuBar(menuBar);
		
		// Icon next to image
		icon = new JLabel();
		icon.setIcon(new ImageIcon(getClass().getResource("/ch/hsr/ifs/worldfiletool/ui/images/cross.png")));
		icon.setBounds(245, 65, 20, 20);
		icon.setToolTipText("File does not exists");
		// Some Listener
		KeyListener numListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {
				int k = e.getKeyChar();
				/*
				 * 48-57: 0-9
				 * 45: minus
				 * 46: dot
				 */
				if (!(k > 44 && k < 58)) {
					e.setKeyChar((char) KeyEvent.VK_CLEAR);
				}
			}
		};
		KeyListener fileListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				comp.check_image(new File(path + File.separator + image.getText()), icon);
			}
			@Override
			public void keyTyped(KeyEvent e) {}
		};
		// Textfields
		name = comp.create_textfield("Name", new Rectangle(15, 5, 250, 15), panel);
		image = comp.create_textfield("Image", new Rectangle(15, 50, 225, 15), panel);
		image.addKeyListener(fileListener);
		north = comp.create_textfield("North*", new Rectangle(15, 100, 120, 15), panel);
		north.addKeyListener(numListener);
		west = comp.create_textfield("West*", new Rectangle(145, 100, 120, 15), panel);
		west.addKeyListener(numListener);
		south = comp.create_textfield("South*", new Rectangle(15, 140, 120, 15), panel);
		south.addKeyListener(numListener);
		east = comp.create_textfield("East*", new Rectangle(145, 140, 120, 15), panel);
		east.addKeyListener(numListener);
		floor = comp.create_textfield("Floor", new Rectangle(15, 200, 120, 15), panel);
		floor.setText("0");
		floor.addKeyListener(numListener);
		priority = comp.create_textfield("Priority", new Rectangle(15, 240, 120, 15), panel);
		priority.setText("0");
		priority.addKeyListener(numListener);
		JLabel req_worldfile = new JLabel();
		req_worldfile.setBounds(18, 270, 150, 30);
		req_worldfile.setText("*required for world file");
		req_worldfile.setFont(new Font("Arial", Font.PLAIN, 11));
		// MapType
		JLabel lbl_maptype = new JLabel();
		lbl_maptype.setText("Map Type");
		lbl_maptype.setBounds(145, 200, 120, 15);
		lbl_maptype.setFont(new Font("Arial", Font.PLAIN, 11));
		maptype = new JComboBox();
		final String[] type = { "Buildings", "Landscape", "Others" };
		for (int i = 0; i < type.length; i++) {
			maptype.addItem(type[i]);
		}
		maptype.setFont(new Font("Arial", Font.PLAIN, 11));
		maptype.setBounds(145, 215, 120, 19);
		maptype.setSelectedIndex(0);
		
		final JComponent[] elements = { button_openkml, button_savekml, button_openwf, button_exit, icon,
				name, image, north, west, south, east, floor, priority,	req_worldfile, lbl_maptype, maptype};
		for (int i = 0; i < elements.length; i++) {
			panel.add(elements[i]);
		}
		add(panel);
		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				save_and_exit();
			}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
		});
		setVisible(true);
	}
	
	/**
	 * Import File.
	 */
	public void import_file() {
		JFileChooser fc = new JFileChooser() {
			private static final long serialVersionUID = 1L;
			@Override
			public void approveSelection() {
				File file = getSelectedFile();
				if (file.exists() && getDialogType() == SAVE_DIALOG) {
					int result = JOptionPane.showConfirmDialog(
							getTopLevelAncestor(),
							"The selected file already exists. Do you want to overwrite it?",
							"The file already exists",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					switch (result) {
					case JOptionPane.YES_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};
		fc.setDialogTitle("WorldFileTool - Import...");
		fc.setFileFilter(new FileNameExtensionFilter("KML (*.kml) / Image (*.jpg; *.png; *.gif; *.tiff)",
														new String[] { "kml", "jpg", "jpeg", "png", "gif", "tif", "tiff" }));
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (properties.containsKey(lastpath)) {
			fc.setCurrentDirectory(new File(properties.getProperty(lastpath)));
		}
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			path = fc.getCurrentDirectory();
			properties.setProperty(lastpath, path.toString());
			if (file.getName().toLowerCase().endsWith("kml")) {
				clear();
				kml.parse_kml(path.toString(), file);
				update_textfields();
				comp.check_image(new File(path + File.separator + image.getText()), icon);
			} else if (data.check_ext(file, new String[] { "jpg", "jpeg", "png", "gif", "tif", "tiff" })) {
				clear();
				wf.parse_worldfile(path.toString(), file);
				update_textfields();
				comp.check_image(new File(path + File.separator + image.getText()), icon);
			}
		}
	}
	
	/**
	 * Export KML.
	 */
	public void export_kml() {
		if (!floor.getText().contains(".") && !floor.getText().isEmpty()) {
			if (Integer.valueOf(floor.getText()) < -99 || 99 < Integer.valueOf(floor.getText()))  {
				messagebox("WorldFileTool - Not valid Floor", "Choose an integer between -99 and 99 (default: 0)");
				return;
			}
		} else {
			messagebox("WorldFileTool - Not valid Floor", "Choose an integer between -99 and 99 (default: 0)");
			return;
		} 
		if (!priority.getText().contains(".") && !priority.getText().isEmpty() && !priority.getText().contains("-")) {
			if (100 < Integer.valueOf(priority.getText())) {
				messagebox("WorldFileTool - Not valid Priority", "Choose an integer between 1 and 100 or 0 for unknown");
				return;
			}
		} else {
			messagebox("WorldFileTool - Not valid Priority", "Choose an integer between 1 and 100 or 0 for unknown");
			return;
		}
		if (name.getText().isEmpty() || image.getText().isEmpty() || north.getText().isEmpty()
				|| west.getText().isEmpty() || south.getText().isEmpty() || east.getText().isEmpty()) {
			messagebox("WorldFileTool - Empty Textboxes", "Not all Textboxes are filled");
			return;
		}
		if (floor.getText().isEmpty())
			floor.setText("0");
		if (priority.getText().isEmpty())
			priority.setText("0");
		setContent();
		JFileChooser fc = new JFileChooser() {
			private static final long serialVersionUID = 1L;
			@Override
			public void approveSelection() {
				File file = getSelectedFile();
				if (file.exists() && getDialogType() == SAVE_DIALOG) {
	        		if (!file.getName().endsWith("map")) {
	        			file = new File(file.toString() + ".kml");
	        		}
					int result = JOptionPane.showConfirmDialog(
							getTopLevelAncestor(),
							"The selected file already exists. Do you want to overwrite it?",
							"The file already exists",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					switch (result) {
					case JOptionPane.YES_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith("kml") || f.isDirectory();
			}
			@Override
			public String getDescription() {
				return "Google Earth KML-File (*.kml)";
			}
		});
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogTitle("Save as KML");
		if (properties.containsKey(lastpath)) {
			fc.setCurrentDirectory(new File(properties.getProperty(lastpath)));
		}
		final String[] ext = { "jpg", "jpeg", "png", "gif", "tif", "tiff" };
		if (file != null) {
			for (int i = 0; i < ext.length; i++) {
				if (file.getName().toLowerCase().endsWith(ext[i])) {
					fc.setSelectedFile(new File(Pattern.compile("."+ext[i], Pattern.CASE_INSENSITIVE).matcher(file.toString()).replaceAll(".kml")));
					break;
				} else {
					fc.setSelectedFile(new File(file.toString()));
				}
			}
		}
		int result = fc.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if (file.toString().endsWith(".kml")) {
				kml.write_kml(file);
			} else {
				kml.write_kml(new File(file.getName() + ".kml"));
			}
		}
	}
	
	/**
	 * Export WorldFile.
	 */
	public void export_worldfile() {
		if (north.getText().isEmpty() || west.getText().isEmpty() || south.getText().isEmpty() || east.getText().isEmpty()) {
			messagebox("WorldFileTool - Empty Textboxes", "Not all required Textboxes for a WorldFile are filled");
			return;
		}
		setContent();
		final String file = data.rename(data.getImage(), "." + data.getExtension(), "." + data.getWorldFileExt());
		if (path != null) {
			if (!new File(path + File.separator + file).exists()) {
				wf.write_wf(new File(path + File.separator + file));
			} else if (data.getExtension().isEmpty() && data.getWorldFileExt().isEmpty()) {
				wf.write_wf(new File(path + File.separator + image.getText()));
			} else {
				int result = JOptionPane.showConfirmDialog(null, "The file already exists. Do you want to overwrite it?",
						"WorldFileTool - File already exists", JOptionPane.YES_NO_OPTION);
				switch(result) {
				case JOptionPane.YES_OPTION:
					wf.write_wf(new File(path + File.separator + file));
				case JOptionPane.NO_OPTION:
					return;
				}
			}
		} else {
			messagebox("WorldFileTool - File not saved", "Could not save " + file);
		}
	}
	
	/**
	 * Clear Textfields.
	 */
	private void clear() {
		name.setText("(unnamed)");
		image.setText("");
		north.setText("");
		west.setText("");
		south.setText("");
		east.setText("");
	}
	
	/**
	 * Update Textfields.
	 */
	private void update_textfields() {
		name.setText(data.getName());
		image.setText(data.getImage());
		north.setText(data.getNorth());
		west.setText(data.getWest());
		south.setText(data.getSouth());
		east.setText(data.getEast());
	}
	
	/**
	 * prepare to write.
	 */
	private void setContent() {
		data.setName(name.getText().trim());
		data.setImage(image.getText().trim());
		data.setNorth(north.getText().trim());
		data.setWest(west.getText().trim());
		data.setSouth(south.getText().trim());
		data.setEast(east.getText().trim());
		data.setFloor(floor.getText().trim());
		data.setMaptype(maptype.getSelectedItem().toString());
		if (data.getMaptype().equalsIgnoreCase("Buildings")) {
			data.setMaptype("0");
		} else if (data.getMaptype().equalsIgnoreCase("Landscape")) {
			data.setMaptype("1");
		} else if (data.getMaptype().equalsIgnoreCase("Others")){
			data.setMaptype("2");
		}
		data.setPriority(priority.getText().trim());
	}
	
	/**
	 * save properties and exit.
	 */
	void save_and_exit() {
		comp.save(properties);
		String type = "";
		if (maptype.getSelectedItem().equals("Buildings")) {
			type = "0";
		} else if (maptype.getSelectedItem().equals("Landscape")) {
			type = "1";
		} else if (maptype.getSelectedItem().equals("Others")){
			type = "2";
		}
		String[] values = { name.getText(), image.getText(),
				north.getText(), west.getText(), south.getText(), east.getText(),
				floor.getText(), type, priority.getText() };
		int result = 0;
		if (!Arrays.asList(data.getValues()).containsAll(Arrays.asList(values))) {
			result = JOptionPane.showConfirmDialog(null, "Data has changed. Do you really want to quit?",
					"WorldFileTool - Quit?", JOptionPane.YES_NO_OPTION);
		}
		switch(result) {
		case JOptionPane.YES_OPTION:
			System.exit(0);
		case JOptionPane.NO_OPTION:
			return;
		}
	}
	
	/**
	 * Error Message.
	 * 
	 * @param text
	 * @param message
	 */
	public void messagebox(String text, String message) {
		JOptionPane.showMessageDialog(null, message, text, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * set cursor in swing.
	 * 
	 * @param cursor the new cursor
	 */
	public void setCursor(String cursor) {
		if (cursor == "wait") {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
		} else if (cursor == "arrow") {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
