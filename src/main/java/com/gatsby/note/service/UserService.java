package com.gatsby.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.gatsby.note.dao.UserDao;
import com.gatsby.note.po.User;
import com.gatsby.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * @PACKAGE_NAME: com.gatsby.note.service
 * @NAME: UserService
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */
public class UserService {
    private UserDao userDao = new UserDao();

    public ResultInfo<User> userLogin(String username, String password) {
        ResultInfo<User> resultInfo = new ResultInfo<>();

        User u = new User();
        u.setUname(username);
        u.setUpwd(password);
        resultInfo.setResult(u);

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名或密码不能为空！");
            return resultInfo;
        }

        User user = userDao.queryUserByName(username);

        if (user == null) {
            resultInfo.setCode(0);
            resultInfo.setMsg("该用户不存在！");
            return resultInfo;
        }

        // password = DigestUtil.md5Hex(password);

        if (!password.equals(user.getUpwd())) {
            resultInfo.setCode(0);
            resultInfo.setMsg("密码不正确！");
            return resultInfo;
        }

        resultInfo.setCode(1);
        resultInfo.setResult(user);
        return resultInfo;
    }

    public Integer checkNick(String nick, Integer userId) {

        if (StrUtil.isBlank(nick)) {
            return 0;
        }

        User user = userDao.queryUserByNickAndUserId(nick, userId);

        if (user != null) {
            return 0;
        }

        return 1;
    }

    public ResultInfo<User> updateUser(HttpServletRequest req, HttpServletResponse resp) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        String nick = req.getParameter("nick");
        String mood = req.getParameter("mood");

        if (StrUtil.isBlank(nick)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空！");
            return resultInfo;
        }

        User user = (User) req.getSession().getAttribute("user");
        user.setNick(nick);
        user.setMood(mood);

        try {
            Part part = req.getPart("img");
            String header = part.getHeader("Content-Disposition");
            String str = header.substring(header.lastIndexOf("=") + 2);
            String fileName = str.substring(0, str.length() - 1);
            // 判断文件名是否为空
            if (!StrUtil.isBlank(fileName)){
                // 如果用户上传了头像，则更新用户对象中的头像
                user.setHead(fileName);
                String filePath = req.getServletContext().getRealPath("WEB-INF/upload/");
                part.write(filePath+"/"+fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int row = userDao.updateUser(user);

        if (row > 0){
            resultInfo.setCode(1);
            req.getSession().setAttribute("user",user);
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
