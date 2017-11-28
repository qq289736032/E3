package com.jisen.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jisen.e3mall.common.jedis.JedisClient;
import com.jisen.e3mall.common.pojo.EasyUIDataGridResult;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.common.utils.IDUtils;
import com.jisen.e3mall.common.utils.JsonUtils;
import com.jisen.e3mall.mapper.TbItemDescMapper;
import com.jisen.e3mall.mapper.TbItemMapper;
import com.jisen.e3mall.pojo.TbItem;
import com.jisen.e3mall.pojo.TbItemDesc;
import com.jisen.e3mall.pojo.TbItemDescExample;
import com.jisen.e3mall.pojo.TbItemExample;
import com.jisen.e3mall.pojo.TbItemExample.Criteria;
import com.jisen.e3mall.service.ItemService;

/**
 * 商品管理
 * 
 * @author Administrator
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired // 根据类型注入,Java消息服务
	private JmsTemplate jmsTemplate;
	@Resource // 功能比autowired强大,首先尝试类型注入,然后尝试id注入
	private Destination topicDestination;

	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;

	public TbItem getItemById(long itemId) {
		// 查询缓存缓存
		try {
			String json = jedisClient.get("REDIS_ITEM_PRE" + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有,查询数据库
		// 根据主键查询
		// TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);

		// 根据查询条件查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andIdEqualTo(itemId);
		// 执行查询
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			// 把结果添加到缓存
			try {
				jedisClient.set("REDIS_ITEM_PRE" + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
				// 设置过期时间
				jedisClient.expire("REDIS_ITEM_PRE" + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	// 查询分页,条件当前页,显示条数,返回datagrid的json数据
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		// 创建一个返回值对象,进行封装
		EasyUIDataGridResult gridResult = new EasyUIDataGridResult();
		gridResult.setRows(list);
		// 取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		// 取总数
		gridResult.setTotal(pageInfo.getTotal());
		return gridResult;
	}

	/**
	 * 添加商品,插入商品表和商品描述表,返回值封装给E3Result 1.商品id不能采取自增也不能用uuid,采用商品id生成工具
	 */
	public E3Result addItem(TbItem item, String desc) {
		// 生成商品id
		final long itemId = IDUtils.genItemId();
		// 补全Item字段
		item.setId(itemId);
		// 1-正常,2-下架,3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		tbItemMapper.insert(item);
		// 创建商品描述表pojo
		TbItemDesc itemDesc = new TbItemDesc();
		// 将商品描述表补全字段
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setItemId(itemId);
		itemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		tbItemDescMapper.insert(itemDesc);
		// 发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// session
				TextMessage message = session.createTextMessage(itemId + "");
				return message;
			}
		});
		// 返回成功
		return E3Result.ok();
	}

	/**
	 * 修改商品 /**http://localhost:8080/rest/item/update 商品修改功能 barcode 12345678
	 * cid 560 desc 很好的手机 id 150259284773962 image
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPvnKAX3J4AAJn3r1ulnc409.
	 * jpg,
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPvnKADUxWAADON5gZA4c470.
	 * jpg,
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPvnKAFyCEAACFQgzdDhI110.
	 * jpg,
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPvnKAPCD4AABagtBmQTA499.
	 * jpg, http://192.168.25.133
	 * /group1/M00/00/00/wKgZhVmPvnKAHq5WAADlhPSxOOg388.jpg itemParamId
	 * itemParams [] num 354 price 299900 priceView 299.90 sellPoint
	 * 8月14日10:00抢购！骁龙835 旗舰处理器， 6GB 大内存，5.15”四曲面机身！变焦双摄拍人更美！小米5X，变焦双摄，拍人更美！
	 * title小米6 全网通 6GB+128GB 陶瓷黑尊享版 移动联通电信4G手机 双卡双待
	 */
	public E3Result updateItem(final TbItem item, String desc) {
		// 补全Item字段
		// 1-正常,2-下架,3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		tbItemMapper.updateByPrimaryKey(item);
		// 创建商品描述表pojo
		TbItemDesc itemDesc = new TbItemDesc();
		// 将商品描述表补全字段
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		// 向商品描述表插入数据
		tbItemDescMapper.updateByPrimaryKeyWithBLOBs(itemDesc);
		// 修改后发送消息更新索引库
		// 发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// session
				TextMessage message = session.createTextMessage(item.getId() + "");
				return message;
			}
		});
		// 返回成功
		return E3Result.ok();
	}

	// 商品描述回显
	public E3Result getDescById(long id) {
		TbItemDescExample example = new TbItemDescExample();
		example.createCriteria().andItemIdEqualTo(id);
		List<TbItemDesc> list = tbItemDescMapper.selectByExampleWithBLOBs(example);
		TbItemDesc itemDesc = list.get(0);
		return E3Result.ok(itemDesc);
	}

	// 前端页面商品详情,根据id获取商品描述,selectByPrimarykey也包含大文本,因此两种都可以
	public TbItemDesc getItemDescById(long itemId) {
		// 查询缓存缓存
		try {
			String json = jedisClient.get("REDIS_ITEM_PRE" + ":" + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有,查询数据库
		TbItemDescExample example = new TbItemDescExample();
		example.createCriteria().andItemIdEqualTo(itemId);
		List<TbItemDesc> list = tbItemDescMapper.selectByExampleWithBLOBs(example);
		TbItemDesc itemDesc = list.get(0);
		// 把结果添加到缓存
		try {
			jedisClient.set("REDIS_ITEM_PRE" + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			// 设置过期时间
			jedisClient.expire("REDIS_ITEM_PRE" + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
