package commons.box.app.internal.log;

import commons.box.app.AppLog;
import commons.box.app.AppLogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Slf4j实现，优先级1000，覆盖原内置的JDK日志
 * <p>
 * 如果需要覆盖此配置，需要声明优先级大于1000
 *
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalAppLogFactoryBySlf4j implements AppLogFactory {
    private static final String EMPTY_LOG = "APP";

    @Override
    public int priority() {
        return 1000;
    }

    @Override
    public AppLog get(String name) {
        if (name == null) name = EMPTY_LOG;
        Logger logger = LoggerFactory.getLogger(name);
        return new InternalAppLogBySlf4j(name, logger);
    }
}
