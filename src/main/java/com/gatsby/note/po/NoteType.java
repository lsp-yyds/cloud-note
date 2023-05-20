package com.gatsby.note.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @PACKAGE_NAME: com.gatsby.note.po
 * @NAME: NoteType
 * @AUTHOR: Jonah
 * @DATE: 2023/5/20
 */

@Getter
@Setter
public class NoteType {
    private Integer typeId;
    private String typeName;
    private Integer userId;
}
