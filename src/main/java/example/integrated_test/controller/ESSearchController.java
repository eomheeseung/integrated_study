package example.integrated_test.controller;

import example.integrated_test.customException.SearchException;
import example.integrated_test.service.ElasticsearchSearchService;
import example.integrated_test.vo.SearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ESSearchController {
    private final ElasticsearchSearchService searchService;
    /**
     * 주소 이름 검색
     *
     * @param vo
     * @return
     */
    @Tag(name = "Elasticsearch search", description = "Elasticsearch search")
    @Operation(summary = "search engine", description = "기본적인 검색")
    @PostMapping("es/search/address")
    public JSONObject searchEngineCityName(@RequestBody SearchVO vo) {

        return searchEngine(vo);
    }

    /**
     * 주소 코드 검색
     *
     * @param vo
     * @return
     */
    @Tag(name = "Elasticsearch search", description = "Elasticsearch search")
    @Operation(summary = "search engine", description = "기본적인 검색")
    @PostMapping("es/search/address")
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
