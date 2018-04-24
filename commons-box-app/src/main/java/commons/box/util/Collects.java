package commons.box.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/11 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Collects {
    private static final Object[] EMPTY_OBJECTS = new Object[]{};
    private static final List<Object> EMPTY_LIST = ImmutableList.of();
    private static final Set<Object> EMPTY_SET = ImmutableSet.of();

    @SuppressWarnings("unchecked")
    public static <T> List<T> immlist() {
        return (List<T>) EMPTY_LIST;
    }

    /**
     * 不可变更实例
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> immlist(Collection<T> list) {
        if (list == null || list.isEmpty()) return (List<T>) EMPTY_LIST;
        return Collections.unmodifiableList(new ArrayList<T>(list));
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> immset() {
        return (Set<T>) EMPTY_SET;
    }

    /**
     * 不可变更实例
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> immset(Collection<T> set) {
        return immset(set, false);
    }

    /**
     * 不可变更实例  指定是否按插入顺序来返回结果集(如果linked=true则使用linkedhashset)
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> immset(Collection<T> set, boolean linked) {
        if (set == null || set.isEmpty()) return (Set<T>) EMPTY_SET;
        return Collections.unmodifiableSet(linked ? new LinkedHashSet<T>(set) : new HashSet<T>(set));
    }

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || collection.isEmpty();
    }

    /**
     * 判断是否不为空.
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return (collection != null) && (!collection.isEmpty());
    }


    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null) || (array.length < 1);
    }

    /**
     * 判断是否不为空.
     */
    public static boolean isNotEmpty(Object[] array) {
        return (array != null) && (array.length > 0);
    }


    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     */
    public static <T> T first(Collection<T> collection) {
        if (isEmpty(collection)) return null;

        for (T next : collection) {
            if (next != null) return next;
        }

        return null;
    }

    /**
     * 获取Collection的最后一个元素，如果collection为空返回null.
     */
    public static <T> T last(Collection<T> collection) {
        if (isEmpty(collection)) return null;

        return Iterables.getLast(collection);
    }

    /**
     * 循环处理
     *
     * @param ern
     * @param cmd
     * @param <T>
     */
    public static <T> void iter(Enumeration<T> ern, Consumer<T> cmd) {
        if (ern == null || cmd == null) return;
        while (ern.hasMoreElements()) cmd.accept(ern.nextElement());
    }

    public static <T> void iter(Iterator<T> iterator, Consumer<T> cmd) {
        if (iterator == null || cmd == null) return;
        while (iterator.hasNext()) cmd.accept(iterator.next());
    }

    /**
     * 生成Stream 本方法永远不返回空
     *
     * @param col
     * @param <T>
     * @return
     */
    public static <T> Stream<T> stream(Collection<T> col) {
        return stream(col, false);
    }

    /**
     * 生成Stream 本方法永远不返回空
     *
     * @param col
     * @param parallel 使用并行处理机制
     * @param <T>
     * @return
     */
    public static <T> Stream<T> stream(Collection<T> col, boolean parallel) {
        if (col == null) return Stream.empty();
        return (parallel) ? col.parallelStream() : col.stream();
    }

    /**
     * 生成Stream 本方法永远不返回空
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> Stream<T> stream(T[] array) {
        if (array == null || array.length < 1) return Stream.empty();
        return Arrays.stream(array);
    }

    /**
     * 向 source 中增加元素
     *
     * @param source
     * @param b
     * @param <T>
     */
    public static <T> void addAll(final Collection<T> source, final Collection<T> b) {
        if (source == null) return;
        if (b != null && !b.isEmpty()) source.addAll(b);
    }

    /**
     * 向 source 中增加元素
     *
     * @param source
     * @param b
     * @param <T>
     */
    public static <T> void addAll(final Collection<T> source, final T[] b) {
        if (source == null) return;
        if (b != null && b.length > 0) source.addAll(Arrays.asList(b));
    }


    /**
     * 返回a+b的新List.
     */
    public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
        if (a == null && b == null) return new ArrayList<>();
        else if (a == null) return new ArrayList<>(b);
        else if (b == null) return new ArrayList<>(a);

        List<T> result = new ArrayList<>(a);
        result.addAll(b);

        return result;
    }

    /**
     * 返回a-b的新List.
     */
    public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
        List<T> list = new ArrayList<>(a);
        for (T element : b) {
            list.remove(element);
        }

        return list;
    }

    /**
     * 返回a与b的交集的新List.
     */
    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<>();

        for (T element : a) {
            if (b.contains(element)) {
                list.add(element);
            }
        }
        return list;
    }

    /**
     * 由数组生成List
     *
     * @param objs
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> asList(T... objs) {
        if (objs == null || objs.length < 1) return (List<T>) EMPTY_LIST;
        return Arrays.asList(objs);
    }

    // TODO NO_NULL SAFE
    @SafeVarargs
    public static <T> Iterable<T> concat(Iterable<? extends T>... inputs) {
        if (inputs == null || inputs.length < 1) return new ArrayList<>();
        return Iterables.concat(inputs);
    }

    /**
     * 返回一个底层由原始类型long保存的List, 与保存Long相比节约空间.
     */
    public static List<Long> asListLong(long... backingArray) {
        return Longs.asList(backingArray);
    }

    /**
     * 返回一个底层由原始类型int保存的List, 与保存Integer相比节约空间.
     */
    public static List<Integer> asListInt(int... backingArray) {
        return Ints.asList(backingArray);
    }

    /**
     * 返回一个底层由原始类型double保存的Double, 与保存Double相比节约空间.
     */
    public static List<Double> asListDouble(double... backingArray) {
        return Doubles.asList(backingArray);
    }


    /**
     * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
     */
    public static String convertToString(final Collection collection, final String separator) {
        return StringUtils.join(collection, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
     */
    public static String convertToString(final Collection collection, final String prefix, final String postfix) {
        StringBuilder builder = new StringBuilder();
        for (Object o : collection) builder.append(prefix).append(o).append(postfix);
        return builder.toString();
    }
}
