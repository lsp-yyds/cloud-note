package com.gatsby.note;

import com.gatsby.note.dao.BaseDao;
import com.gatsby.note.dao.UserDao;
import com.gatsby.note.po.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.gatsby.note
 * @NAME: TestUser
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */

public class TestUser {

    @Test
    public void testQueryUserByName(){
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }

    @Test
    public void testExecuteUpdate(){
        String sql = "insert into tb_user(uname, upwd, nick, head, mood) values(?,?,?,?,?)";
        List<Object> list = new ArrayList<>();
        list.add("zhaoliu");
        list.add("123456");
        list.add("zhaoliu");
        list.add("404.jpg");
        list.add("Hello World");
        int row = BaseDao.executeUpdate(sql, list);
        System.out.println(row);
    }
}
