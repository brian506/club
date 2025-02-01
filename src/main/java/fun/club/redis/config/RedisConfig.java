package fun.club.redis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    // 서버와의 연결 정보 저장하는 객체
    private final RedisProperties redisProperties;

    // redisProperties 로 yml 에 저장한 레디스 서버와의(host,post) 연결
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(),redisProperties.getPort());
    }

    // redisTemplate 객체를 생성하여 반환(데이터 저장,조회)
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory());
        // setKey,setValue 는 데이터를 문자열로 반환할 떄 사용
        return template;
    }
}
/**
 * 값이 계속해서 변경되는 로직에는 비용 측면에서 손해라 지양해야 한다.
 * 주로 값을 빠르게 조회하기만 할 때 redis 를 사용한다.
 */
