package com.gatsby.note.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @PACKAGE_NAME: com.gatsby.note.vo
 * @NAME: NoteVo
 * @AUTHOR: Jonah
 * @DATE: 2023/5/22
 */

@Getter
@Setter
public class NoteVo {
    private String groupName; // 分组名称
    private long noteCount; // 云记数量

    private Integer typeId; // 类型ID
}
