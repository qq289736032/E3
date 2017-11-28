package com.jisen.e3mall.content.jedis;
/**
 * 测试spring容器中的jedisclient,工具方法
 * @author Administrator
 *
 */


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jisen.e3mall.common.jedis.JedisClient;


public class JedisClientTest {
	@Test
	public void testJedisClient() throws Exception {
		//初始化一个spring容器
		ApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获得jedisclient
		JedisClient jedisClient = xmlApplicationContext.getBean(JedisClient.class);
		jedisClient.set("mytestspringjedisClient", "mytestspringjedis");
		String string = jedisClient.get("mytestspringjedisClient");
		System.out.println(string);
	}
}
