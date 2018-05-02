package commons.box.app.misc;

import commons.box.app.AppError;
import commons.box.util.Dates;
import commons.box.util.Strs;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.function.Consumer;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class TimeMetric {
    public static final FastDateFormat DFT = Dates.fmt("yyyyMMdd HH:mm:ss.SSS");

    /**
     * 空实例
     *
     * @return
     */
    public static TimeMetric inst() {
        return new TimeMetric();
    }

    /**
     * 生成实例 本方法自动调用start方法然后执行命令，结束后调用stop方法写入执行时间
     *
     * @param cmd
     * @return
     */
    public static TimeMetric inst(Consumer<TimeMetric> cmd) {
        TimeMetric tm = new TimeMetric();
        tm.start();
        if (cmd != null) tm.run(cmd);
        return tm;
    }

    private long start = 0;
    private long stop = 0;

    public TimeMetric() {
        this.start = 0;
        this.stop = 0;
    }

    public TimeMetric(long start) {
        this.start = start;
        this.stop = start;
    }

    public TimeMetric(long start, long stop) {
        this.start = start;
        this.stop = stop;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }

    public Date getStartDate() {
        return new Date(start);
    }

    public Date getStopDate() {
        return new Date(stop);
    }

    public TimeMetric start() {
        this.start = System.currentTimeMillis();
        this.stop = this.start;
        return this;
    }

    public TimeMetric stop() {
        this.stop = System.currentTimeMillis();
        if (this.start < 1) this.start = this.stop;
        return this;
    }

    /**
     * 运行，注意如果此单元未开始方法会自动调用start()方法，如已开始且要刷新开始时间则需手工start()
     *
     * @param cmd
     * @throws AppError
     */
    public void run(Consumer<TimeMetric> cmd) throws AppError {
        try {
            if (this.start < 1) this.start();
            cmd.accept(this);
            this.stop();
        } catch (Throwable e) {
            throw AppError.error("运行错误 " + e.getMessage(), e);
        }
    }

    public void print(String title) {
        if (Strs.isNotBlank(title)) System.out.println(title + " " + this.info());
        else System.out.println(this.info());
    }

    public String info() {
        StringBuilder sb = new StringBuilder();
        sb.append("运行时间 ").append(DFT.format(new Date(this.start)))
                .append(" -> ").append(DFT.format(new Date(this.stop)))
                .append(" 用时 ");

        long sn = this.stop - this.start;

        if (sn > 1000) sb.append(sn / 1000.0).append("s");
        else sb.append(sn).append("ms");

        return sb.toString();
    }

    @Override
    public String toString() {
        return this.info();
    }
}
