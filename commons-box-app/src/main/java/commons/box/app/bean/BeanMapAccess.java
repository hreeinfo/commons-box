package commons.box.app.bean;

import java.util.Map;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface BeanMapAccess extends BeanAccess {
    /**
     * 额外构建
     *
     * @param keys
     * @return
     */
    public Map<String, Object> inst2(String[] keys);

    /**
     * 额外构建
     *
     * @param linked
     * @return
     */
    public Map<String, Object> inst2(boolean linked);

    /**
     * 额外构建
     *
     * @return
     */
    public Map<String, Object> inst2();
}
