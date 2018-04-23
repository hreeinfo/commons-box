package commons.box.app.misc;

import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.util.Langs;
import commons.box.util.Logs;
import commons.box.util.Strs;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.UnicodeEscaper;
import org.apache.commons.text.translate.UnicodeUnescaper;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * 编码工具类
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/27 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Encode {
    private static final AppLog LOG = Logs.get(Encode.class);

    public static final Base64Coder BASE64 = new Base64Coder(Base64.getEncoder(), Base64.getDecoder());
    public static final Base64Coder BASE64_MIME = new Base64Coder(Base64.getMimeEncoder(), Base64.getMimeDecoder());
    public static final Base64Coder BASE64_URL = new Base64Coder(Base64.getUrlEncoder(), Base64.getUrlDecoder());

    public static final Base64Coder BASE64_NOPAD = new Base64Coder(Base64.getEncoder().withoutPadding(), Base64.getDecoder());
    public static final Base64Coder BASE64_MIME_NOPAD = new Base64Coder(Base64.getMimeEncoder().withoutPadding(), Base64.getMimeDecoder());
    public static final Base64Coder BASE64_URL_NOPAD = new Base64Coder(Base64.getUrlEncoder().withoutPadding(), Base64.getUrlDecoder());

    public static final Coder XML10 = new XML10Coder();
    public static final Coder XML11 = new XML11Coder();
    public static final Coder HTML = new HTMLCoder();
    public static final Coder JSON = new JSONCoder();
    public static final Coder JS = new JSCoder();
    public static final Coder JAVA = new JAVACoder();
    public static final Coder CSV = new CSVCoder();
    public static final Coder UNICODE = new UnicodeCoder();

    public static final HexCoder HEX = new HexCoder();
    public static final BinCoder BIN01 = new BinCoder();


    public static interface Coder {
        /**
         * 编码
         *
         * @param src
         * @return
         */
        public String encode(String src);

        /**
         * 解码方法 其中字节码统一以UTF-8方式处理 其它语言请适用byte[]相关的方法
         *
         * @param src
         * @return
         */
        public String decode(String src);
    }


    public static interface ByteCoder {
        /**
         * 编码
         *
         * @param bytes
         * @return
         */
        public byte[] encode(byte[] bytes);

        /**
         * 解码方法 其中字节码统一以UTF-8方式处理 其它语言请适用byte[]相关的方法
         *
         * @param bytes
         * @return
         */
        public byte[] decode(byte[] bytes);
    }


    public static enum Base64Type {
        /**
         * 通用
         */
        NORMARL,

        /**
         * 用户MIME
         */
        MIME,

        /**
         * 用于URL
         */
        URL;
    }

    public static class Base64Coder implements Coder, ByteCoder {
        private final Base64.Encoder encoder;
        private final Base64.Decoder decoder;

        private Base64Coder(Base64.Encoder encoder, Base64.Decoder decoder) {
            this.encoder = (encoder != null) ? encoder : Base64.getEncoder();
            this.decoder = (decoder != null) ? decoder : Base64.getDecoder();
        }

        /**
         * 编码
         *
         * @param src
         * @return
         */
        public byte[] decode(byte[] src) {
            if (src == null || this.decoder == null) return null;
            return this.decoder.decode(src);
        }

        /**
         * 编码 其中字节码统一以UTF-8方式处理 其它语言请适用byte[]相关的方法
         *
         * @param src
         * @return
         */
        public String decode(String src) {
            if (src == null || this.decoder == null) return null;
            return Strs.get(this.decoder.decode(Strs.bytes(src, Langs.CHARSET_UTF8)));
        }

        public ByteBuffer decode(ByteBuffer buffer) {
            if (buffer == null) return null;
            try {
                return this.decoder.decode(buffer);
            } catch (Throwable e) {
                LOG.warn("无法处理 BASE64 decoder - ", e);
            }
            return null;
        }

        /**
         * 以OutputStream包装, 提供流方式的编码过程
         *
         * @param is
         * @return
         */
        public InputStream decode(InputStream is) {
            if (is == null || this.decoder == null) return null;
            return this.decoder.wrap(is);
        }

        /**
         * 解码方法
         *
         * @param src
         * @return
         */
        public byte[] encode(byte[] src) {
            if (src == null || this.encoder == null) return null;
            return this.encoder.encode(src);
        }

        /**
         * 解码方法 其中字节码统一以UTF-8方式处理 其它语言请适用byte[]相关的方法
         *
         * @param src
         * @return
         */
        public String encode(String src) {
            if (src == null || this.encoder == null) return null;
            return Strs.get(this.encoder.encode(Strs.bytes(src, Langs.CHARSET_UTF8)));
        }

        public ByteBuffer encode(ByteBuffer buffer) {
            if (buffer == null) return null;
            try {
                return this.encoder.encode(buffer);
            } catch (Throwable e) {
                LOG.warn("无法处理 BASE64 encoder - ", e);
            }
            return null;
        }

        /**
         * 以Inputstream包装,提供流方式的解码过程
         *
         * @param os
         * @return
         */
        public OutputStream encode(OutputStream os) {
            if (os == null || this.encoder == null) return null;
            return this.encoder.wrap(os);
        }
    }

    private static class XML10Coder implements Coder {
        @Override
        public String encode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.escapeXml10(src);
        }

        @Override
        public String decode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.unescapeXml(src);
        }
    }

    private static class XML11Coder extends XML10Coder {
        @Override
        public String encode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.escapeXml11(src);
        }
    }

    private static class HTMLCoder implements Coder {
        @Override
        public String encode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.escapeHtml4(src);
        }

        @Override
        public String decode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.unescapeHtml4(src);
        }
    }

    private static class JSONCoder implements Coder {
        @Override
        public String encode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.escapeJson(src);
        }

        @Override
        public String decode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.unescapeJson(src);
        }
    }

    private static class CSVCoder implements Coder {
        @Override
        public String encode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.escapeCsv(src);
        }

        @Override
        public String decode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.unescapeCsv(src);
        }
    }

    private static class JAVACoder implements Coder {
        @Override
        public String encode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.escapeJava(src);
        }

        @Override
        public String decode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.unescapeJava(src);
        }
    }

    private static class JSCoder implements Coder {
        @Override
        public String encode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.escapeEcmaScript(src);
        }

        @Override
        public String decode(String src) {
            if (src == null) return null;
            return StringEscapeUtils.unescapeEcmaScript(src);
        }
    }

    private static class UnicodeCoder implements Coder {
        private final UnicodeEscaper escaper = new UnicodeEscaper();
        private final UnicodeUnescaper unescaper = new UnicodeUnescaper();

        @Override
        public String encode(String src) {
            if (src == null) return null;
            else if (Strs.isEmpty(src)) return Strs.EMPTY;
            return escaper.translate(src);
        }

        @Override
        public String decode(String src) {
            if (src == null) return null;
            else if (Strs.isEmpty(src)) return Strs.EMPTY;
            return unescaper.translate(src);
        }
    }

    public final static class URLCoder implements Coder {
        @Override
        public String encode(String src) {
            return this.encode(src, Langs.CHARSET_UTF8);
        }

        /**
         * 按指定字符集编码URL
         *
         * @param src
         * @param charset
         * @return
         */
        public String encode(String src, Charset charset) {
            if (charset == null) charset = Langs.CHARSET_UTF8;
            try {
                return URLEncoder.encode(src, charset.name());
            } catch (Throwable e) {
                throw AppError.error("无法序列化 URL:" + src);
            }
        }

        @Override
        public String decode(String src) {
            return this.decode(src, Langs.CHARSET_UTF8);
        }

        /**
         * 按指定字符集解码URL
         *
         * @param src
         * @param charset
         * @return
         */
        public String decode(String src, Charset charset) {
            if (charset == null) charset = Langs.CHARSET_UTF8;
            try {
                return URLDecoder.decode(src, charset.name());
            } catch (Throwable e) {
                throw AppError.error("无法反序列化 URL:" + src);
            }
        }

    }

    public final static class BinCoder {
        private BinCoder() {
        }

        public String encode(byte[] raw) {
            if (raw == null) return null;
            return BinaryCodec.toAsciiString(raw);
        }

        public byte[] decode(String bin01s) {
            if (bin01s == null) return null;
            return BinaryCodec.fromAscii(bin01s.toCharArray());
        }
    }

    public final static class HexCoder {
        private HexCoder() {
        }

        public String encode(byte[] bytes) {
            return encode(bytes, true);
        }

        public String encode(byte[] bytes, boolean toLowerCase) {
            if (bytes == null) return null;
            char[] cs = Hex.encodeHex(bytes, toLowerCase);
            if (cs == null) return null;
            return String.valueOf(cs);
        }

        public byte[] decode(String src) {
            if (src == null) return null;
            char[] cs = src.toCharArray();
            try {
                return Hex.decodeHex(cs);
            } catch (DecoderException e) {
                LOG.warn("无法反向解码 HEX:" + src, e);
            }
            return null;
        }
    }
}
