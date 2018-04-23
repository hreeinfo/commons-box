package commons.box.util;


import commons.box.app.misc.Crypto;
import commons.box.app.misc.Crypto.CipherAlgorithm;
import commons.box.app.misc.Encode;
import commons.box.app.misc.Encode.Base64Coder;
import commons.box.app.misc.Encode.Base64Type;
import commons.box.app.misc.Encode.BinCoder;
import commons.box.app.misc.Encode.HexCoder;
import commons.box.app.misc.Hash;
import commons.box.app.misc.Hash.Coder;

/**
 * 编码相关工具方法,包含md5/sha、加密等工具方法
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/27 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Codes {
    public static final int DEFAULT_IVSIZE = 16;

    private Codes() {
    }


    /**
     * 获取lang值
     *
     * @param bytes
     * @return
     */
    public static long asLong(byte[] bytes) {
        if (bytes == null || bytes.length < 1) return 0;

        long retVal = (bytes[0] & 0xFF);
        for (int i = 1; i < Math.min(bytes.length, 8); i++) {
            retVal |= (bytes[i] & 0xFFL) << (i * 8);
        }
        return retVal;
    }


    /**
     * CRC32最终应该返回long,请使用getLong获取对应结果
     *
     * @return
     */
    public static Coder crc32() {
        return Hash.CRC32.coder();
    }

    /**
     * 返回值与CRC32类似,需要做long处理
     *
     * @return
     */
    public static Coder crc32c() {
        return Hash.CRC32C.coder();

    }

    public static Coder md5() {
        return Hash.MD5.coder();
    }

    public static Coder sha1() {
        return Hash.SHA1.coder();
    }

    public static Coder sha256() {
        return Hash.SHA256.coder();
    }

    public static Coder sha384() {
        return Hash.SHA384.coder();
    }

    public static Coder sha512() {
        return Hash.SHA512.coder();
    }

    public static Coder murmur3_32() {
        return Hash.MURMUR3_32.coder();
    }

    public static Coder murmur3_128() {
        return Hash.MURMUR3_128.coder();
    }

    public static Coder sipHash24() {
        return Hash.SIP_HASH24.coder();
    }


    public static Coder adler32() {
        return Hash.ADLER32.coder();
    }


    /**
     * 返回通用的BASE64编码器
     *
     * @return
     */
    public static Base64Coder base64() {
        return Encode.BASE64;
    }

    /**
     * 按需求获取适用的BASE64编码器
     *
     * @param type
     * @param withoutPadding 不包含结尾的padding位 一般情况下取值是false
     * @return
     */
    public static Base64Coder base64(Base64Type type, boolean withoutPadding) {
        if (type == null) type = Base64Type.NORMARL;
        switch (type) {
            case NORMARL:
                return withoutPadding ? Encode.BASE64_NOPAD : Encode.BASE64;
            case MIME:
                return withoutPadding ? Encode.BASE64_MIME_NOPAD : Encode.BASE64_MIME;
            case URL:
                return withoutPadding ? Encode.BASE64_URL_NOPAD : Encode.BASE64_URL;
        }
        return Encode.BASE64;
    }

    //----------------- 其它编码

    /**
     * 关键字处理 通用XML 1.0版 (新版XML 1.1 要使用xml11方法)
     *
     * @return
     */
    public static Encode.Coder xml10() {
        return Encode.XML10;
    }

    /**
     * 关键字处理 通用XML 1.1版 (XML 1.0 要使用xml10方法)
     *
     * @return
     */
    public static Encode.Coder xml11() {
        return Encode.XML11;
    }

    /**
     * 关键字处理
     *
     * @return
     */
    public static Encode.Coder html() {
        return Encode.HTML;
    }

    /**
     * 关键字处理 转换转义字符串
     *
     * @return
     */
    public static Encode.Coder json() {
        return Encode.JSON;
    }

    /**
     * 关键字处理 转换转义字符串
     *
     * @return
     */
    public static Encode.Coder js() {
        return Encode.JS;
    }

    /**
     * 关键字处理 转换转义字符串
     *
     * @return
     */
    public static Encode.Coder java() {
        return Encode.JAVA;
    }

    /**
     * 关键字处理 过滤CSV关键字
     *
     * @return
     */
    public static Encode.Coder csv() {
        return Encode.CSV;
    }

    /**
     * 处理 Unicode 编码
     *
     * @return
     */
    public static Encode.Coder unicode() {
        return Encode.UNICODE;
    }

    /**
     * 获取hex编码器
     *
     * @return
     */
    public static HexCoder hex() {
        return Encode.HEX;
    }

    /**
     * 转成基本01字符的二进制转换器
     * <p>
     * 比如 字符串 "a" 对应16进制为 61 二进制编码为 01100001
     *
     * @return
     */
    public static BinCoder bin01() {
        return Encode.BIN01;
    }

    /**
     * AES加密 默认128位加密
     *
     * @return
     */
    public static CipherAlgorithm aes() {
        return Crypto.AES128;
    }

    /**
     * AES加密 默认192位加密
     *
     * @return
     */
    public static CipherAlgorithm aes192() {
        return Crypto.AES192;
    }

    /**
     * AES加密 默认256位加密
     *
     * @return
     */
    public static CipherAlgorithm aes256() {
        return Crypto.AES256;
    }

    /*
        public static void main(String[] args) {
        final byte[] key2 = aes().genkey("hello".getBytes());
        System.out.println(hex().encode(key2));


        byte[] bs = aes().get(key2).encrypt(Strs.bytes("testte234234234234sttetdfasdfasdfas测试测试"));
        System.out.println(hex().encode(bs));


        byte[] bso = aes().get(key2).decrypt(bs);
        System.out.println(Strs.get(bso));

        TimeMetric.inst(e -> {
            for (int i = 0; i < 100000; i++) {
                byte[] abs = aes().get(key2).encrypt(Strs.bytes("testte234234234234sttetdfasdfasdfas测试测试"));
                byte[] abso = aes().get(key2).decrypt(abs);
            }
        }).print("用时");
    }
     */
}
