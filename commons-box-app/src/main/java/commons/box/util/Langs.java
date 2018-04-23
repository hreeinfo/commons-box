package commons.box.util;

import commons.box.app.AppLog;
import commons.box.app.SafeRefMap;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * 字符集与语言语区对应工具
 * <p>
 * <p>
 * <p>
 * 自动检测目标字符集
 * <p>
 * com.ibm.icu:icu4j:60.2
 * <p>
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/21 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class Langs {
    private static final AppLog LOG = Logs.get(Langs.class);
    private static final Map<String, Charset> CHARSET_CACHE = new SafeRefMap<>();


    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";


    /**
     * GBK编码
     */
    public static final String GBK = "GBK";


    public static final String LINE_ENDING = "\n";

    public static final Charset CHARSET_UTF8 = charset(UTF8, null);

    private Langs() {
    }


    /**
     * 返回字符集 当未找到时始终返回UTF-8字符集
     *
     * @param encoding
     * @return
     */
    public static Charset charset(String encoding) {
        return charset(encoding, CHARSET_UTF8);
    }

    /**
     * 返回字符集 不存在时返回默认值
     *
     * @param encoding
     * @param defaultCharset
     * @return
     */
    public static Charset charset(String encoding, Charset defaultCharset) {
        if (Strs.isBlank(encoding)) return defaultCharset;
        Charset c = CHARSET_CACHE.get(encoding);
        if (c == null) {
            try {
                c = Charset.forName(encoding);
                CHARSET_CACHE.put(encoding, c);
            } catch (Throwable e) {
                LOG.warn("无法解析字符集 " + encoding, e);
                if (defaultCharset != null) CHARSET_CACHE.put(encoding, defaultCharset);
                return defaultCharset;
            }
        }

        return c;
    }

    /**
     * 判断相等
     *
     * @return
     */
    public static boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static String detectCharset(byte[] content) {
        /*
            CharsetDetector detector = new CharsetDetector();
            detector.setText(data);

            CharsetMatch cm = detector.detect();

            if (cm != null) {
                int confidence = cm.getConfidence();
                System.out.println("Encoding: " + cm.getName() + " - Confidence: " + confidence + "%");
                //Here you have the encode name and the confidence
                //In my case if the confidence is > 50 I return the encode, else I return the default value
                if (confidence > 50) {
                    charset = cm.getName();
                }
            }
         */
        return null;
    }
}
