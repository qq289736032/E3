package com.jisen.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.jisen.e3mall.common.jedis.JedisClient;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.common.utils.JsonUtils;
import com.jisen.e3mall.mapper.TbUserMapper;
import com.jisen.e3mall.pojo.TbUser;
import com.jisen.e3mall.pojo.TbUserExample;
import com.jisen.e3mall.pojo.TbUserExample.Criteria;
import com.jisen.e3mall.sso.service.LoginService;
/**
 * 用户登录注册处理
 * @author Administrator
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbUserMapper userMapper;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result userLogin(String username, String password) {
		//参数:用户名和密码
		//业务逻辑
		
		//1.判断用户名和密码是否正确
		//根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		//2.如果不正确,返回登录失败
		if(list==null||list.size()==0){
			//返回登录失败
			return E3Result.build(400, "用户名或者密码错误1");
		}
		//取用户信心
		TbUser user = list.get(0);
		//判断密码是否正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			return E3Result.build(400, "用户名或者密码错误2");
		}
		//3.如果正确生成token
		String token = UUID.randomUUID().toString();
		//4.把用户信息写入redis,key:token value:用户信息
		user.setPassword(null);//不带密码
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		//5.session的过期时间
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		//6.把token返回
		
		return E3Result.ok(token);
	}

}
