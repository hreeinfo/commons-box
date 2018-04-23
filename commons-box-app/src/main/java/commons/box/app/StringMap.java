package commons.box.app;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 执行是否大小写无关的Map 有序 继承自LinkedHashMap
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/15 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class StringMap<V> extends LinkedHashMap<String, V> {
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private final boolean caseInsensitive;
    private final Map<String, String> caseInsensitiveKeys;
    private final Locale locale;

    /**
     * KEY是否大小写无关
     *
     * @param caseInsensitive
     */
    public StringMap(boolean caseInsensitive) {
        this(caseInsensitive, null);
    }

    /**
     * KEY是否大小写无关
     *
     * @param caseInsensitive
     * @param locale
     */
    public StringMap(boolean caseInsensitive, Locale locale) {
        super();
        this.caseInsensitive = caseInsensitive;
        this.caseInsensitiveKeys = (this.caseInsensitive) ? new HashMap<>() : null;
        this.locale = (locale != null ? locale : DEFAULT_LOCALE);
    }

    public StringMap(int initialCapacity, boolean caseInsensitive) {
        this(initialCapacity, caseInsensitive, null);
    }

    public StringMap(int initialCapacity, boolean caseInsensitive, Locale locale) {
        super(initialCapacity);
        this.caseInsensitive = caseInsensitive;
        this.caseInsensitiveKeys = (this.caseInsensitive) ? new HashMap<>(initialCapacity) : null;
        this.locale = (locale != null ? locale : DEFAULT_LOCALE);
    }


    @Override
    public V put(String key, V value) {
        if (key == null) return null;
        if (this.caseInsensitive && this.caseInsensitiveKeys != null) {
            String oldKey = this.caseInsensitiveKeys.put(convertKey(key), key);
            if (oldKey != null && !oldKey.equals(key)) super.remove(oldKey);
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        if (map.isEmpty()) return;
        for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) put(entry.getKey(), entry.getValue());
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) return false;
        if (this.caseInsensitive && this.caseInsensitiveKeys != null) return (key instanceof String && this.caseInsensitiveKeys.containsKey(convertKey((String) key)));
        else return super.containsKey(key);
    }

    @Override
    public V get(Object key) {
        if (key == null) return null;
        if (key instanceof String) {
            if (this.caseInsensitive && this.caseInsensitiveKeys != null) {
                String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String) key));
                if (caseInsensitiveKey != null) return super.get(caseInsensitiveKey);
            } else return super.get(key);
        }
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        if (key == null) return defaultValue;
        if (key instanceof String) {
            if (this.caseInsensitive && this.caseInsensitiveKeys != null) {
                String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String) key));
                if (caseInsensitiveKey != null) return super.get(caseInsensitiveKey);
            } else return super.getOrDefault(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public V remove(Object key) {
        if (key == null) return null;
        if (key instanceof String) {
            if (this.caseInsensitive && this.caseInsensitiveKeys != null) {
                String caseInsensitiveKey = this.caseInsensitiveKeys.remove(convertKey((String) key));
                if (caseInsensitiveKey != null) return super.remove(caseInsensitiveKey);
            } else return super.remove(key);
        }
        return null;
    }

    @Override
    public void clear() {
        if (this.caseInsensitiveKeys != null) this.caseInsensitiveKeys.clear();
        super.clear();
    }

    private String convertKey(String key) {
        if (key == null) return null;
        if (this.caseInsensitive) return key.toLowerCase(this.locale);
        else return key;
    }
}
