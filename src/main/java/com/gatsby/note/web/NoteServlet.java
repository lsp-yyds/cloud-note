package com.gatsby.note.web;

import cn.hutool.core.util.StrUtil;
import com.gatsby.note.po.Note;
import com.gatsby.note.po.NoteType;
import com.gatsby.note.po.User;
import com.gatsby.note.service.NoteService;
import com.gatsby.note.service.NoteTypeService;
import com.gatsby.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.gatsby.note.web
 * @NAME: NoteServlet
 * @AUTHOR: Jonah
 * @DATE: 2023/5/21
 */

@WebServlet("/note")
public class NoteServlet extends HttpServlet {

    private NoteService noteService = new NoteService();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("menu_page","note");

        String actionName = req.getParameter("actionName");

        if ("view".equals(actionName)){
            noteView(req,resp);
        } else if("addOrUpdate".equals(actionName)){
            addOrUpdate(req,resp);
        } else if("detail".equals(actionName)){
            noteDetail(req,resp);
        } else if("delete".equals(actionName)){
            noteDelete(req,resp);
        }
    }

    // 删除云记
    private void noteDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String noteId = req.getParameter("noteId");
        Integer code = noteService.deleteNote(noteId);

        resp.getWriter().write(code + "");
        resp.getWriter().close();
    }

    private void noteDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String noteId = req.getParameter("noteId");

        Note note = noteService.findNoteById(noteId);

        req.setAttribute("note",note);

        req.setAttribute("changePage","note/detail.jsp");
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }

    private void addOrUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String typeId = req.getParameter("typeId");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        String noteId = req.getParameter("noteId");

        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId,title,content,noteId);

        if (resultInfo.getCode() == 1){
            resp.sendRedirect("index");
        }else {
            req.setAttribute("resultInfo",resultInfo);

            String url = "note?actionName=view";
            if (!StrUtil.isBlank(noteId)){
                url += "&noteId=" + noteId;
            }
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }

    private void noteView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String noteId = req.getParameter("noteId");

        Note note = noteService.findNoteById(noteId);

        req.setAttribute("noteInfo",note);

        User user = (User) req.getSession().getAttribute("user");

        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());

        req.setAttribute("typeList",typeList);

        req.setAttribute("changePage","note/view.jsp");

        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
