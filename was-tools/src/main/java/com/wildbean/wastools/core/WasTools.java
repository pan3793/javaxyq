package com.wildbean.wastools.core;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.Log;
import com.jmhxy.animation.WasFrame;
import com.jmhxy.animation.WasImage;
import com.jmhxy.core.SpriteFactory;
import com.jmhxy.encoder.WasDecoder;
import com.jmhxy.encoder.WasFile;
import com.jmhxy.encoder.WdfFile;
import com.jmhxy.util.BrowserLauncher;
import com.jmhxy.util.SeekByteArrayInputStream;
import com.jmhxy.util.Utils;
import com.wildbean.wastools.comp.EditableList;
import com.wildbean.wastools.comp.FilterTreeModel;
import com.wildbean.wastools.comp.LayerListCellEditor;
import com.wildbean.wastools.comp.LayerListCellRenderer;
import com.wildbean.wastools.comp.StringFilter;
import com.wildbean.wastools.comp.TreeFilter;
import com.wildbean.wastools.comp.WasIcon;
import com.wildbean.wastools.encoder.AnimatedGifEncoder;

public class WasTools extends JFrame {
	public static final String CMD_LAYER_CUT = "cut layer";
	public static final String CMD_LAYER_PASTE = "paste layer";
	private ImageIcon aboutIcon;
	private static final String APPLICATION_NAME = "Was Tools beta 2.0";
	private static final String CMD_ABOUT = "about message";
	private static final String CMD_ALIGN_BOTTOM = "align to bottom";
	private static final String CMD_ALIGN_HOR_CENTER = "align to hor center";
	private static final String CMD_ALIGN_LEFT = "align to left";
	private static final String CMD_ALIGN_RIGHT = "align to right";
	private static final String CMD_ALIGN_SPRITE = "align to sprite center";
	private static final String CMD_ALIGN_TOP = "align to top";
	private static final String CMD_ALIGN_VER_CENTER = "align to ver center";
	private static final String CMD_AUTO_SELECT = "auto select layer";
	private static final String CMD_CANVAS_PREFERENCES = "canvas preferences";
	private static final String CMD_CHANGED_WAS_NAME = "changed was name";
	private static final String CMD_CLEAR_WDF_SETTINGS = "clear wdf settings";
	private static final String CMD_CLOSE_ALL_CANVAS = "close all was animations";
	private static final String CMD_LAYER_DELETE = "Delete seleted CanvasImage";
	private static final String CMD_CLOSE_WDF = "close wdf";
	private static final String CMD_COLLAPSE_WDF = "collapse wdf";
	private static final String CMD_LAYER_COPY = "copy current layer";
	private static final String CMD_DISTILL_WAS = "distill was from wdf";
	private static final String CMD_DISTILL_WAS_CURRENT = "distill was & add to current canvas";
	private static final String CMD_EXIT = "exit applicatin";
	private static final String CMD_EXPAND_WDF = "expand wdf";
	private static final String CMD_EXPORT_CANVAS_GIF = "export canvas as a gif";
	private static final String CMD_EXPORT_CANVAS_PNG = "export canvas as pngs";
	private static final String CMD_EXPORT_WAS = "export was";
	private static final String CMD_EXPORT_WAS_PNG = "export was as png";
	private static final String CMD_GIF_EXPORT_PREFERENCES = "gif export preferences";
	private static final String CMD_HELP = "help message";
	private static final String CMD_HOME_PAGE = "visit home page";
	private static final String CMD_LOAD_WDF_SETTINGS = "load wdf settings";
	private static final String CMD_NEW_CANVAS = "new canvas";
	private static final String CMD_OPEN = "open files";
	private static final String CMD_PREVIEW_CANVAS_PREFERENCES = "preview canvas preferences";
	private static final String CMD_PREVIEW_WAS = "preview was";
	private static final String CMD_SAVE_WDF_SETTINGS = "save wdf settings";
	private static final String CMD_SHOW_BOUNDARY = "whether show the selected layer's boundary";
	private static final String CMD_SORT_BY_ID = "sort by id";
	private static final String CMD_SORT_BY_NAME = "sort by name";
	private static final String CMD_SORT_BY_SIZE = "sort by size";
	private static final String CMD_VISIBLE_LINKED = "visible of all linked layers";
	private static final String CMD_VISIBLE_NON_LINKED = "visible of all non-linked layers";
	private static final String CMD_VISIBLE_OTHERS = "visible of other layers";
	private static final DataFlavor delfaultFlavor = new DataFlavor(List.class, "List");
	public static final int FRAME_INTERVAL = 100;
	private static final String PROPERTY_AUTO_SELECT = "AutoSelect";
	private static final String PROPERTY_SHOW_BOUNDARY = "ShowBoundary";
	private static final long serialVersionUID = 1L;
	private static final String SETTINGS_FILENAME = "Was Tools.ini";
	private static final String CMD_TEXT = "create plain/mhxy text";
	private static final String CMD_CLOSE = "close current canvas";
	private static final String CMD_SAVE = "save current canvas to file";
	private static final String CMD_SAVE_AS = "save as";
	private static final String CMD_SAVE_ALL = "save all";
	private static final String CMD_LAYER_PREFERENCE = "layer settings";
	private JButton btnAlignBottom;
	private JButton btnAlignHorCenter;
	private JButton btnAlignLeft;
	private JButton btnAlignRight;
	private JButton btnAlignSprite;
	private JButton btnAlignTop;
	private JButton btnAlignVerCenter;
	private JCheckBox btnAutoSelect;
	private JCheckBox btnShowBoundary;
	private JPopupMenu canvasPopupMenu;
	private Canvas curCanvas;
	private JDesktopPane desktop;
	private EventHandler eventHandler = new EventHandler();
	private JPopupMenu filelistPopupMenu;
	private JTree filelistTree;
	private DefaultMutableTreeNode filelistTreeRoot;
	private StringFilter filter = new StringFilter();
	private WasFilterTreeModel filterModel;
	private JTextField filterTextField;
	private JLabel hitsLabel;
	private LayerListTransferHandler layerListTransferHandler = new LayerListTransferHandler();
	private JPopupMenu layerPopupMenu;
	private JSplitPane listPanel;
	private JMenuBar mainMenuBar;
	private PreviewCanvas previewCanvas;
	private JPopupMenu previewCcanvasPopupMenu;
	private WasFile previewNode;
	private JToolBar toolBar;
	private JPanel toolPanel;
	private JTextField wasfileNameField;
	private JTextArea layerInfoText;
	private EditableList layerList;
	private DefaultListModel layerListModel;
	private JScrollPane layerListPanel;

	public static void main(String[] args) {
	
		Utils.iniGlobalFont();
		WasTools inst = new WasTools();
		inst.setVisible(true);
		if ((args != null) && (args.length > 0)){
			try {
				File file = new File(args[0]);
				if (file.isDirectory()) {
					// 最小化运行
					inst.setExtendedState(ICONIFIED);
					// 传入的参数是目录时，自动转化目录下所有was文件为gif
					wasToGif(inst, new File(args[0]));
				} else if (file.getAbsolutePath().endsWith(".wdf") || file.getAbsolutePath().endsWith(".WDF")) {
					inst.openWdfFile(new File(args[0]));
					inst.filelistTree.updateUI();
				} else if (file.getAbsolutePath().endsWith(".was") || file.getAbsolutePath().endsWith(".WAS")) {
					inst.openWasFile(new FileInputStream(args[0]), args[0], true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(inst, "打开文件失败:" + e.getMessage(), "Error", 0);
			} 
		}
	}

	/**
	 * 转换目录下的所有was文为gif
	 * 
	 * @param tools
	 *            WasTools工具
	 * @param dir
	 *            要转换的目录
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	public static void wasToGif(WasTools tools, File dir) throws IOException  {
		List<File> list = new ArrayList<>();
		// 统计目录下所有was文件
		countWasFile(tools, dir, list);
		// 转换文件
		if (list.size() > 0) {
			String wasFilePath;
			File gifFile;
			for (int i = 0; i < list.size(); i++) {
				try {
					// 判断gif文件是否存
					wasFilePath = list.get(i).getAbsolutePath();
					gifFile = new File(wasFilePath.substring(0, wasFilePath.lastIndexOf(".")) + ".gif");
					if (!gifFile.exists()) {
						System.out.println(Thread.currentThread().getName() + ":(" + (i + 1) + "/" + list.size() + ")当前文件-"
								+ list.get(i).getAbsolutePath());
						// 打开was文件
						tools.openWasFile(list.get(i));
						// 转换was文件到gif
						tools.exportCanvasAsGIF(gifFile);
						// 关闭was文件
						tools.closeCurrentWasFile();
					} else {
						System.out.println(Thread.currentThread().getName() + ":(" + (i + 1) + "/" + list.size() + ")当前文件-"
								+ list.get(i).getAbsolutePath() + "文件已存在");
					}
				} catch (Exception e) {
					Log.info("文件打开失败", list.get(i).getAbsolutePath());
					Runtime.getRuntime().exec("C:\\Users\\mc\\Desktop\\GlowtoolsA-wdf\\wascompress.exe "+list.get(i).getAbsolutePath());
				} 
			}
			// 关闭转换器
			tools.dispose();
		}
	}

	/**
	 * 递归当前统计目录下的所有was文件
	 * 
	 * @param tools
	 *            WasTools工具
	 * @param dir
	 *            要查询的目录
	 * @param wasFiles
	 *            存放结果的list文件
	 */
	public static void countWasFile(WasTools tools, File dir, List<File> wasFiles) {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			// 如果当前路径是目录，进入目录继续查找
			if (fs[i].isDirectory()) {
				countWasFile(tools, fs[i], wasFiles);
			} else {
				// 当前文件是was文件时
				if (fs[i].getAbsolutePath().endsWith("was") || fs[i].getAbsolutePath().endsWith("WAS")) {
					wasFiles.add(fs[i]);
				}
			}
		}
	}

	/**
	 * 打开was文件
	 * 
	 * @param is
	 * @param name
	 * @param newWindow
	 * @throws FileNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws IOException
	 */
	private void openWasFile(File file) throws IllegalArgumentException, FileNotFoundException, IOException   {
		if (file != null) {
			CanvasImage canvasImage = new SpriteCanvasImage(WasDecoder.loadImage(new FileInputStream(file)),
					file.getName());
			if (this.curCanvas == null)
				newCanvasFrame(new Canvas(canvasImage));
			else {
				this.curCanvas.addImage(canvasImage);
			}
			updateLayerPanel();
		}
	}

	/**
	 * 关闭当前打开文件
	 */
	private void closeCurrentWasFile() {
		this.curCanvas = null;
		JInternalFrame frame = this.desktop.getSelectedFrame();
		if (frame != null)
			frame.doDefaultCloseAction();
	}

	/**
	 *
	 * 自动转换为Gif文件
	 * 
	 * @param canvas
	 * @param file
	 */
	private void exportCanvasAsGIF(File file) {
		FileOutputStream imgOut = null;
		try {
			if (!file.getName().endsWith(".gif")) {
				file = new File(file.getAbsolutePath() + ".gif");
			}
			int width = this.curCanvas.getCanvasWidth();
			int height = this.curCanvas.getCanvasHeight();
			AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
			int gifDelay = 100;
			gifEncoder.setDelay(gifDelay);
			gifEncoder.setRepeat(0);
			gifEncoder.setSize(width, height);
			gifEncoder.setDispose(0);
			gifEncoder.setTransparent(null);
			gifEncoder.setQuality(15);
			imgOut = new FileOutputStream(file);
			gifEncoder.start(imgOut);
			int count = this.curCanvas.getTotalDelay();
			this.curCanvas.firstFrame();
			for (int i = 0; i < count; i++) {
				this.curCanvas.paintCanvas();
				gifEncoder.addFrame(this.curCanvas.bufImg);
				this.curCanvas.nextFrame();
			}
			gifEncoder.finish();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (imgOut != null)
					imgOut.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			this.curCanvas.play();
		}
	}

	private void exportCanvasAsGIF(Canvas canvas) {
		FileOutputStream imgOut = null;
		try {
			File file = Utils.showSaveDialog(this, "请输入GIF名字", Utils.GIF_FILTER);
			if (!file.getName().endsWith(".gif")) {
				file = new File(file.getAbsolutePath() + ".gif");
			}
			int width = this.curCanvas.getCanvasWidth();
			int height = this.curCanvas.getCanvasHeight();
			AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
			int gifDelay = 100;
			gifEncoder.setDelay(gifDelay);
			gifEncoder.setRepeat(0);
			gifEncoder.setSize(width, height);
			gifEncoder.setDispose(0);
			gifEncoder.setTransparent(null);
			gifEncoder.setQuality(15);
			imgOut = new FileOutputStream(file);
			gifEncoder.start(imgOut);
			int count = this.curCanvas.getTotalDelay();
			this.curCanvas.firstFrame();
			for (int i = 0; i < count; i++) {
				this.curCanvas.paintCanvas();
				gifEncoder.addFrame(this.curCanvas.bufImg);
				this.curCanvas.nextFrame();
			}
			gifEncoder.finish();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (imgOut != null)
					imgOut.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			this.curCanvas.play();
		}
	}

	private void exportCanvasAsPNG(Canvas canvas) {
		if (canvas != null) {
			File file = Utils.showSaveDialog(this, "导出画布为PNG", Utils.PNG_FILTER);
			if (file != null) {
				String prexName = file.getAbsolutePath();

				canvas.setExport(true);
				if (canvas.isPlaying()) {
					int minFrameCount = canvas.getTotalDelay();
					canvas.firstFrame();
					for (int i = 0; i < minFrameCount; i++) {
						canvas.paintCanvas();
						String filename = prexName + (i + 1) + ".png";
						Utils.writeImage(canvas.bufImg, new File(filename), "png");
						canvas.nextFrame();
					}
					canvas.play();
				} else {
					String filename = prexName + ".png";

					canvas.paintCanvas();
					Utils.writeImage(canvas.bufImg, new File(filename), "png");
				}
				canvas.setExport(false);
			}
		}
	}

	private void saveCanvas(Canvas canvas) {
		if (canvas != null) {
			if (canvas.getFilename() == null) {
				File file = Utils.showSaveDialog(this, "保存画布 " + canvas.getCanvasName(), Utils.WTC_FILTER,
						canvas.getCanvasName());
				if (file != null) {
					if (!file.getName().endsWith(".wtc")) {
						file = new File(file.getAbsolutePath() + ".wtc");
					}
					canvas.setFilename(file);
				}
			}
			canvas.save();
		}
	}

	private List<Canvas> getCanvasList() {
		JInternalFrame[] frames = this.desktop.getAllFrames();
		List canvasList = new Vector();

		for (int i = 0; i < frames.length; i++) {
			CanvasInternalFrame canvasFrame = (CanvasInternalFrame) frames[i];
			canvasList.add(canvasFrame.getCanvas());
		}
		return canvasList;
	}

	public WasTools() {
		this.layerListModel = new DefaultListModel();
		initGUI();
		addWindowListener(this.eventHandler);
		Utils.loadSettings("Was Tools.ini");
		this.btnAutoSelect.setSelected(Utils.getPropertyAsBoolean("AutoSelect"));
		this.btnShowBoundary.setSelected(Utils.getPropertyAsBoolean("ShowBoundary"));
	}

	private void clearPreviewCanvas() {
		this.wasfileNameField.setText("");
		this.previewCanvas.setImage(null);
		this.previewNode = null;
	}

	private void createWdfTree(int index, WdfFile wdf, boolean expanded) {
		DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(wdf);
		Vector<WasFile> list = wdf.getFileList();
		for (WasFile fnode : list) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(fnode);
			newChild.add(node);
		}
		if (index != -1)
			this.filelistTreeRoot.insert(newChild, index);
		else
			this.filelistTreeRoot.add(newChild);
		if (expanded)
			this.filelistTree.expandPath(new TreePath(this.filterModel.getPathToRoot(newChild)));
	}

	private void exit() {
		this.eventHandler.actionPerformed(new ActionEvent(this, 1001, "close all was animations"));
		JInternalFrame[] frames = this.desktop.getAllFrames();
		if ((frames == null) || (frames.length == 0)) {
			Utils.setProperty("AutoSelect", Boolean.toString(this.btnAutoSelect.isSelected()));
			Utils.setProperty("ShowBoundary", Boolean.toString(this.btnShowBoundary.isSelected()));
			Utils.saveSettings("Was Tools Settings", "Was Tools.ini");
			System.exit(0);
		}
	}

	private void expandAll(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();

		expandAll(tree, new TreePath(root), expand);
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}

		}

		if (expand)
			tree.expandPath(parent);
		else
			tree.collapsePath(parent);
	}

	private void filterTree() {
		this.filterModel.setIncluded(this.filterTextField.getText());
		expandAll(this.filelistTree, true);

		TreeNode root = this.filelistTreeRoot;
		DefaultTreeModel model = this.filterModel;
		if (root.getChildCount() >= 0)
			for (Enumeration e = root.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				if (model.getChildCount(n) > 0) {
					TreeNode aNode = (TreeNode) model.getChild(n, 0);
					TreePath path = new TreePath(model.getPathToRoot(aNode));
					this.filelistTree.setSelectionPath(path);
					break;
				}
			}
	}

	private JPopupMenu getCanvasPopMenu() {
		if (this.canvasPopupMenu == null) {
			this.canvasPopupMenu = new JPopupMenu();

			JMenuItem menuItem = new JMenuItem("属性");
			menuItem.setActionCommand("canvas preferences");
			menuItem.addActionListener(this.eventHandler);
			this.canvasPopupMenu.add(menuItem);
		}

		return this.canvasPopupMenu;
	}

	private JSplitPane getLayerPanel() {
		JSplitPane wasSplitPanel = new JSplitPane();
		wasSplitPanel.setOneTouchExpandable(true);

		this.toolPanel = new JPanel();
		wasSplitPanel.add(this.toolPanel, "right");
		BorderLayout jPanel1Layout = new BorderLayout();
		this.toolPanel.setLayout(jPanel1Layout);
		this.toolPanel.setPreferredSize(new Dimension(150, 10));

		this.toolPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 1, 2, 2),
				BorderFactory.createEtchedBorder(1)));

		JScrollPane wasInfoPanel = new JScrollPane();
		this.toolPanel.add(wasInfoPanel, "North");
		wasInfoPanel.setPreferredSize(new Dimension(10, 120));
		wasInfoPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		this.layerInfoText = new JTextArea();
		wasInfoPanel.setViewportView(this.layerInfoText);
		this.layerInfoText.setEditable(false);
		this.layerInfoText.setBackground(new Color(238, 238, 238));

		this.layerListPanel = new JScrollPane();
		this.toolPanel.add(this.layerListPanel, "Center");

		this.layerListPanel.setMinimumSize(new Dimension(150, 100));
		this.layerListPanel.setBackground(new Color(255, 255, 255));

		this.layerList = new EditableList(this.layerListModel);
		this.layerList.setFont(Utils.DEFAULT_FONT);

		this.layerList.setTransferHandler(this.layerListTransferHandler);
		this.layerList.setDragEnabled(true);
		this.layerList.setDropMode(DropMode.INSERT);
		this.layerList.addMouseListener(this.eventHandler);
		this.layerList.addListSelectionListener(this.eventHandler);
		this.layerList.setCellRenderer(new LayerListCellRenderer());
		this.layerList.setCellEditor(new LayerListCellEditor());
		this.layerList.setFixedCellHeight(26);
		this.layerList.setSelectionMode(0);
		this.layerList.setComponentPopupMenu(getLayerPopupMenu());
		this.layerListPanel.setViewportView(this.layerList);

		this.desktop = new JDesktopPane();
		this.desktop.setBackground(Color.GRAY);
		this.desktop.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		this.desktop.addMouseListener(this.eventHandler);
		this.desktop.addContainerListener(this.eventHandler);
		JPanel desktopPanel = new JPanel(new BorderLayout());
		desktopPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0),
				BorderFactory.createEtchedBorder(1)));
		desktopPanel.add(this.desktop);
		wasSplitPanel.add(desktopPanel, "left");

		wasSplitPanel.setResizeWeight(1.0D);
		wasSplitPanel.setBorder(null);
		wasSplitPanel.setDividerSize(6);
		return wasSplitPanel;
	}

	private JPopupMenu getLayerPopupMenu() {
		if (this.layerPopupMenu == null) {
			this.layerPopupMenu = new JPopupMenu();

			JMenuItem menuItem = new JMenuItem();
			this.layerPopupMenu.add(menuItem);
			menuItem.setText("复制图层");
			menuItem.setActionCommand("copy current layer");
			menuItem.addActionListener(this.eventHandler);

			menuItem = new JMenuItem();
			this.layerPopupMenu.add(menuItem);
			menuItem.setText("删除图层");
			menuItem.setActionCommand("Delete seleted CanvasImage");
			menuItem.addActionListener(this.eventHandler);

			this.layerPopupMenu.addSeparator();

			JMenuItem item = new JMenuItem("显示/隐藏 链接图层");
			item.setActionCommand("visible of all linked layers");
			item.addActionListener(this.eventHandler);
			this.layerPopupMenu.add(item);

			item = new JMenuItem("显示/隐藏 未链接图层");
			item.setActionCommand("visible of all non-linked layers");
			item.addActionListener(this.eventHandler);
			this.layerPopupMenu.add(item);

			item = new JMenuItem("显示/隐藏 所有其它图层");
			item.setActionCommand("visible of other layers");
			item.addActionListener(this.eventHandler);
			this.layerPopupMenu.add(item);

			this.layerPopupMenu.addSeparator();

			JMenuItem propertyMenuItem = new JMenuItem();
			this.layerPopupMenu.add(propertyMenuItem);
			propertyMenuItem.setText("图层属性");
			propertyMenuItem.setActionCommand("layer settings");
			propertyMenuItem.addActionListener(this.eventHandler);
		}

		return this.layerPopupMenu;
	}

	private JMenuBar getMainMenuBar() {
		if (this.mainMenuBar == null) {
			this.mainMenuBar = new JMenuBar();

			JMenu fileMenu = new JMenu();
			this.mainMenuBar.add(fileMenu);
			fileMenu.setText("文件");

			JMenuItem newMenuItem = new JMenuItem();
			fileMenu.add(newMenuItem);
			newMenuItem.setText("新建(N)...");
			newMenuItem.setMnemonic('N');
			newMenuItem.setAccelerator(KeyStroke.getKeyStroke(78, 128));
			newMenuItem.setActionCommand("new canvas");
			newMenuItem.addActionListener(this.eventHandler);

			JMenuItem openMenuItem = new JMenuItem();
			fileMenu.add(openMenuItem);
			openMenuItem.setText("打开(O)...");
			openMenuItem.setMnemonic('O');
			openMenuItem.setAccelerator(KeyStroke.getKeyStroke(79, 128));
			openMenuItem.setActionCommand("open files");
			openMenuItem.addActionListener(this.eventHandler);

			JSeparator jSeparator1 = new JSeparator();
			fileMenu.add(jSeparator1);

			JMenuItem menuItem = new JMenuItem();
			fileMenu.add(menuItem);
			menuItem.setText("关闭");
			menuItem.setActionCommand("close current canvas");
			menuItem.addActionListener(this.eventHandler);

			JMenuItem closeAllFileMenuItem = new JMenuItem();
			fileMenu.add(closeAllFileMenuItem);
			closeAllFileMenuItem.setText("关闭全部");
			closeAllFileMenuItem.setActionCommand("close all was animations");
			closeAllFileMenuItem.addActionListener(this.eventHandler);

			fileMenu.addSeparator();

			menuItem = new JMenuItem();
			fileMenu.add(menuItem);
			menuItem.setText("保存(S)...");
			menuItem.setMnemonic('S');
			menuItem.setAccelerator(KeyStroke.getKeyStroke(83, 128));
			menuItem.setActionCommand("save current canvas to file");
			menuItem.addActionListener(this.eventHandler);

			menuItem = new JMenuItem();
			fileMenu.add(menuItem);
			menuItem.setText("另存为(A)...");
			menuItem.setMnemonic('A');
			menuItem.setActionCommand("save as");
			menuItem.addActionListener(this.eventHandler);

			menuItem = new JMenuItem();
			fileMenu.add(menuItem);
			menuItem.setText("保存全部...");
			menuItem.setAccelerator(KeyStroke.getKeyStroke(83, 192));
			menuItem.setActionCommand("save all");
			menuItem.addActionListener(this.eventHandler);

			fileMenu.addSeparator();

			JMenuItem loadIniFileMenuItem = new JMenuItem();
			fileMenu.add(loadIniFileMenuItem);
			loadIniFileMenuItem.setText("加载注释文件");
			loadIniFileMenuItem.setActionCommand("load wdf settings");
			loadIniFileMenuItem.addActionListener(this.eventHandler);

			JMenuItem saveIniFileMenuItem = new JMenuItem();
			fileMenu.add(saveIniFileMenuItem);
			saveIniFileMenuItem.setText("保存注释文件");
			saveIniFileMenuItem.setActionCommand("save wdf settings");
			saveIniFileMenuItem.addActionListener(this.eventHandler);

			menuItem = new JMenuItem();
			fileMenu.add(menuItem);
			menuItem.setText("清除注释标记");
			menuItem.setActionCommand("clear wdf settings");
			menuItem.addActionListener(this.eventHandler);

			fileMenu.addSeparator();

			menuItem = new JMenuItem();
			fileMenu.add(menuItem);
			menuItem.setText("导出精灵为WAS...");
			menuItem.setActionCommand("export was");
			menuItem.addActionListener(this.eventHandler);

			JMenuItem exportMenuItem = new JMenuItem();
			fileMenu.add(exportMenuItem);
			exportMenuItem.setText("导出精灵为PNG...");
			exportMenuItem.setActionCommand("export was as png");
			exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(73, 128));
			exportMenuItem.addActionListener(this.eventHandler);

			fileMenu.addSeparator();

			menuItem = new JMenuItem("导出画布为PNG...");
			fileMenu.add(menuItem);
			menuItem.setActionCommand("export canvas as pngs");
			menuItem.setAccelerator(KeyStroke.getKeyStroke(80, 128));
			menuItem.addActionListener(this.eventHandler);

			JMenuItem exportGifMenuItem = new JMenuItem();
			fileMenu.add(exportGifMenuItem);
			exportGifMenuItem.setText("导出画布为GIF...");
			exportGifMenuItem.setActionCommand("export canvas as a gif");
			exportGifMenuItem.setAccelerator(KeyStroke.getKeyStroke(71, 128));
			exportGifMenuItem.addActionListener(this.eventHandler);

			JSeparator jSeparator2 = new JSeparator();
			fileMenu.add(jSeparator2);

			JMenuItem exitMenuItem = new JMenuItem();
			fileMenu.add(exitMenuItem);
			exitMenuItem.setText("退出");
			exitMenuItem.setActionCommand("exit applicatin");
			exitMenuItem.addActionListener(this.eventHandler);

			JMenu LayerMenu = new JMenu();
			this.mainMenuBar.add(LayerMenu);
			LayerMenu.setText("图层");

			menuItem = new JMenuItem();
			LayerMenu.add(menuItem);
			menuItem.setText("剪切图层");
			menuItem.setActionCommand("cut layer");
			menuItem.addActionListener(this.eventHandler);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(88, 192));
			menuItem.setEnabled(false);

			JMenuItem copyMenuItem = new JMenuItem();
			LayerMenu.add(copyMenuItem);
			copyMenuItem.setText("复制图层");
			copyMenuItem.setActionCommand("copy current layer");
			copyMenuItem.addActionListener(this.eventHandler);
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 192));

			JMenuItem pasteMenuItem = new JMenuItem();
			LayerMenu.add(pasteMenuItem);
			pasteMenuItem.setText("粘贴图层");
			pasteMenuItem.setActionCommand("paste layer");
			pasteMenuItem.addActionListener(this.eventHandler);
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 192));
			pasteMenuItem.setEnabled(false);

			LayerMenu.addSeparator();

			JMenuItem deleteMenuItem = new JMenuItem();
			LayerMenu.add(deleteMenuItem);
			deleteMenuItem.setText("删除图层");
			deleteMenuItem.setActionCommand("Delete seleted CanvasImage");
			deleteMenuItem.addActionListener(this.eventHandler);
			deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(68, 192));

			LayerMenu.addSeparator();

			JMenuItem propertyMenuItem = new JMenuItem();
			LayerMenu.add(propertyMenuItem);
			propertyMenuItem.setText("图层属性");
			propertyMenuItem.setActionCommand("layer settings");
			propertyMenuItem.addActionListener(this.eventHandler);

			JMenu insertMenu = new JMenu();
			this.mainMenuBar.add(insertMenu);
			insertMenu.setText("工具");

			JMenuItem textMenuItem = new JMenuItem();
			insertMenu.add(textMenuItem);
			textMenuItem.setText("文本动画");
			textMenuItem.setAccelerator(KeyStroke.getKeyStroke(84, 128));
			textMenuItem.setActionCommand("create plain/mhxy text");
			textMenuItem.addActionListener(this.eventHandler);

			JMenu settingMenu = new JMenu();
			this.mainMenuBar.add(settingMenu);
			settingMenu.setText("设置");

			JMenuItem canvasMenuItem = new JMenuItem();
			settingMenu.add(canvasMenuItem);
			canvasMenuItem.setText("画布属性");
			canvasMenuItem.setAccelerator(KeyStroke.getKeyStroke(82, 128));
			canvasMenuItem.setActionCommand("canvas preferences");
			canvasMenuItem.addActionListener(this.eventHandler);

			exportMenuItem = new JMenuItem();
			settingMenu.add(exportMenuItem);
			exportMenuItem.setText("导出选项");
			exportMenuItem.setActionCommand("gif export preferences");
			exportMenuItem.addActionListener(this.eventHandler);
			exportMenuItem.setEnabled(false);

			JMenu helpMenu = new JMenu();
			this.mainMenuBar.add(helpMenu);
			helpMenu.setText("帮助");

			menuItem = new JMenuItem();
			helpMenu.add(menuItem);
			menuItem.setText("使用手册");
			menuItem.setActionCommand("help message");
			menuItem.addActionListener(this.eventHandler);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(112, 0));

			menuItem = new JMenuItem();
			helpMenu.add(menuItem);
			menuItem.setText("个人主页");
			menuItem.setActionCommand("visit home page");
			menuItem.addActionListener(this.eventHandler);

			JMenuItem aboutMenuItem = new JMenuItem();
			helpMenu.add(aboutMenuItem);
			aboutMenuItem.setText("关于 Was Tools");
			aboutMenuItem.setActionCommand("about message");
			aboutMenuItem.addActionListener(this.eventHandler);
		}

		return this.mainMenuBar;
	}

	private TreePath getPathForWdf(WdfFile wdf) {
		DefaultTreeModel treeModel = (DefaultTreeModel) this.filelistTree.getModel();

		int count = treeModel.getChildCount(this.filelistTreeRoot);
		for (int i = 0; i < count; i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeModel.getChild(this.filelistTreeRoot, i);
			if (node.getUserObject() == wdf) {
				return new TreePath(treeModel.getPathToRoot(node));
			}
		}
		return null;
	}

	private JPopupMenu getPreviewCanvasPopMenu() {
		if (this.previewCcanvasPopupMenu == null) {
			this.previewCcanvasPopupMenu = new JPopupMenu();

			JMenuItem menuItem = new JMenuItem("属性");
			menuItem.setActionCommand("preview canvas preferences");
			menuItem.addActionListener(this.eventHandler);
			this.previewCcanvasPopupMenu.add(menuItem);
		}

		return this.previewCcanvasPopupMenu;
	}

	private WdfFile getSelectedWDF() {
		WdfFile wdf = null;
		TreePath path = this.filelistTree.getSelectionPath();
		if ((path == null) && (this.filelistTree.getRowCount() > 0)) {
			path = this.filelistTree.getPathForRow(0);
		}
		if (path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getPathComponent(1);
			wdf = (WdfFile) node.getUserObject();
		}
		return wdf;
	}

	private List<WasFile> getSelectionWasFiles() {
		Vector filelist = new Vector();
		TreePath[] paths = this.filelistTree.getSelectionPaths();
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
				Object userObj = node.getUserObject();
				if ((userObj instanceof WasFile)) {
					filelist.add((WasFile) userObj);
				}
			}
		}
		return filelist;
	}

	private Component getStatusPanel() throws FileNotFoundException, IOException {
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout(0));
		statusPanel.setSize(767, 26);
		WasIcon icon = new WasIcon(SpriteFactory.loadAnimation("/com/wildbean/resources/icon/statusBar.was"));
		this.hitsLabel = new JLabel("欢迎使用 Was Tools beta 2.0", icon, 2);
		statusPanel.add(this.hitsLabel);
		Thread update = new Thread() {
			public void run() {
				while (true)
					try {
						Thread.sleep(100L);
						WasTools.this.hitsLabel.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		};
		update.setDaemon(true);
		update.start();
		return statusPanel;
	}

	private JToolBar getToolBar() {
		if (this.toolBar == null) {
			this.toolBar = new JToolBar();
			this.toolBar.setFloatable(false);
			this.toolBar.setBorder(BorderFactory.createEtchedBorder());

			this.btnAutoSelect = new JCheckBox("自动选择图层");
			this.btnAutoSelect.setActionCommand("auto select layer");
			this.btnAutoSelect.addActionListener(this.eventHandler);
			this.toolBar.add(this.btnAutoSelect);

			this.btnShowBoundary = new JCheckBox();
			this.toolBar.add(this.btnShowBoundary);
			this.btnShowBoundary.setText("显示定界框");
			this.btnShowBoundary.setActionCommand("whether show the selected layer's boundary");
			this.btnShowBoundary.addActionListener(this.eventHandler);

			this.toolBar.addSeparator();

			this.btnAlignSprite = new JButton(Utils.loadIcon("align_sprite.gif"));
			this.btnAlignSprite.setToolTipText("精灵中心点对齐");
			this.btnAlignSprite.setActionCommand("align to sprite center");
			this.btnAlignSprite.addActionListener(this.eventHandler);
			this.toolBar.add(this.btnAlignSprite);

			this.toolBar.addSeparator();

			this.btnAlignTop = new JButton(Utils.loadIcon("align_top.gif"));
			this.toolBar.add(this.btnAlignTop);
			this.btnAlignTop.setToolTipText("顶对齐");
			this.btnAlignTop.setActionCommand("align to top");
			this.btnAlignTop.addActionListener(this.eventHandler);

			this.btnAlignVerCenter = new JButton(Utils.loadIcon("align_ver_center.gif"));
			this.toolBar.add(this.btnAlignVerCenter);
			this.btnAlignVerCenter.setToolTipText("垂直中齐");
			this.btnAlignVerCenter.setActionCommand("align to ver center");
			this.btnAlignVerCenter.addActionListener(this.eventHandler);

			this.btnAlignBottom = new JButton(Utils.loadIcon("align_bottom.gif"));
			this.toolBar.add(this.btnAlignBottom);
			this.btnAlignBottom.setToolTipText("底对齐");
			this.btnAlignBottom.setActionCommand("align to bottom");
			this.btnAlignBottom.addActionListener(this.eventHandler);

			this.toolBar.addSeparator();

			this.btnAlignLeft = new JButton(Utils.loadIcon("align_left.gif"));
			this.toolBar.add(this.btnAlignLeft);
			this.btnAlignLeft.setToolTipText("左对齐");
			this.btnAlignLeft.setActionCommand("align to left");
			this.btnAlignLeft.addActionListener(this.eventHandler);

			this.btnAlignHorCenter = new JButton(Utils.loadIcon("align_hor_center.gif"));
			this.toolBar.add(this.btnAlignHorCenter);
			this.btnAlignHorCenter.setToolTipText("水平中齐");
			this.btnAlignHorCenter.setActionCommand("align to hor center");
			this.btnAlignHorCenter.addActionListener(this.eventHandler);

			this.btnAlignRight = new JButton(Utils.loadIcon("align_right.gif"));
			this.toolBar.add(this.btnAlignRight);
			this.btnAlignRight.setToolTipText("右对齐");
			this.btnAlignRight.setActionCommand("align to right");
			this.btnAlignRight.addActionListener(this.eventHandler);

			this.toolBar.addSeparator();

			JButton canvasPerferences = new JButton("画布属性");
			this.toolBar.add(canvasPerferences);

			canvasPerferences.setActionCommand("canvas preferences");
			canvasPerferences.addActionListener(this.eventHandler);
		}

		return this.toolBar;
	}

	private Component getTreePanel() {
		JPanel fileListPanel = new JPanel();
		BorderLayout fileListPanelLayout = new BorderLayout();
		fileListPanelLayout.setHgap(5);
		fileListPanel.setLayout(fileListPanelLayout);
		fileListPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0),
				BorderFactory.createEtchedBorder(1)));

		JScrollPane treeScrollPanel = new JScrollPane();
		treeScrollPanel.setBorder(null);
		this.listPanel = new JSplitPane(0);
		fileListPanel.add(this.listPanel, "Center");
		this.listPanel.setDividerSize(4);
		this.listPanel.setDividerLocation(300);

		JPanel treePanel = new JPanel(new BorderLayout(5, 5));
		this.listPanel.setTopComponent(treePanel);
		treePanel.setBackground(Color.WHITE);
		treePanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		treePanel.setPreferredSize(new Dimension(150, 10));
		treePanel.add(treeScrollPanel, "Center");
		this.filterTextField = new JTextField();
		treePanel.add(this.filterTextField, "North");
		this.filterTextField.setPreferredSize(new Dimension(100, 22));
		this.filterTextField.getDocument().addDocumentListener(this.eventHandler);

		JPanel panel = new JPanel(new BorderLayout(0, 2));
		this.previewCanvas = new PreviewCanvas(120, 120);
		this.previewCanvas.setComponentPopupMenu(getPreviewCanvasPopMenu());
		panel.add(this.previewCanvas, "Center");
		this.wasfileNameField = new JTextField();
		this.wasfileNameField.setActionCommand("changed was name");
		this.wasfileNameField.addActionListener(this.eventHandler);
		panel.add(this.wasfileNameField, "South");
		this.listPanel.setBottomComponent(panel);

		this.filelistTreeRoot = new DefaultMutableTreeNode("WDF文件列表");
		this.filterModel = new WasFilterTreeModel(this.filelistTreeRoot, this.filter);
		this.filelistTree = new JTree(this.filterModel);
		this.filelistTree.setRootVisible(false);
		this.filelistTree.setFont(Utils.DEFAULT_FONT);
		this.filelistTree.setEditable(true);
		this.filelistTree.setToggleClickCount(1);
		this.filelistTree.addKeyListener(this.eventHandler);
		this.filelistTree.addTreeSelectionListener(this.eventHandler);
		this.filelistTree.addMouseListener(this.eventHandler);
		treeScrollPanel.setViewportView(this.filelistTree);

		this.filelistPopupMenu = new JPopupMenu();
		this.filelistTree.setComponentPopupMenu(this.filelistPopupMenu);

		JMenuItem openMenuItem = new JMenuItem();
		openMenuItem.setText("提取WAS");
		openMenuItem.setActionCommand("distill was from wdf");
		openMenuItem.addActionListener(this.eventHandler);
		this.filelistPopupMenu.add(openMenuItem);

		JMenuItem addToCanvasMenuItem = new JMenuItem();
		addToCanvasMenuItem.setText("提取WAS到当前");
		addToCanvasMenuItem.setActionCommand("distill was & add to current canvas");
		addToCanvasMenuItem.addActionListener(this.eventHandler);
		this.filelistPopupMenu.add(addToCanvasMenuItem);

		JSeparator separator = new JSeparator();
		this.filelistPopupMenu.add(separator);

		JMenuItem exportMenuItem = new JMenuItem();
		this.filelistPopupMenu.add(exportMenuItem);
		exportMenuItem.setText("导出WAS到...");
		exportMenuItem.setActionCommand("export was");
		exportMenuItem.addActionListener(this.eventHandler);

		this.filelistPopupMenu.addSeparator();

		JMenu sortMenu = new JMenu();
		this.filelistPopupMenu.add(sortMenu);
		sortMenu.setText("排序");

		JMenuItem idItem = new JMenuItem();
		sortMenu.add(idItem);
		idItem.setText("ID");
		idItem.setActionCommand("sort by id");
		idItem.addActionListener(this.eventHandler);

		idItem = new JMenuItem();
		sortMenu.add(idItem);
		idItem.setText("名字");
		idItem.setActionCommand("sort by name");
		idItem.addActionListener(this.eventHandler);

		idItem = new JMenuItem();
		sortMenu.add(idItem);
		idItem.setText("大小");
		idItem.setActionCommand("sort by size");
		idItem.addActionListener(this.eventHandler);

		this.filelistPopupMenu.addSeparator();

		JMenuItem expandMenu = new JMenuItem();
		this.filelistPopupMenu.add(expandMenu);
		expandMenu.setText("展开WDF");
		expandMenu.setActionCommand("expand wdf");
		expandMenu.addActionListener(this.eventHandler);

		JMenuItem collapseMenu = new JMenuItem();
		this.filelistPopupMenu.add(collapseMenu);
		collapseMenu.setText("折叠WDF");
		collapseMenu.setActionCommand("collapse wdf");
		collapseMenu.addActionListener(this.eventHandler);

		this.filelistPopupMenu.addSeparator();

		JMenuItem closeMenu = new JMenuItem();
		this.filelistPopupMenu.add(closeMenu);
		closeMenu.setText("关闭WDF文件");
		closeMenu.setActionCommand("close wdf");
		closeMenu.addActionListener(this.eventHandler);

		return fileListPanel;
	}

	private void initGUI() {
		try {
			setTitle("Was Tools beta 2.0");
			setDefaultCloseOperation(0);
			JSplitPane mainPanel = new JSplitPane(1);
			mainPanel.setLeftComponent(getTreePanel());
			mainPanel.setRightComponent(getLayerPanel());
			mainPanel.setDividerLocation(150);
			mainPanel.setOneTouchExpandable(true);
			add(mainPanel, "Center");
			mainPanel.setBorder(BorderFactory.createEtchedBorder(1));
			mainPanel.setDividerSize(6);
			setJMenuBar(getMainMenuBar());
			add(getStatusPanel(), "South");
			getContentPane().add(getToolBar(), "North");
			pack();
			setSize(775, 580);
			setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void openImage(File file, boolean newWindow) {
		Image image = Utils.loadImage(file);
		String name = file.getName();
		CanvasImage canvasImage = new StandardCanvasImage(image, name);
		if ((newWindow) || (this.curCanvas == null))
			newCanvasFrame(new Canvas(canvasImage), name);
		else {
			this.curCanvas.addImage(canvasImage);
		}

		updateLayerPanel();
	}

	private void newCanvasFrame(Canvas canvas, String name) {
		JInternalFrame jif = new CanvasInternalFrame(canvas, name);
		jif.addInternalFrameListener(this.eventHandler);
		this.desktop.add(jif);
		int x = (this.desktop.getWidth() - jif.getWidth()) / 2;
		int y = (this.desktop.getHeight() - jif.getHeight()) / 2;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		jif.setLocation(x, y);
	}

	private void newCanvasFrame(Canvas canvas) {
		newCanvasFrame(canvas, canvas.getCanvasName());
	}

	private void openWasFile(InputStream is, String name, boolean newWindow) throws IllegalArgumentException, IOException  {
		if (is != null) {
			CanvasImage canvasImage = new SpriteCanvasImage(WasDecoder.loadImage(is), name);
			if ((newWindow) || (this.curCanvas == null))
				newCanvasFrame(new Canvas(canvasImage));
			else {
				this.curCanvas.addImage(canvasImage);
			}
			updateLayerPanel();
		}
	}

	private void openWdfFile(File file) {
		WdfFile wdf = new WdfFile(file.getAbsolutePath());
		createWdfTree(-1, wdf, false);
		this.filelistTree.updateUI();
	}

	private void previewWAS(SeekByteArrayInputStream bis, WasFile fnode) throws IOException {
		clearPreviewCanvas();
		CanvasImage image = new SpriteCanvasImage(WasDecoder.loadImage(bis), fnode.toString());
		this.previewCanvas.setImage(image);
		this.previewNode = fnode;
		this.wasfileNameField.setText(fnode.toString());
	}

	private int removeWdfTree(WdfFile wdf) {
		int count = this.filterModel.getChildCount(this.filelistTreeRoot);

		for (int i = 0; i < count; i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.filterModel.getChild(this.filelistTreeRoot, i);
			if (node.getUserObject() == wdf) {
				this.filterModel.removeNodeFromParent(node);
				return i;
			}
		}
		return -1;
	}

	private void setHits(String hits) {
		setHits(hits, HitsType.INFORMATION);
	}

	private void setHits(String hits, HitsType type) {
		switch (type) {
		case ERROR:
			this.hitsLabel.setForeground(Color.RED);
			break;
		case WARNING:
			this.hitsLabel.setForeground(Color.BLUE);
			break;
		case QUESTION:
			this.hitsLabel.setForeground(Color.BLACK);
			break;
		case INFORMATION:
		default:
			this.hitsLabel.setForeground(Color.BLACK);
		}
		this.hitsLabel.setText(hits);
	}

	private void updateLayerPanel() {
		if (this.curCanvas != null) {
			this.curCanvas.removePropertyChangeListener(this.eventHandler);
			this.curCanvas.remove(getCanvasPopMenu());
		}
		this.curCanvas = null;

		JInternalFrame frame = this.desktop.getSelectedFrame();
		CanvasInternalFrame cFrame = null;
		if ((frame instanceof CanvasInternalFrame)) {
			cFrame = (CanvasInternalFrame) frame;
			this.curCanvas = cFrame.getCanvas();
			this.curCanvas.addPropertyChangeListener(this.eventHandler);
			this.curCanvas.setComponentPopupMenu(getCanvasPopMenu());
		}
		this.layerListModel.removeAllElements();
		if (this.curCanvas != null) {
			frame.setTitle(this.curCanvas.isDirty() ? "*" + this.curCanvas.getCanvasName() : this.curCanvas
					.getCanvasName());
			setTitle("Was Tools beta 2.0 - " + frame.getTitle());

			Vector<CanvasImage> images = this.curCanvas.getImages();
			for (CanvasImage image : images) {
				this.layerListModel.addElement(image);
			}

			if ((this.curCanvas.curImage == null) && (this.curCanvas.getImages().size() > 0)) {
				this.curCanvas.setCurrentImage((CanvasImage) this.curCanvas.getImages().get(0));
			}
			this.layerList.setSelectedValue(this.curCanvas.curImage, true);

			this.curCanvas.setAutoSelect(this.btnAutoSelect.isSelected());
			this.curCanvas.setShowBoundary(this.btnShowBoundary.isSelected());
		} else {
			this.layerInfoText.setText(null);
			setTitle("Was Tools beta 2.0");
		}
	}

	private class EventHandler implements ActionListener, PropertyChangeListener, InternalFrameListener,
			ListSelectionListener, WindowListener, ContainerListener, DocumentListener, TreeSelectionListener,
			MouseInputListener, KeyListener {
		private EventHandler() {
		}

		public void actionPerformed(ActionEvent e) {
			List<WasFile> filelist;
			String prexName;
			WdfFile wdf;
			File file;
			WasImage wasImage;
			CanvasPreferences canvasPF;
			int index;
			switch (e.getActionCommand()) {
			case "close wdf":
				wdf = WasTools.this.getSelectedWDF();
				if (wdf != null)
					WasTools.this.removeWdfTree(wdf);
				break;
			case "expand wdf":
				WasTools.this.filelistTree.expandPath(WasTools.this.getPathForWdf(getSelectedWDF()));
				break;
			case "collapse wdf":
				WasTools.this.filelistTree.collapsePath(WasTools.this.getPathForWdf(getSelectedWDF()));
				break;
			case "export was":
				if (WasTools.this.previewNode != null) {
					file = Utils.showSaveDialog(WasTools.this, "导出WAS文件", Utils.WAS_FILTER);
					if (file != null) {
						if (!file.getName().endsWith(".was")) {
							file = new File(file.getAbsolutePath() + ".was");
						}
						wdf = WasTools.this.getSelectedWDF();
						try {
							if (!file.exists())
								file.createNewFile();
							FileOutputStream fos = new FileOutputStream(file);
							fos.write(wdf.getFileData(WasTools.this.previewNode));
							fos.close();
							WasTools.this.setHits("已导出WAS文件:" + file.getAbsolutePath());
						} catch (IOException ex) {
							String hits = "导出WAS文件出错:" + ex.getMessage();
							System.err.println(hits);

							WasTools.this.setHits(hits, WasTools.HitsType.ERROR);
						}
					}
				}
				break;
			case "export was as png":
				if ((WasTools.this.curCanvas == null) || (WasTools.this.curCanvas.curImage == null))
					return;
				if (!(WasTools.this.curCanvas.curImage instanceof SpriteCanvasImage))
					return;
				wasImage = (WasImage) WasTools.this.curCanvas.curImage.getData();
				int spriteIndex = WasTools.this.curCanvas.curImage.getSpriteIndex();
				file = Utils.showSaveDialog(WasTools.this, "导出精灵为PNG序列", Utils.PNG_FILTER);
				if (file != null) {
					prexName = file.getAbsolutePath();
					int width = wasImage.width;
					int height = wasImage.height;
					BufferedImage tmpImage = new BufferedImage(width, height, 2);
					Graphics2D g2d = tmpImage.createGraphics();
					g2d.setComposite(AlphaComposite.Clear);

					if (WasTools.this.curCanvas.isPlaying()) {
						for (int i = 0; i < wasImage.frameCount; i++) {
							g2d.fillRect(0, 0, width, height);
							WasFrame frame = wasImage.frames[spriteIndex][i];
							frame.drawCenter(tmpImage.getRaster(), wasImage.xCenter, wasImage.yCenter);
							String filename = prexName + (i + 1) + ".png";
							Utils.writeImage(tmpImage, new File(filename), "png");
						}
					} else {
						int frameIndex = WasTools.this.curCanvas.curImage.getFrameIndex();
						WasFrame frame = wasImage.frames[spriteIndex][frameIndex];
						frame.drawCenter(tmpImage.getRaster(), wasImage.xCenter, wasImage.yCenter);
						String filename = prexName + ".png";
						Utils.writeImage(tmpImage, new File(filename), "png");
					}
					g2d.dispose();
				}
				break;
			case "distill was from wdf":
				filelist = (java.util.List) WasTools.this;
				for (WasFile f : filelist) {
					try {
						SeekByteArrayInputStream bis = new SeekByteArrayInputStream(f.parent.getFileData(f));
						WasTools.this.openWasFile(bis, String.valueOf(f), true);
						WasTools.this.setHits("已提取WAS到新画布: " + f);
					} catch (Exception ex) {
						String hits = "解释失败: " + f + " 不是WAS格式?";
						WasTools.this.setHits(hits, WasTools.HitsType.ERROR);
						System.err.println(hits);
					}
				}
				break;
			case "distill was & add to current canvas":
				filelist = (java.util.List) WasTools.this;
				for (WasFile f : filelist)
					try {
						SeekByteArrayInputStream bis = new SeekByteArrayInputStream(f.parent.getFileData(f));
						WasTools.this.openWasFile(bis, String.valueOf(f), false);
						WasTools.this.setHits("已提取WAS到当前画布: " + f);
					} catch (Exception ex) {
						String hits = "解释失败: " + f + " 不是WAS格式?";
						WasTools.this.setHits(hits, WasTools.HitsType.ERROR);
						System.err.println(hits);
					}
				break;
			case "preview was":
				clearPreviewCanvas();
				filelist = (java.util.List) WasTools.this;
				if (filelist == null || filelist.size() == 0)
					return;
				WasFile wasFile = (WasFile) filelist.get(filelist.size() - 1);
				try {
					SeekByteArrayInputStream bis = new SeekByteArrayInputStream(wasFile.parent.getFileData(wasFile));
					previewWAS(bis, wasFile);
					setHits((new StringBuilder("预览WAS: ")).append(wasFile).toString());
				} catch (Exception ex) {
					String hits = (new StringBuilder("解释失败: ")).append(wasFile).append(" 不是WAS格式?").toString();
					setHits(hits, HitsType.ERROR);
					System.err.println(hits);
				}
				break;
			case "export canvas as pngs":
				WasTools.this.exportCanvasAsPNG(WasTools.this.curCanvas);
				break;
			case "export canvas as a gif":
				WasTools.this.exportCanvasAsGIF(WasTools.this.curCanvas);
				break;
			case "open files":
				File[] selectedFiles = Utils.showOpenDialog(WasTools.this, "打开文件", Utils.SUPPORT_FILTER);
				for (File f : selectedFiles) {
					try {
						switch (Utils.getFileType(f)) {
						case 1:
							WasTools.this.openWasFile(new FileInputStream(f), f.getName(), !Utils.isAddToCurrent());
							break;
						case 2:
							WasTools.this.openWdfFile(f);
							break;
						case 3:
						case 4:
						case 5:
							WasTools.this.openImage(f, !Utils.isAddToCurrent());
							break;
						case 100:
							WasTools.this.newCanvasFrame(Canvas.loadFromFile(f));
						}

						WasTools.this.setHits("已打开: " + f);
					} catch (Exception ex) {
						String hits = "不能打开文件 " + f.getName() + ":" + ex.getMessage();
						System.err.println(hits);
						WasTools.this.setHits(hits, WasTools.HitsType.ERROR);
					}
				}
				break;
			case "changed was nam":
				String alias = WasTools.this.wasfileNameField.getText();
				if (!alias.equals(WasTools.this.previewNode.name)) {
					WasTools.this.previewNode.name = alias;
					WasTools.this.filelistTree.updateUI();
				}
				break;
			case "new canvas":
				canvasPF = new CanvasPreferences(WasTools.this);
				canvasPF.showDialog(null);
				Canvas canvas = canvasPF.getCanvas();
				if (canvas != null)
					WasTools.this.newCanvasFrame(canvas);
				break;
			case "load wdf settings":
				File[] files = Utils.showOpenDialog(WasTools.this, "打开WDF的注释文件", Utils.iniFilter);
				wdf = WasTools.this.getSelectedWDF();
				if ((files != null) && (wdf != null)) {
					for (File iniFile : files) {
						try {
							wdf.loadIniFile(iniFile);
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(WasTools.this, "加载注释文件: " + iniFile.getName() + " 失败！", "错误",
									0);
						}
					}
				}
				WasTools.this.filelistTree.updateUI();
				break;
			case "save wdf settings":
				wdf = WasTools.this.getSelectedWDF();
				File iniFile = Utils.showSaveDialog(WasTools.this, "保存" + wdf.getName() + "的注释文件", Utils.iniFilter);
				if (!iniFile.getName().endsWith(".ini")) {
					iniFile = new File(iniFile.getAbsolutePath() + ".ini");
				}
				if ((iniFile != null) && (wdf != null))
					wdf.saveIniFile(iniFile);
				break;
			case "clear wdf settings":
				wdf = WasTools.this.getSelectedWDF();
				if (wdf != null) {
					wdf.clearAllMark();
					WasTools.this.filelistTree.updateUI();
				}
				break;
			case "Delete seleted CanvasImage":
				index = layerList.getSelectedIndex();
				Object pictures[] = layerList.getSelectedValues();
				Object aobj[] = pictures;
				int j1 = 0;
				for (int i2 = aobj.length; j1 < i2; j1++) {
					Object picture = aobj[j1];
					curCanvas.removeImage((CanvasImage) picture);
					layerListModel.removeElement(picture);
				}

				if (index >= layerListModel.getSize())
					index = layerListModel.getSize() - 1;
				layerList.setSelectedIndex(index);
				break;
			case "close all was animations":
				JInternalFrame[] frames = WasTools.this.desktop.getAllFrames();
				for (JInternalFrame frame : frames) {
					frame.doDefaultCloseAction();
				}
				WasTools.this.updateLayerPanel();
				break;
			case "canvas preferences":
				canvasPF = new CanvasPreferences(WasTools.this);
				canvasPF.showDialog(WasTools.this.curCanvas);
				break;
			case "preview canvas preferences":
				canvasPF = new CanvasPreferences(WasTools.this);
				canvasPF.showDialog(WasTools.this.previewCanvas);
				break;
			case "gif export preferences":
				GifExportPreferences dlg1 = new GifExportPreferences(WasTools.this, WasTools.this.curCanvas);
				dlg1.setVisible(true);
				break;
			case "help message":
				try {
					BrowserLauncher.openURL(System.getProperty("user.dir") + "\\help\\index.htm");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case "visit home page":
				try {
					BrowserLauncher.openURL("http://kylixs.blog.sohu.com");
				} catch (IOException e1) {
					e1.printStackTrace();
					WasTools.this.setHits("打开主页失败！\n请访问以下地址：http://kylixs.blog.sohu.com", WasTools.HitsType.ERROR);
				}
				break;
			case "about message":
				if (WasTools.this.aboutIcon == null) {
					try {
						WasTools.this.aboutIcon = new ImageIcon(Utils.loadJarFile("/com/wildbean/resources/about.png"));
					} catch (IOException e1) {
						System.err.println("加载About图片失败");
						e1.printStackTrace();
					}
				}
				JOptionPane
						.showMessageDialog(
								WasTools.this,
								"梦幻制图工具系列之\nWas Tools beta 2.0\n\nkylixs(落魄逍遥)\n野豆工作室@2007-2\n\n梦幻制图工具QQ群:9106334\n主页:kylixs.blog.sohu.com\n\n",
								"关于", -1, WasTools.this.aboutIcon);
				break;
			case "align to sprite center":
				WasTools.this.curCanvas.alignImages(0);
				break;
			case "align to left":
				WasTools.this.curCanvas.alignImages(1);
				break;
			case "align to hor center":
				WasTools.this.curCanvas.alignImages(2);
				break;
			case "align to right":
				WasTools.this.curCanvas.alignImages(3);
				break;
			case "align to top":
				WasTools.this.curCanvas.alignImages(4);
				break;
			case "align to ver center":
				WasTools.this.curCanvas.alignImages(5);
				break;
			case "align to bottom":
				WasTools.this.curCanvas.alignImages(6);
				break;
			case "auto select layer":
				WasTools.this.curCanvas.setAutoSelect(WasTools.this.btnAutoSelect.isSelected());
				break;
			case "whether show the selected layer's boundary":
				WasTools.this.curCanvas.setShowBoundary(WasTools.this.btnShowBoundary.isSelected());
				break;
			case "sort by id":
				wdf = WasTools.this.getSelectedWDF();
				wdf.sort(0);
				index = WasTools.this.removeWdfTree(wdf);
				WasTools.this.createWdfTree(index, wdf, true);
				WasTools.this.filelistTree.updateUI();
				break;
			case "sort by name":
				wdf = WasTools.this.getSelectedWDF();
				wdf.sort(1);
				index = WasTools.this.removeWdfTree(wdf);
				WasTools.this.createWdfTree(index, wdf, true);
				WasTools.this.filelistTree.updateUI();
				break;
			case "sort by size":
				wdf = WasTools.this.getSelectedWDF();
				wdf.sort(2);
				index = WasTools.this.removeWdfTree(wdf);
				WasTools.this.createWdfTree(index, wdf, true);
				WasTools.this.filelistTree.updateUI();
				break;
			case "copy current layer":
				if (curCanvas != null && curCanvas.curImage != null) {
					CopyLayerDlg dialog = new CopyLayerDlg(WasTools.this, curCanvas.curImage,
							(java.util.List) WasTools.this);
					dialog.setVisible(true);
					if (dialog.isNewCanvas())
						newCanvasFrame(dialog.getDestCanvas());
					else
						updateLayerPanel();
				}
				break;
			case "visible of all linked layers":
				CanvasImage base = curCanvas.curImage;
				Vector images = curCanvas.getImages();
				boolean visible = false;
				for (Iterator iterator5 = images.iterator(); iterator5.hasNext();) {
					CanvasImage image = (CanvasImage) iterator5.next();
					if (image.getLinkedBase() == base && image.isVisible() == visible) {
						visible = true;
						break;
					}
				}

				for (Iterator iterator6 = images.iterator(); iterator6.hasNext();) {
					CanvasImage image = (CanvasImage) iterator6.next();
					if (image.getLinkedBase() == base)
						image.setVisible(visible);
				}

				base.setVisible(visible);
				updateLayerPanel();
				break;
			case "visible of all non-linked layers":
				CanvasImage base1 = curCanvas.curImage;
				Vector images1 = curCanvas.getImages();
				visible = false;
				for (Iterator iterator7 = images1.iterator(); iterator7.hasNext();) {
					CanvasImage image = (CanvasImage) iterator7.next();
					if (image.getLinkedBase() != base1 && image.isVisible() == visible) {
						visible = true;
						break;
					}
				}

				for (Iterator iterator8 = images1.iterator(); iterator8.hasNext();) {
					CanvasImage image = (CanvasImage) iterator8.next();
					if (image.getLinkedBase() != base1 && image != base1)
						image.setVisible(visible);
				}

				updateLayerPanel();
				break;
			case "visible of other layers":
				Vector images11 = curCanvas.getImages();
				visible = false;
				CanvasImage image;
				for (Iterator iterator3 = images11.iterator(); iterator3.hasNext();) {
					image = (CanvasImage) iterator3.next();
					if (image.isVisible() == visible) {
						visible = true;
						break;
					}
				}

				for (Iterator iterator4 = images11.iterator(); iterator4.hasNext(); image.setVisible(visible))
					image = (CanvasImage) iterator4.next();

				updateLayerPanel();
				break;
			case "create plain/mhxy text":
				TextDlg dlg = new TextDlg(WasTools.this);
				dlg.setVisible(true);
				if (dlg.getText() != null) {
					TextCanvasImage textImage = new TextCanvasImage(dlg.removeChatPanel());
					if (WasTools.this.curCanvas == null) {
						WasTools.this.newCanvasFrame(new Canvas(textImage));
					} else {
						WasTools.this.curCanvas.addImage(textImage);
						WasTools.this.updateLayerPanel();
					}
				}
				break;
			case "close current canvas":
				WasTools.this.curCanvas = null;
				JInternalFrame frame = WasTools.this.desktop.getSelectedFrame();
				if (frame != null)
					frame.doDefaultCloseAction();
				break;
			case "save current canvas to file":
				WasTools.this.saveCanvas(WasTools.this.curCanvas);
				break;
			case "save as":
				if (WasTools.this.curCanvas != null) {
					file = Utils.showSaveDialog(WasTools.this,
							"保存画布 " + WasTools.this.curCanvas.getCanvasName() + " 为", Utils.WTC_FILTER);
					if (file != null) {
						if (!file.getName().endsWith(".wtc")) {
							file = new File(file.getAbsolutePath() + ".wtc");
						}
						WasTools.this.curCanvas.setDirty(true);
						WasTools.this.curCanvas.save(file);
					}
				}
				break;
			case "save all":
				java.util.List list = (java.util.List) WasTools.this;
				Canvas c;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); saveCanvas(c))
					c = (Canvas) iterator.next();

				break;
			case "layer settings":
				if ((WasTools.this.curCanvas != null) && (WasTools.this.curCanvas.curImage != null)) {
					LayerPreferenceDlg dlg11 = new LayerPreferenceDlg(WasTools.this, WasTools.this.curCanvas.curImage);
					dlg11.setVisible(true);
				}

				break;
			case "exit applicatin":
				WasTools.this.exit();
				break;
			}

		}

		public void changedUpdate(DocumentEvent e) {
			WasTools.this.filterTree();
		}

		public void componentAdded(ContainerEvent e) {
			if ((e.getChild() instanceof JInternalFrame)) {
				JInternalFrame f = (JInternalFrame) e.getChild();
				try {
					f.setSelected(true);
				} catch (PropertyVetoException ex) {
					ex.printStackTrace();
				}
			}
		}

		public void componentRemoved(ContainerEvent e) {
		}

		public void insertUpdate(DocumentEvent e) {
			WasTools.this.filterTree();
		}

		public void internalFrameActivated(InternalFrameEvent e) {
			WasTools.this.updateLayerPanel();
		}

		public void internalFrameClosed(InternalFrameEvent e) {
			WasTools.this.updateLayerPanel();
		}

		public void internalFrameClosing(InternalFrameEvent e) {
			CanvasInternalFrame frame = (CanvasInternalFrame) e.getInternalFrame();
			Canvas canvas = frame.getCanvas();
			// TODO 关闭退出保存提示
			// if ((canvas != null) && (canvas.isDirty())) {
			// int returnVal =
			// JOptionPane.showConfirmDialog(WasTools.this.getParent(),
			// "画布 " + canvas.getCanvasName() + " 修改后还没保存，\n想要保存吗?", "提示", 1);
			// if (returnVal == 0)
			// WasTools.this.saveCanvas(canvas);
			// else if (returnVal == 2) {
			// return;
			// }
			// }
			frame.dispose();
		}

		public void internalFrameIconified(InternalFrameEvent e) {
			CanvasInternalFrame frame = (CanvasInternalFrame) e.getInternalFrame();
			Canvas canvas = frame.getCanvas();
			canvas.setUpdate(false);
		}

		public void internalFrameDeiconified(InternalFrameEvent e) {
			CanvasInternalFrame frame = (CanvasInternalFrame) e.getInternalFrame();
			Canvas canvas = frame.getCanvas();
			canvas.setUpdate(true);
		}

		public void internalFrameDeactivated(InternalFrameEvent e) {
		}

		public void internalFrameOpened(InternalFrameEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			Object source = e.getSource();
			if ((source == WasTools.this.filelistTree) && (e.getKeyCode() == 109))
				filelistTree.collapsePath(getPathForWdf(getSelectedWDF()));
		}

		public void keyReleased(KeyEvent e) {
			Object source = e.getSource();
			if ((source == WasTools.this.filelistTree) && (e.getKeyCode() == 10)) {
				WasTools.this.eventHandler.actionPerformed(new ActionEvent(WasTools.this.filelistTree, 1001,
						"distill was & add to current canvas"));
				WasTools.this.filelistTree.requestFocus();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			Object source = e.getSource();
			if (source == WasTools.this.filelistTree) {
				if ((e.getClickCount() == 2) && (e.getButton() == 1)) {
					WasTools.this.eventHandler.actionPerformed(new ActionEvent(WasTools.this.filelistTree, 1001,
							"distill was & add to current canvas"));
				}
			} else if (source == WasTools.this.desktop) {
				if ((e.getClickCount() != 2) || (e.getButton() != 1))
					return;
				if ((e.getModifiersEx() & 0x80) != 128) {
					e.getModifiersEx();
				}
			}
		}

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			Object source = e.getSource();
			if (source == WasTools.this.filelistTree) {
				if (e.getButton() == 3) {
					TreePath path = WasTools.this.filelistTree.getPathForLocation(e.getX(), e.getY());
					TreePath[] paths = WasTools.this.filelistTree.getSelectionPaths();
					if ((path != null) && (paths != null)) {
						for (TreePath p : paths) {
							if (p == path)
								return;
						}
					}
					WasTools.this.filelistTree.setSelectionPath(path);
				}
			} else if ((source == WasTools.this.layerList) && (e.getButton() == 3)) {
				int index = WasTools.this.layerList.locationToIndex(e.getPoint());
				if (index != -1) {
					Rectangle rect = WasTools.this.layerList.getCellBounds(index, index);
					if (e.getY() > rect.y + rect.height)
						return;
					WasTools.this.layerList.setSelectedIndex(index);
					WasTools.this.layerList.setLastSelectedItem(WasTools.this.layerList.getSelectedValue());
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void propertyChange(PropertyChangeEvent evt) {
			String cmd = evt.getPropertyName();
			if ("LayerSelected".equals(cmd)) {
				WasTools.this.layerList.setSelectedValue(evt.getNewValue(), true);
				WasTools.this.layerList.setLastSelectedItem(evt.getNewValue());
			} else if ("canvas data has changed".equals(cmd)) {
				WasTools.this.updateLayerPanel();
			}
		}

		public void removeUpdate(DocumentEvent e) {
			WasTools.this.filterTree();
		}

		public void valueChanged(ListSelectionEvent evt) {
			CanvasImage image = (CanvasImage) WasTools.this.layerList.getSelectedValue();
			if (image != null) {
				if (image != WasTools.this.curCanvas.curImage) {
					WasTools.this.curCanvas.setCurrentImage(image);
				}
				WasTools.this.layerInfoText.setText(image.getInfo());
			} else {
				WasTools.this.layerInfoText.setText(null);
			}
		}

		public void valueChanged(TreeSelectionEvent e) {
			WasTools.this.eventHandler
					.actionPerformed(new ActionEvent(WasTools.this.filelistTree, 1001, "preview was"));
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
			WasTools.this.exit();
		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowOpened(WindowEvent e) {
		}
	}

	private static enum HitsType {
		ERROR, INFORMATION, QUESTION, WARNING;
	}

	class LayerListTransferHandler extends TransferHandler {
		private static final long serialVersionUID = 1L;

		LayerListTransferHandler() {
		}

		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			if (!(comp instanceof JList)) {
				return false;
			}
			for (DataFlavor flavor : transferFlavors) {
				if (WasTools.delfaultFlavor.equals(flavor))
					return true;
			}
			return false;
		}

		protected Transferable createTransferable(JComponent c) {
			if ((c instanceof JList)) {
				JList list = (JList) c;
				Object[] items = list.getSelectedValues();
				return new ListTransferable(items);
			}
			return null;
		}

		public int getSourceActions(JComponent c) {
			return 2;
		}

		public boolean importData(JComponent comp, Transferable t) {
			try {
				JList.DropLocation dropLocation = WasTools.this.layerList.getDropLocation();
				int insertIndex = dropLocation.getIndex();
				Object[] items = (Object[]) t.getTransferData(WasTools.delfaultFlavor);
				for (int i = 0; i < items.length; i++) {
					int index = WasTools.this.layerListModel.indexOf(items[i]);
					if (index < insertIndex)
						insertIndex--;
					WasTools.this.layerListModel.remove(index);
				}
				for (int i = items.length - 1; i >= 0; i--) {
					WasTools.this.layerListModel.insertElementAt(items[i], insertIndex);
				}
				WasTools.this.layerList.setSelectionInterval(insertIndex, insertIndex + items.length - 1);

				WasTools.this.curCanvas.getImages().clear();
				int size = WasTools.this.layerListModel.size();
				for (int i = 0; i < size; i++) {
					WasTools.this.curCanvas.getImages().add((CanvasImage) WasTools.this.layerListModel.elementAt(i));
				}
				return true;
			} catch (Exception localException) {
			}
			return false;
		}
	}

	class ListTransferable implements Transferable {
		Object[] items;

		public ListTransferable(Object[] items) {
			this.items = items;
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			return this.items;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { WasTools.delfaultFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return WasTools.delfaultFlavor.equals(flavor);
		}
	}

	class PreviewCanvas extends Canvas {
		private static final long serialVersionUID = 1L;

		public PreviewCanvas(int width, int height) {
			super(width, height);
			setBorder(null);
			setAutoZoom(true);
		}

		public void setImage(CanvasImage image) {
			this.curImage = image;
			getImages().removeAllElements();
			if (this.curImage != null) {
				getImages().add(this.curImage);
				int width = Math.max(getWidth(), this.curImage.getWidth());
				int height = Math.max(getHeight(), this.curImage.getHeight());
				setCanvasSize(width, height, true);
				int x = (width - this.curImage.getWidth()) / 2;
				int y = (height - this.curImage.getHeight()) / 2;
				if (x < 0)
					x = 0;
				if (y < 0)
					y = 0;
				this.curImage.setLocation(x, y);
				setVisible(true);
			} else {
				setVisible(false);
			}
		}
	}

	private class WasFilterTreeModel extends FilterTreeModel {
		private static final long serialVersionUID = 1L;

		public WasFilterTreeModel(TreeNode root, TreeFilter filter) {
			super(root, filter);
		}

		public void valueForPathChanged(TreePath path, Object newValue) {
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) path.getLastPathComponent();
			try {
				WasFile fnode = (WasFile) aNode.getUserObject();
				fnode.name = newValue.toString();

				nodeChanged(aNode);
			} catch (Exception localException) {
			}
		}
	}
}