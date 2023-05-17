package com.gatsby.note.dao;

import com.gatsby.note.util.DBUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.gatsby.note.dao
 * @NAME: BaseDao
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 * <p>
 * 基础的JDBC操作类
 * 更新操作
 * 查询操作
 */
public class BaseDao {

    /**
     * 更新操作
     * 添加、修改、删除
     * 1.得到数据库链接
     * 2.定义sql语句
     * 3.预编译
     * 4.如果有参数，则设置参数，从下标1开始
     * 5.执行更新，返回受影响的参数
     * 6.关闭资源
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, List<Object> params) {
        int row = 0;
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = DBUtil.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            if (params != null && params.size() > 0) {
                for (int i = 0; i < params.size(); ++i){
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }

            row = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, preparedStatement, conn);
        }

        return row;
    }

    public static Object findSingleValue(String sql, List<Object> params){
        Object obj = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            conn = DBUtil.getConnection();
            preparedStatement = conn.prepareStatement(sql);

            if (params != null && params.size() > 0) {
                for (int i = 0; i < params.size(); ++i){
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                obj = resultSet.getObject(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,conn);
        }

        return obj;
    }

    public static List queryRows(String sql, List<Object> params, Class cls){
        List list = new ArrayList();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DBUtil.getConnection();
            preparedStatement = conn.prepareStatement(sql);

            if (params != null && params.size() > 0){
                for (int i = 0; i < params.size(); ++i) {
                    preparedStatement.setObject(i+1,params.get(i));
                }
            }

            resultSet = preparedStatement.executeQuery();

            // 得到结果集的元数据对象（查询到的子段数量以及查询了哪些字段）
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 得到查询的字段数量
            int fieldNum = resultSetMetaData.getColumnCount();

            // 判断分析结果集
            while (resultSet.next()){
                // 实例化对象
                Object object = cls.newInstance();
                // 遍历查询的字段数量，得到数据库中查询的每一个列名
                for (int i = 1; i <= fieldNum; ++i){
                    // 得到查询的每一个列名
                    // getColumnLabel(): 获取列名或别名
                    // getColumnName(): 获取列名
                    String columnLabel = resultSetMetaData.getColumnLabel(i);
                    // 通过反射使用列名得到对应的field对象
                    Field field = cls.getDeclaredField(columnLabel);
                    // 拼接set方法，得到字符串
                    String setMethod = "set" + columnLabel.substring(0,1).toUpperCase() + columnLabel.substring(1);
                    // 通过反射，将set方法字符串反射成类中对应的set方法
                    Method method = cls.getDeclaredMethod(setMethod,field.getType());
                    // 得到查询的每个字段对应的值
                    Object value = resultSet.getObject(columnLabel);
                    // 通过invoke方法调用set方法
                    method.invoke(object,value);
                }
                // 将JavaBean设置到集合中
                list.add(object);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,conn);
        }

        return list;
    }

    public static Object queryRow(String sql, List<Object> params, Class cls){
        List list = queryRows(sql, params,cls);
        Object obj = null;

        if (list != null && list.size() > 0)
            obj = list.get(0);

        return obj;
    }
}
