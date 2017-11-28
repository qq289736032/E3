package com.jisen.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisen.e3mall.common.pojo.EasyUITreeNode;
import com.jisen.e3mall.pojo.TbItemCat;
import com.jisen.e3mall.service.ItemCatService;

/**
 * 商品分类的管理
 * @author Administrator
 */
@Controller
public class ItemCatController {
	/*
	 * http://localhost:8080/item/cat/list
	 * [{ "id": 1, "text": "Node 1", "state": "closed" },
	 * { "id": 2, "text": "Node 2", "state": "closed" }]
	 */
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody//将返回的数据自动转换为json返回
	public List<EasyUITreeNode> getItemCatList(@RequestParam(name="id", defaultValue="0" ) Long parentId){
		//调用服务查询列表节点
		List<EasyUITreeNode> list = itemCatService.getItemList(parentId);
		return list;
	}
}
