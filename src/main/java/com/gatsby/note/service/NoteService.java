package com.gatsby.note.service;

import cn.hutool.core.util.StrUtil;
import com.gatsby.note.dao.NoteDao;
import com.gatsby.note.po.Note;
import com.gatsby.note.vo.ResultInfo;

/**
 * @PACKAGE_NAME: com.gatsby.note.service
 * @NAME: NoteService
 * @AUTHOR: Jonah
 * @DATE: 2023/5/21
 */
public class NoteService {
    private NoteDao noteDao = new NoteDao();


    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content) {
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
        resultInfo.setResult(note);

        int row = noteDao.addOrUpdate(note);

        if (row > 0){
            resultInfo.setCode(1);
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
