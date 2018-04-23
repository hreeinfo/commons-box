package commons.box.app.internal.log;

import commons.box.app.internal.InternalAppLog;
import org.slf4j.Logger;

import static commons.box.app.AppLog.LogLevel.*;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/6/10 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class InternalAppLogBySlf4j extends InternalAppLog {

    private final String name;
    private final Logger logger;
    private final LogLevel level;

    InternalAppLogBySlf4j(String name, Logger logger) {
        this.name = name;
        this.logger = logger;

        if (this.logger == null) this.level = NONE;
        else {
            if (this.logger.isTraceEnabled()) this.level = TRACE;
            else if (this.logger.isDebugEnabled()) this.level = DEBUG;
            else if (this.logger.isInfoEnabled()) this.level = INFO;
            else if (this.logger.isWarnEnabled()) this.level = WARN;
            else if (this.logger.isErrorEnabled()) this.level = ERROR;
            else this.level = NONE;
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public LogLevel level() {
        return level;
    }


    protected void doLog(LogLevel logLevel, String msg, Throwable es, Object... params) {
        if (this.logger == null || logLevel == null || !this.isEnabled(logLevel)) return;
        String text = msg;
        if (params != null && params.length > 0) text = String.format(((msg != null) ? msg : ""), params);
        switch (logLevel) {
            case ERROR:
                if (es == null) this.logger.error(text);
                else this.logger.error(text, es);
                break;
            case WARN:
                if (es == null) this.logger.warn(text);
                else this.logger.warn(text, es);
                break;
            case INFO:
                if (es == null) this.logger.info(text);
                else this.logger.info(text, es);
                break;
            case DEBUG:
                if (es == null) this.logger.debug(text);
                else this.logger.debug(text, es);
                break;
            case TRACE:
                if (es == null) this.logger.trace(text);
                else this.logger.trace(text, es);
                break;
            case NONE:
                break;
            default:
                break;
        }
    }
}
