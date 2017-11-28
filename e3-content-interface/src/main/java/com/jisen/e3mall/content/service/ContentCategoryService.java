package com.jisen.e3mall.content.service;

import java.util.List;

import com.jisen.e3mall.common.pojo.EasyUITreeNode;
import com.jisen.e3mall.common.utils.E3Result;

/**
 * 内容分类列表查询,返回一个TreeNode
 * @author Administrator
 *
 */
public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	E3Result addContentCategory(long parentId, String name);
}
