package commons.box.spring.resource;

import commons.box.app.AppLog;
import commons.box.app.AppResource;
import commons.box.util.Logs;
import commons.box.util.Resources;
import commons.box.util.Strs;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class SpringAppResourceLoader implements AppResource.Loader {
    private static final AppLog LOG = Logs.get(SpringAppResourceLoader.class);

    private final ResourcePatternResolver resolver;

    public SpringAppResourceLoader(ResourcePatternResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public String[] protocols() {
        return new String[]{Resources.PROTOCOL_FILE, Resources.PROTOCOL_CLASSPATH, Resources.PROTOCOL_CLASSPATHS};
    }

    @Override
    public boolean supportAntPattern() {
        return true;
    }

    /**
     * 与Spring ResourceLoader 不同之处：如果path中包含*，尝试使用getResources匹配所有可能并返回第一个存在的元素
     *
     * @param path
     * @return
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public SpringResource getResource(String path) {
        if (this.resolver == null) return null;


        if (Strs.contains(path, "*")) { // 匹配模式查找时 返回第一个存在的元素
            List<SpringResource> srs = getResources(path);
            for (SpringResource sr : srs) if (sr != null && sr.exists()) return sr;
        } else {
            Resource resource = null;
            try {
                resource = this.resolver.getResource(path);
            } catch (Throwable ignored) {
            }

            if (resource != null) return new SpringAppResourceImpl(resource);
        }

        return null;
    }

    @Override
    @Nonnull
    public List<SpringResource> getResources(String... paths) {
        List<SpringResource> resources = new ArrayList<>();
        if (this.resolver == null) return resources;

        if (paths != null) for (String p : paths) {
            if (Strs.isBlank(p)) continue;

            Resource[] srs = null;
            try {
                srs = this.resolver.getResources(p);
            } catch (Throwable ignored) {
            }

            if (srs != null) for (Resource r : srs) {
                if (r == null) continue;
                resources.add(new SpringAppResourceImpl(r));
            }
        }
        return resources;
    }


    @SuppressWarnings("ConstantConditions")
    public static String toURLString(Resource resource) {
        if (resource == null) return null;
        try {
            URL url = resource.getURL();
            if (url != null) return url.toString();
        } catch (Throwable e) {
            LOG.debug("文件 " + resource + " 无法解析URL");
        }

        try {
            URI uri = resource.getURI();
            if (uri != null) return uri.toString();
        } catch (Throwable e) {
            LOG.debug("文件 " + resource + " 无法解析URI");
        }

        if (resource.isFile()) {
            try {
                File file = resource.getFile();
                if (file != null) return file.getCanonicalPath();
            } catch (Throwable e) {
                LOG.debug("文件 " + resource + " 无法解析 File");
            }
        }

        return null;
    }
}
