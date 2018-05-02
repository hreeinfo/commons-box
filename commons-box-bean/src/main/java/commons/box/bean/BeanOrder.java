package commons.box.bean;

import com.google.common.collect.Ordering;
import commons.box.util.Beans;
import commons.box.util.Collects;
import commons.box.util.Orders;
import commons.box.util.Strs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static commons.box.util.Orders.DefaultOrder.DEFAULT;

/**
 * Bean排序 本排序器空值认为是最小值
 *
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class BeanOrder<T> extends Ordering<T> {
    private final Class<T> type;
    private final ClassAccess<T> ac;
    private final String[] props;
    private final boolean hasInnerProps;

    public BeanOrder(Class<T> type, String... props) {
        this.type = type;
        this.ac = (this.type != null) ? AppClass.from(this.type).access() : null;
        this.props = props;
        this.hasInnerProps = AppBean.hasInnerProps(props);
    }

    public BeanOrder(String... props) {
        this.type = null;
        this.ac = null;
        this.props = props;
        this.hasInnerProps = AppBean.hasInnerProps(props);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(T o1, T o2) {
        if (o1 == null || o2 == null) return Orders.NULLOrder.DEFAULT.compare(o1, o2);
        if (Collects.isEmpty(this.props)) return DEFAULT.compare(o1, o2);

        if (this.ac == null || hasInnerProps) { // 当不存在ac或者具有内属性时 使用AppBean.get获取属性
            for (String p : this.props) {
                if (Strs.isEmpty(p)) continue;
                Object v1 = this.getValueByBean(o1, p);
                Object v2 = this.getValueByBean(o2, p);

                int retval = DEFAULT.compare(v1, v2);
                if (retval != 0) return retval;
            }
        } else {
            for (String p : this.props) {
                if (Strs.isEmpty(p)) continue;
                Object v1 = this.getValueByAccessClass(o1, p);
                Object v2 = this.getValueByAccessClass(o2, p);

                int retval = DEFAULT.compare(v1, v2);
                if (retval != 0) return retval;
            }
        }

        return 0;
    }

    private Object getValueByBean(T o, String p) {
        try {
            return AppBean.inst().prop(o, p);
        } catch (Throwable ignored) {
        }
        return null;
    }


    private Object getValueByAccessClass(T o, String p) {
        try {
            return this.ac.prop(p).get(o);
        } catch (Throwable ignored) {
        }
        return null;
    }


    /**
     * 排序的Builder
     *
     * @param <T>
     */
    public static final class Builder<T> {
        private final boolean mapOrder;
        private final Class<T> type;
        private final List<Comparator<?>> comps = new ArrayList<>();

        private Ordering<T> tmpOrder = null;

        private Builder(Class<T> type) {
            this.mapOrder = false;
            this.type = type;
        }

        private Builder(boolean mapOrder) {
            this.mapOrder = mapOrder;
            this.type = null;
        }

        public static <T> Builder<T> build(Class<T> type) {
            return new Builder<>(type);

        }

        public static <T> Builder<T> build(boolean mapOrder) {
            return new Builder<>(mapOrder);
        }


        /**
         * 按升序排序
         *
         * @param prop
         * @return
         */
        public Builder<T> asc(String... prop) {
            this.tmpOrder = null;
            if (prop == null || prop.length < 1) return this;
            if (this.mapOrder) this.comps.add(Orders.byMap((Object[]) prop));
            else if (this.type == null) this.comps.add(Beans.orderByProps(prop));
            else this.comps.add(Beans.orderByProps(this.type, prop));
            return this;
        }

        /**
         * 按降序排序
         *
         * @param prop
         * @return
         */
        public Builder<T> desc(String... prop) {
            this.tmpOrder = null;
            if (prop == null || prop.length < 1) return this;
            if (this.mapOrder) this.comps.add(Orders.byMap((Object[]) prop).reverse());
            else if (this.type == null) this.comps.add(Beans.orderByProps(prop).reverse());
            else this.comps.add(Beans.orderByProps(this.type, prop).reverse());
            return this;
        }

        /**
         * 组合
         *
         * @param comparators
         * @return
         */
        public Builder<T> comp(Comparator<?>... comparators) {
            this.tmpOrder = null;
            if (comparators != null) for (Comparator<?> c : comps) if (c != null) this.comps.add(c);
            return this;
        }

        /**
         * 构建实例
         *
         * @return
         */
        @SuppressWarnings("unchecked")
        public Ordering<T> build() {
            Ordering<T> to = this.tmpOrder;
            if (to == null) {
                int s = this.comps.size();
                if (s < 1) to = (Ordering<T>) Orders.DefaultOrder.DEFAULT;
                else if (s == 1) {
                    Comparator c = this.comps.get(0);
                    to = (c != null) ? Ordering.from(c) : (Ordering<T>) Orders.DefaultOrder.DEFAULT;
                } else to = new Orders.ChainOrder(this.comps);
                this.tmpOrder = to;
            }

            return to;
        }

        /**
         * 工具方法 直接排序
         *
         * @param c
         * @return
         */
        public Builder<T> sort(List<T> c) {
            return sort(c, true);
        }

        /**
         * 工具方法 直接排序
         *
         * @param c
         * @param asc 声明是升序还是降序
         * @return
         */
        @SuppressWarnings("unchecked")
        public Builder<T> sort(List<T> c, boolean asc) {
            if (c == null || c.isEmpty()) return this;

            Ordering<T> ot = this.build();
            if (ot == null) ot = (Ordering<T>) Orders.DefaultOrder.DEFAULT;
            Collections.sort(c, asc ? ot : ot.reverse());
            return this;
        }
    }


}
