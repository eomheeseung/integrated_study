package example.integrated_test;

import example.integrated_test.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * spring boot 에서 reids를 설정하는데 사용
 */
@Configuration
public class RedisConfig {
    @Autowired
    private ConfigUtil configUtil;

     /*
    redis 연결을 설정 -> 보통 Lettuce의 성능이 좋아서 lettuce를 사용한다.
    Bean으로 등록하고 설정
     */
    @Bean
    public LettuceConnectionFactory factory() {
        /*RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setPort(configUtil.port);
        config.setHostName(configUtil.host);

        return new LettuceConnectionFactory(config);*/
        return new LettuceConnectionFactory(configUtil.host, configUtil.port);
    }

    /*
    RedisTemplate의 Bean 설정.
    Redis와의 상호작용을 담당.
    key, value의 직렬화 방식 세팅.
    key -> StringRedisSerializer
    value -> GenericJackson2JsonRedisSerializer
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
