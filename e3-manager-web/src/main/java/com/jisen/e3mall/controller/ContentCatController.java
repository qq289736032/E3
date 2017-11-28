package com.jisen.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisen.e3mall.common.pojo.EasyUITreeNode;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类管理
 * @author Administrator
 *
 */
@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody								/*形参名称与前端jsp名称不一致时,且可能为空时可以用@RequestParam,且父id最高级开始查,最高级默认为0*/
	public List<EasyUITreeNode> getContentCatList(@RequestParam(name="id",defaultValue="0",required=true)Long parentId){
		List<EasyUITreeNode> categoryList = contentCategoryService.getContentCategoryList(parentId);
		return categoryList;
	}
	
	/**
	 * 添加分类节点http://localhost:8080/content/category/create
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result creatContentCategory(Long parentId,String name){
		//调用服务
		E3Result result = contentCategoryService.addContentCategory(parentId, name);
		return result;
		
	}
	
}
