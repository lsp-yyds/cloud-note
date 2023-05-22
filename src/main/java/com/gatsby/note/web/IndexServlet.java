package com.gatsby.note.web;

import com.gatsby.note.po.Note;
import com.gatsby.note.po.User;
import com.gatsby.note.service.NoteService;
import com.gatsby.note.util.PageUtil;
import com.gatsby.note.vo.NoteVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

        req.setAttribute("menu_page", "index");

        String actionName = req.getParameter("actionName");
        req.setAttribute("action",actionName);

        // 分页查询云记列表
        if ("searchTitle".equals(actionName)) {
            String title = req.getParameter("title");
            req.setAttribute("title", title);

            noteList(req, resp, title, null, null);
        } else if ("searchDate".equals(actionName)) {
            String date = req.getParameter("date");
            req.setAttribute("date", date);

            noteList(req, resp, null, date, null);
        } else if ("searchType".equals(actionName)) {
            String typeId = req.getParameter("typeId");
            req.setAttribute("typeId", typeId);

            noteList(req, resp, null, null, typeId);
        } else {
            noteList(req, resp, null, null, null);
        }


        req.setAttribute("changePage", "note/list.jsp");
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    private void noteList(HttpServletRequest req, HttpServletResponse resp, String title, String date, String typeId) {
        String pageNum = req.getParameter("pageNum");
        String pageSize = req.getParameter("pageSize");

        User user = (User) req.getSession().getAttribute("user");

        PageUtil<Note> page = new NoteService().findNoteListByPage(pageNum, pageSize, user.getUserId(), title, date, typeId);

        req.setAttribute("page", page);

        List<NoteVo> dateInfo = new NoteService().findNoteListByDate(user.getUserId());
        req.getSession().setAttribute("dateInfo", dateInfo);

        List<NoteVo> typeInfo = new NoteService().findNoteListByType(user.getUserId());
        req.getSession().setAttribute("typeInfo", typeInfo);
    }
}
