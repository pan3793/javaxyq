/*    */ package com.wildbean.wastools.comp;
/*    */ 
/*    */ import javax.swing.tree.DefaultMutableTreeNode;
/*    */ import javax.swing.tree.DefaultTreeModel;
/*    */ import javax.swing.tree.TreeNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilterTreeModel
/*    */   extends DefaultTreeModel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   TreeFilter filter;
/*    */   
/*    */   public FilterTreeModel(TreeNode root, TreeFilter filter)
/*    */   {
/* 18 */     super(root);
/* 19 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public void setFiltered(boolean pass) {
/* 23 */     this.filter.setFiltered(pass);
/* 24 */     Object[] path = { this.root };
/* 25 */     int[] childIndices = new int[this.root.getChildCount()];
/* 26 */     Object[] children = new Object[this.root.getChildCount()];
/* 27 */     for (int i = 0; i < this.root.getChildCount(); i++) {
/* 28 */       childIndices[i] = i;
/* 29 */       children[i] = this.root.getChildAt(i);
/*    */     }
/* 31 */     fireTreeStructureChanged(this, path, childIndices, children);
/*    */   }
/*    */   
/*    */   public void setIncluded(String included) {
/* 35 */     this.filter.setIncluded(included);
/* 36 */     Object[] path = { this.root };
/* 37 */     int[] childIndices = new int[this.root.getChildCount()];
/* 38 */     Object[] children = new Object[this.root.getChildCount()];
/* 39 */     for (int i = 0; i < this.root.getChildCount(); i++) {
/* 40 */       childIndices[i] = i;
/* 41 */       children[i] = this.root.getChildAt(i);
/*    */     }
/* 43 */     fireTreeStructureChanged(this, path, childIndices, children);
/*    */   }
/*    */   
/*    */   public int getChildCount(Object parent) {
/* 47 */     int realCount = super.getChildCount(parent);int filterCount = 0;
/*    */     
/* 49 */     for (int i = 0; i < realCount; i++) {
/* 50 */       DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)super.getChild(parent, i);
/* 51 */       if (this.filter.pass(dmtn))
/* 52 */         filterCount++;
/*    */     }
/* 54 */     return filterCount;
/*    */   }
/*    */   
/*    */   public Object getChild(Object parent, int index) {
/* 58 */     int cnt = -1;
/* 59 */     for (int i = 0; i < super.getChildCount(parent); i++) {
/* 60 */       Object child = super.getChild(parent, i);
/* 61 */       if (this.filter.pass(child))
/* 62 */         cnt++;
/* 63 */       if (cnt == index)
/* 64 */         return child;
/*    */     }
/* 66 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\comp\FilterTreeModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */