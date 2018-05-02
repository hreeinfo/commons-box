package commons.box.app;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 平台的所有异常均应封装为AppError，
 * <p>
 * 此类继承自RuntimeException，
 * <p>
 * 方便代码结构整理并能更加有效地处理程序逻辑
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class AppError extends RuntimeException {
    private static final long serialVersionUID = 1606731603520432271L;

    /**
     *
     */
    public AppError() {
        super();
    }

    /**
     * @param e
     */
    public AppError(Throwable e) {
        super(e);
    }

    /**
     * @param msg
     */
    public AppError(String msg) {
        super(msg);
    }


    /**
     * @param msg
     * @param e
     */
    public AppError(String msg, Throwable e) {
        super(msg, e);
    }


    /**
     * 获取真正发生的异常 由于AppError本质上是uncheck机制 要获取实际异常需要调用此方法
     *
     * @return
     */
    public Throwable real() {
        Throwable ex = this;
        Throwable cause;
        while ((cause = ex.getCause()) != null) {
            ex = cause;
            if (!(ex instanceof AppError)) return ex;
        }
        return ex;
    }

    /**
     * 直接生成错误
     *
     * @param msg
     * @return
     */
    public static AppError error(String msg) {
        return new AppError(msg);
    }

    /**
     * 封装异常为错误
     *
     * @param e
     * @return
     */
    public static AppError error(Throwable e) {
        if (e == null) return new AppError();
        return new AppError(e.getMessage(), e);
    }

    /**
     * 封装异常，并附加错误消息
     *
     * @param msg
     * @param e
     * @return
     */
    public static AppError error(String msg, Throwable e) {
        if (msg == null) msg = "";
        if (e == null) return new AppError(msg);
        return new AppError(msg, e);
    }

    /**
     * 将ErrorStack转化为String.
     */
    public static String toString(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 获取组合本异常信息与底层异常信息的异常描述, 适用于本异常为统一包装异常类，底层异常才是根本原因的情况。
     */
    public static String msgNested(Throwable ex) {
        if (ex == null) return null;
        Throwable nestedException;
        if (ex instanceof AppError) nestedException = ((AppError) ex).real();
        else nestedException = ex.getCause();
        if (nestedException == null) return ex.getMessage();
        return ex.getMessage() + " 实际异常为 " + nestedException.getClass().getName() + ":" + nestedException.getMessage();
    }

    /**
     * 获取异常的Root Cause.
     */
    public static Throwable root(Throwable ex) {
        Throwable cause;
        while ((cause = ex.getCause()) != null) ex = cause;
        return ex;
    }


    /**
     * 判断异常是否由某些底层的异常引起.
     */
    @SuppressWarnings("unchecked")
    public static boolean is(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) if (causeClass.isInstance(cause)) return true;
            cause = cause.getCause();
        }
        return false;
    }
}
