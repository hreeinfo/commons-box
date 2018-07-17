package commons.box.app.bean;


import commons.box.app.MetaBean;
import commons.box.app.MetaBeanConfig;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface BeanMetaAccess extends BeanAccess {
    /**
     * 额外构建
     *
     * @param configID
     * @return
     */
    public MetaBean inst2(String configID);

    /**
     * 额外构建
     *
     * @param config
     * @return
     */
    public MetaBean inst2(MetaBeanConfig config);
}
