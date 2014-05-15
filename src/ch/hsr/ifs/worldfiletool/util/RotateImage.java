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

package ch.hsr.ifs.worldfiletool.util;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The Class RotateImage.
 */
public class RotateImage {
	
	/**
	 * 2D image rotation with vectors.
	 * 
	 * @param file InputFile
	 * @param vecx Vector x
	 * @param vecy Vector y
	 * 
	 * @return bi - BufferedImage
	 */
	public static BufferedImage rotate_vectors(File file, double vecx, double vecy) {
		double angle = 0;
		// distance of two points in 2D: sqrt((dx)^2 + (dy)^2)
		double dist_vec = Math.sqrt(Math.pow(vecx, 2) + Math.pow(vecy, 2));
		// calc angle in deegree (trigonometry)
		angle = Math.toDegrees(Math.acos(vecy / dist_vec));
		if (vecx < 0) {
			angle = -angle;
		}
		return rotate(file, angle);
	}
	
	/**
	 * 2D image rotation with angle in deegree.
	 * 
	 * @param file InputFile
	 * @param angle Angle in deegree
	 * 
	 * @return bi - BufferedImage
	 */
	public static BufferedImage rotate_angle(File file, double angle) {
		angle = -angle;
		return rotate(file, angle);
	}

	/**
	 * translate to make sure the rotation doesn't cut off any image data.
	 * 
	 * @param at AffineTransform
	 * @param bi BufferedImage
	 * @param angle Rotation in deegree
	 * 
	 * @return att Translate AffineTransform
	 */
	private static AffineTransform translate(AffineTransform at, BufferedImage bi, double angle) {
		double ytrans = 0;
		double xtrans = 0;
		if (0 < angle && angle < 90) {
			ytrans = transform_point(0.0, 0.0, at).getY();
			xtrans = transform_point(0.0, bi.getHeight(), at).getX();
		} else if (90 < angle && angle < 180) {
			ytrans = transform_point(0.0, bi.getHeight(), at).getY();
			xtrans = transform_point(bi.getWidth(), bi.getHeight(), at).getX();
		} else if (180 < angle && angle < 270) {
			ytrans = transform_point(bi.getWidth(), bi.getHeight(), at).getY();
			xtrans = transform_point(bi.getWidth(), 0.0, at).getX();
		} else if (270 < angle && angle < 360) {
			ytrans = transform_point(bi.getWidth(), 0.0, at).getY();
			xtrans = transform_point(0.0, 0.0, at).getX();
		}
		AffineTransform att = new AffineTransform();
		att.translate(-xtrans, -ytrans);
		return att;
	}
	
	private static Point2D transform_point(double x, double y, AffineTransform at) {
		Point2D p2d = new Point2D.Double(x, y);
		return at.transform(p2d, null);
	}
	
	/**
	 * Rotate and Translate Image.
	 * 
	 * @param file InputFile
	 * @param angle Rotation in deegree
	 * 
	 * @return BufferedImage
	 */
	private static BufferedImage rotate(File file, double angle) {
		BufferedImage bi = null;
		AffineTransformOp ato = null;
		try {
			bi = ImageIO.read(file);
			// make angle between 0-360°
			while (angle > 360) {
				angle -= 360;
			}
			while (angle < 0) {
				angle += 360;
			}
			// rotation around imagecenter
			// y-axis = 0°
			// clockwise = positiv rotation
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians(angle), bi.getWidth()/2, bi.getHeight()/2);
			at.preConcatenate(translate(at, bi, angle));
			ato = new AffineTransformOp(at, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ato.filter(bi, null);
	}
}
