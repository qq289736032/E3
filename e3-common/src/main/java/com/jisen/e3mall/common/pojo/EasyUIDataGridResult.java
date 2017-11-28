package com.jisen.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 该pojo用于封装返回页面的datagrid的json数据
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
public class EasyUIDataGridResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long total;
	private List  rows;
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
}
