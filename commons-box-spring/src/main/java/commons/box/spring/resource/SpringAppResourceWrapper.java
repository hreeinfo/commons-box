package commons.box.spring.resource;

import commons.box.app.AppLog;
import commons.box.app.AppResource;
import commons.box.util.IOs;
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
 * <p>创建日期：2018/4/22 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class SpringAppResourceWrapper implements SpringResource {
    private static final AppLog LOG = Logs.get(SpringAppResourceWrapper.class);

    private final AppResource appResource;
    private final Resource resource;
    private final String url;

    public SpringAppResourceWrapper(AppResource appResource) {
        this.appResource = appResource;
        if (appResource instanceof Resource) this.resource = (Resource) appResource;
        else this.resource = null;
        this.url = this.tryGenURL();
    }

    private String tryGenURL() {
        if (this.appResource == null) return null;
        String url = null;
        if (this.resource != null) url = SpringAppResourceLoader.toURLString(this.resource);

        if (url == null) url = this.appResource.url();

        return url;
    }

    @Override
    public File file() {
        if (this.appResource == null) return null;
        return this.appResource.file();
    }

    @Override
    public String url() {
        return this.url;
    }

    @Override
    public InputStream stream() {
        if (this.appResource == null) return null;
        return this.appResource.stream();
    }

    @Override
    public long lastModified() {
        if (this.appResource == null) return -1;
        else if (this.resource == null) return this.appResource.lastModified();
        else {
            try {
                return this.resource.lastModified();
            } catch (Throwable ignored) {
            }
            return this.appResource.lastModified();
        }
    }

    @Override
    public boolean exists() {
        if (this.appResource == null) return false;
        else if (this.resource == null) {
            try {
                File f = this.file();
                if (f != null) {
                    return f.exists();
                }
            } catch (Throwable ignored) {
            }
            try {
                InputStream is = stream();
                if (is != null) {
                    IOs.close(is);
                    return true;
                }
            } catch (Throwable ignored) {
            }
            return false;
        } else return this.resource.exists();
    }

    @Override
    public URL getURL() throws IOException {
        if (this.appResource == null) return null;
        else if (this.resource == null) return new URL(this.appResource.url());
        else return this.resource.getURL();
    }

    @Override
    public URI getURI() throws IOException {
        if (this.appResource == null) return null;
        else if (this.resource == null) {
            try {
                return new URI(this.appResource.url());
            } catch (Throwable e) {
                LOG.debug("URL 无法生成表达式 " + this.appResource);
            }
        } else return this.resource.getURI();
        return null;
    }

    @Override
    public File getFile() throws IOException {
        if (this.resource == null) return file();
        return this.resource.getFile();
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
        if (this.resource == null) return stream();
        return this.resource.getInputStream();
    }

    @Override
    public boolean isFile() {
        if (this.appResource == null) return false;
        else if (this.resource == null) return this.appResource.isFile();
        else return this.resource.isFile() || this.appResource.isFile();
    }

    @Override
    public boolean isReadable() {
        if (this.appResource == null) return true;
        else if (this.resource == null) return true;
        else return this.resource.isReadable();
    }

    @Override
    public boolean isOpen() {
        if (this.appResource == null) return false;
        else if (this.resource == null) return false;
        else return this.resource.isOpen();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpringAppResourceWrapper that = (SpringAppResourceWrapper) o;

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
