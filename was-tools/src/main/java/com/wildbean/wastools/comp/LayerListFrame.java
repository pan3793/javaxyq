/*    */ package com.wildbean.wastools.comp;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Container;
/*    */ import javax.swing.DefaultComboBoxModel;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JTextField;
/*    */ import javax.swing.ListModel;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LayerListFrame
/*    */   extends JFrame
/*    */ {
/*    */   private JList layerList;
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 33 */     LayerListFrame inst = new LayerListFrame();
/* 34 */     inst.setVisible(true);
/*    */   }
/*    */   
/*    */   public LayerListFrame()
/*    */   {
/* 39 */     initGUI();
/* 40 */     setLocationRelativeTo(null);
/*    */   }
/*    */   
/*    */   private void initGUI() {
/*    */     try {
/* 45 */       setDefaultCloseOperation(2);
/* 46 */       setTitle("LayerListTest");
/*    */       
/* 48 */       JScrollPane scrollPanel = new JScrollPane();
/* 49 */       getContentPane().add(scrollPanel, "Center");
/*    */       
/* 51 */       ListModel listModel = new DefaultComboBoxModel(new String[] { "图层1", "图层2", "图层3", "图层4" });
/* 52 */       this.layerList = new JList(listModel);
/* 53 */       this.layerList.setBackground(new Color(238, 238, 238));
/* 54 */       this.layerList.setDragEnabled(true);
/* 55 */       this.layerList.setCellRenderer(new LayerListCellRenderer());
/* 56 */       scrollPanel.setViewportView(this.layerList);
/*    */       
/* 58 */       getContentPane().add(new JTextField("test"), "South");
/*    */       
/*    */ 
/* 61 */       pack();
/* 62 */       setSize(230, 300);
/*    */     } catch (Exception e) {
/* 64 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\comp\LayerListFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */