package commons.box.util;

import org.apache.commons.lang3.time.FastDateFormat;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 安全的格式化类 避免标准JDK中 formatter 的线程冲突问题
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class Fmts {
    /**
     * 线程安全的 日期格式化 对象，可用于全局静态变量、字段及局部变量
     * <p>
     * 同 pattern 的实例已经内部缓存，多次 get 对象 不会有太多额外开销
     *
     * @param pattern
     * @return
     */
    @Nonnull
    public static FastDateFormat date(String pattern) {
        if (Strs.isBlank(pattern)) return FastDateFormat.getInstance();
        else return FastDateFormat.getInstance(pattern);
    }

    /**
     * 线程安全的 日期格式化 对象，可用于全局静态变量、字段及局部变量
     * <p>
     * 同 pattern（及语区和时区） 的实例已经内部缓存，多次 get 对象 不会有太多额外开销
     *
     * @param pattern
     * @param timeZone
     * @param locale
     * @return
     */
    @Nonnull
    public static FastDateFormat date(String pattern, TimeZone timeZone, Locale locale) {
        if (Strs.isBlank(pattern) && timeZone == null && locale == null) return FastDateFormat.getInstance();
        else return FastDateFormat.getInstance(pattern, timeZone, locale);
    }

    // TODO 使用 ThreadLocal 实现逻辑
    public static String fmtDate(String pattern, Date date) {
        return null;
    }

    public static String fmtDate(String pattern, TimeZone timeZone, Locale locale, Date date) {
        return null;
    }

    public static String fmtNum(String pattern, Number number) {
        return null;
    }

    public static Date parseDate(String pattern, String msg) {
        return null;
    }

    public static Date parseDate(String pattern, TimeZone timeZone, Locale locale, String msg) {
        return null;
    }

    public static Number parseNum(String pattern, String msg) {
        return null;
    }

    public static void date(String pattern, Consumer<DateFormat> consumer) {
    }

    public static void date(String pattern, TimeZone timeZone, Locale locale, Consumer<DateFormat> consumer) {
    }

    public static void num(String pattern, Consumer<NumberFormat> consumer) {
    }

    public static Function<DateFormat, String> fmtDate(String pattern) {
        return null;
    }

    public static Function<DateFormat, String> fmtDate(String pattern, TimeZone timeZone, Locale locale) {
        return null;
    }

    public static Function<NumberFormat, String> fmtNum(String pattern) {
        return null;
    }

    public static Function<DateFormat, Date> parseDate(String pattern) {
        return null;
    }

    public static Function<DateFormat, Date> parseDate(String pattern, TimeZone timeZone, Locale locale) {
        return null;
    }

    public static Function<NumberFormat, Number> parseNum(String pattern) {
        return null;
    }
}
