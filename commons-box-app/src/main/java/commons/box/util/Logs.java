package commons.box.util;

import commons.box.app.AppLog;
import commons.box.app.AppLogFactory;
import commons.box.app.SafeRefMap;
import commons.box.app.internal.InternalAppLogFactoryByJDK;
import commons.box.app.internal.InternalServiceLoaders;

import java.util.Map;

/**
 * 通用日志工具 获取日志必须通过此工具类获取 工具类会缓存已知LOG
 * <p>
 * <p>
 * 本工具类使用了 Slf4j
 * <code>
 * compile("org.slf4j:slf4j-api:${version_slf4j}")
 * <p>
 * 默认LOG实现
 * compile("ch.qos.logback:logback-classic:${version_logback}")
 * <p>
 * compile("junit:junit:${version_junit}")
 * </code>
 * <p>
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/10 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Logs {
    private static final Map<String, AppLog> CACHE = new SafeRefMap<>();
    private static final AppLogFactory FACTORY = InternalServiceLoaders.loadService(AppLogFactory.class, new InternalAppLogFactoryByJDK());
    private static final AppLog EMPTY = FACTORY.get(null);

    private Logs() {
    }


    public static AppLog get(Class<?> cls) {
        if (FACTORY == null || cls == null) return EMPTY;
        return get(cls.getName());
    }

    public static AppLog get(String name) {
        if (FACTORY == null || Strs.isEmpty(name)) return EMPTY;

        AppLog log = CACHE.get(name);
        if (log == null) {
            log = FACTORY.get(name);
            if (log == null) log = EMPTY;
            CACHE.put(name, log);
        }
        return log;
    }
}