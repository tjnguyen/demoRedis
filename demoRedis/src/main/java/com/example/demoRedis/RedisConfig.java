package com.example.demoRedis;

import java.util.Arrays;
import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.demoRedis.service.CustomKeyGenerator;
import com.example.demoRedis.service.SegmentCustomKeyGenerator;
import com.example.demoRedis.service.StockNumberCustomKeyGenerator;




@Configuration
@EnableCaching
public class RedisConfig {
	
	private List<String> cacheNames = Arrays.asList("DPP","ABC"); 
	
	@Bean
	public JedisConnectionFactory connectionFactory() {
		JedisConnectionFactory connection = new JedisConnectionFactory();
		connection.setHostName("localhost");
		connection.setPort(6379);
		
		return connection;
		
	}
	
   /*@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
	  RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
	  .master("mymaster")
	  .sentinel("127.0.0.1", 26379)
	  .sentinel("127.0.0.1", 26380);
	  return new JedisConnectionFactory(sentinelConfig);
	}*/


	
	@Bean
	public RedisTemplate<String, String>  redisTemplate() {
		RedisTemplate<String,String> template = new RedisTemplate<String, String>();
	
		template.setConnectionFactory(connectionFactory());
		
		
		return template;
				
	}
	
	@Bean
	public RedisCacheManager cacheManager() {
		
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate(), cacheNames);
				 
		return rcm;
	}
	
	@Bean(name="customKey")
	public CustomKeyGenerator customKey() {
		return new CustomKeyGenerator();
	}
	
	@Bean(name="stockNumberCustomKey")
	public StockNumberCustomKeyGenerator stockNumberCustomKey() {
		return new StockNumberCustomKeyGenerator();
	}

	@Bean(name="segmentCustomKey")
	public SegmentCustomKeyGenerator segmentCustomKey() {
		return new SegmentCustomKeyGenerator();
	}
	
}
