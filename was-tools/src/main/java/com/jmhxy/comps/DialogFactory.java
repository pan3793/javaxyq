/*    */ package com.jmhxy.comps;
/*    */ 
/*    */ import com.jmhxy.core.ScriptLoader;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DialogFactory
/*    */ {
/* 17 */   private static TreeMap<String, GDialog> dialogs = new TreeMap();
/*    */   
/*    */ 
/* 20 */   private static TreeMap<String, String> dialogScripts = new TreeMap();
/*    */   
/*    */   public static GDialog getDialog(String id) {
/* 23 */     GDialog dialog = null;
/* 24 */     if ((id == null) || (id.length() == 0)) {
/* 25 */       return null;
/*    */     }
/* 27 */     dialog = (GDialog)dialogs.get(id);
/* 28 */     if (dialog != null) {
/* 29 */       return dialog;
/*    */     }
/* 31 */     String filename = (String)dialogScripts.get(id);
/* 32 */     if (filename != null) {
/* 33 */       ScriptLoader.load(filename);
/* 34 */       dialog = (GDialog)dialogs.get(id);
/*    */     }
/* 36 */     return dialog;
/*    */   }
/*    */   
/*    */   public static void addDialog(String id, GDialog dlg) {
/* 40 */     if ((id != null) && (dlg != null))
/* 41 */       dialogs.put(id, dlg);
/*    */   }
/*    */   
/*    */   public static void addScript(String id, String filename) {
/* 45 */     if ((id != null) && (filename != null)) {
/* 46 */       dialogScripts.put(id, filename);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\comps\DialogFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */