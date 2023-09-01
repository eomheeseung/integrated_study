package example.integrated_test.controller;

import example.integrated_test.customException.SearchException;
import example.integrated_test.service.ElasticsearchSearchService;
import example.integrated_test.vo.SearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ESSearchController {
    private final ElasticsearchSearchService searchService;
    private final RedisCacheManager redisCacheManager;

    /**
     * 주소 이름 검색
     *
     *
     * TODO
     *  redis caching
     *  deserialize 문제
     *  1. redis에 저장 된 캐시의 이름으로 조회를 해봐야 할 듯...
     *  2. ES에서 검색한 데이터들을 redis 자료구조에 맞게 바꿔야 함.
     * 캐싱을 사용하지 않을 때는 여러번 검색 request를 날려도 오류가 발생하지 않음.
     * 캐싱을 사용할 경우 deserialize ex 발생
     * @param vo
     * @return
     */
    @Tag(name = "Elasticsearch search", description = "Elasticsearch search")
    @Operation(summary = "search address city name", description = "order에 저장된 도시 이름으로 검색")
    @PostMapping("es/search/address/cityName")
//    @Cacheable(cacheNames = "myCache")
    public JSONObject searchEngineCityName(@RequestBody SearchVO vo) {
        JSONObject jsonObject = searchEngine(vo);

        /*Cache cache = redisCacheManager.getCache("myCache");

        if (cache == null) {
            log.info("not caching");
        }else{
            log.info(cache.getName());
        }*/
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
    public JSONObject searchEngineCityCode(@RequestBody SearchVO vo) {
        return searchEngine(vo);
    }


    private JSONObject searchEngine(@RequestBody SearchVO vo) {
        if (vo.getKeyword().contains("*")) {
            try {
                JSONObject jsonObject = searchService.searchWildCardMethod("address.cityName", vo);
                log.info(jsonObject.toString());
                return jsonObject;
            } catch (IOException e) {
                throw new SearchException("not found search result");
            }
        } else {
            try {
                JSONObject jsonObject = searchService.searchNoWildCardMethod("address.cityName", vo);
                log.info(jsonObject.toString());
                return jsonObject;
            } catch (IOException e) {
                throw new SearchException("not found search result");
            }
        }
    }
}
