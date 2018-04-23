package commons.box.app;

/**
 * 数据载入器 用于载入数据
 *
 * @param <K>
 * @param <V>
 */
public interface DataLoader<K, V> {
    public V load(K key);
}
