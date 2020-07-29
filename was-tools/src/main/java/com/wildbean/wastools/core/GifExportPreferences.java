/*     */ package com.wildbean.wastools.core;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GifExportPreferences
/*     */   extends JDialog
/*     */ {
/*     */   private JRadioButton jLabel1;
/*     */   private JPanel jPanel1;
/*     */   private JLabel jLabel4;
/*     */   private JLabel jLabel8;
/*     */   private JLabel jLabel7;
/*     */   private JLabel jLabel6;
/*     */   private JButton jButton3;
/*     */   private JButton jButton2;
/*     */   private JButton jButton1;
/*     */   private JTextField jTextField4;
/*     */   private JLabel jLabel5;
/*     */   private JComboBox jComboBox1;
/*     */   private JTextField jTextField3;
/*     */   private JLabel jLabel3;
/*     */   private JLabel jLabel2;
/*     */   private JCheckBox jCheckBox1;
/*     */   private JTextField jTextField2;
/*     */   private JRadioButton jRadioButton1;
/*     */   private JTextField jTextField1;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*  55 */     JFrame frame = new JFrame();
/*  56 */     GifExportPreferences inst = new GifExportPreferences(frame);
/*  57 */     inst.setVisible(true);
/*     */   }
/*     */   
/*     */   public GifExportPreferences(JFrame frame) {
/*  61 */     super(frame);
/*  62 */     initGUI();
/*  63 */     setLocationRelativeTo(frame);
/*     */   }
/*     */   
/*     */   public GifExportPreferences(JFrame parent, Canvas canvas) {
/*  67 */     initGUI();
/*     */     
/*     */ 
/*  70 */     setLocationRelativeTo(parent);
/*     */   }
/*     */   
/*     */   private void initGUI()
/*     */   {
/*     */     try {
/*  76 */       setTitle("导出GIF");
/*  77 */       setResizable(false);
/*  78 */       setDefaultCloseOperation(2);
/*  79 */       getContentPane().setLayout(null);
/*     */       
/*  81 */       this.jPanel1 = new JPanel();
/*  82 */       this.jPanel1.setLayout(null);
/*  83 */       getContentPane().add(this.jPanel1);
/*  84 */       this.jPanel1.setBounds(14, 14, 364, 77);
/*  85 */       this.jPanel1.setBorder(BorderFactory.createTitledBorder("延时"));
/*     */       
/*  87 */       this.jLabel1 = new JRadioButton();
/*  88 */       this.jPanel1.add(this.jLabel1);
/*  89 */       this.jLabel1.setText("每帖延时(ms)");
/*  90 */       this.jLabel1.setBounds(14, 21, 105, 21);
/*     */       
/*     */ 
/*  93 */       this.jTextField1 = new JTextField();
/*  94 */       this.jPanel1.add(this.jTextField1);
/*  95 */       this.jTextField1.setText("100");
/*  96 */       this.jTextField1.setBounds(126, 21, 91, 21);
/*     */       
/*     */ 
/*  99 */       this.jRadioButton1 = new JRadioButton();
/* 100 */       this.jPanel1.add(this.jRadioButton1);
/* 101 */       this.jRadioButton1.setText("每秒帖数(fps)");
/* 102 */       this.jRadioButton1.setBounds(14, 49, 105, 21);
/*     */       
/*     */ 
/* 105 */       this.jTextField2 = new JTextField();
/* 106 */       this.jPanel1.add(this.jTextField2);
/* 107 */       this.jTextField2.setText("10");
/* 108 */       this.jTextField2.setBounds(126, 49, 91, 21);
/*     */       
/*     */ 
/* 111 */       this.jLabel8 = new JLabel();
/* 112 */       this.jPanel1.add(this.jLabel8);
/* 113 */       this.jLabel8.setText("(delay=1000/fps)");
/* 114 */       this.jLabel8.setBounds(224, 49, 119, 21);
/*     */       
/*     */ 
/*     */ 
/* 118 */       this.jCheckBox1 = new JCheckBox();
/* 119 */       getContentPane().add(this.jCheckBox1);
/* 120 */       this.jCheckBox1.setText("透明颜色");
/* 121 */       this.jCheckBox1.setBounds(14, 98, 84, 21);
/*     */       
/*     */ 
/* 124 */       this.jLabel2 = new JLabel();
/* 125 */       getContentPane().add(this.jLabel2);
/* 126 */       this.jLabel2.setText("请选择颜色");
/* 127 */       this.jLabel2.setBounds(105, 98, 126, 21);
/* 128 */       this.jLabel2.setHorizontalAlignment(0);
/* 129 */       this.jLabel2.setForeground(new Color(255, 255, 255));
/* 130 */       this.jLabel2.setBackground(new Color(0, 0, 0));
/* 131 */       this.jLabel2.setOpaque(true);
/*     */       
/*     */ 
/* 134 */       this.jLabel3 = new JLabel();
/* 135 */       getContentPane().add(this.jLabel3);
/* 136 */       this.jLabel3.setText("播放次数");
/* 137 */       this.jLabel3.setBounds(21, 154, 77, 21);
/*     */       
/*     */ 
/* 140 */       this.jTextField3 = new JTextField();
/* 141 */       getContentPane().add(this.jTextField3);
/* 142 */       this.jTextField3.setText("0");
/* 143 */       this.jTextField3.setBounds(105, 154, 126, 21);
/*     */       
/*     */ 
/* 146 */       this.jLabel4 = new JLabel();
/* 147 */       getContentPane().add(this.jLabel4);
/* 148 */       this.jLabel4.setText("GIF质量");
/* 149 */       this.jLabel4.setBounds(21, 126, 77, 21);
/*     */       
/*     */ 
/* 152 */       ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "Item One", "Item Two" });
/* 153 */       this.jComboBox1 = new JComboBox();
/* 154 */       getContentPane().add(this.jComboBox1);
/* 155 */       this.jComboBox1.setModel(jComboBox1Model);
/* 156 */       this.jComboBox1.setBounds(105, 126, 126, 21);
/*     */       
/*     */ 
/* 159 */       this.jLabel5 = new JLabel();
/* 160 */       getContentPane().add(this.jLabel5);
/* 161 */       this.jLabel5.setText("保存路径");
/* 162 */       this.jLabel5.setBounds(21, 189, 77, 21);
/*     */       
/*     */ 
/* 165 */       this.jTextField4 = new JTextField();
/* 166 */       getContentPane().add(this.jTextField4);
/* 167 */       this.jTextField4.setBounds(105, 189, 252, 21);
/*     */       
/*     */ 
/* 170 */       this.jButton1 = new JButton();
/* 171 */       getContentPane().add(this.jButton1);
/* 172 */       this.jButton1.setText("...");
/* 173 */       this.jButton1.setBounds(357, 189, 21, 21);
/*     */       
/*     */ 
/* 176 */       this.jButton2 = new JButton();
/* 177 */       getContentPane().add(this.jButton2);
/* 178 */       this.jButton2.setText("确定");
/* 179 */       this.jButton2.setBounds(70, 231, 84, 28);
/*     */       
/*     */ 
/* 182 */       this.jButton3 = new JButton();
/* 183 */       getContentPane().add(this.jButton3);
/* 184 */       this.jButton3.setText("取消");
/* 185 */       this.jButton3.setBounds(224, 231, 84, 28);
/*     */       
/*     */ 
/* 188 */       this.jLabel6 = new JLabel();
/* 189 */       getContentPane().add(this.jLabel6);
/* 190 */       this.jLabel6.setText("(0表示无限循环)");
/* 191 */       this.jLabel6.setBounds(238, 154, 140, 21);
/*     */       
/*     */ 
/* 194 */       this.jLabel7 = new JLabel();
/* 195 */       getContentPane().add(this.jLabel7);
/* 196 */       this.jLabel7.setText("(1-最好，20-最差)");
/* 197 */       this.jLabel7.setBounds(238, 126, 140, 21);
/*     */       
/*     */ 
/* 200 */       setSize(400, 300);
/*     */     } catch (Exception e) {
/* 202 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\core\GifExportPreferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */