package com.yan.smarteye.purchase.config;


import com.yan.common.constant.RedisConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class MySessionConfig {

    //注意这里的RedisSerializer、CookieSerializer都是SpringSession包下的，不是原生的

    @Bean // SpringSession的 Redis的序列化设置：使用json序列化
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean // SpringSession的 cookie的设置
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(RedisConstant.SESSION); //修改cookie的key名称
        serializer.setUseHttpOnlyCookie(false); //允许前端js使用该cookie
        //TODO 部署的时候我没有域名备案，得用ip访问，这里得改
        //serializer.setDomainName("smarteye.com"); // 扩大cookie的有效域
        return serializer;
    }
}
