package commons.box.util;

import commons.box.app.AppLog;
import commons.box.app.AppResource;
import org.ini4j.Ini;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 载入INI配置文件
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：15/11/31 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class INIs {
    private static final AppLog LOG = Logs.get(INIs.class);
    private static final Map<String, Map<String, String>> EMPTY_MAP = Maps.immmap(null);

    /**
     * 返回配置 每节一个map 未分节的配置在默认空字符串为key的map中
     *
     * @param file INI文件
     * @return
     */
    public static Map<String, Map<String, String>> load(File file) {
        return load(new FileIniLoadConsumer(file));
    }


    /**
     * 返回配置 每节一个map 未分节的配置在默认空字符串为key的map中
     *
     * @param is INI文件流
     * @return
     */
    public static Map<String, Map<String, String>> load(InputStream is) {
        return load(new InputStreamIniLoadConsumer(is));
    }


    /**
     * 返回配置 每节一个map 未分节的配置在默认空字符串为key的map中
     *
     * @param url INI文件路径
     * @return
     */
    public static Map<String, Map<String, String>> load(URL url) {
        return load(new URLIniLoadConsumer(url));
    }

    /**
     * 返回配置 每节一个map 未分节的配置在默认空字符串为key的map中
     *
     * @param resourcePath 资源路径 支持classpath*:/a/b 样式的通配符 整合多个匹配资源
     * @return
     */
    public static Map<String, Map<String, String>> load(String resourcePath) {
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        List<AppResource> res = Resources.resources(resourcePath);
        for (AppResource r : res) {
            Map<String, Map<String, String>> m = load(r);
            if (m != null) Maps.combin(map, m);
        }

        return map;
    }

    /**
     * 返回配置 每节一个map 未分节的配置在默认空字符串为key的map中
     *
     * @param resource
     * @return
     */
    public static Map<String, Map<String, String>> load(AppResource resource) {
        if (resource == null) return EMPTY_MAP;
        InputStream is = null;
        try {
            is = resource.stream();
            return load(new InputStreamIniLoadConsumer(is));
        } catch (Throwable e) {
            LOG.error("无法读取文件 " + resource);
        } finally {
            if (is != null) IOs.close(is);
        }
        return null;
    }

    /**
     * 返回配置 每节一个map 未分节的配置在默认空字符串为key的map中
     *
     * @param content INI文件内容
     * @return
     */
    public static Map<String, Map<String, String>> loadByContent(String content) {
        return load(new StringIniLoadConsumer(content));
    }


    /**
     * 返回配置 每节一个map 未分节的配置在默认空字符串为key的map中 需实现iniConsumer方法调用ini.load()
     *
     * @param iniConsumer
     * @return
     */
    public static Map<String, Map<String, String>> load(Consumer<Ini> iniConsumer) {
        if (iniConsumer == null) return EMPTY_MAP;

        Map<String, Map<String, String>> configs = new LinkedHashMap<>();

        try {
            Ini ini = new Ini();

            iniConsumer.accept(ini);

            for (Map.Entry<String, Ini.Section> me : ini.entrySet()) {
                String mk = me.getKey();
                Ini.Section mv = me.getValue();
                if (mk == null || mv == null) continue;
                Map<String, String> cv = configs.computeIfAbsent(mk, k -> new LinkedHashMap<>());

                for (Map.Entry m : mv.entrySet()) {
                    if (m.getKey() == null) continue;
                    cv.put(Strs.toString(m.getKey()), Strs.toString(m.getValue()));
                }
            }
        } catch (Throwable e) {
            LOG.warn("载入配置发生错误 " + e.getMessage(), e);
        }
        return configs;
    }

    private static final class FileIniLoadConsumer implements Consumer<Ini> {
        private final File file;

        public FileIniLoadConsumer(File file) {
            this.file = file;
        }

        @Override
        public void accept(Ini ini) {
            if (ini == null || this.file == null) return;
            try {
                ini.load(this.file);
            } catch (Throwable e) {
                LOG.error("载入INI内容发生错误 FILE=" + this.file);
            }
        }
    }


    private static final class InputStreamIniLoadConsumer implements Consumer<Ini> {
        private final InputStream is;

        public InputStreamIniLoadConsumer(InputStream is) {
            this.is = is;
        }

        @Override
        public void accept(Ini ini) {
            if (ini == null || this.is == null) return;
            try {
                ini.load(this.is);
            } catch (Throwable e) {
                LOG.error("载入INI内容发生错误 InputStream=" + this.is);
            }
        }
    }


    private static final class StringIniLoadConsumer implements Consumer<Ini> {
        private final String str;

        public StringIniLoadConsumer(String str) {
            this.str = str;
        }

        @Override
        public void accept(Ini ini) {
            if (ini == null || Strs.isBlank(this.str)) return;
            InputStream is = null;
            try {
                byte[] bytes = this.str.getBytes(Langs.CHARSET_UTF8);
                is = new ByteArrayInputStream(bytes);
                ini.load(is);
            } catch (Throwable e) {
                LOG.error("载入INI内容发生错误 InputStream=" + this.str);
            } finally {
                if (is != null) IOs.close(is);
            }
        }
    }


    private static final class URLIniLoadConsumer implements Consumer<Ini> {
        private final URL url;

        public URLIniLoadConsumer(URL url) {
            this.url = url;
        }

        @Override
        public void accept(Ini ini) {
            if (ini == null || this.url == null) return;
            try {
                ini.load(this.url);
            } catch (Throwable e) {
                LOG.error("载入INI内容发生错误 URL=" + this.url);
            }
        }
    }
}
