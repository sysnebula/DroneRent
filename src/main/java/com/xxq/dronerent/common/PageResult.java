package com.xxq.dronerent.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回对象
 * 用于封装分页查询的结果
 *
 * @param <T> 数据类型
 * @author yourcompany
 * @since 2024-01-01
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 每页显示条数
     */
    private Long pageSize;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private List<T> list;

    public PageResult() {
    }

    public PageResult(Long total, Long pageNum, Long pageSize, List<T> list) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = list;
        this.pages = (total + pageSize - 1) / pageSize;
    }

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> build(Long total, Long pageNum, Long pageSize, List<T> list) {
        return new PageResult<>(total, pageNum, pageSize, list);
    }
}
