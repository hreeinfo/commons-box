package commons.box.util;

import commons.box.app.AppLog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * 系统工具
 * <p>创建作者：xingxiuyi </p>
 */
public final class SYSs {
    private static final AppLog LOG = Logs.get(SYSs.class);
    private static final ExecutorService ES = Executors.newCachedThreadPool();

    private SYSs() {
    }

    /**
     * 异步执行
     *
     * @param cmd
     * @return
     */
    public static Future<?> async(final Runnable cmd) {
        return async(null, cmd, null);
    }

    /**
     * 异步执行，当失败时允许响应
     *
     * @param cmd
     * @param failed
     * @return
     */
    public static Future<?> async(final Runnable cmd, final Consumer<Throwable> failed) {
        return async(null, cmd, failed);
    }


    /**
     * 异步提交命令 使用默认的线程池 失败时具有相应
     *
     * @param executorService
     * @param cmd
     * @param failed
     * @return
     */
    public static Future<?> async(final ExecutorService executorService, final Runnable cmd, final Consumer<Throwable> failed) {
        if (cmd == null) return null;
        ExecutorService es = (executorService != null) ? executorService : ES;
        return es.submit(() -> {
            try {
                cmd.run();
            } catch (Throwable e) {
                LOG.warn(e.getMessage(), e);
                if (failed != null) {
                    try {
                        failed.accept(e);
                    } catch (Throwable ef) {
                        LOG.warn(ef.getMessage(), ef);
                    }
                }
            }
        });
    }

    /**
     * 在执行大批量循环时,当前线程可能会分配全部的可用时间片
     * <p>
     * 本方法使线程休眠0ms, 对真实运行效果没有影响,但是能够释放cpu资源给哪些急需cpu的其它线程
     */
    public static void ts0() {
        try {
            Thread.sleep(0);
        } catch (Throwable ignored) {
        }
    }



}
