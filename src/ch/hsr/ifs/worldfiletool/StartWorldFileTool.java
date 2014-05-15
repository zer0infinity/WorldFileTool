/**
 * 
 * World File Tool - A metadata management tool for georeferenced images.
 * Copyright (C) 2008
 * HSR University of Applied Science Rapperswil
 * IFS Institute for Software
 *
 * WorldFileTool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WorldFileTool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with WorldFileTool.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ch.hsr.ifs.worldfiletool;

import ch.hsr.ifs.worldfiletool.ui.WorldFileTool_GUI;

/**
 * Main Class.
 */
public class StartWorldFileTool {
	
	private static final String version = "0.3.7";

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		WorldFileTool_GUI gui = new WorldFileTool_GUI();
		gui.show_gui(version);
	}
}
