package com.jmhxy.encoder;

import java.util.Vector;

public class OcTreePal {
	public short[] Pal;
	private int m_iColor;
	private int m_PalIndex;
	private OcTree pOctroot;
	private Vector<OcTree> m_Rev2ndNodes;

	public class OcTree {
		public OcTree[] pChildren = new OcTree[8];
		public OcTree pParent;
		int m_iChildren = 0;
		int m_iRef = 0;
		int m_iIndex;
		int m_Red = 0;
		int m_Green;
		int m_Blue = 0;

		public OcTree() {
		}

		public boolean isLeaf() {
			return this.m_iChildren == 0;
		}

		public boolean isRev2nd() {
			boolean isR = false;

			for (int i = 0; i < 8; i++)
				if (this.pChildren[i] != null) {
					isR = true;
					if (!this.pChildren[i].isLeaf()) {
						isR = false;
						break;
					}
				}
			return isR;
		}

		public void SumChildren() {
			for (int i = 0; i < 8; i++)
				if (this.pChildren[i] != null) {
					this.m_iRef += this.pChildren[i].m_iRef;
					this.m_Red += this.pChildren[i].m_Red;
					this.m_Green += this.pChildren[i].m_Green;
					this.m_Blue += this.pChildren[i].m_Blue;
				}
		}
	}

	private class MyComparator implements java.util.Comparator {
		private MyComparator() {
		}

		public int compare(Object o1, Object o2) {
			int r1 = ((OcTreePal.OcTree) o1).m_iRef;
			int r2 = ((OcTreePal.OcTree) o2).m_iRef;
			return Integer.compare(r2, r1);
		}
	}

	public OcTreePal(java.util.List<byte[]> frames) {
		this.pOctroot = new OcTree();
		this.m_Rev2ndNodes = new Vector();

		this.m_iColor = 0;
		int size = frames.size();
		for (int i = 0; i < size; i++)
			BuildOctree((byte[]) frames.get(i));
		ReduceTo256();
		BuildPal();
	}

	public OcTreePal() {
		this.pOctroot = new OcTree();
		this.m_Rev2ndNodes = new Vector();
		this.m_iColor = 0;
	}

	public void addFrame(byte[] frame) {
		BuildOctree(frame);
	}

	public short[] getPalette() {
		ReduceTo256();
		BuildPal();
		return this.Pal;
	}

	public int getColorIndex(int r, int g, int b) {
		OcTree pNode = this.pOctroot;
		for (int i = 7; i >= 0; i--) {
			int val = r >> i & 0x1;
			val = val << 1 | g >> i & 0x1;
			val = val << 1 | b >> i & 0x1;
			if (pNode.pChildren[val] == null)
				break;
			pNode = pNode.pChildren[val];
		}
		return pNode.m_iIndex;
	}

	private void BuildOctree(byte[] frame) {
		for (int i = 0; i < frame.length;) {
			int red = frame[i];
			int green = frame[(i + 1)];
			int blue = frame[(i + 2)];

			OcTree pOctreeNode = this.pOctroot;
			for (int j = 7; j >= 0; j--) {
				int val = red >> j & 0x1;
				val = val << 1 | green >> j & 0x1;
				val = val << 1 | blue >> j & 0x1;
				if (pOctreeNode.pChildren[val] == null) {
					pOctreeNode.pChildren[val] = new OcTree();
					pOctreeNode.pChildren[val].pParent = pOctreeNode;
				}
				pOctreeNode = pOctreeNode.pChildren[val];
			}
			if (pOctreeNode.m_iRef == 0)
				this.m_iColor += 1;
			pOctreeNode.m_iRef += 1;
			pOctreeNode.m_Red += red;
			pOctreeNode.m_Green += green;
			pOctreeNode.m_Blue += blue;

			i += 3;
		}
	}

	private void ReduceTo256() {
		if (this.m_iColor < 256)
			return;
		BuildVector(this.pOctroot);
		java.util.Collections.sort(this.m_Rev2ndNodes, new MyComparator());
		for (;;) {
			OcTree pNode = (OcTree) this.m_Rev2ndNodes.lastElement();
			for (int i = 0; i < 8; i++)
				if (pNode.pChildren[i] != null) {
					this.m_iColor -= 1;
					pNode.pChildren[i] = null;
				}
			this.m_iColor += 1;
			if (this.m_iColor < 256)
				break;
			this.m_Rev2ndNodes.remove(this.m_Rev2ndNodes.size() - 1);
			if (pNode.pParent.isRev2nd()) {
				pNode = pNode.pParent;
				pNode.SumChildren();
				for (int i = this.m_Rev2ndNodes.size() - 1; i >= 0; i--)
					if (((OcTree) this.m_Rev2ndNodes.get(i)).m_iRef >= pNode.m_iRef) {
						this.m_Rev2ndNodes.add(i + 1, pNode);
						break;
					}
			}
		}
	}

	private void BuildPal() {
		this.Pal = new short['Ā'];
		this.m_PalIndex = 0;
		BuildPalIt(this.pOctroot);
	}

	private void BuildVector(OcTree pNode) {
		if (pNode.isRev2nd()) {
			pNode.SumChildren();
			this.m_Rev2ndNodes.add(pNode);
		} else {
			for (int i = 0; i < 8; i++) {
				if (pNode.pChildren[i] != null) {

					BuildVector(pNode.pChildren[i]);
				}
			}
		}
	}

	private void BuildPalIt(OcTree pNode) {
		if (pNode.isLeaf()) {
			if (pNode.m_iRef > 0) {
				pNode.m_Red /= pNode.m_iRef;
				pNode.m_Green /= pNode.m_iRef;
				pNode.m_Blue /= pNode.m_iRef;
			}
			pNode.m_iIndex = this.m_PalIndex;
			short color = (short) (pNode.m_Red >> 3);
			color = (short) (color << 6 | pNode.m_Green >> 2);
			color = (short) (color << 5 | pNode.m_Blue >> 3);
			this.Pal[this.m_PalIndex] = color;
		} else {
			for (int i = 0; i < 8; i++) {
				if (pNode.pChildren[i] != null) {
					BuildPalIt(pNode.pChildren[i]);
				}
			}
		}
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\encoder\OcTreePal.class
 * Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */