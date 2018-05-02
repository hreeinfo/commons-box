package commons.box.app.test;

import commons.box.app.SafeLinkedMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class MapsTests {
    @Test
    @DisplayName("测试插入获取")
    public void testPutGet() {
        SafeLinkedMap<String, Object> m = new SafeLinkedMap.Builder<String, Object>()
                .maximumWeightedCapacity(10).build();

        assertEquals(0, m.size());
        m.put("k1", 100);
        assertEquals(1, m.size());
        assertNull(m.get("nosuchkey"));
        assertEquals(100, m.get("k1"));

        m.put("k2", 200);
        assertEquals(2, m.size());
        assertEquals(200, m.get("k2"));
    }

    @Test
    @DisplayName("测试LRU机制")
    public void testLRU() {
        SafeLinkedMap<String, Object> m = new SafeLinkedMap.Builder<String, Object>()
                .maximumWeightedCapacity(5).build();

        assertEquals(0, m.size());
        m.put("k1", 100);
        assertEquals(1, m.size());
        m.put("k2", 101);
        assertEquals(2, m.size());
        m.put("k3", 102);
        assertEquals(3, m.size());
        m.put("k4", 103);
        assertEquals(4, m.size());
        m.put("k5", 104);
        assertEquals(5, m.size());
        m.put("k6", 105);
        assertEquals(5, m.size());
        m.put("k7", 106);
        assertEquals(5, m.size());
        m.put("k8", 107);
        assertEquals(5, m.size());

        m.remove("k6");
        assertEquals(4, m.size());
    }
}
