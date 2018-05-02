package commons.box.app;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class SafeLinkedSet<E> extends AbstractSet<E> implements Set<E> {
    private static final Object DUMMY = Boolean.TRUE;
    private final Map<E, Object> theMap;


    public SafeLinkedSet() {
        theMap = new ConcurrentHashMap<>();
    }


    public SafeLinkedSet(int initialCapacity) {
        theMap = new SafeLinkedMap.Builder<E, Object>().initialCapacity(initialCapacity).build();
    }

    public SafeLinkedSet(int initialCapacity, float loadFactor) {
        theMap = new SafeLinkedMap.Builder<E, Object>().initialCapacity(initialCapacity).build();
    }

    public SafeLinkedSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        theMap = new SafeLinkedMap.Builder<E, Object>().initialCapacity(initialCapacity).concurrencyLevel(concurrencyLevel).build();
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
