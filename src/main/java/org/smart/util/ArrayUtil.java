package org.smart.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @ClassName ArrayUtil
 * @Description 数组工具类
 * @Author wangss
 * @date 2020.01.02 20:42
 * @Version 1.0
 */
public final class ArrayUtil {

    /**
     * 判断数组是否非空
     *
     * @param array
     * @return
     */
    public static boolean isNotEmpty(Object[] array) {
        return !ArrayUtils.isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return ArrayUtils.isEmpty(array);
    }

}
