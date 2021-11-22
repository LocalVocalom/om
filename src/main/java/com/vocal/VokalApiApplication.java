package com.vocal;


import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.planetj.servlet.filter.compression.CompressingFilter;
import com.vocal.configs.StorageProperties;




@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class})
@EnableScheduling
//@EnableCaching
public class VokalApiApplication extends SpringBootServletInitializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VokalApiApplication.class);
	
	// @Autowired
    // RedisConnectionFactory redisConnectionFactory;

	public static void main(String[] args)  {
		SpringApplication.run(VokalApiApplication.class, args);
		LOGGER.info("Vokal API is running.........");
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(VokalApiApplication.class);
	}
	
	@Bean
	public Filter compressingFilter() {
	    CompressingFilter compressingFilter = new CompressingFilter();
	    return compressingFilter;
	}
	
	
	
	//@SuppressWarnings("deprecation")
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		//jedisConnectionFactory.setHostName("localhost");
		//jedisConnectionFactory.setPort(6379);
		return jedisConnectionFactory;
	}
	
	
	@Bean
	public RedisTemplate<String, Long> redisTemplate() {
		RedisTemplate<String, Long> redisTemplate  = new RedisTemplate<>();
		initDomainRedisTemplate(redisTemplate);
        // redisTemplate.setConnectionFactory(redisConnectionFactory); // the auto injecton not working don't know why
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
	
	private void initDomainRedisTemplate(RedisTemplate<String, Long> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    }
    
}

