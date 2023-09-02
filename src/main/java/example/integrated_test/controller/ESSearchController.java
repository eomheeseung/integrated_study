package example.integrated_test.controller;

import example.integrated_test.customException.SearchException;
import example.integrated_test.service.ElasticsearchSearchService;
import example.integrated_test.vo.SearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ESSearchController {
    private final ElasticsearchSearchService searchService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 주소 이름 검색
     *
     * @Cacheable 어노테이션은 메서드의 파라미터를 기반으로 캐시 키를 생생한다.
     * 따라서 "test::SearchVO(keyword=busan)" 이런형태로 저장된다.
     * 만약 캐시 키를 원하는대로 커스터마이징하고 싶다면 @Cacheable 어노테이션의 key 속성을 사용하여 캐시 키를 직접 지정할 수 있다.
     *
     * 또한 Spring의 @Cacheable 어노테이션을 사용하여 캐시에 저장된 데이터가 Redis에 바이너리 형태로 저장되는 것은 기본 동작이다.
     *
     * 캐시의 유효시간 설정하는 방법이 3가지
     * 1. application.yml
     * 2. cacheManager
     * 3. annotation이용
     * @param vo
     * @return
     */
    @Tag(name = "Elasticsearch search", description = "Elasticsearch search")
    @Operation(summary = "search address city name", description = "order에 저장된 도시 이름으로 검색")
    @PostMapping("es/search/address/cityName")
    @Cacheable(cacheNames = "name", key = "1")
    public JSONObject searchEngineCityName(@RequestBody SearchVO vo) {
        JSONObject jsonObject = searchEngine("address.cityName", vo);
        log.info(jsonObject.toString());
        return jsonObject;
    }


    /**
     * 주소 코드 검색
     *
     * @param vo
     * @return
     */
    @Tag(name = "Elasticsearch search", description = "Elasticsearch search")
    @Operation(summary = "search address city code", description = "order에 저장된 도시 코드로 검색")
    @PostMapping("es/search/address/cityCode")
    @Cacheable(cacheNames = "code", key = "2")
    public JSONObject searchEngineCityCode(@RequestBody SearchVO vo) {
        return searchEngine("address.cityCode", vo);
    }


    private JSONObject searchEngine(String target, SearchVO vo) {
        if (vo.getKeyword().contains("*")) {
            try {
                JSONObject jsonObject = searchService.searchWildCardMethod(target, vo);
                log.info(jsonObject.toString());
                return jsonObject;
            } catch (IOException e) {
                throw new SearchException("not found search result");
            }
        } else {
            try {
                JSONObject jsonObject = searchService.searchNoWildCardMethod(target, vo);
                log.info(jsonObject.toString());
                return jsonObject;
            } catch (IOException e) {
                throw new SearchException("not found search result");
            }
        }
    }
}
