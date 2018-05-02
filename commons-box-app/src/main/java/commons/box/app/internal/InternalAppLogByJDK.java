package commons.box.app.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import static commons.box.app.AppLog.LogLevel.*;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalAppLogByJDK extends InternalAppLog {
    private final String name;
    private final Logger logger;
    private final LogLevel level;

    InternalAppLogByJDK(String name, Logger logger) {
        this.name = name;
        this.logger = logger;

        if (this.logger == null) this.level = NONE;
        else {
            if (this.logger.isLoggable(Level.FINEST) || this.logger.isLoggable(Level.FINER)) this.level = TRACE;
            else if (this.logger.isLoggable(Level.FINE) || this.logger.isLoggable(Level.CONFIG)) this.level = DEBUG;
            else if (this.logger.isLoggable(Level.INFO)) this.level = INFO;
            else if (this.logger.isLoggable(Level.WARNING)) this.level = WARN;
            else if (this.logger.isLoggable(Level.SEVERE)) this.level = ERROR;
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
                if (es == null) this.logger.log(Level.SEVERE, text);
                else this.logger.log(Level.SEVERE, text, es);
                break;
            case WARN:
                if (es == null) this.logger.log(Level.WARNING, text);
                else this.logger.log(Level.WARNING, text, es);
                break;
            case INFO:
                if (es == null) this.logger.log(Level.INFO, text);
                else this.logger.log(Level.INFO, text, es);
                break;
            case DEBUG:
                if (es == null) this.logger.log(Level.FINE, text);
                else this.logger.log(Level.FINE, text, es);
                break;
            case TRACE:
                if (es == null) this.logger.log(Level.FINEST, text);
                else this.logger.log(Level.FINEST, text, es);
                break;
            case NONE:
                break;
            default:
                break;
        }
    }
}
