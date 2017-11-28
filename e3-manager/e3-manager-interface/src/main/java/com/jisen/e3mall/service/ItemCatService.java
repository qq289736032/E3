package com.jisen.e3mall.service;

import java.util.List;

import com.jisen.e3mall.common.pojo.EasyUITreeNode;

/**
 * 商品分类列表
 * @author Administrator
 * 根据parent_id去查询节点列表,parent是bigint所以参数类型是long
 */
public interface ItemCatService {
	List<EasyUITreeNode> getItemList(long parentId);  
}
