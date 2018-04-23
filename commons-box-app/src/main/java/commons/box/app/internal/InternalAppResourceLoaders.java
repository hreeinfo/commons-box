package commons.box.app.internal;

import commons.box.app.AppLog;
import commons.box.app.AppResource;
import commons.box.util.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/22 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalAppResourceLoaders {
    private static final AppLog LOG = Logs.get(AppResource.Loader.class);

    public static class FileResource implements AppResource {
        private final File file;
        private final String url;

        public FileResource(File file) {
            this.file = file;
            this.url = URLs.toURL(this.file);
        }

        @Override
        public boolean isFile() {
            return true;
        }

        @Override
        public File file() {
            return this.file;
        }

        @Override
        public String url() {
            return this.url;
        }

        @Override
        public InputStream stream() {
            return IOs.inputStreamCatched(this.file);
        }

        @Override
        public long lastModified() {
            if (this.file == null) return -1;
            return this.file.lastModified();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FileResource that = (FileResource) o;

            return Objects.equals(this.file, that.file);
        }

        @Override
        public int hashCode() {
            return Objects.hash(file);
        }

        @Override
        public String toString() {
            return "FileResource{" + file + '}';
        }
    }

    public static class ClasspathResource implements AppResource {
        private final String path;

        public ClasspathResource(String path) {
            this.path = path;
        }

        @Override
        public boolean isFile() {
            return false;
        }

        @Override
        public File file() {
            return null;
        }

        @Override
        public String url() {
            return "classpath:/" + this.path;
        }

        @Override
        public InputStream stream() {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(this.path);
        }

        @Override
        public long lastModified() {
            return -1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClasspathResource that = (ClasspathResource) o;

            return Objects.equals(this.path, that.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }


        @Override
        public String toString() {
            return this.url();
        }
    }

    public static class FileLoader implements AppResource.Loader {
        public static final FileLoader INST = new FileLoader();

        public FileLoader() {
        }

        @Override
        public String[] protocols() {
            return new String[]{Resources.PROTOCOL_FILE};
        }

        @Override
        public boolean supportAntPattern() {
            return false;
        }

        @Override
        public AppResource getResource(String path) {
            if (Strs.isBlank(path)) return null;
            try {
                File file;
                if (Strs.startsWith(path, Resources.PROTOCOL_FILE)) file = Paths.get(new URL(path).toURI()).toFile();
                else file = new File(path);

                if (file != null) return new FileResource(file);
            } catch (Throwable e) {
                LOG.error("文件不存在 " + path);
            }

            return null;
        }

        @Override
        public List<? extends AppResource> getResources(String... paths) {
            List<AppResource> res = new ArrayList<>();

            if (paths != null) for (String p : paths) {
                AppResource ap = this.getResource(p);
                if (ap != null) res.add(ap);
            }
            return res;
        }


    }


    public static class ClasspathLoader implements AppResource.Loader {
        public static final ClasspathLoader INST = new ClasspathLoader();

        private ClasspathLoader() {
        }

        @Override
        public String[] protocols() {
            return new String[]{Resources.PROTOCOL_CLASSPATH};
        }

        @Override
        public boolean supportAntPattern() {
            return false;
        }

        @Override
        public AppResource getResource(String path) {
            if (Strs.isBlank(path)) return null;
            if (path.startsWith(Resources.PROTOCOL_CLASSPATH)) path = Strs.subAfter(path, Resources.PROTOCOL_CLASSPATH);
            if (path.startsWith("/")) path = Strs.subAfter(path, "/");

            return new ClasspathResource(path);
        }

        @Override
        public List<? extends AppResource> getResources(String... paths) {
            List<AppResource> res = new ArrayList<>();

            if (paths != null) for (String p : paths) {
                AppResource ap = this.getResource(p);
                if (ap != null) {
                    res.add(ap);
                }
            }
            return res;
        }
    }

    public static class NullLoader implements AppResource.Loader {
        private static final List<AppResource> EMPTY = Collects.immlist(new ArrayList<>());
        public static final NullLoader INST = new NullLoader();

        private NullLoader() {
        }

        @Override
        public String[] protocols() {
            return new String[]{};
        }

        @Override
        public boolean supportAntPattern() {
            return false;
        }

        @Override
        public AppResource getResource(String path) {
            return null;
        }

        @Override
        public List<? extends AppResource> getResources(String... paths) {
            return EMPTY;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new File("/cdd/aaa").toURI().toURL());
        System.out.println(InternalAppResourceLoaders.class.getClassLoader().getResourceAsStream("java/lang/Integer.class"));
    }
}
