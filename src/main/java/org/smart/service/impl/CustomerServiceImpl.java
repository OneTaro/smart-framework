package org.smart.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.helper.DatabaseHelper;
import org.smart.model.Customer;
import org.smart.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomerServiceImpl
 * @Description 提供客户数据服务
 * @Author wangss
 * @date 2019.12.16 19:18
 * @Version 1.0
 */
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public List<Customer> getCustomerList() {

        return DatabaseHelper.queryEntityList(Customer.class, null, null);
    }

    @Override
    public Customer getCustomer(long id) {
        Map<String, Object> fieldMap = new HashMap<>(1);
        fieldMap.put("id", id);
        return DatabaseHelper.queryEntity(Customer.class, null, fieldMap);
    }

    @Override
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    @Override
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    @Override
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
