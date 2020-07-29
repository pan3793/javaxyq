/*    */ package com.wildbean.wastools.comp;
/*    */ 
/*    */ import javax.swing.tree.DefaultMutableTreeNode;
/*    */ 
/*    */ public class StringFilter implements TreeFilter {
/*  6 */   boolean pass = false;
/*    */   private String included;
/*    */   
/*    */   public boolean pass(Object obj)
/*    */   {
/* 11 */     if (this.pass)
/* 12 */       return true;
/* 13 */     if ((this.included != null) && (this.included.length() > 0) && ((obj instanceof DefaultMutableTreeNode))) {
/* 14 */       DefaultMutableTreeNode node = (DefaultMutableTreeNode)obj;
/* 15 */       if (node.isLeaf()) {
/* 16 */         String nodeText = node.toString();
/* 17 */         return nodeText.indexOf(this.included) >= 0;
/*    */       }
/*    */     }
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public void setFiltered(boolean pass) {
/* 24 */     this.pass = pass;
/*    */   }
/*    */   
/*    */   public boolean isFiltered() {
/* 28 */     return this.pass;
/*    */   }
/*    */   
/*    */   public void setIncluded(String included) {
/* 32 */     this.included = included;
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\comp\StringFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */