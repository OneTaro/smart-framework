package org.smart.bean;

import org.smart.util.CastUtil;

import java.util.Map;

/**
 * @ClassName Param
 * @Description 请求参数对象
 * @Author wangss
 * @date 2020.01.03 19:15
 * @Version 1.0
 */
public class Param {

    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 根据参数名获取 long 型参数值
     *
     * @param name
     * @return
     */
    public long getLong(String name) {
        return CastUtil.castLong(paramMap.get(name));
    }

    /**
     * 获取所有字段信息
     *
     * @return
     */
    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
