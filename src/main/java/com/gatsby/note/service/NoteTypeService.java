package com.gatsby.note.service;

import cn.hutool.core.util.StrUtil;
import com.gatsby.note.dao.NoteTypeDao;
import com.gatsby.note.po.NoteType;
import com.gatsby.note.vo.ResultInfo;

import java.util.List;

/**
 * @PACKAGE_NAME: com.gatsby.note.service
 * @NAME: NoteTypeService
 * @AUTHOR: Jonah
 * @DATE: 2023/5/20
 */
public class NoteTypeService {
    NoteTypeDao typeDao = new NoteTypeDao();

    public List<NoteType> findTypeList(Integer userId){
        List<NoteType> typeList = typeDao.findTypeListByUserId(userId);
        return typeList;
    }

    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();

        if (StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常，请重试");
            return resultInfo;
        }

        long noteCount = typeDao.findNoteCountByTypeId(typeId);

        if (noteCount > 0){
            resultInfo.setCode(0);
            resultInfo.setMsg("该类型存在子记录，不可删除！");
            return resultInfo;
        }

        int row = typeDao.deleteTypeById(typeId);

        if (row > 0){
            resultInfo.setCode(1);
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("删除类型失败！");
        }

        return resultInfo;
    }

    public ResultInfo<Integer> addOrUpdate(String typeName, Integer userId, String typeId) {
        ResultInfo<Integer> resultInfo = new ResultInfo<>();

        if (StrUtil.isBlank(typeName)){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称不能为空！");
            return resultInfo;
        }

        Integer code = typeDao.checkTypeName(typeName,userId,typeId);

        if (code == 0){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型名称已存在，请重新输入！");
            return resultInfo;
        }

        Integer key = null;

        if (StrUtil.isBlank(typeId)){
            key = typeDao.addType(typeName,userId);
        }else {
            key = typeDao.updateType(typeName,typeId);
        }

        if (key > 0){
            resultInfo.setCode(1);
            resultInfo.setResult(key);
        }else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
        }

        return resultInfo;
    }
}
