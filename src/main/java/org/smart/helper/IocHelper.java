package org.smart.helper;

import org.smart.annotation.Inject;
import org.smart.util.ArrayUtil;
import org.smart.util.CollectionUtil;
import org.smart.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @ClassName IocHelper
 * @Description 依赖注入助手类
 * @Author wangss
 * @date 2020.01.02 20:32
 * @Version 1.0
 */
public final class IocHelper {

    static {
        // 获取所有的 Bean 类与 Bean 实例之间的映射关系（简称 Bean Map）
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历 beanMap
            for (Map.Entry<Class<?>, Object> beanEntity : beanMap.entrySet()) {
                // 从 beanMap 中获取 bean 类 与 bean 实例
                Class<?> beanClass = beanEntity.getKey();
                Object beanInstance = beanEntity.getValue();
                // 获取 Bean 类定义的所有成员变量（简称 Bean Field）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历 bean field
                    for (Field beanField : beanFields) {
                        // 判断当前 bean field 是否带有 Inject 注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            // 在 bean map 中获取 bean field 对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                // 通过反射初始化 beanField 的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
