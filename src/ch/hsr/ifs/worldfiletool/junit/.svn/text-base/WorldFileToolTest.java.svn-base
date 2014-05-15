package ch.hsr.ifs.worldfiletool.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import ch.hsr.ifs.worldfiletool.logic.Data;
import ch.hsr.ifs.worldfiletool.logic.KML;
import ch.hsr.ifs.worldfiletool.logic.WorldFile;
import ch.hsr.ifs.worldfiletool.ui.WorldFileTool_GUI;

/**
 * The Class WorldFileTest. 
 */
public class WorldFileToolTest {
	
	/**
	 * writing my first tests ever.
	 * show a lil' mercy. ;)
	 */
	@Test
	public void testParse_kml() {
		int[] angles = { 45, 135, 225, 315};
		for (int angle: angles) {
			File testfile = new File("testkml.kml");
			create_test_image();
			String kml_test = 
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">" + "\n" +
				"<GroundOverlay>" + "\n" +
				"    <name>test</name>" + "\n" +
				"    <Icon>" + "\n" +
				"        <href>testimage.png</href>" + "\n" +
				"        <viewBoundScale>0.75</viewBoundScale>" + "\n" +
				"    </Icon>" + "\n" +
				"    <LatLonBox>" + "\n" +
				"        <north>47.22384390486059</north>" + "\n" +
				"        <west>8.815489285344636</west>" + "\n" +
				"        <south>47.22345122640866</south>" + "\n" +
				"        <east>8.816304349533887</east>" + "\n" +
				"        <rotation>" + angle + "</rotation>" + "\n" +	
				"    </LatLonBox>" + "\n" +
				"    <Metadata>" + "\n" +
				"        <floor>0</floor>" + "\n" +
				"        <background>0</background>" + "\n" +
				"        <priority>0</priority>" + "\n" +
				"    </Metadata>" + "\n" +
				"</GroundOverlay>" + "\n" +
				"</kml>" + "\n";
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(testfile));
				bw.write(kml_test);
				bw.close();
			} catch (IOException e) {
				fail(e.getMessage());
			}
			Data data = new Data();
			String input = "";
			KML kml = new KML(new WorldFileTool_GUI(), data, input);
			kml.parse_kml(System.getProperty("user.dir"), testfile);
			assertEquals("Name", data.getName(), "test");
			assertEquals("View", data.getView(), "0.75");
			assertEquals("Image", data.getImage(), "testimage.png");
			assertEquals("North", data.getNorth(), "47.22385666691027");
			assertEquals("West", data.getWest(), "8.81502877407771");
			assertEquals("South", data.getSouth(), "47.22343846435898");
			assertEquals("East", data.getEast(), "8.816764860800813");
			assertEquals("Image Height", data.getImage_height(), 213);
			assertEquals("Image Width", data.getImage_width(), 213);
			del_test_files();
		}
	}

	@Test
	public void testParse_worldfile() {
		int[][] coord = {{-1, 0}, {0, 0}, {1, 0}};
		for (int[] xy: coord) {
		String image_test =
			"0.0057987615" + "\n" +
			xy[0] + "\n" +
			xy[1] + "\n" +
			"-0.0057987615" + "\n" +
			"5.2211660445" + "\n" +
			"48.1645342726" + "\n";
			try {
				create_test_image();
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File("testimage.pgw")));
				bw.write(image_test);
				bw.close();
			} catch (IOException e) {
				fail(e.getMessage());
			}
			Data data = new Data();
			String input = "";
			WorldFile wf = new WorldFile(new WorldFileTool_GUI(), data, input);
			wf.parse_worldfile(System.getProperty("user.dir"), new File("testimage.png"));
			assertEquals("Image", data.getImage(), "testimage.png");
			assertEquals("North", data.getNorth(), "48.1645342726");
			assertEquals("West", data.getWest(), "5.2211660445");
			assertEquals("South", data.getSouth(), "47.0047819726");
			assertEquals("East", data.getEast(), "5.801042194500001");
			assertEquals("Image Height", data.getImage_height(), 200);
			assertEquals("Image Width", data.getImage_width(), 100);
			del_test_files();
		}
	}

	@Test
	public void testCheck_ext() {
		Data data = new Data();
		final String[] ext = { "jpg", "jpeg", "png", "gif", "tif", "tiff" };
		assertFalse(data.check_ext(new File("testfile"), ext));
		assertTrue(data.check_ext(new File("testfile.jpg"), ext));
		assertTrue(data.check_ext(new File("testfile.png"), ext));
		assertTrue(data.check_ext(new File("testfile.gif"), ext));
		assertTrue(data.check_ext(new File("testfile.tif"), ext));
		assertTrue(data.check_ext(new File("testfile.tiff"), ext));
	}

	private void create_test_image() {
		File testimage = new File("testimage.png");
		BufferedImage bitest = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
		try {
			ImageIO.write(bitest, "png", testimage);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	private void del_test_files() {
		new File("testimage.png").delete();
		new File("testimage_ori.png").delete();
		new File("testimage.pgw").delete();
		new File("testkml.kml").delete();
	}
}
