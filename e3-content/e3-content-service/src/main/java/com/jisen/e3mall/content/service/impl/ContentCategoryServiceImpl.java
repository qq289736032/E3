package com.jisen.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jisen.e3mall.common.pojo.EasyUITreeNode;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.content.service.ContentCategoryService;
import com.jisen.e3mall.mapper.TbContentCategoryMapper;
import com.jisen.e3mall.pojo.TbContentCategory;
import com.jisen.e3mall.pojo.TbContentCategoryExample;
import com.jisen.e3mall.pojo.TbContentCategoryExample.Criteria;
/**
 * 内容分类管理,根据parentid查询分类列表
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		//根据parentID查询子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		//转换成easyUItreenode的列表
		ArrayList<EasyUITreeNode> nodeLis = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode treeNode = new EasyUITreeNode();
			treeNode.setId(tbContentCategory.getId());
			treeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
			treeNode.setText(tbContentCategory.getName());
			//添加到列表
			nodeLis.add(treeNode);
		}
		return nodeLis;
	}

	/**
	 * 编辑分类并保存
	 */
	public E3Result addContentCategory(long parentId, String name) {
		//创建一个tb_content_category表对应的pojo,(为什么不直接用pojo就收参数)
		TbContentCategory contentCategory = new TbContentCategory();
		//创建pojo属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//1(正常),2删除
		contentCategory.setStatus(1);
		//默认排序是1
		contentCategory.setSortOrder(1);
		//新添加的节点是叶子节点
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//插入到数据库,返回主键
		contentCategoryMapper.insert(contentCategory);
		//判断父节点的isparent属性,如果不是true该为True
		//根据parentId查询父节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		//返回e3result,包含pojo
		return E3Result.ok(contentCategory);
	}

}
