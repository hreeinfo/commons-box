package commons.box.app;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 线程安全的Set
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/28 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class SafeSet<E> extends AbstractSet<E> implements Set<E> {
    private static final Object DUMMY = Boolean.TRUE;
    private final ConcurrentMap<E, Object> theMap;


    public SafeSet() {
        theMap = new ConcurrentHashMap<>();
    }


    public SafeSet(int initialCapacity) {
        theMap = new ConcurrentHashMap<>(initialCapacity);
    }

    public SafeSet(int initialCapacity, float loadFactor) {
        theMap = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    public SafeSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        theMap = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    @Override
    public int size() {
        return theMap.size();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<E> iterator() {
        return theMap.keySet().iterator();
    }

    @Override
    public boolean isEmpty() {
        return theMap.isEmpty();
    }

    @Override
    public boolean add(final E o) {
        return theMap.put(o, DUMMY) == null;
    }

    @Override
    public boolean contains(final Object o) {
        return theMap.containsKey(o);
    }

    @Override
    public void clear() {
        theMap.clear();
    }

    @Override
    public boolean remove(final Object o) {
        return theMap.remove(o) == DUMMY;
    }

    public boolean addIfAbsent(final E o) {
        Object obj = theMap.putIfAbsent(o, DUMMY);
        return obj == null;
    }
}
