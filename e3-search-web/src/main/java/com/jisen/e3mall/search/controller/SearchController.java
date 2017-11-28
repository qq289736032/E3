package com.jisen.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jisen.e3mall.common.pojo.SearchResult;
import com.jisen.search.service.SearchService;

/**
 * 商品搜索
 * @author Administrator
 */
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	
	@RequestMapping("/search")
	public String searchItemList(String keyword, @RequestParam(defaultValue="1") Integer page, Model model) throws Exception{
		//处理乱码
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		SearchResult searchResult = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		//把结果传递给页面
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages",searchResult.getTotalPages());
		model.addAttribute("recordCount",searchResult.getRecordCount());
		model.addAttribute("page",page);
		model.addAttribute("itemList",searchResult.getItemList());
		//返回逻辑视图
		return "search";
		
		
	}
}
