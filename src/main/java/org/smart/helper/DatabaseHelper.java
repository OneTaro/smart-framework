package org.smart.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.util.CollectionUtil;
import org.smart.util.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.smart.common.IntegerConstant.ONE;
import static org.smart.common.IntegerConstant.ZERO;
import static org.smart.common.StringConstant.*;

/**
 * @ClassName DatabaseHelper
 * @Description 数据库操作助手类
 * @Author wangss
 * @date 2019.12.17 20:57
 * @Version 1.0
 */
public final class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    /**
     * 连接池
     */
    private static final BasicDataSource DATA_SOURCE;

    /**
     * 初始化时加载参数
     */
    static {

        CONNECTION_HOLDER = new ThreadLocal<>();
        QUERY_RUNNER = new QueryRunner();

        Properties conf = PropsUtil.loadProps("db.properties");
        String driver = PropsUtil.getString(conf, "jdbc.driver");
        String url = PropsUtil.getString(conf, "jdbc.url");
        String username = PropsUtil.getString(conf, "jdbc.username");
        String password = PropsUtil.getString(conf, "jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 查询实体列表（单表）
     *
     * @param entityClass
     * @param fieldList   查询字段
     * @param fieldMap    条件和值
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, List<String> fieldList, Map<String, Object> fieldMap) {
        List<T> entityList = null;
        Object[] params = null;
        StringBuilder sql = new StringBuilder(SELECT);
        try {
            Connection conn = getConnection();

            dealSelectColumns(entityClass, sql, fieldList);

            params = dealWhereColumns(fieldMap, sql);

            entityList = QUERY_RUNNER.query(conn, sql.toString(), new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询实体（单表）
     *
     * @param entityClass
     * @param fieldList
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, List<String> fieldList, Map<String, Object> fieldMap) {
        T entity = null;
        Object[] params = null;
        StringBuilder sql = new StringBuilder(SELECT);
        try {
            Connection conn = getConnection();

            dealSelectColumns(entityClass, sql, fieldList);

            params = dealWhereColumns(fieldMap, sql);

            entity = QUERY_RUNNER.query(conn, sql.toString(), new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        }

        return entity;
    }

    /**
     * 执行查询语句（多表查询）
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result;

        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("execute query failure", e);
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * 执行更新语句（包括 update、insert、delete）
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;

        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
            throw new RuntimeException(e);
        }

        return rows;
    }

    /**
     * 插入实体
     *
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = INSERT + getTableName(entityClass);

        StringBuilder columns = new StringBuilder(LEFT_PARENTHESIS);
        StringBuilder values = new StringBuilder(LEFT_PARENTHESIS);

        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(COMMA);
            values.append(QUESTION).append(COMMA);
        }

        columns.replace(columns.lastIndexOf(COMMA), columns.length(), RIGHT_PARENTHESIS);
        values.replace(values.lastIndexOf(COMMA), columns.length(), RIGHT_PARENTHESIS);

        sql += columns + VALUES + values;

        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == ONE;
    }

    /**
     * 更新实体
     *
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = UPDATE + getTableName(entityClass) + SET;

        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(EQUAL).append(QUESTION).append(COMMA);
        }
        sql += columns.substring(ZERO, columns.lastIndexOf(COMMA)) + WHERE + ID + EQUAL + QUESTION;

        List<Object> paramList = new ArrayList<>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == ONE;
    }

    /**
     * 删除实体
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = DELETE + FROM + getTableName(entityClass) + WHERE + ID + EQUAL + QUESTION;
        return executeUpdate(sql, id) == ONE;
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    /**
     * 处理查询字段
     *
     * @param entityClass
     * @param sql
     * @param fieldList
     */
    private static void dealSelectColumns(Class<?> entityClass, StringBuilder sql, List<String> fieldList) {

        // 根据是否有查询字段，拼接sql
        if (CollectionUtil.isEmpty(fieldList)) {
            sql.append(STAR).append(FROM).append(getTableName(entityClass));
        } else {
            StringBuilder selectColumns = new StringBuilder();
            for (String field : fieldList) {
                selectColumns.append(field).append(COMMA);
            }
            selectColumns.substring(ZERO, selectColumns.lastIndexOf(COMMA));
            sql.append(selectColumns).append(FROM).append(getTableName(entityClass));
        }

    }

    /**
     * 处理条件字段
     *
     * @param fieldMap
     * @param sql
     * @return
     */
    private static Object[] dealWhereColumns(Map<String, Object> fieldMap, StringBuilder sql) {

        if (CollectionUtil.isNotEmpty(fieldMap)) {
            StringBuilder whereColumns = new StringBuilder();
            for (String fieldName : fieldMap.keySet()) {
                whereColumns.append(fieldName).append(EQUAL).append(QUESTION).append(COMMA);
            }
            whereColumns.replace(whereColumns.lastIndexOf(COMMA), whereColumns.length(), SEMICOLON);
            sql.append(WHERE).append(whereColumns);

            return fieldMap.values().toArray();
        }

        return null;
    }

    public static void executeSqlFile(String filePath) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            String sql;
            while ((sql = br.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (IOException e) {
            LOGGER.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }
}
