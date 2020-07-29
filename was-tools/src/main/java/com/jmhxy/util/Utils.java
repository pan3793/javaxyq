package com.jmhxy.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;

public class Utils {
	public static final ExampleFileFilter WTC_FILTER = new ExampleFileFilter("wtc", "WasTools Canvas 文件");

	public static final ExampleFileFilter GIF_FILTER = new ExampleFileFilter("gif", "GIF 文件");

	public static final ExampleFileFilter IMAGE_FILTER = new ExampleFileFilter(new String[] { "jpg", "png", "gif" },
			"All Images");

	public static final ExampleFileFilter SUPPORT_FILTER = new ExampleFileFilter(
			new String[] { "wtc", "was", "wdf", "wd1", "wd2", "wd3", "wd4", "jpg", "png", "gif" }, "All Support Files");

	public static final ExampleFileFilter PNG_FILTER = new ExampleFileFilter("png", "PNG 文件");

	public static final ExampleFileFilter WAS_FILTER = new ExampleFileFilter("was", "WAS 文件");

	public static final ExampleFileFilter WDF_FILTER = new ExampleFileFilter(
			new String[] { "wdf", "wd1", "wd2", "wd3", "wd4" }, "WDF 文件");

	public static final FileFilter JPEG_FILTER = new ExampleFileFilter(new String[] { "jpg", "jpeg" }, "JPEG 文件");

	public static final FileFilter MAP_FILTER = new ExampleFileFilter("map", "地图文件");

	public static FileFilter iniFilter = new ExampleFileFilter("ini", "INI 文件");
	public static final String PROPERTY_LAST_OPEN_DIR = "LastOpenDir";
	public static final String PROPERTY_LAST_SAVE_DIR = "LastSaveDir";
	public static final String PROPERTY_ADD_TO_CURRENT = "AddToCurrent";
	public static final int FILE_TYPE_WAS = 1;
	public static final int FILE_TYPE_WDF = 2;
	public static final int FILE_TYPE_JPG = 3;
	public static final int FILE_TYPE_PNG = 4;
	public static final int FILE_TYPE_GIF = 5;
	public static final int FILE_TYPE_INI = 6;
	public static final int FILE_TYPE_WTC = 100;
	public static final int FILE_TYPE_UNKNOW = 255;
	private static File lastOpenDir = new File(".");

	private static File lastSaveDir = new File(".");

	public static Font DEFAULT_FONT = new Font("宋体", 0, 12);

	private static OpenDialog openChooser = null;
	private static Properties properties;
	private static boolean isAddToCurrent;

	public static void iniGlobalFont() {
		FontUIResource fontRes = new FontUIResource(DEFAULT_FONT);
		for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if ((value instanceof FontUIResource))
				UIManager.put(key, fontRes);
		}
	}

	public static File[] showOpenDialog(Component parent, String title, FileFilter filter) {
		if (openChooser == null) {
			openChooser = new OpenDialog();
		}
		if (filter == SUPPORT_FILTER) {
			openChooser.addChoosableFileFilter(WTC_FILTER);
			openChooser.addChoosableFileFilter(WAS_FILTER);
			openChooser.addChoosableFileFilter(WDF_FILTER);
			openChooser.addChoosableFileFilter(JPEG_FILTER);
			openChooser.addChoosableFileFilter(PNG_FILTER);
			openChooser.addChoosableFileFilter(GIF_FILTER);
		}
		int returnVal = openChooser.showDialog(parent, lastOpenDir, title, filter, isAddToCurrent);
		if (returnVal == 0) {
			lastOpenDir = openChooser.getCurrentDirectory();
			isAddToCurrent = openChooser.isAddToCurrent();
			return openChooser.getSelectedFiles();
		}
		return new File[0];
	}

	public static Image loadImage(String filename) {
		return new ImageIcon(filename).getImage();
	}

	public static File showSaveDialog(Component parent, String title, FileFilter filter) {
		return showSaveDialog(parent, title, filter, null);
	}

	public static byte[] loadJarFile(String filename) throws IOException {
		InputStream is = Utils.class.getResourceAsStream(filename);
		if (is == null) {
			if (filename.startsWith("/")) {
				filename = filename.substring(1);
			}
			is = new FileInputStream(filename);
		}
		byte[] buf = new byte[is.available()];
		int a = 0;
		int count = 0;
		while (is.available() > 0) {
			a = is.read(buf, count, is.available());
			count += a;
		}
		return buf;
	}

	public static ImageWriter getImageWriter(String format) {
		ImageWriter imgWriter = null;
		Iterator iter = ImageIO.getImageWritersByFormatName(format);
		if (iter.hasNext()) {
			imgWriter = (ImageWriter) iter.next();
		}
		return imgWriter;
	}

	public static boolean writeImage(BufferedImage img, File outputFile, String format) {
		ImageWriter imgWriter = getImageWriter(format);
		if (imgWriter != null) {
			try {
				ImageOutputStream imgOut = ImageIO.createImageOutputStream(outputFile);
				imgWriter.setOutput(imgOut);
				IIOImage iioImage = new IIOImage(img, null, null);
				imgWriter.write(iioImage);
				imgOut.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static void loadSettings(String filename) {
		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(filename);
			properties.load(fis);
			fis.close();
			lastOpenDir = new File(properties.getProperty("LastOpenDir", "."));
			lastSaveDir = new File(properties.getProperty("LastSaveDir", "."));
			isAddToCurrent = getPropertyAsBoolean("AddToCurrent");
		} catch (Exception localException) {
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	public static boolean saveSettings(String comments, String filename) {
		try {
			properties.setProperty("LastOpenDir", lastOpenDir.getAbsolutePath());
			properties.setProperty("LastSaveDir", lastSaveDir.getAbsolutePath());
			properties.setProperty("AddToCurrent", Boolean.toString(isAddToCurrent));
			OutputStream fos = new FileOutputStream(filename);
			properties.store(fos, comments);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Icon loadIcon(String filename) {
		Icon icon = null;
		if (filename.charAt(0) != '/') {
			filename = "/com/wildbean/resources/icon/" + filename;
		}
		icon = new ImageIcon(Utils.class.getResource(filename));
		return icon;
	}

	public static int getFileType(File file) {
		return getFileType(file.getName());
	}

	public static int getFileType(String filename) {
		filename = filename.toLowerCase();
		int type = 255;
		if ((filename.endsWith(".was")) || (filename.endsWith(".WAS")))
			type = 1;
		else if ((filename.endsWith(".wdf")) || (filename.endsWith(".WDF")))
			type = 2;
		else if ((filename.endsWith(".wd1")) || (filename.endsWith(".WD1")))
			type = 2;
		else if ((filename.endsWith(".wd2")) || (filename.endsWith(".WD2")))
			type = 2;
		else if ((filename.endsWith(".wd3")) || (filename.endsWith(".WD3")))
			type = 2;
		else if ((filename.endsWith(".wd4")) || (filename.endsWith(".WD4")))
			type = 2;
		else if ((filename.endsWith(".jpg")) || (filename.endsWith(".JPG")))
			type = 3;
		else if ((filename.endsWith(".jpeg")) || (filename.endsWith(".JPEG")))
			type = 3;
		else if ((filename.endsWith(".png")) || (filename.endsWith(".PNG")))
			type = 4;
		else if ((filename.endsWith(".gif")) || (filename.endsWith(".GIF")))
			type = 5;
		else if ((filename.endsWith(".wtc")) || (filename.endsWith(".WTC")))
			type = 100;
		else if ((filename.endsWith(".ini")) || (filename.endsWith(".INI"))) {
			type = 6;
		}
		return type;
	}

	public static boolean isAddToCurrent() {
		return isAddToCurrent;
	}

	public static Image loadImage(File file) {
		return loadImage(file.getAbsolutePath());
	}

	public static boolean getPropertyAsBoolean(String key) {
		return Boolean.parseBoolean(getProperty(key));
	}

	public static int getPropertyAsInt(String key) {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (Exception localException) {
		}
		return 0;
	}

	public static File showSaveDialog(Component parent, String title, FileFilter filter, String name) {
		File file = null;
		JFileChooser chooser = new JFileChooser();
		if (name != null)
			chooser.setSelectedFile(new File(lastSaveDir, name));
		chooser.setCurrentDirectory(lastSaveDir);
		chooser.setFileFilter(filter);
		chooser.setDialogTitle(title);
		int returnVal = chooser.showSaveDialog(parent);
		if (returnVal == 0) {
			file = chooser.getSelectedFile();
			lastSaveDir = chooser.getCurrentDirectory();
		}
		return file;
	}
}