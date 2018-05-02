package commons.box.app.misc;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.util.Langs;
import commons.box.util.Logs;

import java.io.Closeable;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Supplier;

/**
 * Hash机制 支持md5/sha/crc等
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Hash {
    private static final AppLog LOG = Logs.get(Hash.class);
    private static final int BUF_SIZE = 2048; // 2K缓冲区

    public static final Hash CRC32 = new Hash(Hashing.crc32());
    public static final Hash CRC32C = new Hash(Hashing.crc32c());
    @SuppressWarnings("deprecation")
    public static final Hash MD5 = new Hash(Hashing.md5());
    @SuppressWarnings("deprecation")
    public static final Hash SHA1 = new Hash(Hashing.sha1());
    public static final Hash SHA256 = new Hash(Hashing.sha256());
    public static final Hash SHA384 = new Hash(Hashing.sha384());
    public static final Hash SHA512 = new Hash(Hashing.sha512());
    public static final Hash MURMUR3_32 = new Hash(Hashing.murmur3_32());
    public static final Hash MURMUR3_128 = new Hash(Hashing.murmur3_128());
    public static final Hash SIP_HASH24 = new Hash(Hashing.sipHash24());
    public static final Hash ADLER32 = new Hash(Hashing.adler32());

    private final HashFunction func;

    private Hash(HashFunction func) {
        this.func = func;
    }

    /**
     * 获取 guava hasher对象 提供更多额外操作
     *
     * @return
     * @throws AppError
     */
    public final Coder coder() {
        return new Coder((this.func != null) ? this.func.newHasher() : null);
    }

    public static final class Coder {
        private final Hasher hasher;

        private Coder(Hasher hasher) {
            this.hasher = hasher;
            if (this.hasher == null) LOG.warn("Hash实例为空 请检查对应HashFunction是否正确");
        }

        public Hasher hasher() {
            return this.hasher;
        }

        /**
         * HEX值 默认为小写
         *
         * @return
         */
        public String hex() {
            return this.hex(false);
        }


        /**
         * HEX值 可指定大小写 此值返回了计算后的hash值 调用后若要再次计算附加部分 请重新生成实例再次进行计算
         *
         * @param upperCase
         * @return
         */
        public String hex(boolean upperCase) {
            if (this.hasher == null) return null;
            String hex = this.hasher.hash().toString();
            if (upperCase) return hex.toUpperCase();
            else return hex;
        }

        /**
         * 获取值 此值返回了计算后的hash值 调用后若要再次计算附加部分 请重新生成实例再次进行计算
         *
         * @return
         */
        public byte[] get() {
            if (this.hasher == null) return null;
            return this.hasher.hash().asBytes();
        }

        /**
         * 仅用于类似于CRC32的机制 返回long
         *
         * @return
         * @throws AppError
         */
        public long getLong() throws AppError {
            try {
                return this.hasher.hash().asLong();
            } catch (Throwable e) {
                throw AppError.error("返回long值", e);
            }
        }

        /**
         * 操作数据
         *
         * @param bytes
         * @return
         */
        public Coder put(byte[] bytes) {
            if (this.hasher == null) return this;
            return this;
        }

        /**
         * 操作字符串 默认UTF-8编码
         *
         * @param str
         * @return
         */
        public Coder put(String str) {
            return put(str, Langs.CHARSET_UTF8);
        }

        /**
         * 操作字符串 可设置字符集
         *
         * @param str
         * @param charset
         * @return
         */
        public Coder put(String str, Charset charset) {
            if (this.hasher == null) return this;
            if (charset == null) charset = Langs.CHARSET_UTF8;
            this.hasher.putString(str, charset);
            return this;
        }

        /**
         * 读取流内容 注意此处未对流进行回收关闭 需要在外部做try-catch-finally保护以回收流空间
         *
         * @param is
         * @return
         * @throws AppError
         */
        public Coder put(InputStream is) throws AppError {
            return this.put(is, BUF_SIZE);
        }

        /**
         * 读取流内容 注意此处使用了function机制 使用 supplier 打开流对象 操作结束或异常时 自动关闭流资源
         *
         * @param sis
         * @return
         * @throws AppError
         */
        public Coder put(Supplier<InputStream> sis) throws AppError {
            return this.put(sis, BUF_SIZE);
        }

        /**
         * 读取流内容 注意此处未对流进行回收关闭 需要在外部做try-catch-finally保护以回收流空间
         *
         * @param is
         * @param bufSize
         * @return
         * @throws AppError
         */
        public Coder put(InputStream is, int bufSize) throws AppError {
            if (this.hasher == null || is == null) return this;
            try {
                int bufferLength = (bufSize > 0) ? bufSize : BUF_SIZE;
                byte[] buffer = new byte[bufferLength];
                int read = is.read(buffer, 0, bufferLength);

                while (read > 0) {
                    this.hasher.putBytes(buffer, 0, read);
                    read = is.read(buffer, 0, bufferLength);
                }
            } catch (Throwable e) {
                throw AppError.error("无法打开输入流" + e.getMessage(), e);
            }
            return this;
        }

        /**
         * 读取流内容 注意此处使用了function机制 使用 supplier 打开流对象 操作结束或异常时 自动关闭流资源
         *
         * @param sis
         * @param bufSize
         * @return
         * @throws AppError
         */
        public Coder put(Supplier<InputStream> sis, int bufSize) throws AppError {
            if (this.hasher == null || sis == null) return this;
            InputStream is = null;
            try {
                is = sis.get();
                return put(is, bufSize);
            } catch (Throwable e) {
                throw AppError.error("无法打开输入流" + e.getMessage(), e);
            } finally {
                close(is);
            }
        }
    }

    public static void close(Closeable target) {
        try {
            if (target != null) target.close();
        } catch (Throwable ioe) {
            // 忽略
        }
    }
}
