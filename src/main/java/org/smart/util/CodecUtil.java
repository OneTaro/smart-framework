package org.smart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @ClassName CodecUtil
 * @Description 编码与解码工具类
 * @Author wangss
 * @date 2020.01.03 19:37
 * @Version 1.0
 */
public final class CodecUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 将 URL 编码
     *
     * @param source
     * @return
     */
    public static String encodeURL(String source) {
        String target;

        try {

            target = URLEncoder.encode(source, "UTF-8");

        } catch (Exception e) {
            LOGGER.error("encode url failure", e);
            throw new RuntimeException(e);
        }

        return target;
    }

    /**
     * 将 URL 解码
     *
     * @param source
     * @return
     */
    public static String decodeURL(String source) {
        String target;

        try {

            target = URLDecoder.decode(source, "UTF-8");

        } catch (Exception e) {
            LOGGER.error("decode url failure", e);
            throw new RuntimeException(e);
        }

        return target;
    }
}
