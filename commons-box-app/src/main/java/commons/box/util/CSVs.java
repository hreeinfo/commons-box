package commons.box.util;


import commons.box.app.misc.CSV;
import org.apache.commons.csv.CSVFormat;

/**
 * CSV解析工具类
 * <p>
 * 需要额外依赖 <code>compile("org.apache.commons:commons-csv:${version_commons_csv}")</code>
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class CSVs {
    private CSVs() {
    }

    // TODO 基于BUILDER的生成器
    public static CSV excel() {
        return new CSV(CSVFormat.EXCEL.withHeader());
    }
}
