package org.smart;

import org.smart.helper.BeanHelper;
import org.smart.helper.ClassHelper;
import org.smart.helper.ControllerHelper;
import org.smart.helper.IocHelper;
import org.smart.util.ClassUtil;

/**
 * @ClassName HelperLoader
 * @Description 加载相应的 Helper 类
 * @Author wangss
 * @date 2020.01.02 21:39
 * @Version 1.0
 */
public final class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
     }
}
