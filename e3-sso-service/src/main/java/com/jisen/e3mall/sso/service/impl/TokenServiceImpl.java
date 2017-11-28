package com.jisen.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jisen.e3mall.common.jedis.JedisClient;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.common.utils.JsonUtils;
import com.jisen.e3mall.pojo.TbUser;
import com.jisen.e3mall.sso.service.TokenService;
/**
 * 
 * @author Administrator
 *
 */
@Service
public class TokenServiceImpl implements TokenService {
	@Autowired
	private JedisClient JedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3Result getUserByToken(String token) {
		//根据token从redis中取用户信息
		String json = JedisClient.get("SESSION:"+token);
		if (StringUtils.isBlank(json)) {
			//取不到用户信息,登录已过期,返回登录过期
			return E3Result.build(201, "用户登录已经过期");
		}
		//取到用户信息更新token的过期时间,
		JedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		
		return E3Result.ok(user);
	}

}
