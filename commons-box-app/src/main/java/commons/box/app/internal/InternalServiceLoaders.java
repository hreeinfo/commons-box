package commons.box.app.internal;

import commons.box.app.AppLogFactory;
import commons.box.app.Priority;
import commons.box.util.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/7/3 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalServiceLoaders {
    private static final List EMPTY_SERVICES = Collections.EMPTY_LIST;

    /**
     * 获取系统定义的services 通过 ServiceLoader 载入配置的实例
     * <p>
     * 如果实现了 Priority 接口，值越小优先级越高
     *
     * @param type
     * @param defaultValue
     * @param <S>
     * @return
     */
    @SuppressWarnings("ConstantConditions")
    public static <S> S loadService(Class<S> type, S defaultValue) {
        if (type == null) return defaultValue;
        try {
            ServiceLoader<S> serviceLoader = ServiceLoader.load(type);

            S service = null;

            for (S srv : serviceLoader) {
                if (srv == null) continue;

                if (service == null) service = srv;

                if (Priority.class.isAssignableFrom(type)) {
                    if (!(srv instanceof Priority)) break;
                    if (!(service instanceof Priority)) break;


                    if (((Priority) srv).priority() < ((Priority) service).priority()) service = srv;
                } else break; // 默认只取第一个元素
            }

            return (service != null) ? service : defaultValue;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static <S> S loadService(Class<S> type) {
        return loadService(type, null);
    }

    /**
     * 获取已注册的全部服务
     *
     * @param type
     * @param <S>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S> List<S> loadServices(Class<S> type) {
        if (type == null) return (List<S>) EMPTY_SERVICES;
        List<S> services = new ArrayList<S>();

        try {
            ServiceLoader<S> serviceLoader = ServiceLoader.load(type);
            for (S srv : serviceLoader) {
                if (srv == null) continue;
                services.add(srv);
            }
        } catch (Throwable ignored) {
        }

        if (Types.isType(Comparable.class, type)) try {
            Collections.sort((List<Comparable>) services);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return services;
    }

    public static void main(String[] args) {
        System.out.println(Priority.class.isAssignableFrom(AppLogFactory.class));
    }
}
