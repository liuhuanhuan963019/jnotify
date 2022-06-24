
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyAdapter;
import net.contentobjects.jnotify.JNotifyException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class NewListen extends JNotifyAdapter {
    // 需要监听的文件路径地址
    String path = "E:/data";
    /** 关注目录的事件 */
    int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
    /** 是否监视子目录，即级联监视 */
    boolean watchSubtree = true;
    /** 监听程序Id */
    public int watchID;

    public static void main(String[] args) {
        new NewListen().beginWatch();
    }
    /**
     * 容器启动时启动监视程序
     *
     * @return
     */
    public void beginWatch() {
        /** 添加到监视队列中 */
        try {
            this.watchID = JNotify.addWatch(path, mask, watchSubtree, this);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println("开始文件监听:" + df.format(new Date()) + "\t 文件目录:" + path);
        } catch (JNotifyException e) {
            e.printStackTrace();
        }
        // 死循环，线程一直执行，休眠一分钟后继续执行，主要是为了让主线程一直执行
        // 休眠时间和监测文件发生的效率无关（就是说不是监视目录文件改变一分钟后才监测到，监测几乎是实时的，调用本地系统库）
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {// ignore it
            }
        }
    }

    /**
     * 当监听目录下一旦有新的文件被创建，则即触发该事件
     *
     * @param wd
     *            监听线程id
     * @param rootPath
     *            监听目录
     * @param name
     *            文件名称
     */
    @Override
    public void fileCreated(int wd, String rootPath, String name) {
        System.err.println("文件被创建, 创建位置为： " + rootPath + "/" + name);
    }

    @Override
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        System.err.println("文件被重命名, 原文件名为：" + rootPath + "/" + oldName
                + ", 现文件名为：" + rootPath + "/" + newName);
    }
    @Override
    public void fileModified(int wd, String rootPath, String name) {
        System.err.println("文件内容被修改, 文件名为：" + rootPath + "/" + name);
    }
    @Override
    public void fileDeleted(int wd, String rootPath, String name) {
        System.err.println("文件被删除, 被删除的文件名为：" + rootPath +"/"+ name);
    }


}
