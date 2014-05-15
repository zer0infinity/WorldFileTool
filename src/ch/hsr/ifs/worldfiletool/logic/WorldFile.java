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
 * The Class WorldFile.
 */
public class WorldFile {
	
	private WorldFileTool_GUI gui;
	private Data data;
	private String input;
	
	/**
	 * Instantiates a new WorldFile.
	 * 
	 * @param guiswing
	 */
	public WorldFile(WorldFileTool_GUI guiswing, Data data, String input) {
		this.gui = guiswing;
		this.data = data;
		this.input = input;
	}
	
	/**
	 * Parse WorldFile.
	 * 
	 * @param path
	 * @param file
	 */
	public void parse_worldfile(String path, File file) {
		data.reset();
		data.get_image_data(file);
		if (data.check_ext(file, new String[] { "jpg", "jpeg" })) {
			parse(data.rename(file.toString(), ".jpe?g", ".jgw"));
		} else if (data.check_ext(file, new String[] { "png" })) {
			parse(data.rename(file.toString(), ".png", ".pgw"));
		} else if (data.check_ext(file, new String[] { "gif" })) {
			parse(data.rename(file.toString(), ".gif", ".gfw"));
		} else if (data.check_ext(file, new String[] { "tif", "tiff" })) {
			parse(data.rename(file.toString(), ".tiff?", ".tfw"));
		}
		if (!(data.getRot_x() == 0 && data.getRot_y() == 0)) {
			final String wait = "wait";
			final String arrow = "arrow";
			try {
				File imagefile = new File(data.getImage());
				File imagefile_ori = new File(data.rename(data.getImage(), "." + data.getExtension(), "_ori." + data.getExtension()));
				imagefile.renameTo(imagefile_ori);
				ImageIO.write(ImageIO.read(imagefile_ori), data.getExtension(), imagefile);
				gui.setCursor(wait);
				ImageIO.write(RotateImage.rotate_vectors(file, data.getRot_x(), data.getRot_y()), data.getExtension(), new File(data.getImage()));
				gui.setCursor(arrow);
				data.calc_new_values(path);
			} catch (IOException e) {
				gui.messagebox("WorldFileTool - " + data.getImage() + " not found",
									"The image should be either in JPG, PNG, GIF or TIFF and in a relative path.");
				return;
			}
		}
		data.setValues();
	}
	
	/**
	 * Parse WorldFile.
	 * 
	 * @param file
	 */
	private void parse(String file) {
		double lon_per_pixel = 0.0;
		double lat_per_pixel = 0.0;
		double upperleft_lon = 0.0;
		double upperleft_lat = 0.0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			lon_per_pixel = Double.valueOf(br.readLine().trim());
			data.setRot_y(Double.valueOf(br.readLine().trim()));
			data.setRot_x(Double.valueOf(br.readLine().trim()));
			lat_per_pixel = Double.valueOf(br.readLine().trim());
			upperleft_lon = Double.valueOf(br.readLine().trim());
			upperleft_lat = Double.valueOf(br.readLine().trim());
			br.close();
		} catch (FileNotFoundException e) {
			gui.messagebox("WorldFileTool - File not found", e.getMessage());
			return;
		} catch (IOException e) {
			gui.messagebox("WorldFileTool - Could not read file", e.getMessage());
			return;
		}
		if (!(-180 < upperleft_lon && upperleft_lon < 180) || !(-90 < upperleft_lat && upperleft_lat < 90)) {
			gui.messagebox("WorldFileTool - Wrong coordinate system", "Convert your coordinates to the geographic coordinate system and retry");
		} else {
			data.setNorth(String.valueOf(upperleft_lat));
			data.setWest(String.valueOf(upperleft_lon));
			data.setEast(String.valueOf(upperleft_lon + data.getImage_width()*lon_per_pixel));
			data.setSouth(String.valueOf(upperleft_lat + data.getImage_height()*lat_per_pixel));
		}
	}
	
	/**
	 * write worldfile.
	 * 
	 * @param file
	 */
	public void write_wf(File file) {
		double upperleft_lat = Double.valueOf(data.getNorth());
		double upperleft_lon = Double.valueOf(data.getWest());
		double lon_per_pixel = (Double.valueOf(data.getEast()) - upperleft_lon)/ data.getImage_width();
		double lat_per_pixel = (Double.valueOf(data.getSouth()) - upperleft_lat)/ data.getImage_height();
		input =
			lon_per_pixel + "\n" +
			0 + "\n" +
			0 + "\n" +
			lat_per_pixel + "\n" +
			upperleft_lon + "\n" +
			upperleft_lat + "\n";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(input);
			bw.close();
		} catch (IOException e) {
			gui.messagebox("WorldFileTool - File not saved", e.getMessage());
			return;
		}
		gui.messagebox("WorldFileTool - File saved", "Successfully exported to " + file);
	}
}
