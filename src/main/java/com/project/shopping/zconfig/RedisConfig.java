package com.project.shopping.zconfig;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@Configuration
public class RedisConfig implements CachingConfigurer {
	private Map<String, Duration> configMap;

	@Autowired(required = false)
	public void setConfiguration(Map<String, Duration> configMap) {
		this.configMap = configMap;
	}

	private static class RelaxedCacheErrorHandler extends SimpleCacheErrorHandler {
		@Override
		public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {

			if (log.isErrorEnabled()) {
				log.error("error={}", exception.getMessage());
			}
		}
	}

	@Bean
	CacheManager cacheManager(ResourceLoader resourceLoader, LettuceConnectionFactory factory) {
		final RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(factory);
		final RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JsonRedisSerializer()))
				.entryTtl(Duration.ofHours(2L));
		builder.cacheDefaults(configuration);
		if (configMap != null) {
			configMap.entrySet().forEach(e -> {
				builder.withCacheConfiguration(e.getKey(), configuration.entryTtl(e.getValue()));
			});
		}
		return builder.build();

	}

	protected static class JsonRedisSerializer implements RedisSerializer<Object> {
		private final ObjectMapper mapper;

		public JsonRedisSerializer() {
			final SimpleFilterProvider filterProvider = new SimpleFilterProvider();
			filterProvider.addFilter("maskFilter", SimpleBeanPropertyFilter.serializeAll());
			final PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build();
			this.mapper = new ObjectMapper().activateDefaultTyping(ptv, DefaultTyping.NON_FINAL, As.PROPERTY);
			this.mapper.setFilterProvider(filterProvider);
		}

		@Override
		public byte[] serialize(Object object) {
			try {
				return mapper.writeValueAsBytes(object);
			} catch (JsonProcessingException e) {
				throw new SerializationException(e.getMessage(), e);
			}
		}

		@Override
		public Object deserialize(byte[] bytes) {
			if (bytes == null) {
				return null;
			}

			try {
				return mapper.readValue(bytes, Object.class);
			} catch (IOException e) {
				throw new SerializationException(e.getMessage(), e);
			}
		}

	}

	@Override
	public CacheErrorHandler errorHandler() {
		return new RelaxedCacheErrorHandler();
	}

}
