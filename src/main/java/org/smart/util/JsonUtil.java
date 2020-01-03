package org.smart.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName JsonUtil
 * @Description JSON工具类
 * @Author wangss
 * @date 2020.01.03 19:43
 * @Version 1.0
 */
public final class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将 POJO 转换为 JSON
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJson(T obj) {
        String json;

        try {

            json = OBJECT_MAPPER.writeValueAsString(obj);

        } catch (Exception e) {
            LOGGER.error("convert POJO to JSON failure", e);
            throw new RuntimeException(e);
        }

        return json;
    }

    /**
     * 将 JSON 转换为 POJO
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> type) {
        T pojo;

        try {

            pojo = OBJECT_MAPPER.readValue(json, type);

        } catch (Exception e) {
            LOGGER.error("convert JSON to POJO failure", e);
            throw new RuntimeException(e);
        }

        return pojo;
    }
}
