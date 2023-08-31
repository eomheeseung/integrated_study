package example.integrated_test.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * RedisConnecionFactory의 host와 port를 설정해주는 util 클래스
 * application.yml을 참조하여 @Value() 내부에 작성함.
 * 주의!!
 * => @Value()는 static를 사용할 수 없음... (당연한거였음.)
 */
@Component
public class ConfigUtil {
    // redis port num
    @Value("${spring.data.redis.port}")
    public int port;

    // redis port host
    @Value("${spring.data.redis.host}")
    public String host;


    // es uris (port, host, http)
    @Value("${spring.elasticsearch.uris}")
    public String uris;

    // es index name
    public String indexName = "orders";
}
