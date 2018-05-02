package commons.box.util;

import org.apache.commons.lang3.time.FastDateFormat;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>创建作者：xingxiuyi </p>
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
    @Nonnull
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
    @Nonnull
    public static FastDateFormat fmt(String pattern, TimeZone timeZone, Locale locale) {
        return Fmts.date(pattern, timeZone, locale);
    }
}
