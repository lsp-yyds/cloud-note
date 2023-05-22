package com.gatsby.note.dao;

import cn.hutool.core.util.StrUtil;
import com.gatsby.note.po.Note;
import com.gatsby.note.vo.NoteVo;

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
        String sql = "";

        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        if (note.getNoteId() == null){
            sql = "insert into tb_note (typeId, title, content, pubTime) values(?, ?, ?, now())";
        }else {
            sql = "update tb_note set typeId = ?, title = ?, content = ? where noteId = ? ";
            params.add(note.getNoteId());
        }

        int row = BaseDao.executeUpdate(sql, params);

        return row;
    }

    public long findNoteCount(Integer userId, String title, String date, String typeId) {
        String sql = "select count(1) from tb_note n inner join tb_note_type t on n.typeId = t.typeId where userId = ?";

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (!StrUtil.isBlank(title)){
            sql += " and title like concat('%',?,'%') ";
            params.add(title);
        }else if (!StrUtil.isBlank(date)){
            sql += " and date_format(pubTime, '%Y年%m月') = ? ";
            params.add(date);
        } else if (!StrUtil.isBlank(typeId)) {
            sql += " and n.typeId = ? ";
            params.add(typeId);
        }

        long count = (long) BaseDao.findSingleValue(sql, params);

        return count;
    }

    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize, String title, String date, String typeId) {
        String sql = "select noteId, title, pubTime from tb_note n inner join tb_note_type t on n.typeId = t.typeId where userId = ? ";

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (!StrUtil.isBlank(title)){
            sql += " and title like concat('%',?,'%') ";
            params.add(title);
        }else if (!StrUtil.isBlank(date)){
            sql += " and date_format(pubTime, '%Y年%m月') = ? ";
            params.add(date);
        }else if (!StrUtil.isBlank(typeId)) {
            sql += " and n.typeId = ? ";
            params.add(typeId);
        }

        sql += " order by pubTime desc limit ?, ?";
        params.add(index);
        params.add(pageSize);

        List<Note> noteList = BaseDao.queryRows(sql,params,Note.class);

        return noteList;
    }

    public List<NoteVo> findNoteListByDate(Integer userId) {
        String sql = "select count(1) noteCount, DATE_FORMAT(pubTime, '%Y年%m月') groupName from tb_note n inner join tb_note_type t on n.typeId = t.typeId where userId = ? group by DATE_FORMAT(pubTime, '%Y年%m月') order by DATE_FORMAT(pubTime, '%Y年%m月') desc";

        List<Object> params = new ArrayList<>();
        params.add(userId);

        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    public List<NoteVo> findNoteListByType(Integer userId) {
        String sql = "select count(noteId) noteCount, t.typeId, typeName groupName from tb_note n right join tb_note_type t on t.typeId = n.typeId where userId = ? group by t.typeId order by count(noteId) desc";

        List<Object> params = new ArrayList<>();
        params.add(userId);

        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    // 通过id查询
    public Note findNoteById(String noteId) {
        String sql = "select noteId, title, content, pubTime, typeName, n.typeId from tb_note n inner join tb_note_type t on n.typeId = t.typeId where noteId = ? ";

        List<Object> params = new ArrayList<>();
        params.add(noteId);

        Note note =(Note) BaseDao.queryRow(sql,params,Note.class);

        return note;
    }

    public int deleteNoteById(String noteId) {
        String sql = "delete from tb_note where noteId = ?";

        List<Object> params = new ArrayList<>();
        params.add(noteId);

        int row = BaseDao.executeUpdate(sql,params);

        return row;
    }
}
