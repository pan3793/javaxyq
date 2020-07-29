/*     */ package com.wildbean.wastools.core;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LayerPreferenceDlg
/*     */   extends JDialog
/*     */   implements ActionListener
/*     */ {
/*     */   private JLabel jLabel1;
/*     */   private JTextField jtfLayerName;
/*     */   private CanvasImage image;
/*     */   private JLabel jLabel2;
/*     */   private JLabel jLabel3;
/*     */   private JLabel jLabel4;
/*     */   private JLabel lblSize;
/*     */   private JLabel lblInfo;
/*     */   private JLabel jLabel5;
/*     */   private JButton btnCancel;
/*     */   private JButton btnOK;
/*     */   private JSeparator jSeparator2;
/*     */   private JLabel jLabel6;
/*     */   private JLabel lblFrameCount;
/*     */   private JLabel lblLacation;
/*     */   private JLabel lblType;
/*     */   private JSeparator jSeparator1;
/*     */   
/*     */   public LayerPreferenceDlg(JFrame frame, CanvasImage image)
/*     */   {
/*  44 */     super(frame, true);
/*  45 */     this.image = image;
/*  46 */     initGUI();
/*  47 */     setLocationRelativeTo(frame);
/*  48 */     this.jtfLayerName.setText(image.getName());
/*  49 */     this.lblType.setText(image.getTypeName());
/*  50 */     this.lblLacation.setText(image.getX() + "," + image.getY());
/*  51 */     this.lblSize.setText(image.getWidth() + "," + image.getHeight());
/*  52 */     this.lblFrameCount.setText(String.valueOf(image.getFrameCount()));
/*  53 */     this.lblInfo.setText(image.getInfo());
/*     */   }
/*     */   
/*     */   private void initGUI() {
/*     */     try {
/*  58 */       setDefaultCloseOperation(2);
/*  59 */       setTitle("图层属性");
/*  60 */       getContentPane().setLayout(null);
/*     */       
/*  62 */       this.jLabel1 = new JLabel();
/*  63 */       getContentPane().add(this.jLabel1);
/*  64 */       this.jLabel1.setText("图层名称");
/*  65 */       this.jLabel1.setBounds(14, 14, 63, 28);
/*     */       
/*     */ 
/*  68 */       this.jtfLayerName = new JTextField();
/*  69 */       getContentPane().add(this.jtfLayerName);
/*  70 */       this.jtfLayerName.setBounds(77, 14, 294, 28);
/*     */       
/*     */ 
/*  73 */       this.jSeparator1 = new JSeparator();
/*  74 */       getContentPane().add(this.jSeparator1);
/*  75 */       this.jSeparator1.setBounds(14, 56, 357, 7);
/*     */       
/*     */ 
/*  78 */       this.jLabel2 = new JLabel();
/*  79 */       getContentPane().add(this.jLabel2);
/*  80 */       this.jLabel2.setText("类型");
/*  81 */       this.jLabel2.setBounds(14, 70, 63, 28);
/*     */       
/*     */ 
/*  84 */       this.lblType = new JLabel();
/*  85 */       getContentPane().add(this.lblType);
/*  86 */       this.lblType.setText("type");
/*  87 */       this.lblType.setBounds(77, 70, 294, 28);
/*     */       
/*     */ 
/*  90 */       this.jLabel3 = new JLabel();
/*  91 */       getContentPane().add(this.jLabel3);
/*  92 */       this.jLabel3.setText("位置");
/*  93 */       this.jLabel3.setBounds(14, 98, 63, 28);
/*     */       
/*     */ 
/*  96 */       this.jLabel4 = new JLabel();
/*  97 */       getContentPane().add(this.jLabel4);
/*  98 */       this.jLabel4.setText("大小");
/*  99 */       this.jLabel4.setBounds(14, 126, 63, 28);
/*     */       
/*     */ 
/* 102 */       this.lblLacation = new JLabel();
/* 103 */       getContentPane().add(this.lblLacation);
/* 104 */       this.lblLacation.setText("x,y");
/* 105 */       this.lblLacation.setBounds(77, 98, 294, 28);
/*     */       
/*     */ 
/* 108 */       this.lblSize = new JLabel();
/* 109 */       getContentPane().add(this.lblSize);
/* 110 */       this.lblSize.setText("w,h");
/* 111 */       this.lblSize.setBounds(77, 126, 294, 28);
/*     */       
/*     */ 
/* 114 */       this.jLabel5 = new JLabel();
/* 115 */       getContentPane().add(this.jLabel5);
/* 116 */       this.jLabel5.setText("帧数");
/* 117 */       this.jLabel5.setBounds(14, 154, 63, 28);
/*     */       
/*     */ 
/* 120 */       this.lblFrameCount = new JLabel();
/* 121 */       getContentPane().add(this.lblFrameCount);
/* 122 */       this.lblFrameCount.setText("count");
/* 123 */       this.lblFrameCount.setBounds(77, 154, 301, 28);
/*     */       
/*     */ 
/* 126 */       this.jLabel6 = new JLabel();
/* 127 */       getContentPane().add(this.jLabel6);
/* 128 */       this.jLabel6.setText("其它");
/* 129 */       this.jLabel6.setBounds(14, 182, 63, 28);
/*     */       
/*     */ 
/* 132 */       this.lblInfo = new JLabel();
/* 133 */       getContentPane().add(this.lblInfo);
/* 134 */       this.lblInfo.setText("info");
/* 135 */       this.lblInfo.setBounds(77, 182, 301, 28);
/*     */       
/*     */ 
/* 138 */       this.jSeparator2 = new JSeparator();
/* 139 */       getContentPane().add(this.jSeparator2);
/* 140 */       this.jSeparator2.setBounds(14, 210, 357, 7);
/*     */       
/*     */ 
/* 143 */       this.btnOK = new JButton();
/* 144 */       getContentPane().add(this.btnOK);
/* 145 */       this.btnOK.setText("OK");
/* 146 */       this.btnOK.setBounds(70, 224, 84, 28);
/* 147 */       this.btnOK.addActionListener(this);
/*     */       
/*     */ 
/* 150 */       this.btnCancel = new JButton();
/* 151 */       getContentPane().add(this.btnCancel);
/* 152 */       this.btnCancel.setText("Cancel");
/* 153 */       this.btnCancel.setBounds(252, 224, 84, 28);
/* 154 */       this.btnCancel.addActionListener(this);
/*     */       
/* 156 */       setSize(400, 300);
/*     */     } catch (Exception e) {
/* 158 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 163 */     Object source = e.getSource();
/* 164 */     if (source == this.btnOK) {
/* 165 */       this.image.setName(this.jtfLayerName.getText());
/* 166 */       dispose();
/* 167 */     } else if (source == this.btnCancel) {
/* 168 */       dispose();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\core\LayerPreferenceDlg.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */