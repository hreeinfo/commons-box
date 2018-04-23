package commons.box.app;

import java.util.Map;

/**
 * 元素节点
 *
 * @param <V>
 */
public final class DataEntry<K, V> implements Map.Entry<K, V>, DataObject {
    private static final long serialVersionUID = 9069687761676127614L;

    private final K key;
    private V value;

    public DataEntry(K key) {
        this.key = key;
    }

    public DataEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}
