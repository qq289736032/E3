package com.jisen.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 * @author Administrator
 */
@Controller
public class PageController {
	
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	//http://localhost:8080/item-list?_=1502420655756
	//陈列商品,从路径中取出来,因此用@PathVariable
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
}
