package com.gatsby.note.dao;

import com.gatsby.note.po.User;
import com.gatsby.note.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.gatsby.note.dao
 * @NAME: UserDao
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */
public class UserDao {

    /**
     * 通过用户名查询用户对象
     * 1.定义sql语句
     * 2.设置参数集合
     * 3.调用BaseDao查询方法
     * @param username
     * @return
     */
    public User queryUserByName(String username){

        String sql = "select * from tb_user where uname = ?";

        List<Object> params = new ArrayList<>();
        params.add(username);

        User user = (User) BaseDao.queryRow(sql, params, User.class);

        return user;
    }

    public User queryUserByName02(String username) {
        User user = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "select * from tb_user where uname = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUname(username);
                user.setHead(resultSet.getString("head"));
                user.setNick(resultSet.getString("nick"));
                user.setMood(resultSet.getString("mood"));
                user.setUpwd(resultSet.getString("upwd"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(resultSet, preparedStatement, conn);
        }

        return user;
    }
}
