package org.smart.service;

import org.smart.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomerServiceImpl
 * @Description 提供客户数据服务
 * @Author wangss
 * @date 2019.12.16 19:18
 * @Version 1.0
 */
public interface CustomerService {

    /**
     * 获取客户列表
     *
     * @return
     */
    List<Customer> getCustomerList();

    /**
     * 获取客户
     *
     * @param id
     * @return
     */
    Customer getCustomer(long id);

    /**
     * 创建客户
     *
     * @param fieldMap
     * @return
     */
    boolean createCustomer(Map<String, Object> fieldMap);

    /**
     * 更新客户
     *
     * @param id
     * @param fieldMap
     * @return
     */
    boolean updateCustomer(long id, Map<String, Object> fieldMap);

    /**
     * 删除客户
     *
     * @param id
     * @return
     */
    boolean deleteCustomer(long id);
}
