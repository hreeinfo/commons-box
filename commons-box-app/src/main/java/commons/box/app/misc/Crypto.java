package commons.box.app.misc;


import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.util.Logs;
import commons.box.util.Randoms;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * 加密器
 * <p>
 * 使用了两种实现方式:
 * <p>
 * <ul>
 * <li>1. 对称加密: CipherAlgorithm 及其操作类 CipherCryptor, 支持的算法包括: AES DES等</li>
 * <li>2. 非对称加密: PKeyAlgorithm 及其操作类 PKeyCryptor, 支持的机制包括: RSA 等</li>
 * </ul>
 * <p>
 * <p>
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Crypto {
    private static final AppLog LOG = Logs.get(Crypto.class);
    private static final int DEFAULT_IVSIZE = 16;

    public static final CipherAlgorithm AES128 = new CipherAlgorithm("AES", "AES/CBC/PKCS5Padding", "AES", 128, DEFAULT_IVSIZE);
    public static final CipherAlgorithm AES192 = new CipherAlgorithm("AES", "AES/CBC/PKCS5Padding", "AES", 192, DEFAULT_IVSIZE);
    public static final CipherAlgorithm AES256 = new CipherAlgorithm("AES", "AES/CBC/PKCS5Padding", "AES", 256, DEFAULT_IVSIZE);


    public static final CipherAlgorithm DES = new CipherAlgorithm("AES", "AES/CBC/PKCS5Padding", "AES", 128, DEFAULT_IVSIZE);

    private static SecretKey createSecretKey(String algorithm, byte[] key) {
        if (key == null) return null;
        try {
            return new SecretKeySpec(key, algorithm);
        } catch (Throwable e) {
            LOG.warn("无法创建目标key变量 key=" + Encode.HEX.encode(key) + " 错误原因:" + e.getMessage(), e);
        }
        return null;
    }

    private static IvParameterSpec createIV(byte[] iv) {
        if (iv == null || iv.length < 1) return null;
        try {
            return new IvParameterSpec(iv);
        } catch (Throwable e) { // 不会发生异常
            LOG.warn("无法创建目标IV变量 salt=" + Encode.HEX.encode(iv) + " 错误原因:" + e.getMessage(), e);
        }
        return null;
    }

    public static class CipherAlgorithm {
        private final String algorithm;
        private final String algorithmWithIV;
        private final String keytype;
        private final int keysize;
        private final int ivsize;

        public CipherAlgorithm(String algorithm, String algorithmWithIV, String keytype, int keysize, int ivsize) {
            this.algorithm = algorithm;
            this.algorithmWithIV = algorithmWithIV;
            this.keytype = keytype;
            this.keysize = keysize;
            this.ivsize = ivsize;
        }

        public String algorithm() {
            return this.algorithm;
        }

        public String algorithmWithIV() {
            return this.algorithmWithIV;
        }

        public String keytype() {
            return this.keytype;
        }

        public int keysize() {
            return this.keysize;
        }

        public int ivsize() {
            return this.ivsize;
        }

        public byte[] genkey() {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(this.keytype());
                keyGenerator.init(this.keysize());
                SecretKey secretKey = keyGenerator.generateKey();
                return secretKey.getEncoded();
            } catch (GeneralSecurityException e) {
                throw AppError.error(e);
            }
        }

        /**
         * 使用SHA512掩码的key
         *
         * TODO 当前默认使用了一个简单的SHA机制,需要使用 PBEKeySpec 来为密码生成加密KEY
         *
         * @param source
         * @return
         */
        public byte[] genkey(byte[] source) {
            if (source == null) return null;
            byte[] hk = Hash.SHA512.coder().put(source).get();
            if (hk == null) return null;
            return Arrays.copyOf(hk, this.keysize() / 8);
        }

        public byte[] geniv() {
            return Randoms.bytes(this.ivsize());
        }

        public CipherCryptor get(byte[] key) {
            return get(key, null);
        }

        public CipherCryptor get(byte[] key, byte[] iv) {
            return new CipherCryptor((iv == null || iv.length < 1) ? this.algorithm() : this.algorithmWithIV(), this.keytype(), key, iv);
        }
    }

    public static class CipherCryptor {
        private final String algorithm;
        private final String keytype;
        private final SecretKey key;
        private final IvParameterSpec iv;

        public CipherCryptor(String algorithm, String keytype, byte[] key, byte[] iv) {
            this.algorithm = algorithm;
            this.keytype = keytype;
            this.key = createSecretKey(this.keytype, key);
            this.iv = createIV(iv);
        }

        public String algorithm() {
            return this.algorithm;
        }

        public String keytype() {
            return this.keytype;
        }

        public SecretKey key() {
            return this.key;
        }

        public IvParameterSpec iv() {
            return this.iv;
        }

        public byte[] encrypt(byte[] data) throws AppError {
            try {
                Cipher cipher = Cipher.getInstance(this.algorithm());

                if (this.iv() != null) cipher.init(Cipher.ENCRYPT_MODE, this.key(), this.iv());
                else cipher.init(Cipher.ENCRYPT_MODE, this.key());

                return cipher.doFinal(data);
            } catch (Throwable e) {
                throw AppError.error("使用算法 " + algorithm + " 未能加密目标内容", e);
            }
        }

        public byte[] decrypt(byte[] cipherData) throws AppError {
            try {
                Cipher cipher = Cipher.getInstance(this.algorithm());

                if (this.iv() != null) cipher.init(Cipher.DECRYPT_MODE, this.key(), this.iv());
                else cipher.init(Cipher.DECRYPT_MODE, this.key());

                return cipher.doFinal(cipherData);
            } catch (Throwable e) {
                throw AppError.error("使用算法 " + algorithm + " 未能解密目标内容", e);
            }
        }
    }

    public static class PKeyAlgorithm {

    }

    public interface PKeyCryptor {
        public String algorithm();

        public int keysize();

        public boolean hasPrivate();

        public boolean hasPublic();

        public String getPublicPEM();

        public boolean verify(byte[] data, byte[] signature) throws AppError;

        public byte[] sign(byte[] data) throws AppError;

        public byte[] encrypt(byte[] data) throws AppError;

        public byte[] decrypt(byte[] cipherData) throws AppError;
    }
}
