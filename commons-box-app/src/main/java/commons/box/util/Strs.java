package commons.box.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import commons.box.app.AppError;
import commons.box.app.misc.EntitiesASCII;
import commons.box.app.misc.EntitiesHTML;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * 字符串常用工具类
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Strs {
    public static final String EMPTY = StringUtils.EMPTY;
    public static final String[] EMPTY_STRS = new String[]{};
    public static final int INDEX_NOT_FOUND = -1;

    private Strs() {
    }

    // --------------- 相等判断 ---------------

    /**
     * 相等
     *
     * @param a
     * @param b
     * @return
     */
    @SuppressWarnings("StringEquality")
    public static boolean equals(final String a, final String b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * 忽略大小写
     *
     * @param a
     * @param b
     * @return
     */
    @SuppressWarnings("StringEquality")
    public static boolean equalsIgnoreCase(final String a, final String b) {
        return (a == b) || (a != null && a.equalsIgnoreCase(b));
    }

    /**
     * 数字
     *
     * @param cs
     * @return
     */
    public static boolean isNumeric(final String cs) {
        return StringUtils.isNumeric(cs);
    }

    /**
     * 数字和空格
     *
     * @param cs
     * @return
     */
    public static boolean isNumericSpace(final String cs) {
        return StringUtils.isNumericSpace(cs);
    }

    /**
     * 是否为空格
     *
     * @param cs
     * @return
     */
    public static boolean isWhitespace(final String cs) {
        return StringUtils.isWhitespace(cs);
    }

    // --------------- 空字符串相关 ---------------

    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static final boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }


    /**
     * 不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }

    /**
     * 可能为空
     *
     * @param css
     * @return
     */
    public static boolean isAnyEmpty(final String... css) {
        return StringUtils.isAnyEmpty(css);
    }

    /**
     * 没有空
     *
     * @param css
     * @return
     */
    public static boolean isNoneEmpty(final String... css) {
        return !isAnyEmpty(css);
    }

    /**
     * 是否为空或仅包括空格等
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(final String cs) {
        return StringUtils.isBlank(cs);
    }

    /**
     * 非空格
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank(final String cs) {
        return !isBlank(cs);
    }

    /**
     * 是否包含空格
     *
     * @param css
     * @return
     */
    public static boolean isAnyBlank(final String... css) {
        if (css == null || css.length < 1) return true;
        for (final String cs : css) if (isBlank(cs)) return true;
        return false;
    }

    /**
     * 是否无空格
     *
     * @param css
     * @return
     */
    public static boolean isNoneBlank(final String... css) {
        return !isAnyBlank(css);
    }

    /**
     * trim字符串
     *
     * @param str
     * @return
     */
    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }

    /**
     * trim字符串 第二个参数指示null或空字符串是否trim成空字符串 false时输出null
     *
     * @param str
     * @param toEmpty
     * @return
     */
    public static String trim(final String str, final boolean toEmpty) {
        return toEmpty ? StringUtils.trimToEmpty(str) : StringUtils.trimToNull(str);
    }

    /**
     * 清空前后字符串 与trim不同的是此方法将制表符等认为是空 一起清理
     *
     * @param str
     * @return
     */
    public static String strip(final String str) {
        return StringUtils.strip(str);
    }

    /**
     * 清空前后字符串 与trim不同的是此方法将制表符等认为是空 一起清理 第二个参数指示null或空字符串是否trim成空字符串 false时输出null
     *
     * @param str
     * @param toEmpty
     * @return
     */
    public static String strip(final String str, final boolean toEmpty) {
        return toEmpty ? StringUtils.stripToEmpty(str) : StringUtils.stripToNull(str);
    }

    // --------------- 大小写操作 ---------------

    /**
     * 转为大写
     *
     * @param s
     * @return
     */
    public static String caseUpper(String s) {
        if (s == null) return null;
        return s.toUpperCase(Locale.ROOT);
    }

    /**
     * 转为小写
     *
     * @param s
     * @return
     */
    public static String caseLower(String s) {
        if (s == null) return null;
        return s.toLowerCase(Locale.ROOT);
    }

    // --------------- 字符串查找 ---------------

    /**
     * 查找
     *
     * @param seq
     * @param searchSeq
     * @return
     */
    public static int indexOf(final String seq, final String searchSeq) {
        if (seq == null || searchSeq == null) return INDEX_NOT_FOUND;
        return seq.indexOf(searchSeq);
    }

    /**
     * 查找 按给定位置开始
     *
     * @param seq
     * @param searchSeq
     * @param startPos
     * @return
     */
    public static int indexOf(final String seq, final String searchSeq, final int startPos) {
        if (seq == null || searchSeq == null) return INDEX_NOT_FOUND;
        return seq.indexOf(searchSeq, startPos);
    }

    /**
     * 查找 忽略大小写
     *
     * @param str
     * @param searchStr
     * @return
     */
    public static int indexOfIgnoreCase(final String str, final String searchStr) {
        return StringUtils.indexOfIgnoreCase(str, searchStr); // TODO 考虑使用原始String机制来实现
    }

    /**
     * 查找 忽略大小写 按给定位置开始
     *
     * @param str
     * @param searchStr
     * @param startPos
     * @return
     */
    public static int indexOfIgnoreCase(final String str, final String searchStr, int startPos) {
        return StringUtils.indexOfIgnoreCase(str, searchStr, startPos);// TODO 考虑使用原始String机制来实现
    }

    /**
     * 查找最后出现的位置
     *
     * @param seq
     * @param searchSeq
     * @return
     */
    public static int indexOfLast(final String seq, final String searchSeq) {
        if (seq == null || searchSeq == null) return INDEX_NOT_FOUND;
        return seq.lastIndexOf(searchSeq);
    }

    /**
     * 查找最后出现的位置 按给定位置开始
     *
     * @param seq
     * @param searchSeq
     * @param startPos
     * @return
     */
    public static int indexOfLast(final String seq, final String searchSeq, final int startPos) {
        if (seq == null || searchSeq == null) return INDEX_NOT_FOUND;
        return seq.lastIndexOf(searchSeq, startPos);
    }

    /**
     * 查找最后出现的位置 忽略大小写
     *
     * @param str
     * @param searchStr
     * @return
     */
    public static int indexOfLastIgnoreCase(final String str, final String searchStr) {
        return StringUtils.lastIndexOfIgnoreCase(str, searchStr);// TODO 考虑使用原始String机制来实现
    }

    /**
     * 查找最后出现的位置 忽略大小写 按给定位置开始
     *
     * @param str
     * @param searchStr
     * @param startPos
     * @return
     */
    public static int indexOfLastIgnoreCase(final String str, final String searchStr, int startPos) {
        return StringUtils.lastIndexOfIgnoreCase(str, searchStr, startPos);// TODO 考虑使用原始String机制来实现
    }

    /**
     * 是否按前缀开始
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWith(final String str, final String prefix) {
        return StringUtils.startsWith(str, prefix);
    }

    /**
     * 是否按前缀开始 忽略大小写
     *
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsWithIgnoreCase(final String str, final String prefix) {
        return StringUtils.startsWithIgnoreCase(str, prefix);
    }


    /**
     * 是否按后缀结束 忽略大小写
     *
     * @param str
     * @param suffix
     * @return
     */
    public static boolean endsWithIgnoreCase(final String str, final String suffix) {
        return StringUtils.endsWithIgnoreCase(str, suffix);
    }

    /**
     * 是否包含空白
     *
     * @param seq
     * @return
     */
    public static boolean hasWhitespace(final String seq) {
        return StringUtils.containsWhitespace(seq);
    }


    // --------------- 字符串分隔 ---------------

    /**
     * 按位置获取字串
     *
     * @param str
     * @param start
     * @return
     */
    public static String sub(final String str, int start) {
        return StringUtils.substring(str, start);
    }

    /**
     * 按位置截取字串
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String sub(final String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    /**
     * 字串前的内容
     *
     * @param str
     * @param separator
     * @return
     */
    public static String subBefore(final String str, final String separator) {
        return StringUtils.substringBefore(str, separator);
    }

    /**
     * 字串后的内容
     *
     * @param str
     * @param separator
     * @return
     */
    public static String subAfter(final String str, final String separator) {
        return StringUtils.substringAfter(str, separator);
    }

    /**
     * 最后一个字串前的内容
     *
     * @param str
     * @param separator
     * @return
     */
    public static String subBeforeLast(final String str, final String separator) {
        return StringUtils.substringBeforeLast(str, separator);
    }

    /**
     * 最后一个字串后的内容
     *
     * @param str
     * @param separator
     * @return
     */
    public static String subAfterLast(final String str, final String separator) {
        return StringUtils.substringAfterLast(str, separator);
    }

    /**
     * 按前后缀分隔 只返回第一组符合结果的内容
     *
     * @param str
     * @param open
     * @param close
     * @return
     */
    public static String subBetween(final String str, final String open, final String close) {
        return StringUtils.substringBetween(str, open, close);
    }

    /**
     * 按前后缀分隔
     *
     * @param str
     * @param open
     * @param close
     * @return
     */
    public static String[] subsBetween(final String str, final String open, final String close) {
        if (str == null) return EMPTY_STRS;
        return StringUtils.substringsBetween(str, open, close);
    }

    /**
     * 左边字串 给定长度
     *
     * @param str
     * @param len
     * @return
     */
    public static String left(final String str, final int len) {
        return StringUtils.left(str, len);
    }

    /**
     * 右边字串 给定长度
     *
     * @param str
     * @param len
     * @return
     */
    public static String right(final String str, final int len) {
        return StringUtils.right(str, len);
    }

    /**
     * 中间字串
     *
     * @param str
     * @param pos
     * @param len
     * @return
     */
    public static String mid(final String str, int pos, final int len) {
        return StringUtils.mid(str, pos, len);
    }

    /**
     * 拼接
     *
     * @param elements
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> String join(final T... elements) {
        return StringUtils.join(elements);
    }

    /**
     * 拼接
     *
     * @param array
     * @param separator
     * @return
     */
    public static String join(final Object[] array, final String separator) {
        return StringUtils.join(array, separator);
    }

    /**
     * 拼接
     *
     * @param iterator
     * @param separator
     * @return
     */
    public static String join(final Iterator<?> iterator, final String separator) {
        return StringUtils.join(iterator, separator);
    }

    /**
     * 删除空白
     *
     * @param str
     * @return
     */
    public static String removeWhitespace(final String str) {
        return StringUtils.deleteWhitespace(str);
    }

    /**
     * 删除字串
     *
     * @param str
     * @param remove
     * @return
     */
    public static String remove(final String str, final String remove) {
        return StringUtils.remove(str, remove);
    }

    /**
     * 替换
     *
     * @param text
     * @param searchString
     * @param replacement
     * @return
     */
    public static String replace(final String text, final String searchString, final String replacement) {
        return StringUtils.replace(text, searchString, replacement);
    }

    /**
     * 替换
     *
     * @param text
     * @param searchString
     * @param replacement
     * @param max
     * @return
     */
    public static String replace(final String text, final String searchString, final String replacement, int max) {
        return StringUtils.replace(text, searchString, replacement, max);
    }

    /**
     * 按正则替换
     *
     * @param source
     * @param regex
     * @param replacement
     * @return
     */
    public static String replacePattern(final String source, final String regex, final String replacement) {
        return StringUtils.replacePattern(source, regex, replacement);
    }


    // --------------- 字符串编码与输出 ---------------
    public static byte[] bytes(String str) {
        return bytes(str, Langs.CHARSET_UTF8);
    }

    public static byte[] bytes(String str, Charset charset) {
        if (str == null) return null;
        if (charset == null) charset = Langs.CHARSET_UTF8;
        return str.getBytes(charset);
    }

    public static String get(byte[] bytes) {
        return get(bytes, Langs.CHARSET_UTF8);
    }

    public static String get(byte[] bytes, Charset charset) {
        if (bytes == null) return null;
        if (charset == null) charset = Langs.CHARSET_UTF8;
        return new String(bytes, charset);
    }

    public static String toString(Object object) {
        if (object == null) return null;
        else if (object.getClass().isArray()) return Arrays.toString((Object[]) object);
        return object.toString();
    }

    public static String toString(final byte[] bytes, final Charset charset) {
        if (bytes == null) return null;
        return StringUtils.toEncodedString(bytes, charset);
    }

    public static ToStringHelper toStringHelper(Object object) {
        if (object == null) object = new Object();
        return MoreObjects.toStringHelper(object);
    }

    //----------- FORK Strman (shekhargulati/strman-java) from : acdb15d6fe12ec25e5e0a15dba1c0f5c20db5588

    private static final Predicate<String> STRING_PREDICATE_NULL = str -> (str == null);
    private static final Supplier<String> STRING_MSG_SUPPLIER_NULL = () -> "值不允许为空";
    private static final Supplier<String> STRING_MSG_SUPPLIER_INVALID = () -> "值不符合要求";

    /**
     * 验证规则: 是否为空
     *
     * @throws AppError
     */
    public static void valid(String str) throws AppError {
        valid(str, STRING_PREDICATE_NULL, STRING_MSG_SUPPLIER_NULL);
    }

    /**
     * @param str
     * @param predicate
     * @throws AppError
     */
    public static void valid(String str, Predicate<String> predicate) throws AppError {
        valid(str, predicate, STRING_MSG_SUPPLIER_INVALID);
    }

    /**
     * 验证字符串
     *
     * @param str
     * @param predicate
     * @param msg
     * @throws AppError
     */
    public static void valid(String str, Predicate<String> predicate, Supplier<String> msg) throws AppError {
        valid(str, predicate, msg);
    }


    /**
     * Append字符串 类似于StringBuilder的append
     *
     * @param value   原始字符串
     * @param appends 增加的目标字符串(对象以toString方式解析)
     * @return 返回后的字符串
     */
    public static String append(final String value, final Object... appends) {
        if (value == null) return null;
        if (appends == null || appends.length == 0) return value;
        StringJoiner joiner = new StringJoiner("");
        for (Object append : appends) joiner.add((append != null) ? append.toString() : null);
        return value + joiner.toString();
    }


    /**
     * 获取index位置的字符 如果未找到或者index超出范围则返回Optional empty
     *
     * @param value 输入
     * @param index 位置
     * @return Optional String (未找到返回empty)
     */
    public static Optional<String> at(final String value, int index) {
        if (value == null || value.isEmpty()) return Optional.empty();
        int length = value.length();
        if (index < 0) index = length + index;
        return (index < length && index >= 0) ? Optional.of(String.valueOf(value.charAt(index))) : Optional.empty();
    }

    /**
     * 返回数组,每个元素是start和end之间的内容
     *
     * @param value input
     * @param start start
     * @param end   end
     * @return 包含每个start和end之间的内容
     */

    public static String[] between(final String value, final String start, final String end) {
        if (value == null || start == null || end == null) return EMPTY_STRS;
        String[] parts = value.split(end);
        return Arrays.stream(parts).map(subPart -> subPart.substring(subPart.indexOf(start) + start.length())).toArray(String[]::new);
    }

    /**
     * 返回数组,每个元素是str的数组
     *
     * @param value input
     * @return 字符数组
     */
    public static String[] chars(final String value) {
        return value.split("");
    }


    /**
     * 合并空格 所有的连续空格将被合并为一个空格符
     *
     * @param value 原始字符串
     * @return 组合后的字符串
     */
    public static String collapseWhitespace(final String value) {
        if (value == null) return null;
        return value.trim().replaceAll("\\s\\s+", " ");
    }

    /**
     * 是否包含给定字符串
     *
     * @param value  to search
     * @param needle to find
     * @return 找到为true
     */
    public static boolean contains(final String value, final String needle) {
        return contains(value, needle, true);
    }


    /**
     * 是否包含给定字符串 忽略大小写
     *
     * @param value  原始字符串
     * @param needle 要查找的字符串
     * @return 找到为true
     */
    public static boolean containsIgnoreCase(final String value, final String needle) {
        return contains(value, needle, false);
    }

    /**
     * 是否包含给定字符串 按需忽略大小写
     *
     * @param value         原始字符串
     * @param needle        要查找的字符串
     * @param caseSensitive 是否大小写敏感
     * @return 找到为true
     */
    public static boolean contains(final String value, final String needle, final boolean caseSensitive) {
        if (value == null || needle == null) return false;
        if (caseSensitive) return value.contains(needle);
        return value.toLowerCase().contains(needle.toLowerCase());
    }


    /**
     * 验证是否包含全部给定字符串
     *
     * @param value   原始字符串
     * @param needles 要查找的字符串数组
     * @return 如果全部包含则返回true, 否则返回false
     */
    public static boolean containsAll(final String value, final String[] needles) {
        return !(value == null || needles == null) && Arrays.stream(needles).allMatch(needle -> contains(value, needle));
    }

    /**
     * 验证是否包含全部给定字符串
     *
     * @param value         原始字符串
     * @param needles       要查找的字符串数组
     * @param caseSensitive 是否大小写敏感
     * @return 如果全部包含则返回true, 否则返回false
     */
    public static boolean containsAll(final String value, final String[] needles, final boolean caseSensitive) {
        return !(value == null || needles == null) && Arrays.stream(needles).allMatch(needle -> contains(value, needle, caseSensitive));
    }

    /**
     * 验证是否包含全部给定字符串中的任意一个
     *
     * @param value   原始字符串
     * @param needles 要查找的字符串数组
     * @return 如果包含任意一个则返回true, 否则返回false
     */
    public static boolean containsAny(final String value, final String[] needles) {
        return containsAny(value, needles, false);
    }

    /**
     * 验证是否包含全部给定字符串中的任意一个
     *
     * @param value         原始字符串
     * @param needles       要查找的字符串数组
     * @param caseSensitive 是否大小写敏感
     * @return 如果包含任意一个则返回true, 否则返回false
     */
    public static boolean containsAny(final String value, final String[] needles, final boolean caseSensitive) {
        return !(value == null || needles == null) && Arrays.stream(needles).anyMatch(needle -> contains(value, needle, caseSensitive));
    }

    /**
     * 统计出现次数
     *
     * @param value  原始字符串
     * @param subStr 要查找的字符串
     * @return 字符串出现次数
     */
    public static long countSubstr(final String value, final String subStr) {
        return countSubstr(value, subStr, true, false);
    }

    /**
     * 统计出现次数
     *
     * @param value            原始字符串
     * @param subStr           要查找的字符串
     * @param caseSensitive    是否大小写敏感
     * @param allowOverlapping boolean to take into account overlapping
     * @return 字符串出现次数
     */
    public static long countSubstr(final String value, final String subStr, final boolean caseSensitive, boolean allowOverlapping) {
        if (value == null || subStr == null) return 0L;
        return countSubstr(caseSensitive ? value : value.toLowerCase(), caseSensitive ? subStr : subStr.toLowerCase(), allowOverlapping, 0L);
    }

    /**
     * 是否以search字符串结尾
     *
     * @param value  原始字符串
     * @param search 要查找的字符串
     * @return 是否以目标结尾
     */
    public static boolean endsWith(final String value, final String search) {
        return endsWith(value, search, value.length(), true);
    }

    /**
     * 是否以search字符串结尾
     *
     * @param value         原始字符串
     * @param search        要查找的字符串
     * @param caseSensitive 是否大小写敏感
     * @return 是否以目标结尾
     */
    public static boolean endsWith(final String value, final String search, final boolean caseSensitive) {
        if (value == null || search == null) return false;
        return endsWith(value, search, value.length(), caseSensitive);
    }

    /**
     * 是否以search字符串结尾
     *
     * @param value         原始字符串
     * @param search        要查找的字符串
     * @param position      查找的开始位置
     * @param caseSensitive 是否大小写敏感
     * @return 是否以目标结尾
     */
    public static boolean endsWith(final String value, final String search, final int position, final boolean caseSensitive) {
        if (value == null || search == null) return false;
        int remainingLength = position - search.length();
        if (caseSensitive) return value.indexOf(search, remainingLength) > -1;
        return value.toLowerCase().indexOf(search.toLowerCase(), remainingLength) > -1;
    }

    /**
     * 确定字符串以prefix开头(left) 如未包含前缀则自动增加前缀
     *
     * @param value  输入
     * @param prefix 前缀
     * @return 返回已prefix开头的字符串, 如果原字符串未以prefix开头则增加此prefix
     */
    public static String ensureLeft(final String value, final String prefix) {
        return ensureLeft(value, prefix, true);
    }

    /**
     * 确定字符串以prefix开头(left) 如未包含前缀则自动增加前缀
     *
     * @param value         输入
     * @param prefix        前缀
     * @param caseSensitive 是否大小写敏感
     * @return 返回已prefix开头的字符串, 如果原字符串未以prefix开头则增加此prefix
     */
    public static String ensureLeft(final String value, final String prefix, final boolean caseSensitive) {
        if (value == null || prefix == null) return value;
        if (caseSensitive) return value.startsWith(prefix) ? value : prefix + value;
        String _value = value.toLowerCase();
        String _prefix = prefix.toLowerCase();
        return _value.startsWith(_prefix) ? value : prefix + value;
    }

    /**
     * 确定字符串以suffix结尾(right) 如未包含前缀则自动增加前缀
     *
     * @param value  输入
     * @param suffix 后缀
     * @return 返回已suffix结尾的字符串, 如果原字符串未以suffix结尾则增加此suffix结尾
     */
    public static String ensureRight(final String value, final String suffix) {
        return ensureRight(value, suffix, true);
    }

    /**
     * 确定字符串以suffix结尾(right) 如未包含前缀则自动增加前缀
     *
     * @param value         输入
     * @param suffix        后缀
     * @param caseSensitive 是否大小写敏感
     * @return 返回已suffix结尾的字符串, 如果原字符串未以suffix结尾则增加此suffix结尾
     */
    public static String ensureRight(final String value, final String suffix, boolean caseSensitive) {
        if (value == null || suffix == null) return value;
        return endsWith(value, suffix, caseSensitive) ? value : append(value, suffix);
    }

    /**
     * 返回开头的n个字符
     *
     * @param value 输入
     * @param n     前n个
     * @return 开头的n个字符
     */
    public static String first(final String value, final int n) {
        if (value == null || n < 0 || n > value.length()) return value;
        return value.substring(0, n);
    }

    /**
     * 开头的第一个字符
     *
     * @param value 输入
     * @return 开头的第一个字符
     */
    public static String head(final String value) {
        return first(value, 1);
    }

    /**
     * 格式化
     * <p>
     * 当给定的参数不足时 用空字符串填充目标
     *
     * @param value  原始模板
     * @param params 参数
     * @return 格式化后字符串
     */
    public static String format(final String value, String... params) {
        if (value == null) return null;
        Pattern p = Pattern.compile("\\{(\\w+)\\}");
        Matcher m = p.matcher(value);
        String result = value;
        while (m.find()) {
            int paramNumber = Integer.parseInt(m.group(1));
            String str = (params == null || paramNumber >= params.length) ? "" : params[paramNumber];
            result = result.replace(m.group(), str);
        }
        return result;
    }


    /**
     * 返回出现needle子串的位置,如果定义了offset那么从offset位置开始查找,如果未找到则返回-1
     *
     * @param value         输入
     * @param needle        查找子串
     * @param offset        开始查找的位置
     * @param caseSensitive 是否大小写敏感
     * @return 首次出现的位置
     */
    public static int indexOf(final String value, final String needle, int offset, boolean caseSensitive) {
        if (value == null || needle == null) return -1;
        if (caseSensitive) return value.indexOf(needle, offset);
        return value.toLowerCase().indexOf(needle.toLowerCase(), offset);
    }


    /**
     * 在给定位置插入字串
     *
     * @param value  输入
     * @param substr 要插入的子串
     * @param index  要插入的位置
     * @return 加工后的内容
     */
    public static String insert(final String value, final String substr, final int index) {
        if (value == null || substr == null) return value;
        if (index > value.length() || index < 0) return value;
        return append(value.substring(0, index), substr, value.substring(index));
    }

    /**
     * 是否全为大写
     *
     * @param value 输入
     * @return 是否全为大写
     */
    public static boolean isUpperCase(final String value) {
        if (value == null) return false;
        for (int i = 0; i < value.length(); i++) {
            if (Character.isLowerCase(value.charAt(i))) return false;
        }
        return true;
    }

    /**
     * 是否全为小写
     *
     * @param value 输入
     * @return 是否全为小写
     */
    public static boolean isLowerCase(final String value) {
        if (value == null) return false;
        for (int i = 0; i < value.length(); i++) {
            if (Character.isUpperCase(value.charAt(i))) return false;
        }
        return true;
    }

    /**
     * 返回第n个结尾字符
     *
     * @param value 输入
     * @param n     个数n
     * @return n Last characters
     */
    public static String last(final String value, int n) {
        if (value == null) return null;
        if (n > value.length() || n < 0) return value;
        return value.substring(value.length() - n);
    }

    /**
     * 左边
     *
     * @param value  输入
     * @param pad    The pad
     * @param length Length of the String we want
     * @return Padded String
     */
    public static String leftPad(final String value, final String pad, final int length) {
        if (value == null || pad == null || length < 0) return value;
        if (value.length() > length) return value;
        return append(repeat(pad, length - value.length()), value);
    }

    /**
     * 检测是否为字符串
     *
     * @param value 输入
     * @return 是否为字符串
     */
    public static boolean isString(final Object value) {
        if (value == null) return false;
        return value instanceof String;
    }

    /**
     * 最后一次出现查找子串的位置
     *
     * @param value  输入
     * @param needle 查找的字符串
     * @return 最后一次出现查找子串的位置
     */
    public static int lastIndexOf(final String value, final String needle) {
        if (value == null || needle == null) return -1;
        return lastIndexOf(value, needle, value.length(), true);
    }

    /**
     * 最后一次出现查找子串的位置
     *
     * @param value         输入
     * @param needle        查找的字符串
     * @param caseSensitive 是否
     * @return 最后一次出现查找子串的位置
     */
    public static int lastIndexOf(final String value, final String needle, boolean caseSensitive) {
        if (value == null || needle == null) return -1;
        return lastIndexOf(value, needle, value.length(), caseSensitive);
    }

    /**
     * 最后一次出现查找子串的位置
     *
     * @param value         输入
     * @param needle        查找的字符串
     * @param offset        位置
     * @param caseSensitive 是否大小写敏感
     * @return 最后一次出现查找子串的位置
     */
    public static int lastIndexOf(final String value, final String needle, final int offset, final boolean caseSensitive) {
        if (value == null || needle == null) return -1;
        if (caseSensitive) return value.lastIndexOf(needle, offset);
        return value.toLowerCase().lastIndexOf(needle.toLowerCase(), offset);
    }

    /**
     * 清除左边空格
     *
     * @param value 输入
     * @return 结果
     */
    public static String leftTrim(final String value) {
        if (value == null) return null;
        return value.replaceAll("^\\s+", "");
    }

    /**
     * 返回长度
     *
     * @param value 输入
     * @return 长度
     */
    public static int length(final String value) {
        if (value == null) return -1;
        return value.length();
    }

    /**
     * 染回增加前缀集合的结果
     *
     * @param value    输入
     * @param prepends 增加的字符串数组
     * @return 操作后的值
     */
    public static String prepend(final String value, final String... prepends) {
        if (prepends == null || prepends.length == 0) return value;
        StringJoiner joiner = new StringJoiner("");
        for (String prepend : prepends) joiner.add(prepend);
        return joiner.toString() + ((value == null) ? "" : value);
    }


    /**
     * 清理空的字符串元素
     *
     * @param strings 原始数组
     * @return 不包含空字符串的数组
     */
    public static String[] removeEmptyStrings(String[] strings) {
        if (strings == null || strings.length < 1) return EMPTY_STRS;
        return Arrays.stream(strings).filter(str -> str != null && !str.trim().isEmpty()).toArray(String[]::new);
    }

    /**
     * 删除左边前缀
     *
     * @param value  输入
     * @param prefix 删除的前缀
     * @return 结果
     */
    public static String removeLeft(final String value, final String prefix) {
        return removeLeft(value, prefix, true);
    }

    /**
     * 删除左边前缀
     *
     * @param value         输入
     * @param prefix        删除的前缀
     * @param caseSensitive 大小写标识
     * @return 结果
     */
    public static String removeLeft(final String value, final String prefix, final boolean caseSensitive) {
        if (value == null || prefix == null) return value;
        if (caseSensitive) return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
        return value.toLowerCase().startsWith(prefix.toLowerCase()) ? value.substring(prefix.length()) : value;
    }

    /**
     * 删除所有非word字符
     *
     * @param value 输入
     * @return 结果
     */
    public static String removeNonWords(final String value) {
        if (value == null) return null;
        return value.replaceAll("[^\\w]+", "");
    }

    /**
     * 删除右边结尾
     *
     * @param value  输入
     * @param suffix 要删除的后缀
     * @return 不包含后缀的结果
     */
    public static String removeRight(final String value, final String suffix) {
        return removeRight(value, suffix, true);
    }

    /**
     * 删除右边结尾
     *
     * @param value         输入
     * @param suffix        要删除的后缀
     * @param caseSensitive 是否大小写敏感 or not
     * @return 不包含后缀的结果
     */
    public static String removeRight(final String value, final String suffix, final boolean caseSensitive) {
        if (value == null || suffix == null) return value;
        return endsWith(value, suffix, caseSensitive) ? value.substring(0, value.toLowerCase().lastIndexOf(suffix.toLowerCase())) : value;
    }

    /**
     * 删除所有空格
     *
     * @param value 输入
     * @return 无空格的字符串
     */
    public static String removeSpaces(final String value) {
        if (value == null) return null;
        return value.replaceAll("\\s", "");
    }

    /**
     * 重复给定次数
     *
     * @param value      输入
     * @param multiplier 次数
     * @return 结果
     */
    public static String repeat(final String value, final int multiplier) {
        if (value == null || multiplier < 1) return null;
        else if (multiplier == 1) return value;
        return Stream.generate(() -> value).limit(multiplier).collect(joining());
    }

    /**
     * 替换给定字符串
     *
     * @param value         输入
     * @param search        查找
     * @param newValue      替换
     * @param caseSensitive 是否大小写敏感 or not
     * @return 结果
     */
    public static String replace(final String value, final String search, final String newValue, final boolean caseSensitive) {
        if (value == null || search == null) return value;
        if (caseSensitive) return value.replace(search, newValue);
        return Pattern.compile(search, Pattern.CASE_INSENSITIVE).matcher(value).replaceAll(Matcher.quoteReplacement(newValue));
    }

    /**
     * 反转
     *
     * @param value 输入
     * @return 反转
     */
    public static String reverse(final String value) {
        if (value == null) return null;
        return new StringBuilder(value).reverse().toString();
    }

    /**
     * 填充右边
     *
     * @param value  输入
     * @param length 长度
     * @param pad    重复的字符串
     * @return 结果
     */
    public static String rightPad(final String value, String pad, final int length) {
        if (value == null) return null;
        if (value.length() > length) return value;
        return append(value, repeat(pad, length - value.length()));
    }

    /**
     * 删除右侧空格
     *
     * @param value 字符串
     * @return 结果
     */
    public static String rightTrim(final String value) {
        if (value == null) return null;
        return value.replaceAll("\\s+$", "");
    }

    /**
     * 安全截断字符串,超出商都的部分会被忽略,同时在返回结果中以类似 ... 结尾字串结尾的标识符
     * <p>
     * 长度未超过限制直接返回原有字符串
     * <p>
     * 此方法不会打断最后一个单词
     *
     * @param value  输入
     * @param length 最大长度
     * @param filler 要附加的省略符号,例如: '...'
     * @return 结果
     */
    public static String truncateSafely(final String value, final int length, final String filler) {
        if (value == null) return null;
        if (length == 0) return "";
        if (length >= value.length()) return value;

        String[] words = words(value);
        StringJoiner result = new StringJoiner(" ");
        int spaceCount = 0;
        for (String word : words) {
            if (result.length() + word.length() + filler.length() + spaceCount > length) {
                break;
            } else {
                result.add(word);
                spaceCount++;
            }
        }
        return append(result.toString(), filler);
    }

    /**
     * 安全截断字符串,超出商都的部分会被忽略,同时在返回结果中以类似 ... 结尾字串结尾的标识符
     * <p>
     * 长度未超过限制直接返回原有字符串
     * <p>
     * 此方法直接按长度操作,最后一个单词可能会被打断,与truncateSafely相比此方法性能稍好
     *
     * @param value  输入
     * @param length 最大长度
     * @param filler 要附加的省略符号,例如: '...'
     * @return 结果
     */
    public static String truncate(final String value, final int length, final String filler) {
        if (value == null) return null;
        if (length == 0) return "";
        if (length >= value.length()) return value;
        return append(value.substring(0, length - filler.length()), filler);
    }

    /**
     * 分隔
     *
     * @param value 输入
     * @param regex 查询Regex
     * @return 返回结果
     */
    @Nonnull
    public static String[] split(final String value, final String regex) {
        if (value == null) return EMPTY_STRS;
        return value.split(regex);
    }

    /**
     * 按单词分隔(英文分词)
     *
     * @param value 输入
     * @return 结果
     */
    @Nonnull
    public static String[] words(final String value) {
        if (value == null) return EMPTY_STRS;
        return value.split("\\W+");
    }


    /**
     * 编解码: MIME base64
     *
     * @param value 内容
     * @return 解码后的内容
     */
    public static String base64Decode(final String value) {
        if (value == null) return null;
        return new String(Base64.getDecoder().decode(value));
    }

    /**
     * 编解码: MIME base64
     *
     * @param value 内容
     * @return 编码后的内容
     */
    public static String base64Encode(final String value) {
        if (value == null) return null;
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    /**
     * 编解码: 二进制 decode(value, 16, 2);
     *
     * @param value 内容
     * @return 解码后的内容
     */
    public static String binDecode(final String value) {
        return decode(value, 16, 2);
    }

    /**
     * 编解码: 二进制 encode(value, 16, 2);
     *
     * @param value 内容
     * @return 编码后的内容
     */
    public static String binEncode(final String value) {
        return encode(value, 16, 2);
    }

    /**
     * 编解码: 十进制 decode(value, 5, 10);
     *
     * @param value 内容
     * @return 解码后的内容
     */
    public static String decDecode(final String value) {
        return decode(value, 5, 10);
    }

    /**
     * 编解码: 十进制 encode(value, 5, 10);
     *
     * @param value 内容
     * @return 编码后的内容
     */
    public static String decEncode(final String value) {
        return encode(value, 5, 10);
    }


    /**
     * 编解码: 十六进制 decode(value, 4, 16);
     *
     * @param value 内容
     * @return 解码后的内容
     */
    public static String hexDecode(final String value) {
        return decode(value, 4, 16);
    }

    /**
     * 编解码: 十六进制 encode(value, 4, 16);
     *
     * @param value 内容
     * @return 编码后的内容
     */
    public static String hexEncode(final String value) {
        return encode(value, 4, 16);
    }

    /**
     * 编解码: HTML
     *
     * @param encodedHtml 内容
     * @return 解码后的内容
     */
    public static String htmlDecode(final String encodedHtml) {
        if (encodedHtml == null) return null;
        String[] entities = encodedHtml.split("&\\W+;");
        return Arrays.stream(entities).map(EntitiesHTML.decodedEntities::get).collect(joining());
    }

    /**
     * 编解码: HTML
     *
     * @param html 内容
     * @return 编码后的内容
     */
    public static String htmlEncode(final String html) {
        if (html == null) return null;
        return html.chars().mapToObj(c -> "\\u" + String.format("%04x", c).toUpperCase()).map(EntitiesHTML.encodedEntities::get).collect(joining());
    }

    /**
     * 乱序给定字符串
     *
     * @param value 输入
     * @return 乱序
     */
    public static String shuffle(final String value) {
        if (value == null) return null;
        String[] chars = chars(value);
        Random random = new Random();
        for (int i = 0; i < chars.length; i++) {
            int r = random.nextInt(chars.length);
            String tmp = chars[i];
            chars[i] = chars[r];
            chars[r] = tmp;
        }
        return Arrays.stream(chars).collect(joining());
    }

    /**
     * 子串
     * <p>
     * 本方法提供了安全操作,如果 begin和end超出范围自动规范 如果子串长度小于等于0返回空字符串
     *
     * @param value 输入
     * @param begin 开始
     * @param end   结尾
     * @return 字符串 sliced!
     */
    public static String slice(final String value, int begin, int end) {
        if (value == null) return null;


        if (begin < 0) begin = 0;
        if (end > value.length()) end = value.length();

        int subLen = end - begin;
        if (subLen <= 0) return EMPTY;

        return value.substring(begin, end);
    }


    /**
     * 包围
     *
     * @param value  输入
     * @param prefix 前缀
     * @param suffix 后缀 如果suffix为空则使用prefix
     * @return 字符串 with surround substrs!
     */
    public static String surround(final String value, final String prefix, final String suffix) {
        if (value == null) return null;
        String _prefix = Optional.ofNullable(prefix).orElse("");
        return append(_prefix, value, Optional.ofNullable(suffix).orElse(_prefix));
    }

    /**
     * 转换为 camelCase
     *
     * @param value 输入
     * @return 转换为 camelCase.
     */
    public static String toCamelCase(final String value) {
        if (value == null) return null;
        String str = toStudlyCase(value);
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 转换为 StudlyCaps.
     *
     * @param value 输入
     * @return 转换为 StudlyCaps.
     */
    public static String toStudlyCase(final String value) {
        if (value == null) return null;
        String[] words = collapseWhitespace(value.trim()).split("\\s*(_|-|\\s)\\s*");
        return Arrays.stream(words).filter(w -> !w.trim().isEmpty()).map(w -> head(w).toUpperCase() + tail(w)).collect(joining());
    }

    /**
     * tail字符串
     *
     * @param value 输入
     * @return 结果
     */
    public static String tail(final String value) {
        if (value == null) return null;
        return last(value, value.length() - 1);
    }

    /**
     * Decamelize 字符串
     *
     * @param value 输入
     * @param chr   string to use
     * @return String decamelized.
     */
    public static String toDecamelize(final String value, final String chr) {
        String camelCasedString = toCamelCase(value);
        String[] words = camelCasedString.split("(?=\\p{Upper})");
        return Arrays.stream(words).map(String::toLowerCase).collect(joining(Optional.ofNullable(chr).orElse(" ")));
    }

    /**
     * 转为 kebab-case.
     *
     * @param value 输入
     * @return kebab-case结果
     */
    public static String toKebabCase(final String value) {
        return toDecamelize(value, "-");
    }

    /**
     * 转为蛇形
     *
     * @param value 输入
     * @return 蛇形大小写
     */
    public static String toSnakeCase(final String value) {
        return toDecamelize(value, "_");
    }

    /**
     * 解码为指定进制 2 8 10 16等
     *
     * @param value
     * @param digits 位数
     * @param radix  进制
     * @return
     */
    public static String decode(final String value, final int digits, final int radix) {
        if (value == null) return null;
        return Arrays.stream(value.split("(?<=\\G.{" + digits + "})"))
                .map(data -> String.valueOf(Character.toChars(Integer.parseInt(data, radix))))
                .collect(joining());
    }

    /**
     * 编码为指定进制 2 8 10 16等
     *
     * @param value
     * @param digits 位数
     * @param radix  进制
     * @return
     */
    public static String encode(final String value, final int digits, final int radix) {
        if (value == null) return null;
        return value.chars().mapToObj(ch -> leftPad(Integer.toString(ch, radix), "0", digits)).collect(joining());
    }

    /**
     * slug字符串
     *
     * @param value 字符串
     * @return slug字符串
     */
    public static String slugify(final String value) {
        if (value == null) return null;
        String transliterated = transliterate(collapseWhitespace(value.trim().toLowerCase()));
        return Arrays.stream(words(transliterated.replace("&", "-and-"))).collect(joining("-"));
    }

    /**
     * 删除无效字符
     *
     * @param value 输入
     * @return 结果
     */
    public static String transliterate(final String value) {
        if (value == null) return null;
        String result = value;
        Set<Map.Entry<String, List<String>>> entries = EntitiesASCII.ascii.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            for (String ch : entry.getValue()) result = result.replace(ch, entry.getKey());
        }
        return result;
    }

    private static long countSubstr(String value, String subStr, boolean allowOverlapping, long count) {
        int position = value.indexOf(subStr);
        if (position == -1) {
            return count;
        }
        int offset;
        if (!allowOverlapping) {
            offset = position + subStr.length();
        } else {
            offset = position + 1;
        }
        return countSubstr(value.substring(offset), subStr, allowOverlapping, ++count);
    }
}
