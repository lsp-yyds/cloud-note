package com.gatsby.note.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @PACKAGE_NAME: com.gatsby.note.po
 * @NAME: User
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */

@Getter
@Setter
public class User {
    private Integer userId;
    private String uname;
    private String upwd;
    private String nick;
    private String head;
    private String mood;
}
