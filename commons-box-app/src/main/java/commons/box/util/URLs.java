package commons.box.util;


import commons.box.app.AppError;
import commons.box.app.AppLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Map;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class URLs {
    private static final AppLog LOG = Logs.get(URLs.class);
    public static final int CONNECT_TIMEOUT = 5 * 60000; // 默认5分钟超时
    public static final String UNKNOW = "unknown";

    private URLs() {
    }

    public static String request(final String url) throws AppError {
        return request(url, null, Langs.UTF8, CONNECT_TIMEOUT);
    }

    /**
     * 精简的HTTP调用
     *
     * @param url
     * @param postData 如果值为null 使用GET方法 否则使用POST方法
     * @param encoding
     * @param timeout
     * @return
     * @throws AppError
     */
    public static String request(final String url, final String postData, final String encoding, int timeout) throws AppError {
        final URLConnection raw;
        try {
            raw = new URL(url).openConnection();
        } catch (IOException e) {
            throw new AppError(String.format("'%s' 打开HttpURLConnection错误", url));
        }

        if (!(raw instanceof HttpURLConnection))
            throw new AppError(String.format("'%s' 打开 %s 替换了期望的 HttpURLConnection", url, raw.getClass().getName()));

        final String method = (postData == null) ? "GET" : "POST";
        final HttpURLConnection conn = (HttpURLConnection) raw;

        InputStream connDataRecive = null;
        OutputStream connDataPost = null;
        try {
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);

            conn.setRequestMethod(method);
            conn.setUseCaches(false);

            if (method.equals("POST")) {
                conn.setDoOutput(true);
                try {
                    connDataPost = conn.getOutputStream();
                    IOs.write(postData, connDataPost, encoding, false);
                    connDataPost.flush();
                } catch (Throwable oe) {
                    throw new AppError(String.format("写入失败：%s 目标： %s", method, url), oe);
                }
            }

            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) connDataRecive = conn.getInputStream();
            else throw new AppError(String.format("请求失败：%s 目标： %s 状态码 %d", method, url, conn.getResponseCode()));

            return IOs.readString(connDataRecive, encoding, false);
        } catch (final Throwable exp) {
            throw new AppError(String.format("请求失败：%s 目标： %s", method, url), exp);
        } finally {
            try {
                conn.disconnect();
            } catch (Throwable ignored) {
            }
            IOs.close(connDataPost);
            IOs.close(connDataRecive);
        }
    }

    /**
     * 构建URL 组装map为参数
     *
     * @param base
     * @param params
     * @return
     */
    public static String buildURLs(String base, Map<String, String> params) {
        return buildURLs(base, params, false);
    }

    /**
     * 构建URL 组装map为参数 可以选择是否编码内容
     *
     * @param base
     * @param params
     * @param encode
     * @return
     */
    public static String buildURLs(String base, Map<String, String> params, boolean encode) {
        if (Strs.isBlank(base)) base = "";
        StringBuilder sb = new StringBuilder();
        sb.append(base);

        if (params != null && params.size() > 0) {
            if (base.contains("?")) sb.append("&");
            else sb.append("?");

            int i = 0;
            for (Map.Entry<String, String> me : params.entrySet()) {
                if (i > 0) sb.append("&");
                String k = me.getKey();
                if (Strs.isBlank(k)) k = "";
                String v = me.getValue();
                if (Strs.isBlank(v)) v = "";

                if (encode) v = encode(v);

                sb.append(k).append("=").append(v);
                i = i + 1;
            }
        }

        return sb.toString();
    }

    public static String encode(String data) {
        return encode(data, Langs.UTF8);
    }

    public static String decode(String data) {
        return decode(data, Langs.UTF8);
    }


    public static String encode(String data, String charset) {
        return Codes.url().encode(data, charset);
    }

    public static String decode(String data, String charset) {
        return Codes.url().decode(data, charset);
    }


    public static String toURL(File file) {
        if (file == null) return null;
        try {
            return file.toURI().toURL().toString();
        } catch (Throwable ignored) {
        }
        return null;
    }
}
