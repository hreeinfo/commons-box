package commons.box.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import commons.box.app.AppError;

/**
 * JSON解析类
 * <p>
 * 要使用本工具类 必须在最终构建中显式包含 fastjson
 * <p>
 * <code>
 * compile("com.alibaba:fastjson:${version_fastjson}")
 * </code>
 * <p>
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：16/3/21 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class JSONs {
    public static final SerializerFeature[] FEATURES_TOJSON = new SerializerFeature[]{
            //SerializerFeature.WriteClassName,
            SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.QuoteFieldNames,
            //SerializerFeature.SortField,
    };

    public static final SerializerFeature[] FEATURES_TOJSON_DEBUG = new SerializerFeature[]{
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


    public static final Feature[] FEATURES_FROMJSON = new Feature[]{
            Feature.AllowArbitraryCommas,
            Feature.AllowComment,
            Feature.AllowSingleQuotes,
            Feature.AllowUnQuotedFieldNames,
            Feature.AutoCloseSource,
            Feature.IgnoreNotMatch,
            Feature.SortFeidFastMatch,
    };

    static {
        JSON.DEFFAULT_DATE_FORMAT = "yyyyMMdd HH:mm:ss.SSS";
    }

    private JSONs() {
    }

    public static String toJson(Object object, SerializerFeature... serializerFeatures) {
        if (serializerFeatures == null || serializerFeatures.length < 1) serializerFeatures = FEATURES_TOJSON;
        return JSON.toJSONString(object, serializerFeatures);
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
    public static <T> T fromJson(String json, Feature... features) throws AppError {
        if (features == null || features.length < 1) features = FEATURES_FROMJSON;
        return (T) JSON.parse(json, features);
    }

    /**
     * 转换Json文本为指定类的对象，使用特性标识
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> type, Feature... features) {
        if (features == null || features.length < 1) features = FEATURES_FROMJSON;
        return JSON.parseObject(json, type, features);
    }

    /**
     * 转换Json文本为JSONObject对象，使用特性标识
     *
     * @param json
     * @return
     */
    public static JSONObject fromJsonToJO(String json, Feature... features) {
        if (features == null || features.length < 1) features = FEATURES_FROMJSON;
        return JSON.parseObject(json, features);
    }

    /**
     * 转换Json文本为原始JSONArray对象，不使用任何特性标识
     *
     * @param json
     * @return
     */
    public static JSONArray fromJsonToJA(String json) {
        return JSON.parseArray(json);
    }

}
