package com.jisen.e3mall.common.pojo;

import java.io.Serializable;

/**
 * 列展示商品分类列表,采用easyUI的树形tree控件展示,因此返回treeNode,该treenode是一串json数据
 * 
 * @author Administrator
 */
public class EasyUITreeNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * [{ "id": 1, "text": "Node 1", "state": "closed" },
	 * { "id": 2, "text": "Node 2", "state": "closed" }]
	 */
	
	private Long id;
	private String text;
	private String state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
