package example.integrated_test.service;

import example.integrated_test.basicSearch.BasicSearchLogic;
import example.integrated_test.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchSearchService {
    private final BasicSearchLogic basicSearchLogic;

    /**
     * basic search engine
     */
    public JSONObject searchNoWildCardMethod(String target, SearchVO vo) throws IOException {
        JSONObject jsonObject = responseToJson(basicSearchLogic.searchNoWildCard(target, vo));
        return jsonObject;
    }

    public JSONObject searchWildCardMethod(String target, SearchVO vo) throws IOException {
        JSONObject jsonObject = responseToJson(basicSearchLogic.searchWildCard(target, vo));
        return jsonObject;
    }

    private JSONObject responseToJson(SearchResponse searchResponse) {
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceMap = hit.getSourceAsMap(); // 검색 결과의 소스 맵을 추출
            jsonArray.add(sourceMap); // 맵을 JSON 배열에 추가
        }

        jsonObject.put("hits", jsonArray);
        return jsonObject;
    }
}
