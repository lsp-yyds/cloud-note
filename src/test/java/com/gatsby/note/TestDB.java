package com.gatsby.note;

import com.gatsby.note.util.DBUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @PACKAGE_NAME: com.gatsby.note
 * @NAME: TestDB
 * @AUTHOR: Jonah
 * @DATE: 2023/5/17
 */
public class TestDB {
    private Logger logger = LoggerFactory.getLogger(TestDB.class);

    @Test
    public void testDB() {
        // System.out.println(DBUtil.getConnection());
        logger.info("获取数据库连接：" + DBUtil.getConnection());
        logger.info("获取数据库{}连接", DBUtil.getConnection());
        logger.info("获取数据库连接：{}", DBUtil.getConnection());
    }
}
