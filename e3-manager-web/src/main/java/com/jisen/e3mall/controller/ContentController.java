package com.jisen.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisen.e3mall.common.pojo.EasyUIDataGridResult;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.content.service.ContentService;
import com.jisen.e3mall.pojo.TbContent;

/**
 * 内容管理
 * 
 * @author Administrator
 *
 */
@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	/*
	 * http://localhost:8080/content/save 
	 * categoryId 91 
	 * content 内容啊啊啊啊啊啊啊啊啊啊啊啊啊
	 * pic http://192.168.25.133/group1/M00/00/00/wKgZhVmRXdOAII8AAABUZFrUPH8377.jpg
	 * pic2 http://192.168.25.133/group1/M00/00/00/wKgZhVmRXd2AdfkuAABUZFrUPH8756.jpg
	 * subTitle 子标题 
	 * title 插入内容 
	 * titleDesc 内容描述巴拉巴拉拉 
	 * url uri~~~~~~~~~
	 */
	@RequestMapping(value="/content/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent content){
		//调用服务
		E3Result result = contentService.addContent(content);
		return result;
	}
	
	/**
	 * 分页查询
	 * http://localhost:8080/content/query/list?categoryId=0&page=1&rows=20
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getPageListByCid(Long categoryId,Integer page, Integer rows){
		EasyUIDataGridResult gridResult = contentService.getContentPageByCatId(categoryId, page, rows);
		return gridResult;
		
	}
}
