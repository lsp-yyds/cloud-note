package com.gatsby.note.service;

import cn.hutool.core.util.StrUtil;
import com.gatsby.note.dao.NoteDao;
import com.gatsby.note.po.Note;
import com.gatsby.note.util.PageUtil;
import com.gatsby.note.vo.NoteVo;
import com.gatsby.note.vo.ResultInfo;

import java.util.List;

/**
 * @PACKAGE_NAME: com.gatsby.note.service
 * @NAME: NoteService
 * @AUTHOR: Jonah
 * @DATE: 2023/5/21
 */
public class NoteService {
    private NoteDao noteDao = new NoteDao();


    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content, String noteId) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();

        Note note = new Note();

        if (StrUtil.isBlank(typeId)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("请选择云记类型！");
            resultInfo.setResult(note);
            return resultInfo;
        }

        note.setTypeId(Integer.parseInt(typeId));

        if (StrUtil.isBlank(title)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记标题不能为空！");
            resultInfo.setResult(note);
            return resultInfo;
        }

        note.setTitle(title);

        if (StrUtil.isBlank(content)) {
            resultInfo.setCode(0);
            resultInfo.setMsg("云记内容不能为空！");
            resultInfo.setResult(note);
            return resultInfo;
        }

        note.setContent(content);

        if(!StrUtil.isBlank(noteId)){
            note.setNoteId(Integer.parseInt(noteId));
        }

        resultInfo.setResult(note);

        int row = noteDao.addOrUpdate(note);

        if (row > 0) {
            resultInfo.setCode(1);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }

    public PageUtil<Note> findNoteListByPage(String pageNumStr, String pageSizeStr, Integer userId, String title, String date, String typeId) {
        Integer pageNum = 1;
        Integer pageSize = 5;

        if (!StrUtil.isBlank(pageNumStr)) {
            pageNum = Integer.parseInt(pageNumStr);
        }

        if (!StrUtil.isBlank(pageSizeStr)) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        long count = noteDao.findNoteCount(userId, title, date, typeId);

        if (count < 1) {
            return null;
        }

        PageUtil<Note> page = new PageUtil<>(pageNum, pageSize, count);

        Integer index = (pageNum - 1) * pageSize;

        List<Note> noteList = noteDao.findNoteListByPage(userId, index, pageSize, title, date, typeId);

        page.setDataList(noteList);

        return page;
    }

    public List<NoteVo> findNoteListByDate(Integer userId) {
        return noteDao.findNoteListByDate(userId);
    }

    public List<NoteVo> findNoteListByType(Integer userId) {
        return noteDao.findNoteListByType(userId);
    }

    public Note findNoteById(String noteId) {
        if (StrUtil.isBlank(noteId)) {
            return null;
        }

        Note note = noteDao.findNoteById(noteId);
        return note;
    }

    public Integer deleteNote(String noteId) {
        if (StrUtil.isBlank(noteId)){
            return 0;
        }

        int row = noteDao.deleteNoteById(noteId);

        if (row > 0){
            return 1;
        }

        return 0;
    }
}
