package commons.box.app.internal;

import commons.box.app.AppLog;
import commons.box.app.AppLogFactory;

import java.util.logging.Logger;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/21 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalAppLogFactoryByJDK implements AppLogFactory {
    private static final String EMPTY_LOG = "APP";

    @Override
    public int priority() {
        return Integer.MAX_VALUE; // 优先级最低
    }

    @Override
    public AppLog get(String name) {
        if (name == null) name = EMPTY_LOG;
        Logger logger = Logger.getLogger(name);
        return new InternalAppLogByJDK(name, logger);
    }
}
