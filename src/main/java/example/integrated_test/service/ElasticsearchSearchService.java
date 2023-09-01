package example.integrated_test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.integrated_test.basicSearch.BasicSearch;
import example.integrated_test.util.ConfigUtil;
import example.integrated_test.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchSearchService {
    private final RestHighLevelClient client;
    private static final String sourceDetail = "{\"index\":{\"number_of_shards\":1,\"number_of_replicas\":0}}";
    private final ConfigUtil configUtil;

    private ObjectMapper mapper = new ObjectMapper();

    private final BasicSearch basicSearch;

    /**
     * basic search engine
     */
    public JSONObject searchNoWildCardMethod(String target, SearchVO vo) throws IOException {
        JSONObject jsonObject = responseToJson(basicSearch.searchNoWildCard(target, vo));
        return jsonObject;
    }

    public JSONObject searchWildCardMethod(String target, SearchVO vo) throws IOException {
        JSONObject jsonObject = responseToJson(basicSearch.searchWildCard(target, vo));
        return jsonObject;
    }

    private JSONObject responseToJson(SearchResponse searchResponse) {
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(Arrays.asList(searchHits));
        jsonObject.put("hits", jsonArray);
        return jsonObject;
    }
}
