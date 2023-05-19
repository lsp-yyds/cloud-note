package com.gatsby.note;

import java.nio.charset.Charset;

/**
 * @PACKAGE_NAME: com.gatsby.note
 * @NAME: Test
 * @AUTHOR: Jonah
 * @DATE: 2023/5/19
 */
public class Test {
    public static void main(String[] args) {
        String str= "your string";
        // Charset charset = Charset.forName("UTF-8");

        // System.out.println(charset.displayName());
        System.out.println(System.getProperty("file.encoding"));
        System.out.println(Charset.defaultCharset());
    }
}
