package ch.hsr.ifs.worldfiletool.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import ch.hsr.ifs.worldfiletool.ui.WorldFileTool_GUI;
import ch.hsr.ifs.worldfiletool.util.RotateImage;

/**
 * The Class KML.
 */
public class KML {
	
	private WorldFileTool_GUI gui;
	private Data data;
	private String input;
	
	/**
	 * Instantiates a new KML.
	 * 
	 * @param guiswing
	 */
	public KML(WorldFileTool_GUI guiswing, Data data, String input) {
		this.gui = guiswing;
		this.data = data;
		this.input = input;
	}

	/**
	 * Parse KML.
	 * 
	 * @param path
	 * @param file
	 */
	public void parse_kml(final String path, File file) {
		data.reset();
		String line;
		boolean foundImage = false;
		StringBuffer temp = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null) {
				temp.append(line + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			gui.messagebox("File Not Found", e.getMessage());
		} catch (IOException e) {
			System.out.println("read kml " + e);
		}
		input = temp.toString();
		data.setName(extract("<name>", "</name>").trim());
		data.setColor(extract("<color>", "</color>").trim());
		data.setView(extract("<viewBoundScale>", "</viewBoundScale>").trim());
		data.setImage(extract("<href>", "</href>").trim());
		data.setNorth(extract("<north>", "</north>").trim());
		data.setWest(extract("<west>", "</west>").trim());
		data.setSouth(extract("<south>", "</south>").trim());
		data.setEast(extract("<east>", "</east>").trim());
		data.setRotation(extract("<rotation>", "</rotation>").trim());
		final String[] ext = { "jpg", "jpeg", "png", "gif", "tif", "tiff" };
		if(data.check_ext(new File(data.getImage()), ext)) {
			data.get_image_data(new File(path + File.separator + data.getImage()));
			foundImage = true;
		}
		if(!foundImage) {
			gui.messagebox("WorldFileTool - " + data.getImage() + " not found",
						"The image should be either in JPG, PNG, GIF or TIFF and in a relative path.");
		} else if(foundImage && !data.getRotation().isEmpty() && !data.getRotation().equals("0")) {
			final String wait = "wait";
			final String arrow = "arrow";
			try {
				if (!data.getNorth().isEmpty() && !data.getSouth().isEmpty() && !data.getWest().isEmpty() && !data.getEast().isEmpty()) {
					File imagefile = new File(path + File.separator + data.getImage());
					File imagefile_ori = new File(data.rename(path + File.separator + data.getImage(), "." + data.getExtension(), "_ori." + data.getExtension()));
					imagefile.renameTo(imagefile_ori);
					ImageIO.write(ImageIO.read(imagefile_ori), data.getExtension(), imagefile);
					gui.setCursor(wait);
					ImageIO.write(RotateImage.rotate_angle(new File(path + File.separator + data.getImage()),
							Double.valueOf(data.getRotation())), data.getExtension(), new File(path + File.separator + data.getImage()));
					gui.setCursor(arrow);
					data.calc_new_values(path);
				} else {
					gui.messagebox("Verify your coordinates",
							"Could not read coordinates from kml therefore no rotation has been made");
				}
			} catch (IOException e) {
				gui.messagebox("WorldFileTool - " + data.getImage() + " not found",
									"The image should be either in JPG, PNG, GIF or TIFF and in a relative path.");
				return;
			}
		}
		data.setValues();
	}
	
	
	/**
	 * extract file input.
	 * 
	 * @param start start tag
	 * @param end end tag
	 * 
	 * @return string
	 */
	private String extract(String start, String end) {
		String str = "";
		if (input.contains(start)) {
			String[] split = input.split(start);
			split = split[1].split(end);
			str = split[0];
		}
		return str;
	}
	
	/**
	 * Write KML.
	 * 
	 * @param file
	 */
	public void write_kml(File file) {
		if (data.getColor().isEmpty()) {
			data.setColor("cfffffff");
		}
		if (data.getView().isEmpty()) {
			data.setView("0.75");
		}
		input = 
			"<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
			"<kml xmlns=\"http://earth.google.com/kml/2.2\">" + "\n" +
			"<GroundOverlay>" + "\n" +
			"    <name>" + data.getName() + "</name>" + "\n" +
			"    <color>" + data.getColor() + "</color>" + "\n" +
			"    <Icon>" + "\n" +
			"        <href>" + data.getImage() + "</href>" + "\n" +
			"        <viewBoundScale>" + data.getView() + "</viewBoundScale>" + "\n" +
			"    </Icon>" + "\n" +
			"    <LatLonBox>" + "\n" +
			"        <north>" + data.getNorth() + "</north>" + "\n" +
			"        <west>" + data.getWest() + "</west>" + "\n" +
			"        <south>" + data.getSouth() + "</south>" + "\n" +
			"        <east>" + data.getEast() + "</east>" + "\n" +
			"    </LatLonBox>" + "\n" +
			"    <Metadata>" + "\n" +
			"        <floor>" + data.getFloor() + "</floor>" + "\n" +
			"        <background>" + data.getMaptype() + "</background>" + "\n" +
			"        <priority>" + data.getPriority() + "</priority>" + "\n" +
			"    </Metadata>" + "\n" +
			"</GroundOverlay>" + "\n" +
			"</kml>" + "\n";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(input);
			bw.close();
		} catch (IOException e) {
			gui.messagebox("WorldFileTool - File not saved", e.getMessage());
		}
	}
}
