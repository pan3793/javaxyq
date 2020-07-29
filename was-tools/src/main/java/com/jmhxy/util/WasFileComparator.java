package com.jmhxy.util;

import com.jmhxy.encoder.WasFile;
import java.util.Comparator;

public class WasFileComparator implements Comparator {
	private static int sortType;
	public static final int SORT_ID = 0;
	public static final int SORT_NAME = 1;
	public static final int SORT_SIZE = 2;
	private static WasFileComparator instance = null;

	public static void setSortType(int type) {
		sortType = type;
	}

	public static WasFileComparator getInstance() {
		if (instance == null)
			instance = new WasFileComparator();
		return instance;
	}

	public int compare(Object o1, Object o2) {
		try {
			WasFile file1 = (WasFile) o1;
			WasFile file2 = (WasFile) o2;
			switch (sortType) {
			case 0:
				return file1.id == file2.id ? 0 : file1.id > file2.id ? 1 : -1;
			case 2:
				return file1.size == file2.size ? 0 : file1.size > file2.size ? 1 : -1;
			case 1:
				if ((file1.name == null) && (file2.name != null))
					return 1;
				if ((file1.name != null) && (file2.name == null))
					return -1;
				if ((file1.name == null) && (file2.name == null)) {
					return file1.id == file2.id ? 0 : file1.id > file2.id ? 1 : -1;
				}
				String s1 = new String(file1.name.getBytes("GB2312"), "ISO-8859-1");
				String s2 = new String(file2.name.getBytes("GB2312"), "ISO-8859-1");

				return s1.compareTo(s2);
			}
		} catch (Exception localException) {
		}
		return 0;
	}
}