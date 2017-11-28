package com.jisen.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jisen.e3mall.common.pojo.EasyUITreeNode;
import com.jisen.e3mall.mapper.TbItemCatMapper;
import com.jisen.e3mall.pojo.TbItemCat;
import com.jisen.e3mall.pojo.TbItemCatExample;
import com.jisen.e3mall.pojo.TbItemCatExample.Criteria;
import com.jisen.e3mall.service.ItemCatService;

/**
 * 商品分类列表
 * @author Administrator
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
	/**
	 * 调用dao层实现业务
	 */
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	
	public List<EasyUITreeNode> getItemList(long parentId) {
		//根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		//创建返回结果list
		List<EasyUITreeNode> resultList = new ArrayList<EasyUITreeNode>();
		//把列表转换成EasyUITreeNode列表
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			//设置属性
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		//返回结果
		return resultList;
	}

}
