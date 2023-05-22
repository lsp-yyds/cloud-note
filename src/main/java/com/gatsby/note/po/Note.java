package com.gatsby.note.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @PACKAGE_NAME: com.gatsby.note.po
 * @NAME: Note
 * @AUTHOR: Jonah
 * @DATE: 2023/5/21
 */

@Getter
@Setter
public class Note {
    private Integer noteId;
    private String title;
    private String content;
    private Integer typeId;
    private Date pubTime;

    private String typeName;
}
