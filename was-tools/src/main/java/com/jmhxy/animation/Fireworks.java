package com.jmhxy.animation;

import com.jmhxy.core.SpriteFactory;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.Serializable;
import java.util.Vector;

public class Fireworks implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int DELAY_INTERVAL = 100;
	private final int maxSequence;
	private final int columnCount;
	private final int rowCount;
	private int x;
	private int y;
	private int cx;
	private int cy;
	private int rx;
	private int ry;
	private final CellObject[][] table;
	private final Vector<int[][]> sequence;
	private int cellWidth = 17;

	private int cellHeight = 19;
	private int[][] currentSequence;

	public Fireworks(int rows, int cols, int sequences) {
		this.maxSequence = sequences;
		this.rowCount = rows;
		this.columnCount = cols;
		this.table = new CellObject[this.columnCount][this.rowCount];
		this.sequence = new Vector(this.maxSequence);
		this.sequence.add(new int[this.rowCount][this.columnCount]);

		setCellSize(17, 19);
	}

	public void setCellSize(int width, int height) {
		this.cellWidth = width;
		this.cellHeight = height;
		double radian1 = 1.0995574287564276D;
		double radian2 = 1.0995574287564276D;

		this.cx = ((int) (this.cellWidth * Math.sin(radian1)));
		this.cy = (-(int) (this.cellWidth * Math.cos(radian1)));

		this.rx = ((int) (this.cellHeight * Math.sin(radian2)));
		this.ry = ((int) (this.cellHeight * Math.cos(radian2)));
	}

	public Fireworks() {
		this(13, 13, 50);
	}

	public void setCellGoods(int row, int col, int goodsID) {
		CellObject cellObject = this.table[row][col];
		if (cellObject == null) {
			cellObject = new CellObject();
			this.table[row][col] = cellObject;
		}
		cellObject.goods = SpriteFactory.getAnimationByID(goodsID);
	}

	public int getCellGoods(int row, int col) {
		CellObject cellObject = this.table[row][col];
		if ((cellObject == null) || (cellObject.goods == null))
			return -1;
		return cellObject.goods.getId();
	}

	public void setCellEffect(int row, int col, int effectID) {
		CellObject cellObject = this.table[row][col];
		if (cellObject == null) {
			cellObject = new CellObject();
			this.table[row][col] = cellObject;
		}
		cellObject.effect = SpriteFactory.getAnimationByID(effectID);
	}

	public int getCellEffect(int row, int col) {
		CellObject cellObject = this.table[row][col];
		if ((cellObject == null) || (cellObject.effect == null))
			return -1;
		return cellObject.effect.getId();
	}

	public void setSequence(int index, int[][] snap) {
		if (this.sequence.size() < index + 1) {
			this.sequence.setSize(index + 1);
		}
		this.sequence.set(index, snap);
	}

	public int[][] getSequence(int index) {
		int[][] snap = this.sequence.get(index);
		return snap == null ? new int[this.columnCount][this.rowCount] : snap;
	}

	public void addSequence(int[][] snap) {
		this.sequence.add(snap);
	}

	public void removeSequence(int index) {
		this.sequence.remove(index);
	}

	public void removeSequence(int begIndex, int endIndex) {
		for (int i = begIndex; i < endIndex; i++) {
			this.sequence.remove(begIndex);
		}
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	public int getRowCount() {
		return this.rowCount;
	}

	public void updateToTime(long playTime) {
		if (this.sequence.size() > 0) {
			int seqIndex = (int) Math.ceil(playTime / 100.0) % this.sequence.size();
			this.currentSequence = this.sequence.get(seqIndex);
		}

		for (int r = 0; r < this.rowCount; r++) {
			for (int c = 0; c < this.columnCount; c++) {
				try {
					this.table[r][c].goods.updateToTime(playTime);
				} catch (Exception localException) {
				}
				try {
					this.table[r][c].effect.updateToTime(playTime);
				} catch (Exception localException1) {
				}
			}
		}
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int r = 0; r < this.rowCount; r++) {
			for (int c = 0; c < this.columnCount; c++) {
				Animation anim = getCellEffectAnimation(r, c);
				if (anim != null) {
					g.drawImage(anim.getImage(), this.x - anim.getCenterX() + this.rx * r + this.cx * c,
							this.y - anim.getCenterY() + this.ry * r + this.cy * c, null);
				}

				anim = getCellGoodsAnimation(r, c);
				if (anim != null) {
					Image image;
					if ((this.currentSequence != null) && (this.currentSequence[r][c] != 0)) {
						image = anim.getBrightenImage();
					} else {
						image = anim.getImage();
					}
					g2.drawImage(image, this.x - anim.getCenterX() + this.rx * r + this.cx * c,
							this.y - anim.getCenterY() + this.ry * r + this.cy * c, null);
				}
			}
		}
	}

	private Animation getCellEffectAnimation(int row, int col) {
		try {
			return this.table[row][col].effect;
		} catch (Exception e) {
		}
		return null;
	}

	private Animation getCellGoodsAnimation(int row, int col) {
		try {
			return this.table[row][col].goods;
		} catch (Exception e) {
		}
		return null;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	class CellObject {
		Animation goods;
		Animation effect;

		CellObject() {
		}
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\animation\Fireworks.class
 * Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */