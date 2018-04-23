package commons.box.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/22 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class Dates {
    /**
     * Fmts.fmtDate 方法快捷方式
     *
     * @param pattern
     * @return
     * @see Fmts#date(String)
     */
    public static FastDateFormat fmt(String pattern) {
        return Fmts.date(pattern);
    }

    /**
     * Fmts.fmtDate 方法快捷方式
     *
     * @param pattern
     * @param timeZone
     * @param locale
     * @return
     * @see Fmts#date(String, TimeZone, Locale)
     */
    public static FastDateFormat fmt(String pattern, TimeZone timeZone, Locale locale) {
        return Fmts.date(pattern, timeZone, locale);
    }
}
