package com.jisen.e3mall.search.mapper;

import java.util.List;

import com.jisen.e3mall.common.pojo.SearchItem;

/**
 * 用于搜索的mapper
 * @author Administrator
 */
public interface ItemMapper {
	//查询所有的商品
	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
