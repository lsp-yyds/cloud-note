package com.gatsby.note.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @PACKAGE_NAME: com.gatsby.note.util
 * @NAME: PageUtil
 * @AUTHOR: Jonah
 * @DATE: 2023/5/21
 */

@Getter
@Setter
public class PageUtil<E> {
    private Integer pageNum;  // 当前页 (前台传递的参数，若未传递，则默认第一页)
    private Integer pageSize; // 每页显示的数量 (前台传递或后台设定)
    private long totalCount;  // 总记录数 (count()函数)

    private Integer totalPages; // 总页数 (总记录数/每页显示的数量)
    private Integer prePage; // 下一页
    private Integer nextPage; // 上一页

    private Integer startNavPage; // 导航起始页
    private Integer endNavPage; // 导航结束页

    private List<E> dataList; // 当前页的数据集合

    public PageUtil(Integer pageNum, Integer pageSize, long totalCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;

        this.totalPages = (int) Math.ceil(totalCount / (pageSize * 1.0));

        this.prePage = Math.max(pageNum - 1, 1);
        this.nextPage = Math.min(pageNum + 1, totalPages);

        this.startNavPage = pageNum - 5;
        this.endNavPage = pageNum + 4;

        if (this.startNavPage < 1){
            this.startNavPage = 1;
            this.endNavPage = Math.min(this.startNavPage + 9, totalPages);
        }

        if (this.endNavPage > totalPages){
            this.endNavPage = totalPages;
            this.startNavPage = Math.max(this.endNavPage - 9, 1);
        }
    }
}
