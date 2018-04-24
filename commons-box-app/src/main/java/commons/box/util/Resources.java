package commons.box.util;

import commons.box.app.AppLog;
import commons.box.app.AppResource;
import commons.box.app.SafeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static commons.box.app.internal.InternalAppResourceLoaders.*;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/21 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Resources {
    private static final AppLog LOG = Logs.get(Resources.class);
    public static final String PROTOCOL_FILE = "file:";
    public static final String PROTOCOL_CLASSPATH = "classpath:";
    public static final String PROTOCOL_CLASSPATHS = "classpath*:";

    private static final Holder HOLDER = new Holder();

    private Resources() {
    }

    public static void addLoader(String protocol, AppResource.Loader loader, boolean overwrite) {
        HOLDER.addLoader(protocol, loader, overwrite);
    }

    public static void addLoader(AppResource.Loader loader, boolean overwrite) {
        if (loader != null) {
            String[] protocols = loader.protocols();
            if (protocols != null) for (String p : protocols) HOLDER.addLoader(p, loader, overwrite);
        }
    }

    public static void getLoader(String protocol) {
        HOLDER.getLoader(protocol);
    }

    /**
     * 获取定义的资源 返回找到的首个资源文件
     *
     * @param pattern
     * @return
     */
    public static AppResource resource(String pattern) {
        AppResource.Loader loader = HOLDER.getLoaderByPattern(pattern);

        if (loader != null) return loader.getResource(pattern);
        else return null;
    }

    /**
     * 获取定义的资源 返回找到的全部文件
     * <p>
     * 注意本方法中，未找到资源是返回 Empty List
     * <p>
     * 此方法支持 * 匹配
     *
     * @param patterns
     * @return 找到的资源，未找到返回空List，总是不返回null
     */
    public static List<AppResource> resources(String... patterns) {
        List<AppResource> resources = new ArrayList<>();
        if (patterns != null) for (String p : patterns) {
            AppResource.Loader loader = HOLDER.getLoaderByPattern(p);

            if (loader != null) {
                List<? extends AppResource> ars = loader.getResources(p);
                if (ars != null) resources.addAll(ars);
            }
        }
        return Collects.immlist(resources);
    }

    private static class Holder {
        private final Map<String, AppResource.Loader> loaders;

        public Holder() {
            this.loaders = new SafeMap<>();
            this.addLoader(PROTOCOL_FILE, FileLoader.INST, true);
            this.addLoader(PROTOCOL_CLASSPATH, ClasspathLoader.INST, true);
        }

        public void addLoader(String protocol, AppResource.Loader loader, boolean overwrite) {
            if (loader == null) return;
            if (Strs.isBlank(protocol)) protocol = PROTOCOL_FILE;
            protocol = Strs.trim(protocol);
            if (!Strs.endsWith(protocol, ":")) protocol = protocol + ":";

            final String fprotocol = protocol;
            synchronized (this.loaders) {
                if (overwrite) {
                    this.loaders.put(protocol, loader);
                    LOG.debug(() -> "注册了 ResourceLoader - " + fprotocol + " " + loader.getClass().getSimpleName());
                } else if (!this.loaders.containsKey(protocol)) {
                    this.loaders.put(protocol, loader);
                    LOG.debug(() -> "注册了 ResourceLoader - " + fprotocol + " " + loader.getClass().getSimpleName());
                }
            }
        }

        public AppResource.Loader getLoader(String protocol) {
            if (Strs.isBlank(protocol)) protocol = PROTOCOL_FILE;
            protocol = Strs.trim(protocol);
            if (!Strs.endsWith(protocol, ":")) protocol = protocol + ":";
            AppResource.Loader loader = this.loaders.get(protocol);

            if (loader == null) {
                synchronized (this.loaders) {
                    loader = this.loaders.get(protocol); // double check
                    if (loader == null) this.addLoader(protocol, NullLoader.INST, false);
                }
            }

            if (loader == null) return null;

            return (loader != NullLoader.INST) ? loader : null;
        }

        public AppResource.Loader getLoaderByPattern(String pattern) {
            if (Strs.isBlank(pattern)) return null;
            pattern = Strs.trim(pattern);

            String protocol = (Strs.contains(pattern, ":")) ? (Strs.subBefore(pattern, ":") + ":") : PROTOCOL_FILE;

            return getLoader(protocol);
        }

    }
}
