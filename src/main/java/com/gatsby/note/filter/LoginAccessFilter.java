package com.gatsby.note.filter;

import com.gatsby.note.po.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @PACKAGE_NAME: com.gatsby.note.filter
 * @NAME: LoginAccessFilter
 * @AUTHOR: Jonah
 * @DATE: 2023/5/18
 */

/**
 * 需要放行的资源
 *   1.指定页面
 *   2.静态资源
 *   3.指定行为（无须登录即可执行的操作）
 *   4.登录状态（判断session作用域中是否存在user对象）
 */

@WebFilter("/*")
public class LoginAccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String path = req.getRequestURI();
        if (path.contains("/login.jsp")){
            filterChain.doFilter(req,resp);
            return;
        }

        if (path.contains("/resources")){
            filterChain.doFilter(req,resp);
            return;
        }

        if (path.contains("/user")){
            String actionName = req.getParameter("actionName");
            if ("login".equals(actionName)){
                filterChain.doFilter(req,resp);
                return;
            }
        }

        User user =(User) req.getSession().getAttribute("user");
        if (user != null){
            filterChain.doFilter(req,resp);
            return;
        }

        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if("user".equals(cookie.getName())){
                    String value = cookie.getValue();

                    String[] val = value.split("-");
                    String username = val[0];
                    String password = val[1];
                    String url = "user?actionName=login&username="+username+"&password="+password;
                    req.getRequestDispatcher(url).forward(req,resp);
                    return;
                }
            }
        }

        resp.sendRedirect("login.jsp");
    }

    @Override
    public void destroy() {

    }
}
