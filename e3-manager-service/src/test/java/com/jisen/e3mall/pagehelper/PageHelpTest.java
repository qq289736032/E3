package com.jisen.e3mall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jisen.e3mall.mapper.TbItemMapper;
import com.jisen.e3mall.pojo.TbItem;
import com.jisen.e3mall.pojo.TbItemExample;

/**
 * 测试分页助手pagehelp
 * @author Administrator
 *
 */
public class PageHelpTest {
	
	//
	@Test
	public void testPageHelp(){
		//初始化spring容器,从容器中获得mapper代理对象
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
		//执行SQL语句之前设置分页信息使用PageHelp的startPage方法
		PageHelper.startPage(1, 10);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//取分页信息,PageInfo.总记录数rows,总页数,当前页码,
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPageSize());
		System.out.println(pageInfo.getPages());
		System.out.println(list.size());
		
		
		
		
	}
}
