package commons.box.app;

import commons.box.util.Collects;
import commons.box.util.Logs;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 单例模式对象工厂
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class AppSingletonFactory {
    private static final AppLog LOG = Logs.get(AppSingletonFactory.class);
    private static final Object NULL_OBJECT = new Object();

    private final Map<String, Supplier<?>> providers = new SafeMap<>();
    private final Map<String, Object> objects = new SafeMap<>();


    /**
     * 获取实例
     *
     * @return
     */
    public static AppSingletonFactory inst() {
        return new AppSingletonFactory();
    }

    private AppSingletonFactory() {
    }

    @SuppressWarnings("unchecked")
    private <T> T loadByProvider(String name) {
        if (name == null) return null;
        Supplier<T> op = (Supplier<T>) this.providers.get(name);
        if (op != null) return op.get();
        return null;
    }


    /**
     * 载入单例对象
     * <p>
     * 当载入的单例对象是 NULL_OBJECT 且 forceLoad=true 时会尝试再次载入
     *
     * @param name
     * @param forceLoad
     * @param provider
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T doGet(String name, boolean forceLoad, Supplier<T> provider) {
        if (name == null) return null;

        Object obj = this.objects.get(name);
        if (forceLoad && obj == NULL_OBJECT) obj = null;
        if (obj == null) {
            synchronized (this.objects) {
                obj = this.objects.get(name);
                if (forceLoad && obj == NULL_OBJECT) obj = null;
                if (obj == null) {
                    if (provider != null) obj = provider.get();

                    if (obj == null) obj = NULL_OBJECT;
                    this.objects.put(name, obj);
                }
            }
        }

        return obj == NULL_OBJECT ? null : (T) obj;
    }

    public Set<String> names() {
        Set<String> ns = new LinkedHashSet<>();

        ns.addAll(this.providers.keySet());
        ns.addAll(this.objects.keySet());

        return Collects.immset(ns);
    }

    /**
     * 增加provider
     *
     * @param name
     * @param provider
     */
    public void provider(String name, Supplier<?> provider) {
        if (name == null || provider == null) return;
        synchronized (this.objects) { // TODO 验证此处保持同步是否影响性能
            this.providers.put(name, provider);
        }
    }

    public void add(String name, Object object, boolean overwrite) {
        if (name == null || object == null) return;
        synchronized (this.objects) { // TODO 验证此处保持同步是否影响性能
            if (overwrite) this.objects.put(name, object);
            else if (!this.objects.containsKey(name)) this.objects.put(name, object);
        }
    }

    /**
     * 获取单例对象 指定对象类型 如果对象不存在则使用给定的provider获取目标对象
     * <p>
     * 如果provider为空，则尝试使用类的默认构建方法构造对象
     *
     * @param name
     * @param type
     * @param provider
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String name, final Class<T> type, final Supplier<T> provider) {
        return this.get(name, type, false, provider);
    }

    /**
     * 获取单例对象 指定对象类型 如果对象不存在则使用给定的provider获取目标对象
     * <p>
     * 如果provider为空，则尝试使用类的默认构建方法构造对象
     *
     * @param name
     * @param type
     * @param forceReload 强制重新更新对象，使用新实例
     * @param provider
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String name, final Class<T> type, boolean forceReload, final Supplier<T> provider) {
        Object obj = this.doGet(name, forceReload, provider);
        if (obj == null || obj == NULL_OBJECT) {
            // 二次检查 尝试使用默认构建方式构建
            //obj = this.doGet(name, true, () -> this.newInstance(type));
            //if (obj == null || obj == NULL_OBJECT) return null;
            return null;
        }

        if (type != null) {
            if (type.isInstance(obj)) return (T) obj;
            throw new ClassCastException("生成的对象不兼容类型 " + type.getName());
        }

        return (T) obj;
    }

    public <T> T get(final String name, final Class<T> type) {
        return this.get(name, type, () -> this.loadByProvider(name));
    }


    /**
     * 获取单例对象 如果对象不存在则使用给定的provider获取目标对象
     *
     * @param name
     * @param provider
     * @return
     */
    public <T> T get(final String name, final Supplier<T> provider) {
        return this.doGet(name, false, provider);
    }

    /**
     * 获取单例对象 如果对象不存在则使用给定的provider获取目标对象
     * <p>
     * 判断是否强制重载
     *
     * @param name
     * @param forceReload
     * @param provider
     * @param <T>
     * @return
     */
    public <T> T get(final String name, boolean forceReload, final Supplier<T> provider) {
        return this.doGet(name, forceReload, provider);
    }

    /**
     * 获取单例对象 如果定义了name对应的provider则使用它构造对象并返回
     * <p>
     * 如果provider未定义，返回空
     *
     * @param name
     * @return
     */
    public Object get(String name) {
        return this.get(name, () -> this.loadByProvider(name));
    }
}
