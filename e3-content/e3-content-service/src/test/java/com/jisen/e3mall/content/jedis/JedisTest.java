package com.jisen.e3mall.content.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 测试redis集群
 * @author Administrator
 *
 */
public class JedisTest {
 
	@Test
	public void testJedis() {
		//创建一个Jedis对象,参数:host,port
		Jedis jedis = new Jedis("192.168.25.128",6379);
		//直接使用jedis操作redis.所有jedis的命令对应一个方法
		jedis.set("test123", "my first jedis test");
		String string = jedis.get("test123");
		System.out.println(string);
		//关闭连接,连接的单机版
		//关闭连接
		jedis.close();
	}
	
	//测试连接池
	@Test
	public void testJedisPool(){
		//创建一个连接池对象,两个参数是host,port
		JedisPool jedisPool = new JedisPool("192.168.25.128",6379);
		//从连接池获得一个连接,就是一个jedis对象
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作redis
		jedis.set("jedisPool", "测试一个连接池");
		String string = jedis.get("jedisPool");
		System.out.println(string);
		//关闭连接池回收资源
		jedisPool.close();
	}
	
	//连接集群
	@Test
	public void testJedisCluster(){
		//创建一个JedisCluster对象,有一个参数nodes是一个set类型.set中包含若干个hostAndPort对象
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用JedisCluster对象操作redis.
		jedisCluster.set("testJedisCluster", "testJedisCluster");
		String string = jedisCluster.get("testJedisCluster");
		System.out.println(string);
		//关闭jediscluster对象
		jedisCluster.close();
	}
}
