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

package ch.hsr.ifs.worldfiletool.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

/**
 * The Class Data.
 */
public class Data {
	
	private String name = "";
	private String color = "";
	private String image = "";
	private String view = "";
	private String north = "";
	private String west = "";
	private String south = "";
	private String east = "";
	private String floor = "0";
	private String maptype = "0";
	private String priority = "0";
	private double rot_y = 0.0;
	private double rot_x = 0.0;
	private double image_height = 0.0;
	private double image_width = 0.0;
	private String ext = "";
	private String wf_ext = "";
	private String rotation = "0";
	private String[] values = { name, image, north, west, south, east, floor, maptype, priority };
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getNorth() {
		return north;
	}
	public void setNorth(String north) {
		this.north = north;
	}
	public String getWest() {
		return west;
	}
	public void setWest(String west) {
		this.west = west;
	}
	public String getSouth() {
		return south;
	}
	public void setSouth(String south) {
		this.south = south;
	}
	public String getEast() {
		return east;
	}
	public void setEast(String east) {
		this.east = east;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getMaptype() {
		return maptype;
	}
	public void setMaptype(String maptype) {
		this.maptype = maptype;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public double getRot_y() {
		return rot_y;
	}
	public void setRot_y(double rot_y) {
		this.rot_y = rot_y;
	}
	public double getRot_x() {
		return rot_x;
	}
	public void setRot_x(double rot_x) {
		this.rot_x = rot_x;
	}
	public double getImage_height() {
		return image_height;
	}
	public void setImage_height(double image_height) {
		this.image_height = image_height;
	}
	public double getImage_width() {
		return image_width;
	}
	public void setImage_width(double image_width) {
		this.image_width = image_width;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues() {
		values = new String[] { name, image, north, west, south, east, floor, maptype, priority };
	}
	public void setExtension(String ext) {
		this.ext = ext;
	}
	public String getExtension() {
		return ext;
	}
	public void setWorldFileExt(String wf_ext) {
		this.wf_ext = wf_ext;
	}
	public String getWorldFileExt() {
		return wf_ext;
	}
	public String getRotation() {
		return rotation;
	}
	public void setRotation(String rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * clear values
	 */
	void reset() {
		name = "(unnamed)";
		image = "";
		north = "";
		west = "";
		south = "";
		east = "";
	}
	
	/**
	 * calc new values after rotation,
	 * because the image size changed
	 * 
	 * @param path
	 */
	void calc_new_values(String path) {
		double north = Double.valueOf(this.north);
		double south = Double.valueOf(this.south);
		double west = Double.valueOf(this.west);
		double east = Double.valueOf(this.east);
		// save image size
		double image_height = this.image_height;
		double image_width = this.image_width;
		// image height in deegree
		double delta_y = Math.abs(north - south);
		double delta_x = Math.abs(west - east);
		// deegree per pixel
		double yScale = delta_y / image_height;
		double xScale = delta_x / image_width;
		// get image size after rotation
		get_image_data(new File(path + File.separator + this.image));
		// calc height/ width difference 
		double delta_h = Math.abs(image_height - this.image_height);
		double delta_w = Math.abs(image_width - this.image_width);
		// add the difference to the corresponding direction
		if (north > south) {
			this.north = String.valueOf(north + delta_h*yScale/2);
			this.south = String.valueOf(south - delta_h*yScale/2);
		} else if (south > north){
			this.north = String.valueOf(north - delta_h*yScale/2);
			this.south = String.valueOf(south + delta_h*yScale/2);
		}
		if (west > east) {
			this.west = String.valueOf(west + delta_w*xScale/2);
			this.east = String.valueOf(east - delta_w*xScale/2);
		} else if (east > west) {
			this.west = String.valueOf(west - delta_w*xScale/2);
			this.east = String.valueOf(east + delta_w*xScale/2);
		}
	}
	
	/**
	 * check file extension.
	 * 
	 * @param file
	 * @param ext the extension
	 * 
	 * @return true, if successful
	 */
	public boolean check_ext(File file, String[] ext) {
		for (int i = 0; i < ext.length; i++) {
			if (file.getName().toLowerCase().endsWith(ext[i])) {
				if (ext[i].equalsIgnoreCase("jpg")
						|| ext[i].equalsIgnoreCase("jpeg")) {
					this.wf_ext = "jgw";
				} else if (ext[i].equalsIgnoreCase("png")) {
					this.wf_ext = "pgw";
				} else if (ext[i].equalsIgnoreCase("gif")) {
					this.wf_ext = "gfw";
				} else if (ext[i].equalsIgnoreCase("tif")
						|| ext[i].equalsIgnoreCase("tiff")) {
					this.wf_ext = "tfw";
				}
				this.ext = ext[i];
				return true;
			}
		}
		return false;
	}
	
	/**
	 * read image data.
	 * 
	 * @param file
	 */
	void get_image_data(File file) {
		setImage(file.getName().toString());
		try {
			BufferedImage bi = ImageIO.read(file);
			this.image_height = bi.getHeight();
			this.image_width = bi.getWidth();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Rename file.
	 * 
	 * @param file
	 * @param regex
	 * @param repl the replacement
	 */
	public String rename(String file, String regex, String repl) {
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(file).replaceAll(repl);
	}
}
