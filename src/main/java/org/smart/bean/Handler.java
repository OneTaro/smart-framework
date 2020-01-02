package org.smart.bean;

import java.lang.reflect.Method;

/**
 * @ClassName Handler
 * @Description 封装 Action 信息
 * @Author wangss
 * @date 2020.01.02 20:59
 * @Version 1.0
 */
public class Handler {

    /**
     * Controller 类
     */
    private Class<?> controllerClass;

    /**
     * Action 方法
     */
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
