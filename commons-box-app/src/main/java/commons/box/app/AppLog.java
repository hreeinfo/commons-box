package commons.box.app;

import commons.box.util.Logs;

import java.util.function.Supplier;

/**
 * 平台LOG 因各框架使用不同的LOG实现 在此使用统一的接口来规范所有日志机制
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/3/2 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface AppLog {
    enum LogLevel {
        /**
         * 无错误输出
         */
        NONE(100),

        /**
         * 错误，等同于标准Logger的error
         */
        ERROR(50),
        /**
         * 错误，等同于标准Logger的warn
         */
        WARN(25),
        /**
         * 信息，等同于标准Logger的info
         */
        INFO(10),

        /**
         * 调试，等同于标准Logger的debug
         */
        DEBUG(5),

        /**
         * 跟踪,等同于标准Logger的debug
         */
        TRACE(0);

        private int level = 100;

        LogLevel(int level) {
            this.level = level;
        }

        public int level() {
            return this.level;
        }
    }

    /**
     * Logger的名字
     *
     * @return
     */
    public String name();

    /**
     * Level 级别
     *
     * @return
     */
    public LogLevel level();

    /**
     * 带可选异常对象的错误信息
     *
     * @param msg
     * @param es
     */
    public void error(String msg, Throwable es);

    /**
     * 带可选错误参数的错误信息 如果params不为空那么将会使用Strings.format来格式化msg字符串
     *
     * @param msg
     * @param params
     */
    public void error(String msg, Object... params);

    /**
     * 异常对象的错误信息 日志的msg是异常对象对应的message
     *
     * @param es
     */
    public void error(Throwable es);

    /**
     * 错误信息 Supplier msg 用于生成日志消息
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     */
    public void error(Supplier<String> msg);

    /**
     * 错误信息 Supplier msg 用于生成日志消息 包含异常对象
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     * @param es
     */
    public void error(Supplier<String> msg, Throwable es);

    /**
     * 带可选异常对象的警告信息
     *
     * @param msg
     * @param es
     */
    public void warn(String msg, Throwable es);

    /**
     * 带可选错误参数的警报信息 如果params不为空那么将会使用Strings.format来格式化msg字符串
     *
     * @param msg
     * @param params
     */
    public void warn(String msg, Object... params);

    /**
     * 异常对象的警报信息 日志的msg是异常对象对应的message
     *
     * @param es
     */
    public void warn(Throwable es);

    /**
     * 警报信息 Supplier msg 用于生成日志消息
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     */
    public void warn(Supplier<String> msg);

    /**
     * 警报信息 Supplier msg 用于生成日志消息 包含异常对象
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     * @param es
     */
    public void warn(Supplier<String> msg, Throwable es);

    /**
     * 带可选异常对象的记录信息
     *
     * @param msg
     * @param es
     */
    public void info(String msg, Throwable es);

    /**
     * 记录信息 如果params不为空那么将会使用Strings.format来格式化msg字符串
     *
     * @param msg
     * @param params
     */
    public void info(String msg, Object... params);


    /**
     * 异常对象的记录信息 日志的msg是异常对象对应的message
     *
     * @param es
     */
    public void info(Throwable es);

    /**
     * 记录信息 Supplier msg 用于生成日志消息
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     */
    public void info(Supplier<String> msg);

    /**
     * 记录信息 Supplier msg 用于生成日志消息 包含异常对象
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     * @param es
     */
    public void info(Supplier<String> msg, Throwable es);


    /**
     * 带可选异常对象的调试信息
     *
     * @param msg
     * @param es
     */
    public void debug(String msg, Throwable es);

    /**
     * 调试信息  如果params不为空那么将会使用Strings.format来格式化msg字符串
     *
     * @param msg
     * @param params
     */
    public void debug(String msg, Object... params);


    /**
     * 异常对象的调试信息 日志的msg是异常对象对应的message
     *
     * @param es
     */
    public void debug(Throwable es);

    /**
     * 调试信息 Supplier msg 用于生成日志消息
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     */
    public void debug(Supplier<String> msg);

    /**
     * 调试信息 Supplier msg 用于生成日志消息 包含异常对象
     * <p>
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     * @param es
     */
    public void debug(Supplier<String> msg, Throwable es);

    /**
     * 带可选异常对象的跟踪信息
     *
     * @param msg
     * @param es
     */
    public void trace(String msg, Throwable es);

    /**
     * 跟踪信息  如果params不为空那么将会使用Strings.format来格式化msg字符串
     *
     * @param msg
     * @param params
     */
    public void trace(String msg, Object... params);


    /**
     * 异常对象的跟踪信息 日志的msg是异常对象对应的message
     *
     * @param es
     */
    public void trace(Throwable es);

    /**
     * 跟踪信息 Supplier msg 用于生成日志消息
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     */
    public void trace(Supplier<String> msg);

    /**
     * 跟踪信息 Supplier msg 用于生成日志消息 包含异常对象
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param msg
     * @param es
     */
    public void trace(Supplier<String> msg, Throwable es);

    /**
     * 设置级别的日志记录  如果params不为空那么将会使用Strings.format来格式化msg字符串
     *
     * @param level
     * @param msg
     * @param params
     */
    public void log(LogLevel level, String msg, Object... params);


    /**
     * 设置级别的日志记录
     *
     * @param level
     * @param msg
     * @param es
     */
    public void log(LogLevel level, String msg, Throwable es);

    /**
     * 设置级别的日志记录 其中日志信息使用异常对象的message
     *
     * @param level
     * @param es
     */
    public void log(LogLevel level, Throwable es);

    /**
     * 设置级别的日志记录 Supplier msg 用于生成日志消息
     * <p>
     * 注意：此方法用于复杂的log日志运算，仅在符合日志级别的情况下，Supplier值才会被计算
     *
     * @param level
     * @param msg
     */
    public void log(LogLevel level, Supplier<String> msg);

    /**
     * 设置级别的日志记录 带可选异常对象的跟踪信息
     *
     * @param level
     * @param msg
     * @param es
     */
    public void log(LogLevel level, Supplier<String> msg, Throwable es);

    public boolean isErrorEnabled();

    public boolean isWarnEnabled();

    public boolean isInfoEnabled();

    public boolean isDebugEnabled();

    public boolean isTraceEnabled();

    /**
     * 判断某个级别是否启用
     *
     * @param level
     * @return
     */
    public boolean isEnabled(LogLevel level);

    /**
     * 直接获取
     *
     * @param name
     * @return
     */
    public static AppLog get(String name) {
        return Logs.get(name);
    }

    /**
     * 直接获取
     *
     * @param cls
     * @return
     */
    public static AppLog get(Class<?> cls) {
        return Logs.get(cls);
    }
}
