package org.smart.helper;

import org.smart.annotation.Action;
import org.smart.bean.Handler;
import org.smart.bean.Request;
import org.smart.util.ArrayUtil;
import org.smart.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ControllerHelper
 * @Description 控制器助手类
 * @Author wangss
 * @date 2020.01.02 21:02
 * @Version 1.0
 */
public final class ControllerHelper {

    /**
     * 用于存放请求与处理器的映射关系（简称 Action Map）
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

    static {
        // 获取所有的 controller 类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            // 遍历这些 controller 类
            for (Class<?> controllerClass : controllerClassSet) {
                // 获取 controller 类中定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    // 遍历这些 controller 中的方法
                    for (Method method : methods) {
                        // 判断当前方法是否带有 action 注解
                        if (method.isAnnotationPresent(Action.class)) {
                            // 从 action 注解中获取 url 映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();

                            // 验证 url 映射规则
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    // 获取请求方法与请求路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);

                                    // 初始化 action map
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取 Handler
     *
     * @param requestMethod
     * @param requestPath
     * @return
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        // request 中重写了 hashCode方法 和 equals方法
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
