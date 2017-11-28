package com.jisen.e3mall.service;

import com.jisen.e3mall.common.pojo.EasyUIDataGridResult;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.pojo.TbItem;
import com.jisen.e3mall.pojo.TbItemDesc;

public interface ItemService {
	public TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page,int rows);
	E3Result addItem(TbItem item, String desc);
	E3Result updateItem(TbItem item, String desc);
	TbItemDesc getItemDescById(long itemId);
	//商品描述回显
	E3Result getDescById(long id);
}
