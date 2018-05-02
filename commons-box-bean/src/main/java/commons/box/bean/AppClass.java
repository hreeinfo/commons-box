package commons.box.bean;

import commons.box.app.AppError;
import commons.box.app.AppLog;
import commons.box.app.SafeRefMap;
import commons.box.bean.internal.InternalClassAccess;
import commons.box.util.Logs;
import commons.box.util.Strs;
import commons.box.util.Types;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class AppClass<T> {
    private static final AppLog LOG = Logs.get(AppClass.class);
    private static final String ASM_CLASS_LOADER_NAME = "commons.box.bean.InternalASMClassAccess";
    private static final Map<String, AppClass<?>> CACHE_DEFAULT = new ConcurrentHashMap<>();
    private static final Map<String, AppClass<?>> CACHE_ASM = new ConcurrentHashMap<>();

    private static final Map<String, AppClass<?>> CACHE_INNER = new SafeRefMap<>(); // TODO 需使用单例 独立的用于缓存临时类的空间


    private static final AccessClassLoader LOADER_DEFAULT = new InternalClassLoader();
    public static AccessClassLoader LOADER_ASM = LOADER_DEFAULT;

    private static final boolean ASM = detectASM();
    private static final AppClass<Object> DEFAULT = new AppClass<>(ASM, Object.class);
    /**
     * 当 app.asm=false 时使用默认机制, 而不是使用ASM
     */
    public static final String ASM_ENABLE = "app.asm";

    /**
     * 检测是否使用asm
     *
     * @return
     */
    private static boolean detectASM() {
        String ad = System.getProperty(ASM_ENABLE);
        if (Strs.isNotBlank(ad) && !Types.isTrue(ad)) return false;

        try {
            Class<?> asmcls = Class.forName(ASM_CLASS_LOADER_NAME);
            boolean ea = (asmcls != null);
            if (ea) LOG.debug("检测到 ClassAccess(ASM) 环境 - 启用基于ASM机制的类访问机制");
            return ea;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 公共方法
     * <p>
     * 返回实例 实例是缓存过的 本方法总是返回非空的值 如果type是空则返回对应Object.class的实例
     *
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    private static AppClass<?> loadFromCache(boolean enableASM, Class<?> type) {
        if (type == null) return DEFAULT;
        String tn = type.getName();
        if (tn == null) return DEFAULT;
        Map<String, AppClass<?>> CACHE = enableASM ? CACHE_ASM : CACHE_DEFAULT;
        if (CACHE == null) return DEFAULT;

        AppClass<?> ai = CACHE.get(tn);
        if (ai == null) {
            if (Strs.contains(tn, Types.CLASS_INNER_SEPARATOR)) { // 匿名类或CGLIB 使用独立的存储机制 且使用默认反射而不是使用asm
                type = Types.getUserClass(type);
                tn = type.getName();
                if (tn == null) return DEFAULT;
                ai = CACHE_INNER.get(tn);
                if (ai == null) {
                    try {
                        ai = new AppClass<>(false, type);
                    } catch (Throwable e) {
                        ai = DEFAULT;
                    }
                    CACHE_INNER.put(tn, ai);
                }
            } else {
                try {
                    ai = new AppClass<>(enableASM, type);
                } catch (Throwable e) {
                    ai = DEFAULT;
                }
                CACHE.put(tn, ai);
            }
        }
        return ai;
    }

    /**
     * 公共方法 获取实例
     *
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <C> AppClass<C> from(boolean enableASM, Class<C> type) {
        return (AppClass<C>) loadFromCache(enableASM, type);
    }

    /**
     * 公共方法 获取实例
     *
     * @param type
     * @return
     */
    public static <C> AppClass<C> from(Class<C> type) {
        return from(ASM, type);
    }

    public static interface AccessClassLoader {
        public <T> InternalClassAccess<T> inst(Class<T> objectClass);
    }

    private static final class InternalClassLoader implements AccessClassLoader {
        private InternalClassLoader() {
        }

        @Override
        public <T> InternalClassAccess<T> inst(Class<T> objectClass) {
            LOG.debug("载入了 ClassAccess(REF) 实例 - " + objectClass);
            return new InternalClassAccess<T>(objectClass);
        }
    }

    private static <T> InternalClassAccess<T> loadinst(boolean enableASM, Class<T> objectClass) {
        AccessClassLoader cl = enableASM ? LOADER_ASM : LOADER_DEFAULT;
        if (cl == null) cl = LOADER_DEFAULT;
        return cl.inst(objectClass);
    }

    private final String name;
    private final InternalClassAccess<T> accessClass;

    private AppClass(boolean enableASM, Class<T> objectClass) throws AppError {
        if (objectClass == null) throw AppError.error("无法创建 AppClass 缺少类型");
        this.name = objectClass.getName();
        this.accessClass = loadinst(enableASM, objectClass);
    }

    /**
     * 类访问 总是非空
     *
     * @return
     */
    public InternalClassAccess<T> access() {
        return accessClass;
    }

    @Override
    public String toString() {
        return "AppClass{" +
                "name='" + name + '\'' +
                ", accessClass=" + accessClass +
                '}';
    }

}
