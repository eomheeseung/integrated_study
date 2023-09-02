package example.integrated_test;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot에서 @EnableCaching을 적용하면
 * 따로 configuration class에 내용을 적을 필요는 없다.
 */
@Configuration
@EnableCaching
public class CacheConfig {
}
