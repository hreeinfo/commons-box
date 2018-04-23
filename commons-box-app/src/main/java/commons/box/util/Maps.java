package commons.box.util;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 与Map相关的工具类
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/16 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Maps {
    private static final Map<Object, Object> EMPTY_MAP = ImmutableMap.of();

    private Maps() {
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> immmap() {
        return (Map<K, V>) EMPTY_MAP;
    }

    /**
     * 不可变更实例
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> immmap(Map<K, V> map) {
        return immmap(map, false);
    }

    /**
     * 不可变更实例 指定是否按插入顺序来返回结果集(如果linked=true则使用linkedhashmap)
     *
     * @param map
     * @param linked
     * @param <K>
     * @param <V>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> immmap(Map<K, V> map, boolean linked) {
        if (map == null) return (Map<K, V>) EMPTY_MAP;
        return Collections.unmodifiableMap(linked ? new LinkedHashMap<K, V>(map) : new HashMap<K, V>(map));
    }

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Map map) {
        return (map == null) || map.isEmpty();
    }

    /**
     * 判断是否不为空.
     */
    public static boolean isNotEmpty(Map map) {
        return (map != null) && !(map.isEmpty());
    }


    /**
     * 把vmap的值合并到container中
     *
     * @param container
     * @param vmap
     * @param <K>
     * @param <V>
     */
    public static <K, T, V> void combin(Map<K, Map<T, V>> container, Map<K, Map<T, V>> vmap) {
        if (container == null || vmap == null || vmap.isEmpty()) return;

        for (Map.Entry<K, Map<T, V>> vm : vmap.entrySet()) {
            final K vmk = vm.getKey();
            final Map<T, V> vmv = vm.getValue();

            if (vmk == null || vmv == null) continue;

            Map<T, V> cmv = container.get(vmk);
            if (cmv == null) {
                container.put(vmk, vmv);
            } else {
                cmv.putAll(vmv);
            }
        }
    }
}
