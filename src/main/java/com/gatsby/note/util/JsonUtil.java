package com.gatsby.note.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @PACKAGE_NAME: com.gatsby.note.util
 * @NAME: JsonUtil
 * @AUTHOR: Jonah
 * @DATE: 2023/5/20
 */
public final class JsonUtil {

    private JsonUtil(){}

    public static void parse(HttpServletResponse resp, Object result){
        try {
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            String json = JSON.toJSONString(result);

            out.write(json);
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
