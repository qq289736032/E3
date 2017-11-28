package com.jisen.e3mall.sso.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.pojo.TbUser;
import com.jisen.e3mall.sso.service.RegisterService;

/**
 * 注册功能管理
 * @author Administrator
 *
 */
@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	/*
	 * 登录页面跳转
	 */
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	/*
	 * 用户注册校验处理
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param, @PathVariable Integer type){
		E3Result e3Result = registerService.checkData(param, type);
		return e3Result;
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result register = registerService.register(user);
		return register;
	}
	
}
