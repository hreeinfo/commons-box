package commons.box.util;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 与Map相关的工具类
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Maps {
    private static final Map<Object, Object> EMPTY_MAP = ImmutableMap.of();

    private Maps() {
    }

    @SuppressWarnings("unchecked")
    @Nonnull
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
    @SuppressWarnings("unchecked")
    @Nonnull
    public static <K, V> Map<K, V> immmap(Map<K, V> map) {
        if (map == null) return (Map<K, V>) EMPTY_MAP;
        return Collections.unmodifiableMap(map);
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

    /**
     * 当找到 map 的 value 后执行操作
     *
     * @param map
     * @param key
     * @param onValue
     * @param <K>
     * @param <V>
     * @return 找到目标并执行了操作 返回 true 否则返回 false
     */
    public static <K, V> boolean get(Map<K, V> map, K key, Consumer<V> onValue) {
        if (map == null || key == null || onValue == null) return false;

        if (map.containsKey(key)) {
            V value = map.get(key);
            onValue.accept(value);
            return true;
        }

        return false;
    }
}
