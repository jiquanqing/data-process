/** Copyright 2013-2023 步步高商城. */
package com.qjq.data.process.util.jdbc;

import static com.qjq.data.process.util.UtilCollection.isEmpty;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页封装
 * 
 * @since 0.1.0
 */
public class Page<T> {
    /** 每页多少项(至少1项) */
    private Integer pageSize = 50;
    /** 总记录数 */
    private Integer total;
    /** 数据页码 */
    private Integer pageNo = 1;
    /** 页记录 */
    private List<T> items = new ArrayList<>();

    @Override
    public String toString() {
        return "Page@" + Integer.toHexString(hashCode()) + "[" + ("page=" + pageNo + "/" + pageSize + ", ")
                + (total != null ? "total=" + total + ", " : "")
                + (isEmpty(items) ? "" : "items[" + items.size() + "]=[" + items.get(0) + ",……]") + "]";
    }

    /** {@linkplain #pageSize} */
    public Integer getPageSize() {
        return pageSize;
    }

    /** {@linkplain #pageSize} */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /** {@linkplain #total} */
    public Integer getTotal() {
        return total;
    }

    /** {@linkplain #total} */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /** {@linkplain #pageNo} */
    public Integer getPageNo() {
        return pageNo;
    }

    /** {@linkplain #pageNo} */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    /** {@linkplain #items} */
    public List<T> getItems() {
        return items;
    }

    /** {@linkplain #items} */
    public void setItems(List<T> items) {
        this.items = items;
    }

    /** 必须在setTotal后调用，计算limits，并自动处理一些参数，如果pageNo超过了最大页，自动定位到最后一页 */
    public Integer[] getLimits() {
        if (pageSize == null) {
            pageSize = 100;
        } else if (pageSize < 1) {
            pageSize = 1;
        }
        if (pageNo == null || pageNo < 1)
            pageNo = 1;
        if (pageNo > 1 && pageNo > getMaxNo())
            pageNo = getMaxNo();

        return new Integer[] { pageSize * (pageNo - 1), pageSize };
    }

    /** 第一条记录的偏移量 */
    public Integer getOffset0() {
        return (pageNo - 1) * pageSize;
    }

    /** 最大页码 */
    public Integer getMaxNo() {
        return (total + pageSize - 1) / pageSize;
    }
}
