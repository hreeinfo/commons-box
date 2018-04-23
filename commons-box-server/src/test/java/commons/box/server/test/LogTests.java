package commons.box.server.test;

import commons.box.app.AppLog;
import commons.box.util.Logs;

import java.util.Date;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/22 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class LogTests {
    private static final AppLog LOG = Logs.get(LogTests.class);

    public static void main(String[] args) {
        LOG.debug("DEBUG 信息 生成于 " + new Date());
        LOG.info("INFO 信息 生成于 " + new Date());
    }

}
