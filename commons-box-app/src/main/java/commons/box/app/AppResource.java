package commons.box.app;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * 表示系统资源 TODO 具体实现类
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface AppResource {

    public static interface Loader {
        public String[] protocols();

        public boolean supportAntPattern();

        public AppResource getResource(String path);

        public List<? extends AppResource> getResources(String... paths);
    }

    /**
     * 判断是否是文件
     *
     * @return
     */
    public boolean isFile();

    /**
     * 返回文件
     *
     * @return
     */
    public File file();

    /**
     * 返回资源对应的 URL
     *
     * @return
     */
    public String url();

    /**
     * 返回 InputStream 流 可读取内容
     *
     * @return
     */
    public InputStream stream();

    /**
     * 最后修改时间 用于判断时间戳
     *
     * 未实现需返回 -1
     * @return
     */
    long lastModified();
}
