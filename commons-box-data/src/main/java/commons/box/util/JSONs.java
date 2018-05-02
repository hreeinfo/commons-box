package commons.box.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import commons.box.app.AppError;
import commons.box.app.AppLog;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * JSON解析类
 * <p>
 * 要使用本工具类 必须在最终构建中显式包含 fastjson
 * <p>
 * <code>
 * compile("com.alibaba:fastjson:${version_fastjson}")
 * </code>
 * <p>
 * TODO 增加 额外的 consumer 用于解析错误的处理过程，目前没有任何错误抛出
 * <p>
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class JSONs {
    private static final AppLog LOG = AppLog.get(JSONs.class);

    public static final SerializerFeature[] TO_JSON = new SerializerFeature[]{
            //SerializerFeature.WriteClassName,
            SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.QuoteFieldNames,
            //SerializerFeature.SortField,
    };

    public static final SerializerFeature[] TO_JSON_PRETTY = new SerializerFeature[]{
            //SerializerFeature.WriteClassName,
            SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.QuoteFieldNames,
            //SerializerFeature.SortField,
            SerializerFeature.PrettyFormat,
    };


    public static final Feature[] FROM_JSON = new Feature[]{
            Feature.AllowArbitraryCommas,
            Feature.AllowComment,
            Feature.AllowSingleQuotes,
            Feature.AllowUnQuotedFieldNames,
            Feature.AutoCloseSource,
            Feature.IgnoreNotMatch,
            Feature.SortFeidFastMatch,
    };

    public static final BiConsumer<Object, Throwable> NULL_BCO = (o, throwable) -> {
    };
    public static final BiConsumer<String, Throwable> NULL_BCS = (o, throwable) -> {
    };

    static {
        JSON.DEFFAULT_DATE_FORMAT = "yyyyMMdd HH:mm:ss.SSS";
    }

    private JSONs() {
    }

    /**
     * 格式化
     *
     * @param object
     * @param serializerFeatures
     * @return
     */
    public static String to(Object object, SerializerFeature... serializerFeatures) {
        return to(object, NULL_BCO, serializerFeatures);
    }

    public static String to(Object object, BiConsumer<Object, Throwable> onFail, SerializerFeature... serializerFeatures) {
        if (serializerFeatures == null || serializerFeatures.length < 1) serializerFeatures = TO_JSON;

        try {
            return JSON.toJSONString(object, serializerFeatures);
        } catch (Throwable e) {
            LOG.warn("生成 JSON 错误 - " + e.getMessage());
            if (onFail != null) onFail.accept(object, e);
        }

        return null;
    }

    /**
     * 格式化（调试模式，生成格式化后的JSON）
     *
     * @param object
     * @param serializerFeatures
     * @return
     */
    public static String pretty(Object object, SerializerFeature... serializerFeatures) {
        return pretty(object, NULL_BCO, serializerFeatures);
    }

    public static String pretty(Object object, BiConsumer<Object, Throwable> onFail, SerializerFeature... serializerFeatures) {
        if (serializerFeatures == null || serializerFeatures.length < 1) serializerFeatures = TO_JSON_PRETTY;
        return to(object, onFail, serializerFeatures);
    }


    /**
     * 转换Json文本为对应类的对象，使用特性标识 当未指定类型时注意：对于数组内容返回的是List 而对于其他对象返回的是Map
     *
     * @param json
     * @param <T>
     * @return
     * @throws AppError
     */
    @SuppressWarnings("unchecked")

    public static <T> T from(String json, Feature... features) throws AppError {
        return from(json, NULL_BCS, features);
    }

    @SuppressWarnings("unchecked")
    public static <T> T from(String json, BiConsumer<String, Throwable> onFail, Feature... features) throws AppError {
        if (features == null || features.length < 1) features = FROM_JSON;

        try {
            return (T) JSON.parse(json, features);
        } catch (Throwable e) {
            LOG.warn("转换 JSON 错误 - " + e.getMessage());
            if (onFail != null) onFail.accept(json, e);
        }

        return null;
    }

    /**
     * 转换Json文本为指定类的对象，使用特性标识
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */

    public static <T> T from(String json, Class<T> type, Feature... features) {
        return from(json, type, NULL_BCS, features);
    }

    public static <T> T from(String json, Class<T> type, BiConsumer<String, Throwable> onFail, Feature... features) {
        if (features == null || features.length < 1) features = FROM_JSON;

        try {
            return JSON.parseObject(json, type, features);
        } catch (Throwable e) {
            LOG.warn("转换 JSON 错误 - " + e.getMessage());
            if (onFail != null) onFail.accept(json, e);
        }

        return null;
    }

    /**
     * 转换Json文本为JSONObject对象，使用特性标识
     *
     * @param json
     * @return
     */
    public static JSONObject jobj(String json, Feature... features) {
        return jobj(json, NULL_BCS, features);
    }

    public static JSONObject jobj(String json, BiConsumer<String, Throwable> onFail, Feature... features) {
        if (features == null || features.length < 1) features = FROM_JSON;
        try {
            return JSON.parseObject(json, features);
        } catch (Throwable e) {
            LOG.warn("转换 JSON 错误 - " + e.getMessage());
            if (onFail != null) onFail.accept(json, e);
        }

        return null;
    }

    /**
     * 转换Json文本为原始JSONArray对象，不使用任何特性标识
     *
     * @param json
     * @return
     */
    public static JSONArray jarray(String json) {
        return jarray(json, NULL_BCS);
    }

    public static JSONArray jarray(String json, BiConsumer<String, Throwable> onFail) {
        try {
            return JSON.parseArray(json);
        } catch (Throwable e) {
            LOG.warn("转换 JSON 错误 - " + e.getMessage());
            if (onFail != null) onFail.accept(json, e);
        }

        return null;
    }

    /**
     * 转换Json文本为给定类型的对象列表
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */

    public static <T> List<T> jarray(String json, Class<T> type) {
        return jarray(json, type, NULL_BCS);
    }

    public static <T> List<T> jarray(String json, Class<T> type, BiConsumer<String, Throwable> onFail) {
        try {
            return JSON.parseArray(json, type);
        } catch (Throwable e) {
            LOG.warn("转换 JSON 错误 - " + e.getMessage());
            if (onFail != null) onFail.accept(json, e);
        }

        return null;
    }
}
