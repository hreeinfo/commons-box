package commons.box.spring.resource;

import commons.box.app.AppLog;
import commons.box.util.Logs;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class SpringAppResourceImpl implements SpringResource {
    private static final AppLog LOG = Logs.get(SpringResource.class);
    private final Resource resource;
    private final String url;

    public SpringAppResourceImpl(Resource resource) {
        this.resource = resource;
        this.url = SpringAppResourceLoader.toURLString(resource);
    }


    @Override
    public boolean isFile() {
        if (this.resource == null) return false;
        return this.resource.isFile();
    }

    @Override
    public File file() {
        if (this.resource == null) return null;
        try {
            return this.resource.getFile();
        } catch (Throwable e) {
            LOG.debug("文件 " + this.resource + " 无法解析文件");
        }
        return null;
    }

    @Override
    public String url() {
        return this.url;
    }

    @Override
    public InputStream stream() {
        if (this.resource == null) return null;
        try {
            return this.resource.getInputStream();
        } catch (Throwable e) {
            LOG.debug("文件 " + this.resource + " 不存在");
        }
        return null;
    }

    @Override
    public long lastModified() {
        if (this.resource == null) return -1;
        try {
            return this.resource.lastModified();
        } catch (Throwable e) {
            LOG.debug("文件 " + this.resource + " 不存在");
        }
        return -1;
    }

    public Resource resource() {
        return this.resource;
    }

    @Override
    public boolean isReadable() {
        if (this.resource == null) return true;
        return this.resource.isReadable();
    }

    @Override
    public boolean isOpen() {
        if (this.resource == null) return false;
        return this.resource.isOpen();
    }

    @Override
    public boolean exists() {
        if (this.resource == null) return false;
        return this.resource.exists();
    }

    @Override
    public URL getURL() throws IOException {
        if (this.resource == null) return null;
        return this.resource.getURL();
    }

    @Override
    public URI getURI() throws IOException {
        if (this.resource == null) return null;
        return this.resource.getURI();
    }

    @Override
    public File getFile() throws IOException {
        if (this.resource == null) return null;
        return this.file();
    }

    @Override
    public long contentLength() throws IOException {
        if (this.resource == null) return 0;
        return this.resource.contentLength();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        if (this.resource == null) return null;
        return this.resource.createRelative(relativePath);
    }

    @Override
    public String getFilename() {
        if (this.resource == null) return null;
        return this.resource.getFilename();
    }

    @Override
    public String getDescription() {
        if (this.resource == null) return null;
        return this.resource.getDescription();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (this.resource == null) return null;
        return this.stream();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpringAppResourceImpl that = (SpringAppResourceImpl) o;

        return Objects.equals(this.url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]").add(url).toString();
    }
}
