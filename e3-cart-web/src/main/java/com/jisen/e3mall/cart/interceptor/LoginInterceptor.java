package com.jisen.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jisen.e3mall.common.utils.CookieUtils;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.pojo.TbUser;
import com.jisen.e3mall.sso.service.TokenService;

/**
 * 登录拦截器,用户登录处理
 * @author Administrator
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private TokenService tokenService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		// 前处理,执行handler之前执行此方法
		//返回true,放行,false为拦截
		String token = CookieUtils.getCookieValue(request, "token");
		//1.从cookie中取token
		if(StringUtils.isBlank(token)){
			//2.未未登录状态,直接放行
			return true;
		}
		//3.如果取到token,需要调用sso服务,根据token取用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		//4.没有取到用户信息,登录过期,直接放行
		if(e3Result.getStatus()!=200){
			return true;
		}
		//5.取到用户信息,登录状态,
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler, ModelAndView modelAndView) throws Exception {
		// handler执行之后,返回ModelAndView之前,对modelAndView进行处理
		
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		//完成处理,返回modelAndview之后,可以异常处理
		
	}

	
}
