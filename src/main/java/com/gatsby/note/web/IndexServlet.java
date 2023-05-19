package com.gatsby.note.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @PACKAGE_NAME: com.gatsby.note.web
 * @NAME: IndexServlet
 * @AUTHOR: Jonah
 * @DATE: 2023/5/18
 */

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("menu_page","index");

        req.setAttribute("changePage","note/list.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
