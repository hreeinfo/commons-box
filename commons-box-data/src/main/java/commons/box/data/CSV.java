package commons.box.data;

import commons.box.app.AppError;
import commons.box.util.IOs;
import commons.box.util.Langs;
import commons.box.util.Strs;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * CSV解析对象
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/7/2 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class CSV {
    private static final CSVFormat DEFAULT = CSVFormat.EXCEL.withHeader();
    private final CSVFormat format;

    public CSV(CSVFormat format) {
        this.format = ((format == null) ? DEFAULT : format);
    }

    /**
     * 原始格式化工具
     *
     * @return
     */
    public CSVFormat format() {
        return format;
    }

    /**
     * 以List方式输出所有内容 默认UTF-8
     *
     * @param file
     * @return
     * @throws AppError
     */
    public List<Map<String, String>> parse(File file) throws AppError {
        return this.parse(file, Langs.UTF8);
    }

    /**
     * 以List方式输出所有内容
     *
     * @param is
     * @return
     * @throws AppError
     */
    public List<Map<String, String>> parse(InputStream is) throws AppError {
        return this.parse(is, Langs.UTF8);
    }

    /**
     * 以List方式输出所有内容 指定字符集
     *
     * @param file
     * @param encoding
     * @return
     * @throws AppError
     */
    public List<Map<String, String>> parse(File file, String encoding) throws AppError {
        try {
            return parse(IOs.inputStream(file), encoding);
        } catch (IOException e) {
            throw AppError.error("无法打开文件输入流 " + file, e);
        }
    }

    /**
     * 以List方式输出所有内容 指定字符集
     *
     * @param inputStream
     * @param encoding
     * @return
     * @throws AppError
     */
    public List<Map<String, String>> parse(InputStream inputStream, String encoding) throws AppError {
        if (Strs.isBlank(encoding)) encoding = Langs.UTF8;
        if (inputStream == null) throw AppError.error("输入流不能为空");
        List<Map<String, String>> datas = new ArrayList<>();
        Reader in = null;
        try {
            in = new InputStreamReader(inputStream, encoding);
            Iterable<CSVRecord> records = this.format.parse(in);
            for (CSVRecord c : records) {
                if (c == null) continue;
                Map<String, String> m = c.toMap();
                if (m != null) datas.add(m);
            }
        } catch (Throwable e) {
            throw AppError.error("分析CSV文件错误", e);
        } finally {
            IOs.close(in);
            IOs.close(inputStream);
        }
        return datas;
    }

    /**
     * 遍历 用于大文件 默认UTF-8
     *
     * @param file
     * @param cmd
     * @throws AppError
     */
    public void iterate(File file, Consumer<Map<String, String>> cmd) throws AppError {
        this.iterate(file, Langs.UTF8, cmd);
    }

    /**
     * 遍历 用于大文件 指定字符集
     *
     * @param file
     * @param encoding
     * @param cmd
     * @throws AppError
     */
    public void iterate(File file, String encoding, Consumer<Map<String, String>> cmd) throws AppError {
        if (file == null) throw AppError.error("输入文件不能为空");
        InputStream inputStream = null;
        Reader in = null;
        try {
            inputStream = IOs.inputStream(file);
            in = new InputStreamReader(inputStream, encoding);
            Iterable<CSVRecord> records = this.format.parse(in);
            for (CSVRecord c : records) {
                if (c == null) continue;
                Map<String, String> m = c.toMap();
                if (m != null) cmd.accept(m);
            }
        } catch (Throwable e) {
            throw AppError.error("分析CSV文件错误", e);
        } finally {
            IOs.close(in);
            IOs.close(inputStream);
        }
    }
}
