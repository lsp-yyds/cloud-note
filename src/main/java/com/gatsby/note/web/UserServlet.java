package com.gatsby.note.web;

import com.gatsby.note.po.User;
import com.gatsby.note.service.UserService;
import com.gatsby.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @PACKAGE_NAME: com.gatsby.note.web
 * @NAME: UserServlet
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionName = req.getParameter("actionName");
        if ("login".equals(actionName)){
            userLogin(req,resp);
        }else if("logout".equals(actionName)){
            userLogout(req,resp);
        }
    }

    private void userLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        Cookie cookie = new Cookie("user",null);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);;
        resp.sendRedirect("login.jsp");
    }

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
