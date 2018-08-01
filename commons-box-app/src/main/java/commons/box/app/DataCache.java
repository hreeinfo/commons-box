package commons.box.app;

import com.google.common.cache.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 数据map 通过定义loader来实现值得载入机制
 * <p>
 * 如果定义了max（值大于0时），此类将按需清除部分记录
 *
 * @author xingxiuyi
 * 创建于 14/12/14
 * 版权所有 xingxiuyi
 */
public final class DataCache<K, V> {
    private final LoadingCache<K, DataEntry<K, V>> cache;

    public static <K, V> DataCache<K, V> build() {
        Spec spec = new Spec(-1);
        return build(null, spec);
    }

    public static <K, V> DataCache<K, V> build(DataLoader<K, V> loader) {
        Spec spec = new Spec(-1);
        return build(loader, spec);
    }

    public static <K, V> DataCache<K, V> build(DataLoader<K, V> loader, long max) {
        Spec spec = new Spec(max);
        return build(loader, spec);
    }

    public static <K, V> DataCache<K, V> build(DataLoader<K, V> loader, long max, long expireAfterAccess, long expireAfterWrite) {
        Spec spec = new Spec(max, expireAfterAccess, expireAfterWrite);
        return build(loader, spec);
    }


    public static <K, V> DataCache<K, V> build(DataLoader<K, V> loader, long max, long expireAfterAccess, long expireAfterWrite, int initCapacity) {
        Spec spec = new Spec(max, expireAfterAccess, expireAfterWrite, initCapacity);
        return build(loader, spec);
    }


    public static <K, V> DataCache<K, V> build(DataLoader<K, V> loader, long max,
                                               Consumer<DataEntry<K, V>> removalCmd) {

        Spec spec = new Spec(max);
        return build(loader, spec, removalCmd);
    }


    public static <K, V> DataCache<K, V> build(DataLoader<K, V> loader, long max, long expireAfterAccess, long expireAfterWrite,
                                               Consumer<DataEntry<K, V>> removalCmd) {

        Spec spec = new Spec(max, expireAfterAccess, expireAfterWrite);
        return build(loader, spec, removalCmd);
    }

    public static <K, V> DataCache<K, V> build(DataLoader<K, V> loader, long max, long expireAfterAccess, long expireAfterWrite, int initCapacity,
                                               Consumer<DataEntry<K, V>> removalCmd) {
        Spec spec = new Spec(max, expireAfterAccess, expireAfterWrite, initCapacity);
        return build(loader, spec, removalCmd);
    }

    private static <K, V> DataCache<K, V> build(DataLoader<K, V> loader, Spec spec) {
        return build(loader, spec, null);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> DataCache<K, V> build(DataLoader<K, V> loader, Spec spec, Consumer<DataEntry<K, V>> removalCmd) {
        CacheBuilder<K, DataEntry<K, V>> cb = (CacheBuilder) CacheBuilder.newBuilder();
        if (spec != null) {
            if (spec.max > 0) cb = cb.maximumSize(spec.max);
            if (spec.expireAfterAccess > 0) cb = cb.expireAfterAccess(spec.expireAfterAccess, TimeUnit.MILLISECONDS);
            if (spec.expireAfterWrite > 0) cb = cb.expireAfterWrite(spec.expireAfterWrite, TimeUnit.MILLISECONDS);
            if (spec.initCapacity > 0) cb = cb.initialCapacity(spec.initCapacity);
        }

        if (removalCmd != null) cb = cb.removalListener(new RemovalCmds<>(removalCmd));

        return new DataCache(cb.build(new DataCacheLoader<>(loader)));
    }

    private DataCache(LoadingCache<K, DataEntry<K, V>> cache) {
        this.cache = cache;
    }

    public V get(K key) {
        try {
            DataEntry<K, V> nv = this.cache.get(key);
            if (nv != null) return nv.getValue();
        } catch (Throwable e) {
            throw new AppError("DataCache载入元素出错", e);
        }
        return null;
    }

    public V get(K key, DataLoader<K, V> loader) {
        if (loader == null) return this.get(key);
        DataEntry<K, V> nv = this.cache.getIfPresent(key);
        if (nv == null) {
            nv = new DataEntry<>(key, loader.load(key));
            this.cache.put(key, nv);
        }

        return nv.getValue();
    }

    public V getIfPresent(K key) {
        DataEntry<K, V> nv = this.cache.getIfPresent(key);
        if (nv != null) return nv.getValue();
        else return null;
    }

    public Map<K, V> getAll(Iterable<? extends K> keys) {
        Map<K, V> map = new HashMap<>();
        try {
            Map<K, DataEntry<K, V>> all = this.cache.getAll(keys);
            if (all != null) for (Map.Entry<K, DataEntry<K, V>> me : all.entrySet()) {
                if (me == null || me.getKey() == null || me.getValue() == null) continue;
                map.put(me.getKey(), me.getValue().getValue());
            }
        } catch (Throwable e) {
            throw new AppError("DataCache载入元素出错", e);
        }
        return map;
    }

    public void put(K key, V value) {
        if (key == null) return;
        this.cache.put(key, new DataEntry<>(key, value));
    }

    public void putAll(Map<K, V> map) {
        if (map == null) return;
        for (Map.Entry<K, V> me : map.entrySet()) if (me != null) this.put(me.getKey(), me.getValue());
    }

    public long size() {
        return this.cache.size();
    }

    public void remove(K key) {
        this.cache.invalidate(key);
    }

    public void clear() {
        this.cache.cleanUp();
    }

    public Map<K, V> asMap() {
        Map<K, V> map = new HashMap<>();
        try {
            Map<K, DataEntry<K, V>> all = this.cache.asMap();
            if (all != null) for (Map.Entry<K, DataEntry<K, V>> me : all.entrySet()) {
                if (me == null || me.getKey() == null || me.getValue() == null) continue;
                map.put(me.getKey(), me.getValue().getValue());
            }
        } catch (Throwable e) {
            throw AppError.error("DataCache载入元素出错", e);
        }
        return map;
    }

    private static class DataCacheLoader<K, V> extends CacheLoader<K, DataEntry<K, V>> {
        private final DataLoader<K, V> loader;

        public DataCacheLoader(DataLoader<K, V> loader) {
            this.loader = (loader == null) ? new EmptyLoader<K, V>() : loader;//保证loader不为空
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public DataEntry<K, V> load(K key) throws Exception {
            return new DataEntry<>(key, this.loader.load(key));
        }
    }

    private static class EmptyLoader<K, V> implements DataLoader<K, V> {
        @Override
        public V load(K key) {
            return null;
        }
    }

    private static class RemovalCmds<K, V> implements RemovalListener<K, DataEntry<K, V>> {
        private final Consumer<DataEntry<K, V>> cmd;

        public RemovalCmds(Consumer<DataEntry<K, V>> chain) {
            this.cmd = chain;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public void onRemoval(RemovalNotification<K, DataEntry<K, V>> notification) {
            if (notification == null || notification.getValue() == null) return;
            if (this.cmd != null)
                this.cmd.accept(new DataEntry<>(notification.getKey(), notification.getValue().getValue()));
        }
    }

    /**
     * 缓存特性 各特性值为-1时使用默认值
     */
    private final static class Spec {
        private final long max;
        private final long expireAfterAccess;
        private final long expireAfterWrite;
        private final int initCapacity;

        public Spec(long max) {
            this(max, -1, -1, -1);
        }

        public Spec(long max, long expireAfterAccess, long expireAfterWrite) {
            this(max, expireAfterAccess, expireAfterWrite, -1);
        }

        public Spec(long max, long expireAfterAccess, long expireAfterWrite, int initCapacity) {
            this.max = max;
            this.expireAfterAccess = expireAfterAccess;
            this.expireAfterWrite = expireAfterWrite;
            this.initCapacity = initCapacity;
        }

        public long getMax() {
            return max;
        }

        public long getExpireAfterAccess() {
            return expireAfterAccess;
        }

        public long getExpireAfterWrite() {
            return expireAfterWrite;
        }

        public int getInitCapacity() {
            return initCapacity;
        }
    }

}
