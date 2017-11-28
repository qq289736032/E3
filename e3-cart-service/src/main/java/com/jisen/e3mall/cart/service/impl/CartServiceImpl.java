package com.jisen.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jisen.e3mall.cart.service.CartService;
import com.jisen.e3mall.common.jedis.JedisClient;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.common.utils.JsonUtils;
import com.jisen.e3mall.mapper.TbItemMapper;
import com.jisen.e3mall.pojo.TbItem;

/**
 * 购物车处理服务
 * 
 * @author Administrator
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;

	@Autowired
	private TbItemMapper itemMapper;

	public E3Result addCart(long userId, long itemId, int num) {
		// 向redis中添加购物车
		// 数据类型是hash key:用户id field:商品id value:商品信息
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		// 判断商品是否存在
		if (json != null) {
			// 如果不为空则转换成TBItem
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			// 如果存在数量相加
			item.setNum(item.getNum() + num);
			// 写回redis
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		// 如果不存在,根据商品id取商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		// 设置购物车数量
		item.setNum(num);
		// 取一张图片
		String images = item.getImage();
		if (StringUtils.isNotBlank(images)) {
			String[] image = images.split(",");
			item.setImage(image[0]);
		}
		// 添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		// 返回成功
		return E3Result.ok();
	}

	/**
	 * 合并购物车
	 */
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		// 遍历商品列表
		// 把列表添加到购物车
		// 判断购物车
		// 如果有数量相加
		// 如果没有,添加新商品
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3Result.ok();
	}

	/**
	 * 购物车列表
	 */
	public List<TbItem> getCartList(long userId) {
		// 根据用户id查询购物车
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String string : jsonList) {
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}

	// 更新购物车的数量
	public E3Result updataCartNum(long userId, long itemId, int num) {
		// 从redis中取商品信息
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		// 更新商品数量
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		// 添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		// 返回成功
		return E3Result.ok();
	}

	//删除购物车商品
	public E3Result deleteCartItem(long userId, long itemId) {
		//删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}

	//订单提交后删除购物车信息
	public E3Result clearCartItem(long userId) {
		jedisClient.del(REDIS_CART_PRE + ":" + userId);
		return E3Result.ok();
	}

}
