package com.jisen.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.common.utils.JsonUtils;
import com.jisen.e3mall.sso.service.TokenService;

/**
 * 根据token查询用户信息,用于首页显示登录名,
 * 如果传过来两个参数,即callback那么就返回callback和json
 * @author Administrator
 *
 */
@Controller
public class TokenController {
	
	@Autowired
	private TokenService tokenService;
	
	
	@RequestMapping(value="/user/token/{token}", produces="application/json;charset=utf-8")
	@ResponseBody
	private String getUserByToken(@PathVariable String token, String callback){
		E3Result e3Result = tokenService.getUserByToken(token);
		//响应结果之前,判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			//把结果封装成一个js语句响应
			return callback+"("+JsonUtils.objectToJson(e3Result)+");";
		}
		return JsonUtils.objectToJson(e3Result);
	}
}
