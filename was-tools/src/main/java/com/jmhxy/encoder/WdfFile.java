package com.jmhxy.encoder;

import com.jmhxy.util.WasFileComparator;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

public class WdfFile {
	RandomAccessFile fileHandler;
	static final String FILE_TAG = "PFDW";
	int fileCount;
	private Vector<WasFile> fileList;
	private String filename;
	private TreeMap<Integer, WasFile> fileMapping = new TreeMap();

	private boolean isWdfFile(RandomAccessFile raf) throws IOException {
		byte[] buf = new byte[4];
		raf.seek(0L);
		raf.read(buf);
		String fid = new String(buf);
		return fid.equals("PFDW");
	}

	public WdfFile(String filename) {
		try {
			this.filename = filename;
			this.fileHandler = new RandomAccessFile(filename, "r");
			if (!isWdfFile(this.fileHandler))
				throw new IllegalArgumentException("这个不是WDF格式的文件！");
			this.fileCount = readInt(this.fileHandler);
			int offset = readInt(this.fileHandler);
			this.fileHandler.seek(offset);
			this.fileList = new Vector(this.fileCount);
			for (int i = 0; i < this.fileCount; i++) {
				WasFile node = new WasFile();
				node.id = readInt(this.fileHandler);
				node.offset = readInt(this.fileHandler);
				node.size = readInt(this.fileHandler);
				node.space = readInt(this.fileHandler);
				node.parent = this;
				this.fileList.add(node);
				this.fileMapping.put(Integer.valueOf(node.id), node);
			}
			loadIniFile(new File(filename + ".ini"));
		} catch (Exception e) {
			System.err.println("打开WDF文件出错：" + filename);
			e.printStackTrace();
		}
	}

	public void loadIniFile(File iniFile) throws Exception {
		if (iniFile.exists()) {
			Scanner scanner = new Scanner(new FileInputStream(iniFile));
			scanner.useDelimiter("(\r\n)|(\n\r)|[\n\r=]");
			String tag = scanner.next();

			if (tag.startsWith("[Resource]")) {
				while (scanner.hasNext()) {
					scanner.skip("(\r\n)|(\n\r)|[\n\r=]");
					String str = scanner.next();
					int uid = parseInt(str, 16);
					scanner.skip("(\r\n)|(\n\r)|[\n\r=]");
					String alias = scanner.next().trim();
					WasFile wasfile = (WasFile) this.fileMapping.get(Integer.valueOf(uid));
					if (wasfile != null) {
						wasfile.name = alias;
					}
				}
			}
			scanner.close();

			sort(1);
		}
	}

	public void saveIniFile(File iniFile) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(iniFile)));
			writer.write("[Resource]\r\n");
			for (WasFile node : this.fileList)
				if ((node.name != null) && (node.name.length() > 0)) {
					writer.write(Integer.toHexString(node.id).toUpperCase());
					writer.write(61);
					writer.write(node.name);
					writer.write("\r\n");
				}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException localIOException) {
			}
		}
	}

	public void clearAllMark() {
		for (int i = 0; i < this.fileList.size(); i++) {
			((WasFile) this.fileList.get(i)).name = null;
		}
	}

	public static int parseInt(String s, int radix) throws NumberFormatException {
		if (s == null) {
			throw new NumberFormatException("null");
		}
		if (radix < 2) {
			throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");
		}
		if (radix > 36) {
			throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
		}
		int result = 0;
		boolean negative = false;
		int i = 0;
		int max = s.length();

		if (max > 0) {
			int limit;
			if (s.charAt(0) == '-') {
				negative = true;
				limit = Integer.MIN_VALUE;
				i++;
			} else {
				limit = -2147483647;
			}

			if (i < max) {
				int digit = Character.digit(s.charAt(i++), radix);
				if (digit < 0) {
					throw new NumberFormatException("For input string: \"" + s + "\"");
				}
				result = -digit;
			}

			while (i < max) {
				int digit = Character.digit(s.charAt(i++), radix);
				if (digit < 0) {
					throw new NumberFormatException("For input string: \"" + s + "\"");
				}
				result *= radix;
				if (result < limit + digit)
					throw new NumberFormatException("For input string: \"" + s + "\"");
				result -= digit;
			}
		} else {
			throw new NumberFormatException("For input string: \"" + s + "\"");
		}
		int limit;
		if (negative) {
			if (i > 1) {
				return result;
			}
			throw new NumberFormatException("For input string: \"" + s + "\"");
		}

		return -result;
	}

	public int readInt(InputStream is) throws IOException {
		int ch1 = is.read();
		int ch2 = is.read();
		int ch3 = is.read();
		int ch4 = is.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
	}

	public int readInt(RandomAccessFile is) throws IOException {
		int ch1 = is.read();
		int ch2 = is.read();
		int ch3 = is.read();
		int ch4 = is.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
	}

	public Vector<WasFile> getFileList() {
		return this.fileList;
	}

	public int getFileCount() {
		return this.fileCount;
	}

	public void sort(int type) {
		WasFileComparator.setSortType(type);
		Collections.sort(this.fileList, WasFileComparator.getInstance());
	}

	public byte[] getFileData(WasFile fnode) throws IOException {
		byte[] data = (byte[]) null;
		if (fnode != null) {
			data = new byte[fnode.size];
			this.fileHandler.seek(fnode.offset);
			this.fileHandler.readFully(data);
		}
		return data;
	}

	public byte[] getFileData(int uid) throws IOException {
		return getFileData((WasFile) this.fileMapping.get(Integer.valueOf(uid)));
	}

	public String toString() {
		return this.filename;
	}

	public void setAlias(int uid, String alias) {
		WasFile fnode = (WasFile) this.fileMapping.get(Integer.valueOf(uid));
		fnode.name = alias;
	}

	public String getName() {
		return this.filename;
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\encoder\WdfFile.class
 * Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */