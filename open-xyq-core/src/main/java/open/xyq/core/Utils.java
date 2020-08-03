package open.xyq.core;

import lombok.extern.slf4j.Slf4j;
import open.xyq.core.ui.ExampleFileFilter;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;

@Slf4j
public class Utils {

    public static final ExampleFileFilter WTC_FILTER = new ExampleFileFilter("wtc",
            "WasTools Canvas 文件");

    public static final ExampleFileFilter GIF_FILTER = new ExampleFileFilter("gif", "GIF 文件");

    public static final ExampleFileFilter BMP_FILTER = new ExampleFileFilter("bmp", "BMP 文件");

    public static final ExampleFileFilter IMAGE_FILTER = new ExampleFileFilter(
            new String[]{"jpg", "png", "gif"}, "All Images");

    public static final ExampleFileFilter SUPPORT_FILTER = new ExampleFileFilter(
            new String[]{"wtc", "was", "wdf", "wd1", "wd2", "wd3", "wd4", "jpg", "png", "gif"},
            "All Support Files");

    public static final ExampleFileFilter PNG_FILTER = new ExampleFileFilter("png", "PNG 文件");

    public static final ExampleFileFilter WAS_FILTER = new ExampleFileFilter("was", "WAS 文件");

    public static final ExampleFileFilter WDF_FILTER = new ExampleFileFilter(
            new String[]{"wdf", "wd1", "wd2", "wd3", "wd4"}, "WDF 文件");

    public static final FileFilter JPEG_FILTER = new ExampleFileFilter(
            new String[]{"jpg", "jpeg"}, "JPEG 文件");

    public static final FileFilter MAP_FILTER = new ExampleFileFilter("map", "地图文件");

    public static FileFilter iniFilter = new ExampleFileFilter("ini", "INI 文件");

    public static final int FILE_TYPE_WAS = 1;

    public static final int FILE_TYPE_WDF = 2;

    public static final int FILE_TYPE_JPG = 3;

    public static final int FILE_TYPE_PNG = 4;

    public static final int FILE_TYPE_GIF = 5;

    public static final int FILE_TYPE_INI = 6;

    public static final int FILE_TYPE_BMP = 7;

    public static final int FILE_TYPE_WTC = 100;

    public static final int FILE_TYPE_UNKNOWN = 0xFF;

    private static File lastOpenDir = new File(".");

    private static File lastSaveDir = new File(".");

    /**
     * 初始化全部字体
     */
    public static void iniGlobalFont() {
        FontUIResource fontRes = new FontUIResource(DEFAULT_FONT);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
    }

    public static Font DEFAULT_FONT = new Font("宋体", Font.PLAIN, 12);

    private static OpenDialog openChooser = null;

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
            openChooser.addChoosableFileFilter(BMP_FILTER);
        }
        int returnVal = openChooser.showDialog(parent, lastOpenDir, title, filter);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            lastOpenDir = openChooser.getCurrentDirectory();
            return openChooser.getSelectedFiles();
        }
        return new File[]{};
    }

    public static Image loadImage(String filename) {
        return new ImageIcon(filename).getImage();
    }

    public static File showSaveDialog(Component parent, String title, FileFilter filter) {
        return showSaveDialog(parent, title, filter, null);
    }

    /**
     * 获得指定的图像写出器
     */
    public static ImageWriter getImageWriter(String format) {
        ImageWriter imgWriter = null;
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(format);
        if (iter.hasNext()) {
            imgWriter = iter.next();
        }
        return imgWriter;
    }

    /**
     * 根据指定格式输出图像到文件
     */
    public static boolean writeImage(RenderedImage img, File outputFile, String format) {
        ImageWriter imgWriter = getImageWriter(format);
        if (imgWriter != null) {
            try {
                ImageOutputStream imgOut;
                imgOut = ImageIO.createImageOutputStream(outputFile);
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

    public static Icon loadIcon(String filename) {
        return new ImageIcon(Utils.class.getClassLoader().getResource(filename));
    }

    /**
     * identify file type by suffix: "was","wdf","jpg","png","gif"
     */
    public static int getFileType(File file) {
        return getFileType(file.getName());
    }

    /**
     * identify file type by suffix: "was","wdf","jpg","png","gif"
     */
    public static int getFileType(String filename) {
        filename = filename.toLowerCase();
        int type = FILE_TYPE_UNKNOWN;
        if (filename.endsWith(".was")) {
            type = FILE_TYPE_WAS;
        } else if (filename.endsWith(".wdf")) {
            type = FILE_TYPE_WDF;
        } else if (filename.endsWith(".wd1")) {
            type = FILE_TYPE_WDF;
        } else if (filename.endsWith(".wd2")) {
            type = FILE_TYPE_WDF;
        } else if (filename.endsWith(".wd3")) {
            type = FILE_TYPE_WDF;
        } else if (filename.endsWith(".wd4")) {
            type = FILE_TYPE_WDF;
        } else if (filename.endsWith(".jpg")) {
            type = FILE_TYPE_JPG;
        } else if (filename.endsWith(".jpeg")) {
            type = FILE_TYPE_JPG;
        } else if (filename.endsWith(".png")) {
            type = FILE_TYPE_PNG;
        } else if (filename.endsWith(".bmp")) {
            type = FILE_TYPE_BMP;
        } else if (filename.endsWith(".gif")) {
            type = FILE_TYPE_GIF;
        } else if (filename.endsWith(".wtc")) {
            type = FILE_TYPE_WTC;
        } else if (filename.endsWith(".ini")) {
            type = FILE_TYPE_INI;
        }
        return type;
    }

    public static Image loadImage(File file) {
        return loadImage(file.getAbsolutePath());
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
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            lastSaveDir = chooser.getCurrentDirectory();
        }
        return file;
    }

    /**
     * 读取文件流。<br>
     * 查找顺序：1.查找本地文件资源 2.Jar文件中的资源
     */
    public static InputStream getResourceAsStream(String filename) {
        InputStream in = null;
        if (new File(filename).exists()) {
            try {
                in = new FileInputStream(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            if (!filename.startsWith("/")) {
                filename = "/" + filename;
            }
            in = Utils.class.getResourceAsStream(filename);
        }
        if (in == null) {
            log.error("load resource failed: " + filename);
        }
        return in;
    }
}
