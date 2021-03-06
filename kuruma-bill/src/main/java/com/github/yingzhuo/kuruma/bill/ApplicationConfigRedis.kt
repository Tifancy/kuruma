package com.github.yingzhuo.kuruma.bill

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer

@Configuration
@EnableCaching
open class ApplicationConfigRedis : CachingConfigurerSupport() {

    @Value("\${redisDefaultTTL:1800}")
    var redisDefaultTTL: Long = -1

    @Bean(name = arrayOf("keyGenerator", "keyGen", "kg"))
    override fun keyGenerator(): KeyGenerator = KeyGenerator { target, method, params ->
        val prefix = "USER_"
        val sb = StringBuilder(prefix)
        sb.append(target.javaClass.name)
        sb.append(method)

        params.forEach {
            sb.append(
                    if (it != null)
                        it.toString()
                    else
                        "null"
            )
        }
        sb.toString()
    }

    @Bean
    open fun cacheManager(redisTemplate: RedisTemplate<*, *>): CacheManager {
        val manager = RedisCacheManager(redisTemplate)
        manager.setDefaultExpiration(redisDefaultTTL)
        return manager
    }

    @Bean
    open fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = StringRedisTemplate(factory)
        template.valueSerializer = JdkSerializationRedisSerializer()
        template.afterPropertiesSet()
        return template
    }

}
