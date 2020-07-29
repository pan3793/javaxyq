/*     */ package com.wildbean.wastools.core;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CanvasPreferences
/*     */   extends JDialog
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private JLabel colorLabel;
/*     */   private JButton cancelButton;
/*     */   private JButton okButton;
/*     */   private JRadioButton transparentButton;
/*     */   private JTextField jtfName;
/*     */   private JComboBox cbxSize;
/*     */   private JRadioButton colorButton;
/*     */   private JTextField widthText;
/*     */   private JTextField heightText;
/*     */   private Canvas canvas;
/*     */   private static int count;
/*     */   private String canvasName;
/*     */   protected int canvasHeight;
/*     */   protected int canvasWidth;
/*     */   protected boolean approve;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*  83 */     CanvasPreferences inst = new CanvasPreferences(null);
/*  84 */     inst.setVisible(true);
/*     */   }
/*     */   
/*     */   public void showDialog(Canvas canvas) {
/*  88 */     this.canvas = canvas;
/*  89 */     if (canvas != null) {
/*  90 */       this.widthText.setText(String.valueOf(canvas.getCanvasWidth()));
/*  91 */       this.heightText.setText(String.valueOf(canvas.getCanvasHeight()));
/*  92 */       this.colorLabel.setBackground(canvas.getCanvasBackground());
/*  93 */       this.colorButton.setSelected(!canvas.isTransparent());
/*  94 */       this.transparentButton.setSelected(canvas.isTransparent());
/*  95 */       this.jtfName.setText(canvas.getCanvasName());
/*  96 */       this.jtfName.setEditable(false);
/*  97 */       this.cbxSize.setSelectedIndex(5);
/*     */     } else {
/*  99 */       this.jtfName.setText("未命名-" + ++count);
/* 100 */       this.jtfName.setEditable(true);
/* 101 */       this.cbxSize.setSelectedIndex(0);
/* 102 */       this.transparentButton.setSelected(true);
/*     */     }
/* 104 */     this.approve = false;
/* 105 */     setLocationRelativeTo(getParent());
/* 106 */     setVisible(true);
/*     */   }
/*     */   
/*     */   public CanvasPreferences(JFrame parent) {
/* 110 */     super(parent, true);
/* 111 */     initGUI();
/*     */   }
/*     */   
/*     */   private void initGUI()
/*     */   {
/*     */     try {
/* 117 */       setTitle("画布属性设置");
/* 118 */       setResizable(false);
/* 119 */       setModal(true);
/* 120 */       setDefaultCloseOperation(2);
/* 121 */       getContentPane().setLayout(null);
/*     */       
/* 123 */       JPanel jPanel1 = new JPanel();
/* 124 */       getContentPane().add(jPanel1);
/* 125 */       jPanel1.setBounds(14, 147, 364, 77);
/* 126 */       jPanel1.setBorder(BorderFactory.createTitledBorder("画布背景"));
/* 127 */       jPanel1.setLayout(null);
/*     */       
/* 129 */       this.colorButton = new JRadioButton();
/* 130 */       jPanel1.add(this.colorButton);
/* 131 */       this.colorButton.setText("纯色(P)");
/* 132 */       this.colorButton.setMnemonic('P');
/* 133 */       this.colorButton.setBounds(14, 21, 84, 21);
/*     */       
/*     */ 
/* 136 */       this.transparentButton = new JRadioButton();
/* 137 */       jPanel1.add(this.transparentButton);
/* 138 */       this.transparentButton.setText("透明(T)");
/* 139 */       this.transparentButton.setMnemonic('T');
/* 140 */       this.transparentButton.setBounds(14, 49, 84, 21);
/*     */       
/*     */ 
/* 143 */       this.colorLabel = new JLabel();
/* 144 */       this.colorLabel.setCursor(new Cursor(12));
/* 145 */       jPanel1.add(this.colorLabel);
/* 146 */       this.colorLabel.setText("请选择颜色");
/* 147 */       this.colorLabel.setBounds(98, 21, 231, 21);
/* 148 */       this.colorLabel.setBackground(new Color(0, 64, 128));
/* 149 */       this.colorLabel.setOpaque(true);
/* 150 */       this.colorLabel.setForeground(new Color(255, 255, 255));
/* 151 */       this.colorLabel.addMouseListener(new MouseAdapter() {
/*     */         public void mouseClicked(MouseEvent e) {
/* 153 */           if (CanvasPreferences.this.colorLabel.isEnabled()) {
/* 154 */             Color c = JColorChooser.showDialog(CanvasPreferences.this, 
/* 155 */               "请选择面板背景颜色", CanvasPreferences.this.colorLabel.getBackground());
/* 156 */             if (c != null) {
/* 157 */               CanvasPreferences.this.colorLabel.setBackground(c);
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/* 163 */       });
/* 164 */       ButtonGroup bg = new ButtonGroup();
/* 165 */       this.colorButton.addItemListener(new ItemListener() {
/*     */         public void itemStateChanged(ItemEvent evt) {
/* 167 */           CanvasPreferences.this.colorLabel.setEnabled(CanvasPreferences.this.colorButton.isSelected());
/*     */         }
/* 169 */       });
/* 170 */       bg.add(this.transparentButton);
/* 171 */       bg.add(this.colorButton);
/* 172 */       bg.setSelected(this.colorButton.getModel(), true);
/*     */       
/*     */ 
/*     */ 
/* 176 */       this.okButton = new JButton();
/* 177 */       getContentPane().add(this.okButton);
/* 178 */       this.okButton.setText("确定(O)");
/* 179 */       this.okButton.setMnemonic('O');
/* 180 */       this.okButton.setBounds(63, 231, 91, 28);
/* 181 */       this.okButton.addActionListener(new ActionListener() {
/*     */         public void actionPerformed(ActionEvent evt) {
/*     */           try {
/* 184 */             CanvasPreferences.this.canvasWidth = Integer.parseInt(CanvasPreferences.this.widthText.getText());
/* 185 */             CanvasPreferences.this.canvasHeight = Integer.parseInt(CanvasPreferences.this.heightText.getText());
/* 186 */             if ((CanvasPreferences.this.canvasWidth <= 0) || (CanvasPreferences.this.canvasHeight <= 0)) {
/* 187 */               JOptionPane.showMessageDialog(CanvasPreferences.this.getParent(), "画布宽度和高度必须>0!", 
/* 188 */                 "提示", 2);
/* 189 */               return;
/*     */             }
/* 191 */             if (CanvasPreferences.this.canvas != null) {
/* 192 */               CanvasPreferences.this.canvas.setCanvasSize(CanvasPreferences.this.canvasWidth, CanvasPreferences.this.canvasHeight, true);
/* 193 */               CanvasPreferences.this.canvas.setCanvasBackground(CanvasPreferences.this.colorLabel.getBackground());
/* 194 */               CanvasPreferences.this.canvas.setTransparent(CanvasPreferences.this.transparentButton.isSelected());
/*     */               
/*     */ 
/* 197 */               CanvasPreferences.this.canvas.firePreferencesChange();
/*     */             }
/* 199 */             CanvasPreferences.this.approve = true;
/* 200 */             CanvasPreferences.this.dispose();
/*     */           } catch (Exception e) {
/* 202 */             e.printStackTrace();
/* 203 */             JOptionPane.showMessageDialog(CanvasPreferences.this.getParent(), "输入有错误,请检查相关数字!", "错误", 
/* 204 */               0);
/*     */           }
/*     */           
/*     */         }
/*     */         
/* 209 */       });
/* 210 */       this.cancelButton = new JButton();
/* 211 */       getContentPane().add(this.cancelButton);
/* 212 */       this.cancelButton.setText("取消(C)");
/* 213 */       this.cancelButton.setMnemonic('C');
/* 214 */       this.cancelButton.setBounds(238, 231, 91, 28);
/* 215 */       this.cancelButton.addActionListener(new ActionListener() {
/*     */         public void actionPerformed(ActionEvent evt) {
/* 217 */           CanvasPreferences.this.dispose();
/*     */         }
/*     */         
/*     */ 
/* 221 */       });
/* 222 */       JPanel jPanel2 = new JPanel();
/* 223 */       getContentPane().add(jPanel2);
/* 224 */       jPanel2.setBounds(14, 42, 364, 105);
/* 225 */       jPanel2.setBorder(
/* 226 */         BorderFactory.createTitledBorder(null, "画布大小", 4, 2));
/* 227 */       jPanel2.setLayout(null);
/*     */       
/* 229 */       this.widthText = new JTextField();
/* 230 */       jPanel2.add(this.widthText);
/* 231 */       this.widthText.setBounds(77, 49, 105, 21);
/*     */       
/*     */ 
/* 234 */       JLabel jLabel1 = new JLabel();
/* 235 */       jPanel2.add(jLabel1);
/* 236 */       jLabel1.setText("宽度(W):");
/* 237 */       jLabel1.setDisplayedMnemonic('W');
/* 238 */       jLabel1.setLabelFor(this.widthText);
/* 239 */       jLabel1.setBounds(14, 49, 63, 21);
/*     */       
/*     */ 
/* 242 */       this.heightText = new JTextField();
/* 243 */       jPanel2.add(this.heightText);
/* 244 */       this.heightText.setBounds(77, 77, 105, 21);
/*     */       
/*     */ 
/* 247 */       JLabel jLabel2 = new JLabel();
/* 248 */       jPanel2.add(jLabel2);
/* 249 */       jLabel2.setText("高度(H):");
/* 250 */       jLabel2.setDisplayedMnemonic('H');
/* 251 */       jLabel2.setLabelFor(this.heightText);
/* 252 */       jLabel2.setBounds(14, 77, 63, 21);
/*     */       
/*     */ 
/* 255 */       ComboBoxModel cbxSizeModel = new DefaultComboBoxModel(new String[] {
/* 256 */         "默认(320 x 240)", "矩形(180 x 150)", "中等矩形(300 x 250)", 
/* 257 */         "大矩形(336 x 280)", "方形(250 x 250)", "自定" });
/* 258 */       this.cbxSize = new JComboBox();
/* 259 */       jPanel2.add(this.cbxSize);
/* 260 */       this.cbxSize.setModel(cbxSizeModel);
/* 261 */       this.cbxSize.setBounds(77, 21, 252, 21);
/* 262 */       this.cbxSize.addActionListener(new ActionListener() {
/*     */         public void actionPerformed(ActionEvent e) {
/* 264 */           int index = CanvasPreferences.this.cbxSize.getSelectedIndex();
/* 265 */           switch (index) {
/*     */           case 0: 
/* 267 */             CanvasPreferences.this.widthText.setText("320");
/* 268 */             CanvasPreferences.this.heightText.setText("240");
/* 269 */             break;
/*     */           case 1: 
/* 271 */             CanvasPreferences.this.widthText.setText("180");
/* 272 */             CanvasPreferences.this.heightText.setText("150");
/* 273 */             break;
/*     */           case 2: 
/* 275 */             CanvasPreferences.this.widthText.setText("300");
/* 276 */             CanvasPreferences.this.heightText.setText("250");
/* 277 */             break;
/*     */           case 3: 
/* 279 */             CanvasPreferences.this.widthText.setText("336");
/* 280 */             CanvasPreferences.this.heightText.setText("280");
/* 281 */             break;
/*     */           case 4: 
/* 283 */             CanvasPreferences.this.widthText.setText("250");
/* 284 */             CanvasPreferences.this.heightText.setText("250");
/* 285 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */         }
/* 292 */       });
/* 293 */       JLabel jLabel3 = new JLabel();
/* 294 */       jPanel2.add(jLabel3);
/* 295 */       jLabel3.setText("大小(S):");
/* 296 */       jLabel3.setDisplayedMnemonic('S');
/* 297 */       jLabel3.setLabelFor(this.cbxSize);
/* 298 */       jLabel3.setBounds(14, 21, 63, 21);
/*     */       
/*     */ 
/* 301 */       JLabel jLabel5 = new JLabel();
/* 302 */       jPanel2.add(jLabel5);
/* 303 */       jLabel5.setText("像素");
/* 304 */       jLabel5.setBounds(189, 49, 63, 21);
/*     */       
/*     */ 
/* 307 */       JLabel jLabel6 = new JLabel();
/* 308 */       jPanel2.add(jLabel6);
/* 309 */       jLabel6.setText("像素");
/* 310 */       jLabel6.setBounds(189, 77, 63, 21);
/*     */       
/*     */ 
/*     */ 
/* 314 */       this.jtfName = new JTextField();
/* 315 */       getContentPane().add(this.jtfName);
/* 316 */       this.jtfName.setBounds(77, 14, 266, 21);
/*     */       
/*     */ 
/* 319 */       JLabel jLabel4 = new JLabel();
/* 320 */       getContentPane().add(jLabel4);
/* 321 */       jLabel4.setText("名称(N):");
/* 322 */       jLabel4.setDisplayedMnemonic('N');
/* 323 */       jLabel4.setLabelFor(this.jtfName);
/* 324 */       jLabel4.setBounds(14, 14, 63, 21);
/*     */       
/*     */ 
/* 327 */       setSize(400, 300);
/*     */     } catch (Exception e) {
/* 329 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public Canvas getCanvas() {
/* 334 */     Canvas canvas = null;
/* 335 */     if (this.approve) {
/* 336 */       canvas = new Canvas(this.canvasWidth, this.canvasHeight);
/* 337 */       canvas.setCanvasBackground(this.colorLabel.getBackground());
/* 338 */       canvas.setCanvasName(this.jtfName.getText());
/* 339 */       canvas.setTransparent(this.transparentButton.isSelected());
/*     */     }
/* 341 */     return canvas;
/*     */   }
/*     */ }


/* Location:              D:\Desktop\WasTools2.0-all.jar!\com\wildbean\wastools\core\CanvasPreferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */