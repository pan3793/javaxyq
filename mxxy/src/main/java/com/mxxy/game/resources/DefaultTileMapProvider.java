package com.mxxy.game.resources;

import com.mxxy.game.config.MapConfigImpl;
import com.mxxy.game.widget.TileMap;
import open.xyq.core.io.RichRandomAccessFile;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 地图提供者
 * 
 * @author ZAB 邮箱 ：624284779@qq.com
 */
public class DefaultTileMapProvider implements IMapProvider {
	private RichRandomAccessFile raf;
	private int[][] blockOffsetTable;
	private int width;
	private int height;
	private int xBlockCount;
	private int yBlockCount;

	public DefaultTileMapProvider() {
	}

	/**
	 * 加载JPG图片
	 */
	public Image getBlock(int x, int y) {
		byte[] data = getJpegData(x, y);
		return Toolkit.getDefaultToolkit().createImage(data);
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	/**
	 * 获取JPG图片
	 */
	public byte[] getJpegData(int x, int y) {
		byte[] jpegBuf = null;
		try {
			int len = 0;
			this.raf.seek(this.blockOffsetTable[x][y]);
			if (isJPEGData()) {
				len = this.raf.readInt2();
				jpegBuf = new byte[len];
				this.raf.readFully(jpegBuf);
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
			boolean isFilled = false;
			bos.reset();
			bos.write(jpegBuf, 0, 2);
			isFilled = false;
			int p = 4;
			int start;
			for (start = 4; p < jpegBuf.length - 2; p++) {
				if ((!isFilled) && (jpegBuf[p] == -1)) {
					p++;
					if (jpegBuf[p] == -38) {
						isFilled = true;
						jpegBuf[(p + 2)] = 12;
						bos.write(jpegBuf, start, p + 10 - start);
						bos.write(0);
						bos.write(63);
						bos.write(0);
						start = p + 10;
						p += 9;
					}
				}
				if ((isFilled) && (jpegBuf[p] == -1)) {
					bos.write(jpegBuf, start, p + 1 - start);
					bos.write(0);
					start = p + 1;
				}
			}
			bos.write(jpegBuf, start, jpegBuf.length - start);
			jpegBuf = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			System.err.println("获取JPEG 数据块失败：" + e.getMessage());
		}
		return jpegBuf;
	}

	/**
	 * 获取地图资源
	 */
	public TileMap getResource(String resId) {
		return loadMap(resId);
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	public int getXBlockCount() {
		return this.xBlockCount;
	}

	public int getYBlockCount() {
		return this.yBlockCount;
	}

	private boolean isJPEGData() {
		byte[] buf = new byte[4];
		try {
			int len = this.raf.read();
			this.raf.skipBytes(3 + len * 4);
			this.raf.read(buf);
			String str = new String(buf);
			return str.equals("GEPJ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private boolean isValidMapFile() {
		byte[] buf = new byte[4];
		try {
			this.raf.read(buf);
			String str = new String(buf);
			return str.equals("0.1M");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private void loadHeader() {
		if (!isValidMapFile()) {
			throw new IllegalArgumentException("非梦幻地图格式文件!");
		}
		try {
			this.width = this.raf.readInt2();
			this.height = this.raf.readInt2();
			this.xBlockCount = (int) Math.ceil(this.width / 320.0D);
			this.yBlockCount = (int) Math.ceil(this.height / 240.0D);
			this.blockOffsetTable = new int[this.xBlockCount][this.yBlockCount];
			for (int y = 0; y < this.yBlockCount; y++) {
				for (int x = 0; x < this.xBlockCount; x++)
					this.blockOffsetTable[x][y] = this.raf.readInt2();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("地图解码失败:" + e.getMessage());
		}
	}

	/**
	 * 加载地图
	 * 
	 * @param id
	 * @return
	 */
	private TileMap loadMap(String id) {
		if (this.raf != null) {
			try {
				this.raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.blockOffsetTable = null;
		}
		MapConfigImpl cfg = ResourceStores.getInstance().getMapConfig(id);
		if (cfg != null) {
			try {
				File file = new File(cfg.getPath());
				this.raf = new RichRandomAccessFile(file, "r");
				loadHeader();
				return new TileMap(this, cfg);
			} catch (Exception e) {
				System.err.println("create map decoder failed!");
				e.printStackTrace();
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void dispose() {
		try {
			this.raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.blockOffsetTable = null;
	}
}