package commons.box.util;

import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.app.DataEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * IO 相关工具类
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：15/12/29 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class IOs {
    private static final AppLog LOG = Logs.get(IOs.class);

    private IOs() {
    }

    /**
     * 关闭目标
     *
     * @param target
     */
    public static void close(Closeable target) {
        try {
            if (target != null) target.close();
        } catch (Throwable ioe) {
            // 忽略
        }
    }

    /**
     * 关闭目标
     *
     * @param target
     */
    public static void close(ResultSet target) {
        try {
            if (target != null) target.close();
        } catch (Throwable sqle) {
            // 忽略
        }
    }

    public static void close(final LineIterator iterator) {
        try {
            if (iterator != null) {
                iterator.close();
            }
        } catch (final IOException e) {
            // 忽略
        }
    }

    /**
     * 拷贝内容
     *
     * @param input
     * @param output
     * @return
     * @throws AppError
     */
    public static int copy(InputStream input, OutputStream output) throws AppError {
        try {
            return IOUtils.copy(input, output);
        } catch (IOException e) {
            throw AppError.error(e);
        }
    }

    /**
     * 拷贝内容
     *
     * @param reader
     * @param writer
     * @return
     * @throws AppError
     */
    public static int copy(Reader reader, Writer writer) throws AppError {
        try {
            return IOUtils.copy(reader, writer);
        } catch (IOException e) {
            throw AppError.error(e);
        }
    }

    /**
     * 拷贝文件 如果目标文件不存在则新建文件 如果目标文件存在且是目录则拷贝到目录中 否则返回失败
     *
     * @param source
     * @param dest
     * @return
     */
    public static boolean copy(File source, File dest) {
        if (source == null || dest == null) return false;
        if (!source.exists()) return false;

        try {
            if (source.isDirectory()) FileUtils.copyDirectory(source, dest);
            else {
                if (dest.isDirectory()) FileUtils.copyFileToDirectory(source, dest);
                else FileUtils.copyFile(source, dest);
            }
            return true;
        } catch (Throwable e) {
            LOG.warn("复制文件错误", e);
        }

        return false;
    }

    /**
     * 将文件内容复制到目标流
     *
     * @param source       必须是文件
     * @param outputStream
     * @param autoRelease  是否自动释放流
     * @return
     */
    public static boolean copy(File source, OutputStream outputStream, boolean autoRelease) {
        if (source == null || outputStream == null || !source.exists() || source.isDirectory()) return false;

        try {
            FileUtils.copyFile(source, outputStream);
            return true;
        } catch (Throwable e) {
            LOG.warn("复制文件内容错误", e);
        } finally {
            if (autoRelease) close(outputStream);
        }

        return false;
    }

    /**
     * 从原始流生成文件
     *
     * @param inputStream
     * @param dest
     * @param autoRelease
     * @param forceNewFile
     * @return
     */
    public static boolean copy(InputStream inputStream, File dest, boolean autoRelease, boolean forceNewFile) {
        if (dest == null || inputStream == null || dest.isDirectory()) return false;


        BufferedOutputStream os = null;
        try {
            boolean nf = true;

            if (!dest.exists()) nf = dest.createNewFile();
            else if (forceNewFile) nf = del(dest, false) && dest.createNewFile();

            if (!nf) return false; // 文件状态无效

            os = outputStream(dest);
            copy(inputStream, os);
            os.flush();
            return true;
        } catch (Throwable e) {
            LOG.warn("复制文件内容错误", e);
        } finally {
            close(os);
            if (autoRelease) close(inputStream);
        }

        return false;
    }

    /**
     * 从原始流生成文件
     *
     * @param inputStream
     * @param dest
     * @param autoRelease
     * @return
     */
    public static boolean copy(InputStream inputStream, File dest, boolean autoRelease) {
        return copy(inputStream, dest, autoRelease, false);
    }

    /**
     * 根据路径直接获取获取文件对象 避免了异常等
     *
     * @param filename
     * @return 不存在或有异常则返回空
     */
    public static File file(String filename) {
        try {
            return new File(filename);
        } catch (Throwable ignored) {
        }
        return null;
    }

    public static File file(URL url) {
        try {
            return FileUtils.toFile(url);
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * 列出文件
     *
     * @param path
     * @param exts
     * @return
     */
    public static List<File> ls(File path, String... exts) {
        List<File> files = new ArrayList<>();

        try {
            Collection<File> cs = FileUtils.listFiles(path, exts, false);
            files.addAll(cs);
        } catch (Throwable ignored) {
        }
        return files;
    }

    /**
     * 列出文件 循环子目录
     *
     * @param path
     * @param exts
     * @return
     */
    public static List<File> lsr(File path, String... exts) {
        List<File> files = new ArrayList<>();

        try {
            Collection<File> cs = FileUtils.listFiles(path, exts, true);
            files.addAll(cs);
        } catch (Throwable ignored) {
        }
        return files;
    }

    /**
     * 按子目录构建目标文件
     *
     * @param parent
     * @param children
     * @return
     */
    public static File concat(File parent, String... children) {
        StringBuilder sb = new StringBuilder();
        if (children != null) for (String c : children) sb.append(c).append(File.separator);

        return new File(parent, sb.toString());
    }

    public static boolean touch(File file) {
        if (file == null) return false;
        try {

            if (!file.exists()) {
                OutputStream out = outputStream(file);
                close(out);
            }
            return file.setLastModified(System.currentTimeMillis());
        } catch (Throwable e) {
            LOG.warn("touch文件" + file + "错误");
        }
        return false;
    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    public static boolean mkdir(File path) {
        try {
            FileUtils.forceMkdir(path);
            return true;
        } catch (Throwable e) {
            LOG.warn("创建目录错误:" + path, e);
        }

        return false;
    }

    /**
     * 删除文件 参数指定是否允许删除多个文件
     *
     * @param file
     * @param all
     * @return
     */
    public static boolean del(File file, boolean all) {
        try {
            if (all) return FileUtils.deleteQuietly(file);
            else return file.delete();
        } catch (Throwable e) {
            LOG.warn("创建目录错误:" + file, e);
        }

        return false;
    }

    /**
     * 删除文件,如果是文件夹则一起删除内部所有子目录文件等
     *
     * @param file
     * @return
     */
    public static boolean del(File file) {
        return del(file, true);
    }

    /**
     * 打开文件读入流 注意返回的InputStream是Buffered
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static BufferedInputStream inputStream(File file) throws IOException {
        if (file == null) throw new FileNotFoundException("文件为空");

        if (file.exists()) {
            if (file.isDirectory()) throw new IOException("文件 '" + file + "' 是一个目录");
            if (!file.canRead()) throw new IOException("文件 '" + file + "' 不存在");
        } else throw new FileNotFoundException("文件 '" + file + "' 不存在");
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * 打开文件输出流 注意返回的OutputStream是Buffered
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static BufferedOutputStream outputStream(File file) throws IOException {
        if (file == null) throw new FileNotFoundException("文件为空");

        if (!file.exists()) throw new FileNotFoundException("文件 '" + file + "' 不存在");

        return new BufferedOutputStream(new FileOutputStream(file, true));
    }

    /**
     * 打开文件读入流 注意返回的InputStream是Buffered
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static BufferedInputStream inputStream(String filename) throws IOException {
        return inputStream(new File(filename));
    }


    /**
     * 打开文件输出流 注意返回的OutputStream是Buffered
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static BufferedOutputStream outputStream(String filename) throws IOException {
        return outputStream(new File(filename));
    }


    /**
     * 打开文件读入流 注意返回的InputStream是Buffered 此方法总是不抛出异常 当内部产生异常时返回的是null
     *
     * @param file
     * @return
     */
    public static BufferedInputStream inputStreamCatched(File file) {
        if (file == null) return null;

        try {
            if (file.exists()) {
                if (file.isDirectory()) return null;
                if (!file.canRead()) return null;
            } else return null;
            return new BufferedInputStream(new FileInputStream(file));
        } catch (Throwable e) {
            LOG.debug("打开文件流出现错误 目标文件 " + file, e);
        }
        return null;
    }

    /**
     * 打开文件输出流 注意返回的OutputStream是Buffered 此方法总是不抛出异常 当内部产生异常时返回的是null
     *
     * @param file
     * @return
     */
    public static BufferedOutputStream outputStreamCatched(File file) {
        if (file == null) return null;

        try {
            if (!file.exists()) return null;

            return new BufferedOutputStream(new FileOutputStream(file, true));
        } catch (Throwable e) {
            LOG.debug("打开文件流出现错误 目标文件 " + file, e);
        }
        return null;
    }

    /**
     * 打开文件读入流 注意返回的InputStream是Buffered 此方法总是不抛出异常 当内部产生异常时返回的是null
     *
     * @param filename
     * @return
     */
    public static BufferedInputStream inputStreamCatched(String filename) {
        return inputStreamCatched(new File(filename));
    }


    /**
     * 打开文件输出流 注意返回的OutputStream是Buffered 此方法总是不抛出异常 当内部产生异常时返回的是null
     *
     * @param filename
     * @return
     */
    public static BufferedOutputStream outputStreamCatched(String filename) {
        return outputStreamCatched(new File(filename));
    }


    /**
     * 读取字符串 每行一个String对象
     *
     * @param inputStream
     * @param encoding
     * @param autoRelease 是否自动关闭流
     * @return
     * @throws AppError
     */
    public static List<String> readLines(InputStream inputStream, String encoding, boolean autoRelease) throws AppError {
        if (inputStream == null) return null;
        if (Strs.isBlank(encoding)) encoding = Langs.UTF8;
        List<String> strs;
        try {
            strs = IOUtils.readLines(inputStream, encoding);
        } catch (Throwable e) {
            throw AppError.error("读取流数据错误", e);
        } finally {
            if (autoRelease) close(inputStream);
        }
        return strs;
    }

    /**
     * 读取字符串 每行一个String对象
     *
     * @param file
     * @param encoding
     * @return
     * @throws AppError
     */
    public static List<String> readLines(File file, String encoding) throws AppError {
        if (file == null) return null;
        InputStream is = null;
        try {
            is = inputStream(file);
        } catch (Throwable e) {
            throw AppError.error("读取文件错误:" + file, e);
        }

        return readLines(is, encoding, true);
    }

    /**
     * 读取字符串 每行一个String对象
     *
     * @param inputStream
     * @param encoding
     * @return
     * @throws AppError
     */
    public static List<String> readLines(InputStream inputStream, String encoding) throws AppError {
        return readLines(inputStream, encoding, false);
    }

    /**
     * 读取字符串 每行一个String对象
     *
     * @param inputStream
     * @return
     * @throws AppError
     */
    public static List<String> readLines(InputStream inputStream) throws AppError {
        return readLines(inputStream, Langs.UTF8);
    }

    /**
     * 读取字符串 每行一个String对象
     *
     * @param file
     * @return
     * @throws AppError
     */
    public static List<String> readLines(File file) throws AppError {
        return readLines(file, Langs.UTF8);
    }

    /**
     * 读取字符串 可设置是否关闭
     *
     * @param inputStream
     * @param encoding
     * @param autoRelease
     * @return
     * @throws AppError
     */
    public static String readString(InputStream inputStream, String encoding, boolean autoRelease) throws AppError {
        if (inputStream == null) return null;
        if (Strs.isBlank(encoding)) encoding = Langs.UTF8;
        try {
            return IOUtils.toString(inputStream, encoding);
        } catch (Throwable e) {
            throw AppError.error("读取流数据错误", e);
        } finally {
            if (autoRelease) close(inputStream);
        }
    }

    /**
     * 读取字符串
     *
     * @param file
     * @param encoding
     * @return
     * @throws AppError
     */
    public static String readString(File file, String encoding) throws AppError {
        if (file == null) return null;
        InputStream is;
        try {
            is = inputStream(file);
        } catch (Throwable e) {
            throw AppError.error("读取文件错误:" + file, e);
        }

        return readString(is, encoding, true);
    }


    /**
     * 读取字符串
     *
     * @param inputStream
     * @param encoding
     * @return
     * @throws AppError
     */
    public static String readString(InputStream inputStream, String encoding) throws AppError {
        return readString(inputStream, encoding, false);
    }

    /**
     * 读取字符串
     *
     * @param inputStream
     * @return
     * @throws AppError
     */
    public static String readString(InputStream inputStream) throws AppError {
        return readString(inputStream, Langs.UTF8);
    }

    /**
     * 读取字符串
     *
     * @param file
     * @return
     * @throws AppError
     */
    public static String readString(File file) throws AppError {
        return readString(file, Langs.UTF8);
    }

    /**
     * 读取二进制流
     *
     * @param is
     * @param autoRelease
     * @return
     * @throws AppError
     */
    public static byte[] read(InputStream is, boolean autoRelease) throws AppError {
        if (is == null) return null;
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(is);
        } catch (Throwable e) {
            throw AppError.error("读取错误", e);
        } finally {
            if (autoRelease) close(is);
        }
        return bytes;
    }

    /**
     * 读取二进制流 不自动释放流 需手工释放
     *
     * @param is
     * @return
     * @throws AppError
     */
    public static byte[] read(InputStream is) throws AppError {
        return read(is, false);
    }


    /**
     * 读取二进制文件
     *
     * @param file
     * @return
     * @throws AppError
     */
    public static byte[] read(File file) throws AppError {
        if (file == null) return null;
        InputStream is;
        try {
            is = inputStream(file);
        } catch (Throwable e) {
            throw AppError.error("读取文件错误:" + file, e);
        }

        return read(is, true);
    }

    /**
     * 写入字符串 每行一个String对象
     *
     * @param lines
     * @param lineEnding   结束符
     * @param outputStream
     * @param encoding
     * @param autoRelease
     * @throws AppError
     */
    public static void write(List<String> lines, String lineEnding, OutputStream outputStream, String encoding, boolean autoRelease) throws AppError {
        if (lines == null || outputStream == null) return;
        if (Strs.isBlank(encoding)) encoding = Langs.UTF8;
        try {
            IOUtils.writeLines(lines, lineEnding, outputStream, encoding);
        } catch (Throwable e) {
            throw AppError.error("读取流数据错误", e);
        } finally {
            if (autoRelease) close(outputStream);
        }
    }

    /**
     * 写入字符串 每行一个String对象 默认编码UTF-8 默认行结束符 \n
     *
     * @param lines
     * @param outputStream
     * @param autoRelease
     * @throws AppError
     */
    public static void write(List<String> lines, OutputStream outputStream, boolean autoRelease) throws AppError {
        write(lines, Langs.LINE_ENDING, outputStream, Langs.UTF8, autoRelease);
    }


    /**
     * 写入字符串 每行一个String对象 默认编码UTF-8 默认行结束符
     * <p>
     * 注意 流写入后需要手工关闭 本方法不处理流的自动释放
     *
     * @param lines
     * @param outputStream
     * @throws AppError
     */
    public static void write(List<String> lines, OutputStream outputStream) throws AppError {
        write(lines, Langs.LINE_ENDING, outputStream, Langs.UTF8, false);
    }


    /**
     * 写入字符串 每行一个String对象
     *
     * @param lines
     * @param lineEnding   结束符
     * @param file
     * @param encoding
     * @param forceNewFile
     * @throws AppError
     */
    public static void write(List<String> lines, String lineEnding, File file, String encoding, boolean forceNewFile) throws AppError {
        if (lines == null || file == null || file.isDirectory()) return;


        BufferedOutputStream os = null;
        try {
            boolean nf = true;

            if (!file.exists()) nf = file.createNewFile();
            else if (forceNewFile) nf = del(file, false) && file.createNewFile();

            if (!nf) return; // 文件状态无效

            os = outputStream(file);
            IOUtils.writeLines(lines, lineEnding, os, encoding);
            os.flush();
        } catch (Throwable e) {
            LOG.warn("复制文件内容错误", e);
        } finally {
            close(os);
        }
    }

    /**
     * 写入字符串 每行一个String对象 默认编码UTF-8 默认行结束符 \n
     * <p></p>
     * 注意 文件存在则会自动增加内容 而不是会覆盖文件
     *
     * @param lines
     * @param file
     * @throws AppError
     */
    public static void write(List<String> lines, File file) throws AppError {
        write(lines, Langs.LINE_ENDING, file, Langs.UTF8, false);
    }


    /**
     * 写入字符串
     *
     * @param str
     * @param outputStream
     * @param encoding
     * @param autoRelease  是否自动释放流
     * @throws AppError
     */
    public static void write(String str, OutputStream outputStream, String encoding, boolean autoRelease) throws AppError {
        if (str == null || outputStream == null) return;
        if (Strs.isBlank(encoding)) encoding = Langs.UTF8;
        try {
            IOUtils.write(str, outputStream, encoding);
        } catch (Throwable e) {
            throw AppError.error("读取流数据错误", e);
        } finally {
            if (autoRelease) close(outputStream);
        }
    }

    /**
     * 写入字符串 UTF-8编码 不自动释放流
     *
     * @param str
     * @param outputStream
     * @param autoRelease  是否自动释放流
     * @throws AppError
     */
    public static void write(String str, OutputStream outputStream, boolean autoRelease) throws AppError {
        write(str, outputStream, Langs.UTF8, autoRelease);
    }


    /**
     * 写入字符串 UTF-8编码 不自动释放流
     *
     * @param str
     * @param outputStream
     * @throws AppError
     */
    public static void write(String str, OutputStream outputStream) throws AppError {
        write(str, outputStream, Langs.UTF8, false);
    }


    /**
     * 写入字符串
     *
     * @param str
     * @param file
     * @param encoding
     * @param forceNewFile
     * @throws AppError
     */
    public static void write(String str, File file, String encoding, boolean forceNewFile) throws AppError {
        if (str == null || file == null) return;
        if (Strs.isBlank(encoding)) encoding = Langs.UTF8;

        BufferedOutputStream os = null;
        try {
            boolean nf = true;

            if (!file.exists()) nf = file.createNewFile();
            else if (forceNewFile) nf = del(file, false) && file.createNewFile();

            if (!nf) return; // 文件状态无效

            os = outputStream(file);
            IOUtils.write(str, os, encoding);
            os.flush();
        } catch (Throwable e) {
            LOG.warn("复制文件内容错误", e);
        } finally {
            close(os);
        }
    }

    /**
     * 写入字符串 UTF-8编码 不自动释放流
     *
     * @param str
     * @param file
     * @param forceNewFile
     * @throws AppError
     */
    public static void write(String str, File file, boolean forceNewFile) throws AppError {
        write(str, file, Langs.UTF8, false);
    }


    /**
     * 写入字符串 UTF-8编码 不自动释放流
     *
     * @param str
     * @param file
     * @throws AppError
     */
    public static void write(String str, File file) throws AppError {
        write(str, file, Langs.UTF8, false);
    }


    /**
     * 写入数据
     *
     * @param data
     * @param outputStream
     * @param autoRelease  是否自动释放流
     * @throws AppError
     */
    public static void write(byte[] data, OutputStream outputStream, boolean autoRelease) throws AppError {
        if (data == null || outputStream == null) return;
        try {
            IOUtils.write(data, outputStream);
        } catch (Throwable e) {
            throw AppError.error("读取流数据错误", e);
        } finally {
            if (autoRelease) close(outputStream);
        }
    }

    /**
     * 写入数据 不自动释放流
     *
     * @param data
     * @param outputStream
     * @throws AppError
     */
    public static void write(byte[] data, OutputStream outputStream) throws AppError {
        write(data, outputStream, false);
    }


    /**
     * 写入数据 可选是否强制创建新文件(同名文件会被删除)
     *
     * @param data
     * @param file
     * @param forceNewFile
     * @throws AppError
     */
    public static void write(byte[] data, File file, boolean forceNewFile) throws AppError {
        if (data == null || file == null) return;

        BufferedOutputStream os = null;
        try {
            boolean nf = true;

            if (!file.exists()) nf = file.createNewFile();
            else if (forceNewFile) nf = del(file, false) && file.createNewFile();

            if (!nf) return; // 文件状态无效

            os = outputStream(file);
            IOUtils.write(data, os);
            os.flush();
        } catch (Throwable e) {
            LOG.warn("复制文件内容错误", e);
        } finally {
            close(os);
        }
    }

    /**
     * 写入数据  创建不存在文件或追加内容
     *
     * @param data
     * @param file
     * @throws AppError
     */
    public static void write(byte[] data, File file) throws AppError {
        write(data, file, false);
    }

    /**
     * 针对每行执行对应命令
     *
     * @param inputStream
     * @param cmd
     * @param encoding
     * @param autoRelease
     */
    public static void iterateLines(InputStream inputStream, Consumer<DataEntry<Long, String>> cmd, String encoding, boolean autoRelease) {
        if (inputStream == null) return;
        if (Strs.isBlank(encoding)) encoding = Langs.UTF8;
        LineIterator it = null;
        try {
            it = IOUtils.lineIterator(inputStream, encoding);
            long i = 0;
            while (it.hasNext()) {
                if (cmd != null) cmd.accept(new DataEntry<>(i, it.nextLine()));
                i++;
            }
        } catch (Throwable e) {
            throw AppError.error("读取流数据错误", e);
        } finally {
            close(it);
            if (autoRelease) close(inputStream);
        }
    }

    /**
     * 针对每行执行对应命令 UTF-8编码
     *
     * @param inputStream
     * @param cmd
     * @param autoRelease
     */
    public static void iterateLines(InputStream inputStream, Consumer<DataEntry<Long, String>> cmd, boolean autoRelease) {
        iterateLines(inputStream, cmd, Langs.UTF8, autoRelease);
    }

    /**
     * 针对每行执行对应命令 UTF-8编码 不自动释放连接
     *
     * @param inputStream
     * @param cmd
     */
    public static void iterateLines(InputStream inputStream, Consumer<DataEntry<Long, String>> cmd) {
        iterateLines(inputStream, cmd, Langs.UTF8, false);
    }

    /**
     * 针对每行执行对应命令
     *
     * @param file
     * @param cmd
     * @param encoding
     */
    public static void iterateLines(File file, Consumer<DataEntry<Long, String>> cmd, String encoding) {
        if (file == null) return;
        if (Strs.isBlank(encoding)) encoding = Langs.UTF8;
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(file, encoding);
            long i = 0;
            while (it.hasNext()) {
                if (cmd != null) cmd.accept(new DataEntry<>(i, it.nextLine()));
                i++;
            }
        } catch (Throwable e) {
            throw AppError.error("读取流数据错误", e);
        } finally {
            close(it);
        }
    }

    /**
     * 针对每行执行对应命令
     *
     * @param file
     * @param cmd
     */
    public static void iterateLines(File file, Consumer<DataEntry<Long, String>> cmd) {
        iterateLines(file, cmd, Langs.UTF8);
    }

    /**
     * 类似unix系统的iconv方法
     *
     * @param inputStream
     * @param outputStream
     * @param fromEncoding
     * @param toEncoding
     * @param closeStreams
     */
    public static void iconv(InputStream inputStream, OutputStream outputStream, String fromEncoding, String toEncoding, boolean closeStreams) throws AppError {
        try {
            Reader reader = new InputStreamReader(inputStream, fromEncoding);
            Writer writer = new OutputStreamWriter(outputStream, toEncoding);

            IOUtils.copy(inputStream, outputStream);

            if (closeStreams) {
                close(writer);
                close(reader);
            }
        } catch (Exception e) {
            throw AppError.error("ICONV内容错误", e);
        }
    }
}
