package com.jisen.search.service;

import com.jisen.e3mall.common.pojo.SearchResult;

/**
 * 
 * @author Administrator
 *
 */
public interface SearchService {
	SearchResult search(String keyword, int page, int rows) throws Exception;
}
