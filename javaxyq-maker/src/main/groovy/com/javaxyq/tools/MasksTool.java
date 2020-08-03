package com.javaxyq.tools;

import com.javaxyq.ui.CenterLayout;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.Utils;
import open.xyq.core.ui.JMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

/**
 * 地图掩码工具
 *
 * @author dewitt
 * @date 2009-09-21 create
 */
@Slf4j
public class MasksTool extends JFrame {

    static final int CELL_WIDTH = 20;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MasksTool inst = new MasksTool();
            inst.setLocationRelativeTo(null);
            inst.setVisible(true);
        });
    }

    JMenuItem helpMenuItem;
    JMenu jMenu5;
    JMenuItem deleteMenuItem;
    JSeparator jSeparator1;
    JMenuItem pasteMenuItem;
    JMenuItem copyMenuItem;
    JMenuItem cutMenuItem;
    JMenu jMenu4;
    JMenuItem exitMenuItem;
    JSeparator jSeparator2;
    JMenuItem closeFileMenuItem;
    JMenuItem saveAsMenuItem;
    JMenuItem saveMenuItem;
    JMenuItem openFileMenuItem;
    JMenuItem newFileMenuItem;
    JMenu jMenu3;
    JMenuBar jMenuBar1;
    JPanel mainPanel;
    JMap map;
    String filename;
    JScrollPane mapPanel;
    int sceneWidth;
    int sceneHeight;
    String path;

    Color markColor = new Color(192, 0, 0, 80);
    Color lineColor = new Color(128, 0, 0, 120);

    private byte[] masks;

    public MasksTool() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            jMenuBar1 = new JMenuBar();
            setJMenuBar(jMenuBar1);
            jMenu3 = new JMenu();
            jMenuBar1.add(jMenu3);
            jMenu3.setText("File");
            openFileMenuItem = new JMenuItem();
            jMenu3.add(openFileMenuItem);
            openFileMenuItem.setText("Open");
            openFileMenuItem.addActionListener(e -> openMap());
            saveMenuItem = new JMenuItem();
            jMenu3.add(saveMenuItem);
            saveMenuItem.setText("Save");
            saveMenuItem.addActionListener(e -> saveMasks());
            saveAsMenuItem = new JMenuItem();
            jMenu3.add(saveAsMenuItem);
            saveAsMenuItem.setText("Save As ...");
            closeFileMenuItem = new JMenuItem();
            jMenu3.add(closeFileMenuItem);
            closeFileMenuItem.setText("Close");
            jSeparator2 = new JSeparator();
            jMenu3.add(jSeparator2);
            exitMenuItem = new JMenuItem();
            jMenu3.add(exitMenuItem);
            exitMenuItem.setText("Exit");
            jMenu4 = new JMenu();
            jMenuBar1.add(jMenu4);
            jMenu4.setText("Edit");
            cutMenuItem = new JMenuItem();
            jMenu4.add(cutMenuItem);
            cutMenuItem.setText("Cut");
            copyMenuItem = new JMenuItem();
            jMenu4.add(copyMenuItem);
            copyMenuItem.setText("Copy");
            pasteMenuItem = new JMenuItem();
            jMenu4.add(pasteMenuItem);
            pasteMenuItem.setText("Paste");
            jSeparator1 = new JSeparator();
            jMenu4.add(jSeparator1);
            deleteMenuItem = new JMenuItem();
            jMenu4.add(deleteMenuItem);
            deleteMenuItem.setText("Delete");
            jMenu5 = new JMenu();
            jMenuBar1.add(jMenu5);
            jMenu5.setText("Help");
            helpMenuItem = new JMenuItem();
            jMenu5.add(helpMenuItem);
            helpMenuItem.setText("Help");
            mainPanel = new JPanel(new BorderLayout());
            map = new JMap() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Rectangle rect = this.getVisibleRect();
                    int gx = rect.x - rect.x % CELL_WIDTH;
                    int gy = rect.y - rect.y % CELL_WIDTH;
                    for (int x = gx, maxX = rect.x + rect.width; x < maxX; x += CELL_WIDTH) {
                        // 画竖线
                        g.setColor(lineColor);
                        g.drawLine(x, rect.y, x, rect.y + rect.height);
                        for (int y = gy, maxY = rect.y + rect.height; y < maxY; y += CELL_WIDTH) {
                            // 画横线
                            g.setColor(lineColor);
                            g.drawLine(rect.x, y, rect.x + rect.width, y);
                            //画掩码标记
                            if (isMark(x, y)) {
                                g.setColor(markColor);
                                g.fillRect(x, y, CELL_WIDTH, CELL_WIDTH);
                            }
                        }
                    }

                }
            };
            //鼠标按下事件
            map.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (!e.isAltDown() && e.getButton() == MouseEvent.BUTTON1)
                        mark(e.getX(), e.getY());
                    else if (e.getButton() == MouseEvent.BUTTON3 || e.isAltDown())
                        unMark(e.getX(), e.getY());
                    map.paintImmediately(e.getX() - CELL_WIDTH, e.getY() - CELL_WIDTH, 2 * CELL_WIDTH, 2 * CELL_WIDTH);
                }
            });
            //鼠标拖曳
            map.addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    int paintX = e.getX() - CELL_WIDTH;
                    int paintY = e.getY() - CELL_WIDTH;
                    int paintWidth = 2 * CELL_WIDTH;
                    int paintHeight = 2 * CELL_WIDTH;
                    if (e.isAltDown()) {
                        unMark(e.getX(), e.getY());
                    } else if (e.isControlDown()) {
                        mark(e.getX() - CELL_WIDTH, e.getY() - CELL_WIDTH);
                        mark(e.getX() - CELL_WIDTH, e.getY());
                        mark(e.getX(), e.getY());
                        mark(e.getX(), e.getY() + CELL_WIDTH);
                        mark(e.getX() + CELL_WIDTH, e.getY() + CELL_WIDTH);
                        paintX -= CELL_WIDTH;
                        paintY -= CELL_WIDTH;
                        paintWidth *= 2;
                        paintHeight *= 2;
                    } else {
                        mark(e.getX(), e.getY());
                    }
                    map.paintImmediately(paintX, paintY, paintWidth, paintHeight);
                }
            });

            JPanel panel = new JPanel(new CenterLayout());
            panel.add("Center", map);
            mapPanel = new JScrollPane(panel);
            mainPanel.add(mapPanel, BorderLayout.CENTER);
            setContentPane(mainPanel);
            setTitle("Masks Builder");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 标记
     */
    private void mark(int x, int y) {
        int sx = x / CELL_WIDTH;
        int sy = y / CELL_WIDTH;
        int pos = sx + sy * sceneWidth;
        masks[pos] = 1;
    }

    private boolean isMark(int x, int y) {
        int sx = x / CELL_WIDTH;
        int sy = y / CELL_WIDTH;
        int pos = sx + sy * sceneWidth;
        return masks[pos] > 0;
    }

    /**
     * 取消标记
     */
    private void unMark(int x, int y) {
        int sx = x / CELL_WIDTH;
        int sy = y / CELL_WIDTH;
        int pos = sx + sy * sceneWidth;
        masks[pos] = 0;
    }

    private void openMap() {
        File[] files = Utils.showOpenDialog(this, "打开地图", Utils.MAP_FILTER);
        if (files != null) {
            File file = files[0];
            boolean b = map.loadMap(file);
            filename = file.getName();
            path = file.getPath();
            if (b) {
                setTitle("Masks Builder - " + filename);
                JViewport viewport = mapPanel.getViewport();
                viewport.setViewPosition(new Point());
                viewport.setViewSize(map.getMapSize());
                log.info("地图已打开:" + file.getAbsolutePath());
                Dimension size = map.getMapSize();
                log.info("地图大小：" + size.width + "*" + size.height);
                sceneWidth = size.width / CELL_WIDTH;
                sceneHeight = size.height / CELL_WIDTH;
                masks = new byte[sceneWidth * sceneHeight];
                log.info("Steps: " + sceneWidth + "*" + sceneHeight);
                String maskFilename = path.replace(".map", ".msk");
                loadMask(maskFilename);
            } else {
                JOptionPane.showMessageDialog(this, "打开地图文件失败:" + filename, "错误", JOptionPane.ERROR_MESSAGE);
                log.error("地图打开失败");
            }
        }
    }

    private void saveMasks() {
        String maskFilename = path.replace(".map", ".msk");
        try (FileOutputStream fos = new FileOutputStream(maskFilename);
             PrintWriter pw = new PrintWriter(fos)) {
            for (int i = 0; i < masks.length; i++) {
                pw.print(masks[i] == 0 ? '0' : '1');
                if (i % sceneWidth == sceneWidth - 1) pw.println();
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 加载地图的掩码
     */
    public void loadMask(String filename) {
        log.info("map steps: " + sceneWidth + "*" + sceneHeight);
        masks = new byte[sceneWidth * sceneHeight];
        try {
            InputStream in = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str;
            int pos = 0;
            while ((str = reader.readLine()) != null) {
                int len = str.length();
                for (int i = 0; i < len; i++)
                    masks[pos++] = (byte) (str.charAt(i) - '0');
            }
        } catch (Exception e) {
            log.info("加载地图掩码失败！", e);
        }
    }
}
