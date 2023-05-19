package com.gatsby.note.web;

import cn.hutool.core.io.FileUtil;
import com.gatsby.note.po.User;
import com.gatsby.note.service.UserService;
import com.gatsby.note.vo.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @PACKAGE_NAME: com.gatsby.note.web
 * @NAME: UserServlet
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */

@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("menu_page","user");

        String actionName = req.getParameter("actionName");
        if ("login".equals(actionName)){
            userLogin(req,resp);
        }else if("logout".equals(actionName)){
            userLogout(req,resp);
        } else if("userCenter".equals(actionName)){
            userCenter(req,resp);
        }else if("userHead".equals(actionName)){
            userHead(req,resp);
        }else if("checkNick".equals(actionName)){
            checkNick(req,resp);
        }else if("updateUser".equals(actionName)){
            updateUser(req,resp);
        }
    }

    // 修改用户信息
    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultInfo<User> resultInfo = userService.updateUser(req,resp);
        req.setAttribute("resultInfo",resultInfo);
        req.getRequestDispatcher("user?actionName=userCenter").forward(req,resp);
    }

    // 检查昵称是否唯一
    private void checkNick(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nick = req.getParameter("nick");
        User user = (User) req.getSession().getAttribute("user");
        Integer code = userService.checkNick(nick,user.getUserId());
        resp.getWriter().write(code + "");
        resp.getWriter().close();
    }

    // 显示用户头像
    private void userHead(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String head = req.getParameter("imageName");
        String realPath = req.getServletContext().getRealPath("/WEB-INF/upload/");
        File file = new File(realPath + "/" + head);
        String pic = head.substring(head.lastIndexOf(".")+1);
        if ("PNG".equalsIgnoreCase(pic)){
            resp.setContentType("image/png");
        }else if ("JPG".equalsIgnoreCase(pic) || "JPEG".equalsIgnoreCase(pic)){
            resp.setContentType("image/jpeg");
        }else if("GIF".equalsIgnoreCase(pic)){
            resp.setContentType("image/gif");
        }

        FileUtils.copyFile(file,resp.getOutputStream());
    }

    // 用户中心模块
    private void userCenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("changePage","user/info.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    private void userLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        Cookie cookie = new Cookie("user",null);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);;
        resp.sendRedirect("login.jsp");
    }

    // 用户登录模块
    private void userLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        ResultInfo<User> resultInfo = userService.userLogin(username, password);

        if (resultInfo.getCode() == 1){
            req.getSession().setAttribute("user",resultInfo.getResult());
            String rem = req.getParameter("rem");
            if ("1".equals(rem)){
                Cookie cookie = new Cookie("user", username + "-" + password);
                cookie.setMaxAge(3*24*60*60);
                resp.addCookie(cookie);
            }else {
                Cookie cookie = new Cookie("user", null);
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }

            resp.sendRedirect("index");
        }else{
            req.setAttribute("resultInfo",resultInfo);
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }
}
