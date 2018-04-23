package commons.box.app.internal;

import commons.box.app.AppLog;

import java.util.function.Supplier;

/**
 * AppLog 基础实现
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/21 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public abstract class InternalAppLog implements AppLog {
    @Override
    public abstract String name();

    @Override
    public abstract LogLevel level();

    @Override
    public void error(String msg, Throwable es) {
        log(LogLevel.ERROR, msg, es);
    }

    @Override
    public void error(String msg, Object... params) {
        log(LogLevel.ERROR, msg, params);
    }

    @Override
    public void error(Throwable es) {
        log(LogLevel.ERROR, es);
    }

    @Override
    public void error(Supplier<String> msg) {
        log(LogLevel.ERROR, msg);
    }

    @Override
    public void error(Supplier<String> msg, Throwable es) {
        log(LogLevel.ERROR, msg, es);
    }

    @Override
    public void warn(String msg, Throwable es) {
        log(LogLevel.WARN, msg, es);
    }

    @Override
    public void warn(String msg, Object... params) {
        log(LogLevel.WARN, msg, params);
    }

    @Override
    public void warn(Throwable es) {
        log(LogLevel.WARN, es);
    }

    @Override
    public void warn(Supplier<String> msg) {
        log(LogLevel.WARN, msg);
    }

    @Override
    public void warn(Supplier<String> msg, Throwable es) {
        log(LogLevel.WARN, msg, es);
    }

    @Override
    public void info(String msg, Throwable es) {
        log(LogLevel.INFO, msg, es);
    }

    @Override
    public void info(String msg, Object... params) {
        log(LogLevel.INFO, msg, params);
    }

    @Override
    public void info(Throwable es) {
        log(LogLevel.INFO, es);
    }

    @Override
    public void info(Supplier<String> msg) {
        log(LogLevel.INFO, msg);
    }

    @Override
    public void info(Supplier<String> msg, Throwable es) {
        log(LogLevel.INFO, msg, es);
    }

    @Override
    public void debug(String msg, Throwable es) {
        log(LogLevel.DEBUG, msg, es);
    }

    @Override
    public void debug(String msg, Object... params) {
        log(LogLevel.DEBUG, msg, params);
    }

    @Override
    public void debug(Throwable es) {
        log(LogLevel.DEBUG, es);
    }

    @Override
    public void debug(Supplier<String> msg) {
        log(LogLevel.DEBUG, msg);
    }

    @Override
    public void debug(Supplier<String> msg, Throwable es) {
        log(LogLevel.DEBUG, msg, es);
    }

    @Override
    public void trace(String msg, Throwable es) {
        log(LogLevel.TRACE, msg, es);
    }

    @Override
    public void trace(String msg, Object... params) {
        log(LogLevel.TRACE, msg, params);
    }


    @Override
    public void trace(Throwable es) {
        log(LogLevel.TRACE, es);
    }

    @Override
    public void trace(Supplier<String> msg) {
        log(LogLevel.TRACE, msg);
    }

    @Override
    public void trace(Supplier<String> msg, Throwable es) {
        log(LogLevel.TRACE, msg, es);
    }

    @Override
    public void log(LogLevel logLevel, String msg, Throwable es) {
        this.doLog(logLevel, msg, es);
    }

    @Override
    public void log(LogLevel logLevel, String msg, Object... params) {
        this.doLog(logLevel, msg, null, params);
    }

    @Override
    public void log(LogLevel level, Throwable es) {
        this.doLog(level, "", es);
    }

    @Override
    public void log(LogLevel level, Supplier<String> msg) {
        this.doLog(level, msg, null);
    }

    @Override
    public void log(LogLevel level, Supplier<String> msg, Throwable es) {
        this.doLog(level, msg, es);
    }


    @Override
    public boolean isErrorEnabled() {
        return this.isEnabled(LogLevel.ERROR);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.isEnabled(LogLevel.WARN);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.isEnabled(LogLevel.INFO);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.isEnabled(LogLevel.DEBUG);
    }

    @Override
    public boolean isTraceEnabled() {
        return this.isEnabled(LogLevel.TRACE);
    }

    @Override
    public boolean isEnabled(LogLevel lv) {
        if (lv == null || this.level() == null) return false;
        return lv.level() >= this.level().level();
    }

    protected abstract void doLog(LogLevel logLevel, String msg, Throwable es, Object... params);

    protected void doLog(LogLevel logLevel, Supplier<String> msg, Throwable es, Object... params) {
        this.doLog(logLevel, (msg == null) ? "" : msg.get(), es, params);
    }
}
