/*    */ package com.jmhxy.encoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WasFile
/*    */ {
/*    */   public int id;
/*    */   
/*    */ 
/*    */   public int size;
/*    */   
/*    */   protected int offset;
/*    */   
/*    */   protected int space;
/*    */   
/*    */   public String name;
/*    */   
/*    */   public WdfFile parent;
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 23 */     return this.name == null ? Integer.toHexString(this.id).toUpperCase() : this.name;
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\encoder\WasFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */