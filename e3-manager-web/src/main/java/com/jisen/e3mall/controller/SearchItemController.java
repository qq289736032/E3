package com.jisen.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.search.service.SearchItemService;

/**
 * 导入数据到索引库
 * @author Administrator
 *
 */
@Controller
public class SearchItemController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	/*
	 * /index/item/import
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItemList(){
		E3Result e3Result = searchItemService.importAllItems();
		return e3Result;
		
	}
}
