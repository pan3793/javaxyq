package open.xyq.core.fmt.wdf;

import lombok.extern.slf4j.Slf4j;
import open.xyq.core.Utils;
import open.xyq.core.fmt.FileObject;
import open.xyq.core.fmt.FileSystem;
import open.xyq.core.util.StrUtil;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;


/**
 * .WDF文件类
 *
 * @author 龚德伟
 */
@Slf4j
public class WdfFile implements FileSystem {
    // 大话西游2/梦幻西游/富甲西游 WDF文件标记
    private static final String MAGIC_HEAD_WDF = "PFDW";

    // 文件句柄
    private RandomAccessFile raf;
    // 包内结点个数
    private int fileNodeCount;
    // 文件全名
    private String filename;
    // 结点映射表
    private final Map<Long, WdfFileNode> fileNodeMap = new HashMap<>();

    private FileObject rootNode;
    private String fileTag;

    public WdfFile(String filename, boolean resovePath) throws Exception {
        loadWdf(filename, resovePath);
    }

    public WdfFile(String filename) throws Exception {
        loadWdf(filename, true);
    }

    private void loadWdf(String filename, boolean resovePath) throws Exception {
        try {
            filename = filename.replace('\\', '/');
            this.filename = filename;
            raf = new RandomAccessFile(filename, "r");
            if (!isWdfFile(raf)) {
                throw new IllegalArgumentException("这个不是WDF格式的文件！path=" + filename + ",tag=" + fileTag);
            }
            //read
            fileNodeCount = readInt(raf);
            if (fileNodeCount == 0) {//可能是【精灵牧场】格式(四字节00)
                fileNodeCount = readInt(raf);
            }
            int headerSize = readInt(raf);
            raf.seek(headerSize);
            fileNodeMap.clear();

            for (int i = 0; i < fileNodeCount; i++) {
                WdfFileNode node = new WdfFileNode();
                long id = readUnsignInt(raf);
                node.setId(id);
                int offset = readInt(raf);
                node.setOffset(offset);
                int size = readInt(raf);
                node.setSize(size);
                int space = readInt(raf);
                node.setSpace(space);
                node.setFileSystem(this);
                fileNodeMap.put(node.getId(), node);
            }
            rootNode = new WdfDirObject(this);
            if (resovePath) {
                //load description
                load(null);
                //restore path
                String name = StrUtil.substringAfterLast(filename, "/");
                restorePaths(name);
            }
        } catch (Exception e) {
            log.error("打开WDF文件出错: " + filename, e);
            throw new Exception("打开WDF文件出错：" + filename, e);
        }
        log.info("nodeCount={}, total find:{}", fileNodeCount, fileNodes().size());
    }

    /**
     * 判断打开的文件是否为WDF文件
     */
    private boolean isWdfFile(RandomAccessFile raf) throws IOException {
        byte[] buf = new byte[4];
        raf.seek(0);
        raf.read(buf);
        fileTag = new String(buf);
        return fileTag.equals(MAGIC_HEAD_WDF);
    }

    private Map<Long, String> buildPaths(String filename) {
        String cmtFile = "resources/names/" + filename.replaceAll("\\.wd.*", ".cmt");
        Map<Long, String> map = new HashMap<>();
        InputStream is = Utils.getResourceAsStream(cmtFile);
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strPath;
            try {
                while ((strPath = br.readLine()) != null) {
                    try {
                        strPath = strPath.trim();
                        if (strPath.length() > 0) {
                            String[] strs = strPath.split("=");
                            if (strs.length > 1) {
                                map.put(Long.parseLong(strs[0], 16), strs[1]);
                            }
                        }
                    } catch (Exception e) {
                        log.error("解析资源映射失败：" + strPath);
                    }
                }
            } catch (Throwable e) {
                log.error("还原文件名列表失败: " + cmtFile, e);
            }
        } else {
            log.error("读取资源失败: " + cmtFile);
        }
        return map;
    }

    private void restorePaths(String name) {
        Map<Long, String> id2PathMap = buildPaths(name);
        int iCount = 0;
        for (Entry<Long, WdfFileNode> entry : fileNodeMap.entrySet()) {
            WdfFileNode node = entry.getValue();
            String path = id2PathMap.get(entry.getKey());
            if (path != null) {
                path = path.replace('\\', '/');
                if (path.charAt(0) != '/')
                    path = "/" + path;
                node.setPath(path);
                node.setName(StrUtil.substringAfterLast(path, "/"));
                iCount++;
            } else { // 找不到path
                String strId = Long.toHexString(entry.getKey());
                node.setPath("/" + strId);
            }
        }
        log.info("共有文件{}个, 匹配文件{}个, 无匹配文件{}个", fileNodeCount, iCount, fileNodeCount - iCount);
    }

    public void close() throws IOException {
        if (this.raf != null) {
            this.raf.close();
        }
        this.fileNodeMap.clear();
    }

    private int readInt(DataInput di) throws IOException {
        int ch1, ch2, ch3, ch4;
        ch1 = di.readUnsignedByte();
        ch2 = di.readUnsignedByte();
        ch3 = di.readUnsignedByte();
        ch4 = di.readUnsignedByte();
        return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1;
    }

    private long readUnsignInt(DataInput di) throws IOException {
        long ch1, ch2, ch3, ch4;
        ch1 = di.readUnsignedByte();
        ch2 = di.readUnsignedByte();
        ch3 = di.readUnsignedByte();
        ch4 = di.readUnsignedByte();
        return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1;
    }

    /**
     * 获取WdfFile包含的文件结点集合
     */
    public Collection<WdfFileNode> fileNodes() {
        return fileNodeMap.values();
    }

    public int getFileNodeCount() {
        return fileNodeCount;
    }

    /**
     * 根据id获取对应的数据
     */
    public InputStream getNodeAsStream(long nodeId) throws IOException {
        return new ByteArrayInputStream(this.getNodeData(nodeId));
    }

    /**
     * 根据id获取对应的数据
     */
    public byte[] getNodeData(long nodeId) throws IOException {
        //检索结点对象
        WdfFileNode fNode = fileNodeMap.get(nodeId);
        byte[] data = null;
        if (fNode != null) {
            data = new byte[(int) fNode.getSize()];
            //偏移到结点数据段位置
            raf.seek(fNode.getOffset());
            //读取结点数据
            raf.readFully(data);
        }
        return data;
    }

    @Override
    public String toString() {
        return "[wdf name=" + filename + "]";
    }

    public String getName() {
        return filename;
    }

    public WdfFileNode findNode(long nodeId) {
        return fileNodeMap.get(nodeId);
    }

    /**
     * 加载描述文件
     */
    public void loadDescription(InputStream is) {
        if (is != null) {
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("(\r\n)|(\n\r)|[\n\r=]");
            String tag = scanner.next();
            long uid;
            String str = null, alias = null;
            int iCount = 0;
            if (tag.startsWith("[Resource]")) {
                while (scanner.hasNext()) {
                    WdfFileNode node = null;
                    try {
                        scanner.skip("(\r\n)|(\n\r)|[\n\r=]");
                        str = scanner.next();
                        uid = Long.parseLong(str, 16);
                        scanner.skip("(\r\n)|(\n\r)|[\n\r=]");
                        alias = scanner.next().trim();
                        node = fileNodeMap.get(uid);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (node != null) {
                        node.setDescription(alias.replace('\\', '/'));
                        //System.out.println("资源:" + Long.toHexString(uid) + "=" + alias);
                    } else {
                        log.info("找不到对于的资源:" + str + "=" + alias);
                    }
                    iCount++;
                }
            }
            log.info("total : " + iCount);
            scanner.close();
        }
    }

    /**
     * 取path下面的结点列表
     *
     * @param path    路径
     * @param subPath 是否包含子目录
     */
    public List<WdfFileNode> getNodesUnderPath(String path, boolean subPath) {
        return null;
    }

    /**
     * 保存资源描述文件
     */
    public void saveDescription(FileOutputStream os) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
            writer.write("[Resource]\r\n");
            List<WdfFileNode> nodeList = getNodesUnderPath("/", false);
            for (WdfFileNode node : nodeList) {
                if (node.getName() != null && node.getName().length() > 0) {
                    writer.write(Long.toHexString(node.getId()).toUpperCase());
                    writer.write('=');
                    writer.write(node.getName());
                    writer.write("\r\n");
                }
            }
        } catch (Exception ex) {
            log.warn("", ex);
        }
    }

    @Override
    public FileObject getRoot() {
        return rootNode;
    }

    @Override
    public String getType() {
        return "wdf";
    }

    @Override
    public void load(String descfile) {
        if (descfile == null)
            descfile = "resources/desc/" + new File(this.filename).getName() + ".ini";
        this.loadDescription(Utils.getResourceAsStream(descfile));
    }

    @Override
    public void save(String filename) {
        try {
            this.saveDescription(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            log.error("save description failed! filename=" + filename, e);
        }
    }

    /**
     * 从offset开始读取长度为len的数据块
     */
    public void read(byte[] data, int offset, int len) throws IOException {
        raf.seek(offset);
        raf.readFully(data, 0, len);
    }

}
