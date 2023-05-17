package com.gatsby.note.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @PACKAGE_NAME: com.gatsby.note.vo
 * @NAME: ResultInfo
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */

@Getter
@Setter
public class ResultInfo<T> {
    private Integer code;
    private String msg;
    private T result;
}
