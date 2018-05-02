package commons.box.bean;


import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.bean.internal.InternalBeanMapAccess;
import commons.box.bean.internal.InternalBeanMetaAccess;
import commons.box.bean.internal.InternalBeanObjectAccess;
import commons.box.util.Collects;
import commons.box.util.Logs;
import commons.box.util.Strs;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 适应各种类型的bean operator实例 用于直接操作对象
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
@SuppressWarnings("unchecked")
public class AppBean implements BeanAccess {
    private final static AppLog LOG = Logs.get(AppBean.class);

    private final static BeanMetaAccess DEFAULT_META_BEAN_OP = InternalBeanMetaAccess.INSTANCE;
    private final static BeanMapAccess DEFAULT_MAP_BEAN_OP = InternalBeanMapAccess.INSTANCE;
    private final static BeanObjectAccess DEFAULT_OBJECT_BEAN_OP = InternalBeanObjectAccess.INSTANCE;

    private final static String[] EMPTY_NAMES = new String[]{};
    public static final String[] EMPTY_STRS = {};
    public static final Object[] EMPTY_ARRAY = {};
    public static final String DOT = ".";
    public static final String SP_DOT = "\\.";

    private final static AppBean BEAN_OP = new AppBean(true);
    private final static AppBean BEAN_SAFE_OP = new AppBean(false);

    /**
     * 返回实例(全局)
     * <p>
     * throwError表示在操作时发生异常是否抛出 如果不抛出则仅记录error日志
     *
     * @param throwError
     * @return
     */
    public final static AppBean inst(boolean throwError) {
        return throwError ? BEAN_OP : BEAN_SAFE_OP;
    }

    /**
     * 返回实例(全局)
     *
     * @return
     */
    public final static AppBean inst() {
        return BEAN_OP;
    }

    /**
     * 返回新创建的实例 (与inst方法不同,本方法每次调用均返回新创建的实例对象)
     * <p>
     * throwError表示在操作时发生异常是否抛出 如果不抛出则仅记录error日志
     *
     * @param throwError
     * @return
     */
    public static AppBean newinst(boolean throwError) {
        return new AppBean(throwError);
    }

    private final boolean throwError;

    private BeanMetaAccess metaBOP = DEFAULT_META_BEAN_OP;
    private BeanMapAccess mapBOP = DEFAULT_MAP_BEAN_OP;
    private BeanObjectAccess objectBOP = DEFAULT_OBJECT_BEAN_OP;

    public AppBean(boolean throwError) {
        this.throwError = throwError;
    }

    /**
     * 根据对象获取目标
     *
     * @param instance
     * @return 根据类型判断实例 总是不为空
     */
    public BeanAccess from(Object instance) {
        if (instance == null) return this.baObject();
        else if (instance instanceof MetaBean) return this.baMeta();
        else if (instance instanceof Map) return this.baMap();
        else if (instance instanceof Class) return this.fromType((Class<?>) instance);
        else return this.baObject();
    }

    /**
     * 根据类获取目标
     *
     * @param type
     * @return 根据类型判断实例 总是不为空
     */
    public BeanAccess fromType(Class<?> type) {
        if (type == null) return this.baObject();
        else if (MetaBean.class.isAssignableFrom(type)) return this.baMeta();
        else if (Map.class.isAssignableFrom(type)) return this.baMap();
        else return this.baObject();
    }

    private BeanMetaAccess baMeta() {
        BeanMetaAccess bop = this.metaBOP;
        if (bop == null) return DEFAULT_META_BEAN_OP;
        return bop;
    }

    private BeanMapAccess baMap() {
        BeanMapAccess bop = this.mapBOP;
        if (bop == null) return DEFAULT_MAP_BEAN_OP;
        return bop;
    }

    private BeanObjectAccess baObject() {
        BeanObjectAccess bop = this.objectBOP;
        if (bop == null) return DEFAULT_OBJECT_BEAN_OP;
        return bop;
    }

    @Override
    public <T> T inst(Class<T> type) throws AppError {
        if (this.throwError) {
            return this.fromType(type).inst(type);
        } else {
            try {
                return this.fromType(type).inst(type);
            } catch (Throwable e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 可用于替换其它实现
     *
     * @param bop
     */
    public void setBeanMapAccess(BeanMapAccess bop) {
        if (bop == null) bop = DEFAULT_MAP_BEAN_OP;
        this.mapBOP = bop;
    }

    /**
     * 可用于替换其它实现
     *
     * @param bop
     */
    public void setBeanObjectAccess(BeanObjectAccess bop) {
        if (bop == null) bop = DEFAULT_OBJECT_BEAN_OP;
        this.objectBOP = bop;
    }

    /**
     * 可用于替换其它实现
     *
     * @param bop
     */
    public void setBeanMetaAccess(BeanMetaAccess bop) {
        if (bop == null) bop = DEFAULT_META_BEAN_OP;
        this.metaBOP = bop;

    }

    @Override
    public <T, O> O prop(T bean, String property) throws AppError {
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            return this.from(bean).prop(bean, property);
        } else {
            if (bean == null) return null;
            try {
                return this.from(bean).prop(bean, property);
            } catch (Throwable e) {
                LOG.debug(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public <T, O> void prop(T bean, String property, O value) throws AppError {
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            this.from(bean).prop(bean, property, value);
        } else {
            if (bean == null) return;
            try {
                this.from(bean).prop(bean, property, value);
            } catch (Throwable e) {
                LOG.warn(e.getMessage(), e);
            }
        }
    }

    @Override
    public <T, O> O field(T bean, String name) throws AppError {
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            return this.from(bean).field(bean, name);
        } else {
            if (bean == null) return null;
            try {
                return this.from(bean).field(bean, name);
            } catch (Throwable e) {
                LOG.debug(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public <T, O> void field(T bean, String name, O value) throws AppError {
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            this.from(bean).field(bean, name, value);
        } else {
            if (bean == null) return;
            try {
                this.from(bean).field(bean, name, value);
            } catch (Throwable e) {
                LOG.warn(e.getMessage(), e);
            }
        }
    }

    @Override
    public <T, R> R invoke(T bean, String method, Object... args) throws Throwable {
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            return this.from(bean).invoke(bean, method, args);
        } else {
            if (bean == null) return null;
            try {
                return this.from(bean).invoke(bean, method, args);
            } catch (Throwable e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public <T> boolean has(T bean, String name) {
        if (bean == null) return false;
        return this.from(bean).has(bean, name);
    }

    @Override
    public <T> boolean hasField(T bean, String name) {
        if (bean == null) return false;
        return this.from(bean).hasField(bean, name);
    }

    @Override
    public <T> boolean canInvoke(T bean, String method, Object... args) {
        if (bean == null) return false;
        return this.from(bean).canInvoke(bean, method, args);
    }

    @Override
    public <T> String[] props(T bean) {
        if (bean == null) return EMPTY_NAMES;
        return this.from(bean).props(bean);
    }

    @Override
    public <T> String[] fields(T bean) {
        if (bean == null) return EMPTY_NAMES;
        return this.from(bean).fields(bean);
    }

    /**
     * 获取值 与prop方法不同的是支持多级属性操作 如果props是多个元素 表示获取级联下级对象值
     *
     * @param bean
     * @param prop 属性名 如果是多个值表示级联属性
     * @param <T>
     * @param <O>
     * @return
     * @throws AppError
     */
    public <T, O> O get(T bean, String prop) throws AppError {
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            return this.get(this.from(bean), bean, prop);
        } else {
            if (bean == null) return null;
            try {
                return this.get(this.from(bean), bean, prop);
            } catch (Throwable e) {
                LOG.debug(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 获取多个属性值  多级属性使用.分隔 表示获取级联下级对象值
     *
     * @param bean
     * @param props 属性名数组 多个属性对应所需获取的多个字段
     * @param <T>
     * @return
     * @throws AppError
     */
    public <T> Map<String, Object> mget(T bean, String... props) throws AppError {
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            BeanAccess bo = this.from(bean);
            Map<String, Object> map = new LinkedHashMap<>();
            if (props != null) for (String p : props) map.put(p, this.get(bo, bean, p));
            return map;
        } else {
            if (bean == null) return null;
            BeanAccess bo = this.from(bean);
            Map<String, Object> map = new LinkedHashMap<>();

            if (props != null) for (String p : props) {
                try {
                    map.put(p, this.get(bo, bean, p));
                } catch (Throwable e) {
                    LOG.debug(e.getMessage(), e);
                }
            }

            return map;
        }
    }


    /**
     * 设置属性 与prop方法不同的是支持多级属性操作 如果props是多个元素 表示设置级联下级对象值 本方法自动创建中间缺失的对象 等同于autocreate=true
     *
     * @param <T>
     * @param <O>
     * @return
     * @throws AppError
     */
    public <T, O> AppBean set(T bean, String prop, O value) throws AppError {
        return this.set(bean, prop, value, true);
    }


    /**
     * 设置级联属性 与prop方法不同的是支持多级属性操作 如果props是多个元素 表示获取级联下级对象值
     * <p>
     * autocreate 用于控制是否 多级属性时要设置的中间值属性不存在则尝试创建
     *
     * @param bean
     * @param prop
     * @param value
     * @param autocreate 在多级属性时要设置的中间值属性不存在则尝试创建
     * @param <T>
     * @param <O>
     * @return
     * @throws AppError
     */
    public <T, O> AppBean set(T bean, String prop, O value, boolean autocreate) throws AppError {
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            this.set(this.from(bean), bean, prop, value, autocreate);
        } else {
            if (bean == null) return null;
            try {
                this.set(this.from(bean), bean, prop, value, autocreate);
            } catch (Throwable e) {
                LOG.warn(e.getMessage(), e);
            }
        }
        return this;
    }


    /**
     * 设置多个属性值 多级属性使用.分隔 表示获取级联下级对象值
     *
     * @param bean
     * @param props
     * @param <T>
     * @return
     * @throws AppError
     */
    public <T> AppBean mset(T bean, Map<String, Object> props) throws AppError {
        return this.mset(bean, props, true);
    }


    /**
     * 设置多个属性值 多级属性使用.分隔 表示获取级联下级对象值 多级属性可以使用autocreate控制
     *
     * @param bean
     * @param props
     * @param autocreate
     * @param <T>
     * @return
     * @throws AppError
     */
    public <T> AppBean mset(T bean, Map<String, Object> props, boolean autocreate) throws AppError {// TODO
        if (this.throwError) {
            if (bean == null) throw AppError.error("操作目标对象为空 无法操作");
            BeanAccess bo = this.from(bean);
            if (props != null) for (Map.Entry<String, ?> me : props.entrySet()) {
                this.set(bo, bean, me.getKey(), me.getValue(), autocreate);
            }
        } else {
            if (bean == null) return null;
            BeanAccess bo = this.from(bean);
            if (props != null) for (Map.Entry<String, ?> me : props.entrySet()) {
                try {
                    this.set(bo, bean, me.getKey(), me.getValue(), autocreate);
                } catch (Throwable e) {
                    LOG.warn(e.getMessage(), e);
                }
            }
        }
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    private <T, O> O get(BeanAccess bo, T bean, String prop) throws AppError {
        if (bo == null || bean == null || prop == null) return null;

        if (prop.contains(DOT)) {// 多级属性
            String[] props = Strs.split(prop, SP_DOT);

            if (props == null || props.length < 2) return bo.prop(bean, prop); // 仍使用基本属性prop
            else {
                Object pto = bean;
                BeanAccess pbo = bo;

                for (int i = 0; i < props.length; i++) {
                    String pn = props[i];

                    Object prevo = pto;
                    pto = pbo.prop(prevo, pn);
                    if (pto == null) return null;

                    pbo = from(pto);
                    if (pbo == null) return null;
                }

                return (O) pto;
            }
        } else return bo.prop(bean, prop);
    }

    private <T, O> void set(BeanAccess bo, T bean, String prop, O value, boolean autocreate) throws AppError {
        if (bo == null || bean == null || prop == null) return;

        if (prop.contains(DOT)) { // 多级属性
            String[] props = Strs.split(prop, SP_DOT);

            if (props.length < 2) bo.prop(bean, prop, value); // 仍使用基本属性prop
            else {
                Object pto = bean;
                BeanAccess pbo = bo;

                int lget = props.length - 1;

                for (int i = 0; i < lget; i++) {
                    String pn = props[i];
                    Object prevo = pto;
                    pto = pbo.prop(prevo, pn);

                    if (pto == null && autocreate) {
                        Class<?> ptt = pbo.type(prevo, pn);
                        BeanAccess tmpbo = fromType(ptt);
                        if (tmpbo == null) throw AppError.error("类型 [" + bean.getClass() + "] 未能找到构建 " + prop);
                        pto = tmpbo.inst(ptt);
                        if (pto != null) pbo.prop(prevo, pn, pto);
                    }

                    // 即使自动创建后仍然为空 则抛出异常
                    if (pto == null) throw AppError.error("类型 [" + bean.getClass() + "] 未能设置属性 " + prop);

                    pbo = from(pto);
                    if (pbo == null) throw AppError.error("类型 [" + bean.getClass() + "] 未能操作属性 " + prop);
                }

                pbo.prop(pto, props[lget], value);
            }
        } else bo.prop(bean, prop, value);
    }

    @Override
    public <T> Class<?> type(T bean, String prop) {
        return from(bean).type(bean, prop);
    }

    /**
     * 根据含.的多级属性生成分级的数组 用于多级数据的查找或设置
     * 总是不为空
     *
     * @param prop
     * @return
     */
    @SuppressWarnings("ConstantConditions")
    public static String[] split(String prop) {
        if (prop == null || prop.length() < 1) return EMPTY_STRS;
        if (!prop.contains(DOT)) return new String[]{prop};
        String[] props = Strs.split(prop, SP_DOT);
        return (props == null) ? EMPTY_STRS : props;
    }

    /**
     * 判断props是否包含多级属性
     *
     * @param props
     * @return
     */
    public static boolean hasInnerProps(String... props) {
        if (Collects.isEmpty(props)) return false;
        for (String p : props) if (Strs.contains(p, DOT)) return true;
        return false;
    }
}
