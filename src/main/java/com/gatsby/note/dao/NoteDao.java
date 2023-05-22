package com.gatsby.note.dao;

import com.gatsby.note.po.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.gatsby.note.dao
 * @NAME: NoteDao
 * @AUTHOR: Jonah
 * @DATE: 2023/5/21
 */
public class NoteDao {
    public int addOrUpdate(Note note) {
        String sql = "insert into tb_note (typeId, title, content, pubTime) values(?, ?, ?, now())";

        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        int row = BaseDao.executeUpdate(sql, params);

        return row;
    }
}
