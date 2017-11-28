package com.jisen.e3mall.content.service;

import java.util.List;

import com.jisen.e3mall.common.pojo.EasyUIDataGridResult;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.pojo.TbContent;

/**
 * 内容管理
 * @author Administrator
 *
 */
public interface ContentService {
	
	E3Result addContent(TbContent content);
	List<TbContent> getContentListByCid(long cid);
	//内容成列http://localhost:8080/content/query/list?categoryId=0&page=1&rows=20
	EasyUIDataGridResult getContentPageByCatId(long cid, int page,int rows);
}
