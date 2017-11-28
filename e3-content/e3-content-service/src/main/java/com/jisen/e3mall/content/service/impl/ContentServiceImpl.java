package com.jisen.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jisen.e3mall.common.jedis.JedisClient;
import com.jisen.e3mall.common.pojo.EasyUIDataGridResult;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.common.utils.JsonUtils;
import com.jisen.e3mall.content.service.ContentService;
import com.jisen.e3mall.mapper.TbContentMapper;
import com.jisen.e3mall.pojo.TbContent;
import com.jisen.e3mall.pojo.TbContentExample;


import com.jisen.e3mall.pojo.TbContentExample.Criteria;
/**
 * 内容管理
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	public E3Result addContent(TbContent content) {
		//将内容数据插入到内容表,将pojo补全
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//执行插入
		contentMapper.insert(content);
		//删除缓存中对应的旧缓存,使其跟新
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}


	/**
	 * 根据内容分类id查询内容
	 * 加入redis缓存,查询条件为key,响应结果为value添加到缓存中,将value转换为字符串
	 */
	public List<TbContent> getContentListByCid(long cid) {
		try {
			String json = jedisClient.hget(CONTENT_LIST, cid+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//查询缓存
		
		//如果缓存有数据直接响应结果
		//如果没有则查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//结果添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}



	/**
	 * 后台内容分页列表
	 */
	public EasyUIDataGridResult getContentPageByCatId(long cid, int page, int rows) {
		//设置分页信息,输入当前页和每页显示
		PageHelper.startPage(page, rows);
		//执行查询,
		//如果cid=0则查询所有
		TbContentExample example = new TbContentExample();
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		
		if(cid==0){
			//查询所有的内容
			List<TbContent> list = contentMapper.selectByExample(example);
			result.setRows(list);
			//取分页结果
			PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
			result.setTotal(pageInfo.getTotal());
			return result;
		}else{
			//根据cid查询
			example.createCriteria().andCategoryIdEqualTo(cid);
			List<TbContent> list = contentMapper.selectByExample(example);
			result.setRows(list);
			PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
			result.setTotal(pageInfo.getTotal());
			return result;
		}
	}

}
