package commons.box.app;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class SafeMap<K, V> extends ConcurrentHashMap<K, V> {
    public SafeMap() {
    }

    public SafeMap(int initialCapacity) {
        super(initialCapacity);
    }

    public SafeMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public SafeMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public SafeMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(initialCapacity, loadFactor, concurrencyLevel);
    }
}
