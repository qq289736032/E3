package com.jisen.e3mall.order.service.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jisen.e3mall.common.jedis.JedisClient;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.mapper.TbOrderItemMapper;
import com.jisen.e3mall.mapper.TbOrderMapper;
import com.jisen.e3mall.mapper.TbOrderShippingMapper;
import com.jisen.e3mall.order.pojo.OrderInfo;
import com.jisen.e3mall.order.service.OrderService;
import com.jisen.e3mall.pojo.TbOrderItem;
import com.jisen.e3mall.pojo.TbOrderShipping;
/**
 * 订单处理
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	
	
	public E3Result createOrder(OrderInfo orderInfo) {
		//生成订单号,使用redis的increase生成
		if(!jedisClient.exists(ORDER_ID_GEN_KEY)){
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		//补全orderInfo的属性
		orderInfo.setOrderId(orderId);
		//1.未付款2.已付款.3未发货4.已发货5.交易成功6.交易关闭
		orderInfo.setStatus(1);
		//插入订单表
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//向订单明细表插入数据
		orderMapper.insert(orderInfo);
		//向订单物流表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成明细id
			String odId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			//补全pojo的属性
			tbOrderItem.setId(odId);
			tbOrderItem.setOrderId(orderId);
			//向明细表插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//向物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setUpdated(new Date());
		orderShipping.setCreated(new Date());
		orderShippingMapper.insert(orderShipping);
		
		//返回e3result,包含订单号
		
		return E3Result.ok(orderId);
	}

}
