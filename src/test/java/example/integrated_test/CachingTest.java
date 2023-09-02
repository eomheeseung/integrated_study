package example.integrated_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;
import java.util.Set;

@SpringBootTest
@SpringJUnitConfig
public class CachingTest {

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;
    private final Logger logger = LoggerFactory.getLogger(CachingTest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void init() {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

    @AfterEach
    public void delete() {
        Set<String> keys = redisTemplate.keys("*");

        keys.stream().forEach(key -> redisTemplate.delete(key));
    }

    /*
     test class에서
     @SpringJUnitConfig를 사용하고 config class에 DI
     */
    @Test
    @DisplayName("redis save and inquiry")
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

        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        operation.set("test", jsonObject.toString());


        // find
        logger.info(() -> {
            System.out.println(operation.get("test"));
            return "ok";
        });
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
