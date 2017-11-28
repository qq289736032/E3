package com.jisen.e3mall.order.service;

import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.order.pojo.OrderInfo;

/**
 * 
 * @author Administrator
 *
 */
public interface OrderService {
	E3Result createOrder(OrderInfo orderInfo);
	
}
