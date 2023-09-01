package example.integrated_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.integrated_test.util.ConfigUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@SpringBootTest
public class CachingTest {
    private final Logger logger = LoggerFactory.getLogger(CachingTest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RedisTemplate<String, Object> redisTemplate;

    ConfigUtil configUtil = new ConfigUtil();

    @BeforeEach
    public void init() {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(new LettuceConnectionFactory("localhost", 6379));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

    /*
    TODO
     ES -> redis 에서 caching 하려고 test
     일부러 중첩되게 클래스를 만듬.
     test용 redis가 필요할 것 같음...
     */
    @Test
    @DisplayName("redis save")
    void saveInRedis() {

        ZipCode zipCode = new ZipCode();
        zipCode.setStreet("0004");
        zipCode.setCityName("seoul");
        TestDTO testDTO = new TestDTO();
        testDTO.setZipcode(zipCode);
        testDTO.setName("test1");


        Map map = objectMapper.convertValue(testDTO, Map.class);

        JSONObject jsonObject = new JSONObject(map);

        logger.info(() -> {
            System.out.println(jsonObject);
            return "ok";
        });

        redisTemplate.opsForHash().put("myCache", "test", jsonObject.toString());
    }


    class TestDTO {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ZipCode getZipcode() {
            return zipcode;
        }

        public void setZipcode(ZipCode zipcode) {
            this.zipcode = zipcode;
        }

        private String name;
        private ZipCode zipcode;
    }

    class ZipCode {
        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        private String street;
        private String cityName;

    }

}
