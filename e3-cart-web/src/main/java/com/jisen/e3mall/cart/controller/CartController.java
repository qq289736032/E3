package com.jisen.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisen.e3mall.cart.service.CartService;
import com.jisen.e3mall.common.utils.CookieUtils;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.common.utils.JsonUtils;
import com.jisen.e3mall.pojo.TbItem;
import com.jisen.e3mall.pojo.TbUser;
import com.jisen.e3mall.service.ItemService;

/**
 * 购物车管理
 * 
 * @author Administrator
 *
 */
@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			//如果是登录状态把购物车写入redis
			cartService.addCart(user.getId(),itemId , num);
			return "cartSuccess";
		}
		//如果未登录使用cookie
		
		// 先从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		// 判断商品列表中是否存在
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			// 如果存在数量增加
			if (tbItem.getId() == itemId.longValue()) {
				flag = true;
				// 找到商品,数量相加
				tbItem.setNum(tbItem.getNum() + num);
				// 跳出循环
				break;
			}
		}
		if (!flag) {
			// 如果不存在,根据商品id查询商品信息,得到一个TbItem对象
			TbItem item = itemService.getItemById(itemId);
			// 设置商品数量
			item.setNum(num);
			// 取一张图片
			String image = item.getImage();
			if (StringUtils.isNotBlank(image)) {
				item.setImage(image.split(",")[0]);
			}
			// 把商品添加到商品列表
			cartList.add(item);
		}
		// 写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		// 返回添加成功页面
		return "cartSuccess";
	}

	/**
	 * 从cookie中取购物车列表的处理
	 * 
	 * @return
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		// 判断json是否为空,如果为空则创建新的购物车列表
		if (StringUtils.isBlank(json)) {
			return new ArrayList<TbItem>();
		}
		// 如果不为空,把json转换成商品列表,
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}

	/**
	 * 查看购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String showCatList(HttpServletRequest request, HttpServletResponse response) {
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//如果是登录状态
		if(user!=null){
			//如果不为空,把cookie中的购物车商品和服务端的购物车合并.
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中取的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端取购物车列表
			cartList = cartService.getCartList(user.getId());
		}
		
		//未登录状态
		// 把列表传递给页面
		request.setAttribute("cartList", cartList);
		return "cart";
	}

	/**
	 * 更新购物车数量 /cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val()
	 */
	@RequestMapping("cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null){
			cartService.updataCartNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		// cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		// 遍历商列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				// 更新数量
				tbItem.setNum(num);
				break;
			}
		}
		// 把购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		// 返回成功
		return E3Result.ok();
	}

	/**
	 * cart/delete/967021 删除购物车商品
	 */
	@RequestMapping("cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		// cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		// 遍历商列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				// 更新数量
				cartList.remove(tbItem);
				break;
			}
		}
		// 把购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		// 返回成功
		return "redirect:/cart/cart.html";

	}
}
