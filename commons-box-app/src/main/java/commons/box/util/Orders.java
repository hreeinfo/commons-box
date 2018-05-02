package commons.box.util;

import com.google.common.collect.Ordering;
import com.google.common.primitives.*;

import java.util.*;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Orders {

    private Orders() {
    }

    /**
     * 比较两个long类型的大小，如果source小返回-1，相等返回0，否则返回1
     *
     * @param source 源
     * @param target 目标
     * @return 如果source小返回-1，相等返回0，否则返回1
     */
    public static int compare(long source, long target) {
        return Longs.compare(source, target);
    }

    /**
     * 比较两个double类型的大小，如果source小返回-1，相等返回0，否则返回1
     *
     * @param source 源
     * @param target 目标
     * @return 如果source小返回-1，相等返回0，否则返回1
     */
    public static int compare(double source, double target) {
        return Doubles.compare(source, target);
    }


    /**
     * 比较两个int类型的大小，如果source小返回-1，相等返回0，否则返回1
     *
     * @param source 源
     * @param target 目标
     * @return 如果source小返回-1，相等返回0，否则返回1
     */
    public static int compare(int source, int target) {
        return Ints.compare(source, target);
    }

    /**
     * 比较两个double类型的大小，如果source小返回-1，相等返回0，否则返回1
     *
     * @param source 源
     * @param target 目标
     * @return 如果source小返回-1，相等返回0，否则返回1
     */
    public static int compare(float source, float target) {
        return Floats.compare(source, target);
    }

    /**
     * 比较两个boolean类型的大小，如果source小返回-1，相等返回0，否则返回1
     *
     * @param source 源
     * @param target 目标
     * @return 如果source小返回-1，相等返回0，否则返回1
     */
    public static int compare(boolean source, boolean target) {
        return Booleans.compare(source, target);
    }


    /**
     * 比较两个对象的大小，如果o1小返回负数，相等返回0，否则返回正数
     * <p>
     * 本方法所操作的对象如果实现了Comparable则使用Comparable比较,否则使用toString值进行比较
     *
     * @param o1 源
     * @param o2 目标
     * @return 如果source小返回-1，相等返回0，否则返回正数
     */
    public static int compare(Object o1, Object o2) {
        return DefaultOrder.DEFAULT.compare(o1, o2);
    }

    /**
     * 返回Google Guava提供的Ordering 提供更多的功能
     * <p>
     * 级联的排序规则 如果相等那么继续下一个比较判断
     * <p>
     * 如未定义 那么该比较器使用toString产生的字符串来比较值
     *
     * @param comparators 比较器
     * @param <T>         类型
     * @return 如果source小返回-1，相等返回0，否则返回1
     */
    @SuppressWarnings("unchecked")
    public static <T> Ordering<T> ordering(Comparator... comparators) {
        if (comparators == null || comparators.length < 1 || comparators[0] == null) return (Ordering<T>) DefaultOrder.DEFAULT;
        else if (comparators.length == 1) return Ordering.from(comparators[0]);
        else return new ChainOrder<T>(comparators);
    }

    /**
     * 基于Map的比较器 按map属性依次进行值比较 如果属性参数为空那么一句左边(第一个对象)的keys作为比较依据
     *
     * @param keys
     * @return
     */
    public static Ordering<Map> byMap(Object... keys) {
        return Collects.isEmpty(keys) ? MapOrder.DEFAULT : new MapOrder(keys);
    }

    /**
     * 判断空值的排序器 本比较器可用于中间级别的比较,当值不为空时返回0,在ChainOrder中可以继续下一级判断
     * <p>
     * 注意:本实例可作为中间级别的比较器 当o1和o2值不为空时返回0 在chain级联的比较器中可以继续向下进行判断
     *
     * @param nullFirst nullFirst=true 时null值为最小值 否则为最大值
     * @return
     */
    public static Ordering<?> byNULL(boolean nullFirst) {
        return nullFirst ? NULLOrder.ASC : NULLOrder.DESC;
    }


    /**
     * 级联多个比较器
     * <p>
     * 如果无参数 则返回默认的比较器,如果对象支持Comparable那么直接比较,否则使用toString方式比较
     *
     * @param orders 要级联的各个比较器 注意此方法未包含参数化类型,本质上可组合不同类型的比较器以提供兼容性 但具体类型转换时需要保证一致
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Ordering<T> comp(Comparator... orders) {
        if (Collects.isEmpty(orders)) return (Ordering<T>) DefaultOrder.DEFAULT;
        return new ChainOrder(orders);
    }

    /**
     * 级联多个比较器
     * <p>
     * 如果无参数 则返回默认的比较器,如果对象支持Comparable那么直接比较,否则使用toString方式比较
     *
     * @param orders 要级联的各个比较器 注意此方法未包含参数化类型,本质上可组合不同类型的比较器以提供兼容性 但具体类型转换时需要保证一致
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Ordering<T> comp(Collection orders) {
        if (Collects.isEmpty(orders)) return (Ordering<T>) DefaultOrder.DEFAULT;
        return new ChainOrder(orders);
    }

    /**
     * 判断空值的排序器 本比较器可用于中间级别的比较,当值不为空时返回0,在ChainOrder中可以继续下一级判断
     */
    public static final class NULLOrder extends Ordering<Object> {
        public static final NULLOrder ASC = new NULLOrder(true);
        public static final NULLOrder DESC = new NULLOrder(false);
        public static final NULLOrder DEFAULT = ASC;

        private final int ov;

        private NULLOrder(boolean asc) {
            ov = asc ? Integer.MAX_VALUE : -Integer.MAX_VALUE;
        }

        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return 0;
            else if (o1 == null) return -this.ov;
            else if (o2 == null) return this.ov;
            else return 0;
        }
    }

    /**
     *
     */
    public static final class DefaultOrder extends Ordering<Object> {
        public static final DefaultOrder DEFAULT = new DefaultOrder();

        private DefaultOrder() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public int compare(Object o1, Object o2) {
            if (o1 == null || o2 == null) return NULLOrder.DEFAULT.compare(o1, o2);
            return (o1 instanceof Comparable) ? ((Comparable) o1).compareTo(o2) : o1.toString().compareTo(o2.toString());
        }
    }

    /**
     * 根据map的对应属性排序 如果未包含属性则使用默认的map排序器
     */
    public static final class MapOrder extends Ordering<Map> {
        public static final MapOrder DEFAULT = new MapOrder();
        private final Collection<Object> keys;

        private MapOrder(Object... keys) {
            this.keys = Collects.asList(keys);
        }

        @SuppressWarnings("unchecked")
        @Override
        public int compare(Map o1, Map o2) {
            if (o1 == null || o2 == null) return NULLOrder.DEFAULT.compare(o1, o2);

            // 基于空快速判断
            boolean e1 = o1.isEmpty();
            boolean e2 = o2.isEmpty();

            if (e1 && e2) return 0; // 都为空时直接返回相等
            else if (e1) return -1;
            else if (e2) return 1;

            // 基于值做详细比较
            Collection<Object> ps = keys;
            if (Collects.isEmpty(ps)) ps = o1.keySet();

            for (Object p : ps) {
                if (p == null) continue;

                Object v1 = o1.get(p);
                Object v2 = o2.get(p);

                int retval = DefaultOrder.DEFAULT.compare(v1, v2);
                if (retval != 0) return retval;
            }
            return 0;
        }
    }


    /**
     * 用于级联多个比较器 如果没有比较器那么使用默认比较器(实现了Comparable则进行比较,否则使用toString比较)
     *
     * @param <T>
     */
    public static final class ChainOrder<T> extends Ordering<T> {
        private final List<Comparator<T>> comparatorChain;
        private final boolean hasComparator;

        @SafeVarargs
        public ChainOrder(Comparator<T>... comparators) {
            this.comparatorChain = Collects.asList(comparators);
            this.hasComparator = Collects.isNotEmpty(this.comparatorChain);
        }

        public ChainOrder(Collection<Comparator<T>> comparators) {
            this.comparatorChain = Collects.immlist(comparators);
            this.hasComparator = Collects.isNotEmpty(this.comparatorChain);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public int compare(T o1, T o2) {
            if (o1 == null || o2 == null) return NULLOrder.DEFAULT.compare(o1, o2);
            if (!this.hasComparator) return DefaultOrder.DEFAULT.compare(o1, o2);

            for (Comparator<T> comp : this.comparatorChain) {
                if (comp == null) continue;
                int retval = comp.compare(o1, o2);
                if (retval != 0) return retval;
            }

            return 0;
        }
    }

    public static class User {

    }
}
