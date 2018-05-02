package commons.box.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Randoms {
    private static byte[] EMPTY_BYTES = new byte[0];
    private static String EMPTY_STR = "";
    private static SecureRandom random = new SecureRandom();
    private final static int GBK_DELTA = 0x9faa - 0x4e00 + 1; // 中文字符偏移值

    private Randoms() {
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid2() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String base64(int length) {
        if (length < 1) return EMPTY_STR;
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return Strs.get(Base64.getDecoder().decode(randomBytes), Langs.CHARSET_UTF8);
    }

    /**
     * 随机byte数组
     *
     * @param length
     * @return
     */
    public static byte[] bytes(int length) {
        if (length < 1) return EMPTY_BYTES;
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    /**
     * 随机数：boolean
     *
     * @return
     */
    public static boolean bool() {
        return random.nextBoolean();
    }

    /**
     * 随机数：int
     *
     * @param max
     * @return
     */
    public static int intnum(int max) {
        return Math.abs(random.nextInt(max));
    }

    /**
     * 随机数：long 范围在 0-最大值 之间
     * 此随机数给出了一个大范围的数字，如果需要指定范围请使用randomInt来代替
     *
     * @return
     */
    public static long longnum() {
        return Math.abs(random.nextLong());
    }

    /**
     * 随机数：double
     *
     * @param max
     * @return
     */
    public static double doublenum(double max) {
        return Math.abs(random.nextDouble() * max);
    }

    /**
     * 随机字符串：仅字母
     *
     * @return
     */
    public static String str(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    /**
     * 随机字符串：数字
     *
     * @param count
     * @return
     */
    public static String numeric(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    /**
     * 随机字符串：字母和数字
     *
     * @return
     */
    public static String ascii(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

    /**
     * 随机字符串：中文
     *
     * @param count
     * @return
     */
    public static String strZH(int count) {
        if (count < 1) return EMPTY_STR;
        final char[] buf = new char[count];

        for (int i = 0; i < count; i++) {
            buf[i] = (char) (0x4e00 + intnum(GBK_DELTA));
        }

        return new String(buf);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) System.out.println(strZH(intnum(100)));

        StringBuilder sb = new StringBuilder();
        int p = 0;
        for (int i = 0x4e00; i < GBK_DELTA; i++) {
            sb.append((char) i);
            if (p >= 100) {
                sb.append("\n");
                p = 0;
            }
            p++;
        }

        System.out.println(sb.toString());

        System.out.println("生成随机内容：" + strZH(50));
        System.out.println("生成随机内容：" + strZH(50));
        System.out.println("生成随机内容：" + strZH(50));
        System.out.println("生成随机内容：" + strZH(50));
    }
}
