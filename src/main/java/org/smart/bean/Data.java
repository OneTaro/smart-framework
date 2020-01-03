package org.smart.bean;

/**
 * @ClassName Data
 * @Description 返回数据对象
 * @Author wangss
 * @date 2020.01.03 19:26
 * @Version 1.0
 */
public class Data {

    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
