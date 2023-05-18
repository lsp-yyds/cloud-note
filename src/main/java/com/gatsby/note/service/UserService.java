package com.gatsby.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.gatsby.note.dao.UserDao;
import com.gatsby.note.po.User;
import com.gatsby.note.vo.ResultInfo;

/**
 * @PACKAGE_NAME: com.gatsby.note.service
 * @NAME: UserService
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */
public class UserService {
    private UserDao userDao = new UserDao();

    public ResultInfo<User> userLogin(String username, String password){
        ResultInfo<User> resultInfo = new ResultInfo<>();

        User u = new User();
        u.setUname(username);
        u.setUpwd(password);
        resultInfo.setResult(u);

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名或密码不能为空！");
            return resultInfo;
        }

        User user = userDao.queryUserByName(username);

        if (user == null){
            resultInfo.setCode(0);
            resultInfo.setMsg("该用户不存在！");
            return resultInfo;
        }

        // password = DigestUtil.md5Hex(password);

        if (!password.equals(user.getUpwd())){
            resultInfo.setCode(0);
            resultInfo.setMsg("密码不正确！");
            return resultInfo;
        }

        resultInfo.setCode(1);
        resultInfo.setResult(user);
        return resultInfo;
    }
}
