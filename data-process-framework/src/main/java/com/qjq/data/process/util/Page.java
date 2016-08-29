package com.qjq.data.process.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 * 
 * @since 0.1.0
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 4734197461620267293L;

	private Integer offset = 0, pageNo = 1, pageSize = 20;
	private Long totalCount;
	private List<T> list = new ArrayList<>();
	
	public Page() {
		super();
	}
	
	public Page(Integer pageNo, Integer pageSize) {
	 	this(null, pageNo, pageSize);
	}

	public Page(Integer offset, Integer pageNo, Integer pageSize) {
		// 优先级 offset > pageNo
		if (offset != null && pageNo != null) {
			this.offset = offset;
			// 自动忽略pageNo
		} else if (offset != null) {
			this.offset = offset;
			this.pageNo = null;
		} else if (pageNo != null) {
			this.pageNo = pageNo;
			this.offset = null;
		}
		if (pageSize != null && pageSize > 0) {
			this.pageSize = pageSize;
		}
	}

	public Integer getOffset() {
		if (offset != null) return offset;
		return pageSize * (pageNo - 1);
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNo() {
		// if (pageNo == null) {
		if (offset == null) return pageNo;
		return offset / pageSize + 1;
		// }
		// return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotalPage() {
		return (totalCount + pageSize - 1) / pageSize;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}