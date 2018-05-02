package commons.box.bean.internal;

import commons.box.app.AppError;
import commons.box.bean.BeanMetaAccess;
import commons.box.bean.MetaBean;
import commons.box.bean.MetaBeanConfig;

/**
 * // TODO
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalBeanMetaAccess implements BeanMetaAccess {
    public static final InternalBeanMetaAccess INSTANCE = new InternalBeanMetaAccess();

    private InternalBeanMetaAccess() {
    }

    @Override
    public <T> T inst(Class<T> type) throws AppError {
        return null;
    }

    @Override
    public MetaBean inst2(String configID) {
        return null;
    }

    @Override
    public MetaBean inst2(MetaBeanConfig config) {
        return null;
    }


    @Override
    public <T, O> O prop(T bean, String property) throws AppError {
        return null;
    }

    @Override
    public <T, O> void prop(T bean, String property, O value) throws AppError {

    }

    @Override
    public <T, O> O field(T bean, String name) throws AppError {
        return null;
    }

    @Override
    public <T, O> void field(T bean, String name, O value) throws AppError {

    }

    @Override
    public <T, R> R invoke(T bean, String method, Object... args) throws Throwable {
        return null;
    }

    @Override
    public <T> boolean has(T bean, String name) {
        return false;
    }

    @Override
    public <T> boolean hasField(T bean, String name) {
        return false;
    }

    @Override
    public <T> boolean canInvoke(T bean, String method, Object... args) {
        return false;
    }

    @Override
    public <T> String[] props(T bean) {
        return new String[0];
    }

    @Override
    public <T> String[] fields(T bean) {
        return new String[0];
    }

    // TODO 需要实现此方法以便能处理bean的属性类型获取
    @Override
    public <T> Class<?> type(T bean, String prop) {
        throw AppError.error("未实现此机制");
    }
}
